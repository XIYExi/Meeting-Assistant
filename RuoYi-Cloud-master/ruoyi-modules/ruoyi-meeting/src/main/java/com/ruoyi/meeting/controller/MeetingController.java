package com.ruoyi.meeting.controller;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.core.utils.DateUtils;
import com.ruoyi.common.security.utils.SecurityUtils;
import com.ruoyi.cos.api.RemoteCosService;
import com.ruoyi.job.api.RemoteSysJobService;
import com.ruoyi.meeting.component.GeoMapComponent;
import com.ruoyi.meeting.constant.CosConstant;
import com.ruoyi.meeting.constant.MeetingConstant;
import com.ruoyi.meeting.constant.MeetingRedisKeyBuilder;
import com.ruoyi.meeting.domain.MeetingAgenda;
import com.ruoyi.meeting.domain.MeetingClip;
import com.ruoyi.meeting.domain.MeetingGeo;
import com.ruoyi.meeting.entity.MeetingMilvusEntity;
import com.ruoyi.meeting.entity.MeetingRequest;
import com.ruoyi.meeting.entity.MeetingResponse;
import com.ruoyi.meeting.entity.SimplePartUser;
import com.ruoyi.meeting.qo.MeetingInsertQuery;
import com.ruoyi.meeting.service.IMeetingAgendaService;
import com.ruoyi.meeting.service.IMeetingClipService;
import com.ruoyi.meeting.service.IMeetingGeoService;
import com.ruoyi.meeting.service.impl.MeetingServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.security.annotation.RequiresPermissions;
import com.ruoyi.meeting.domain.Meeting;
import com.ruoyi.meeting.service.IMeetingService;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 会议Controller
 *
 * @author xiye
 * @date 2024-12-25
 */
@Slf4j
@RestController
@RequestMapping("/meeting")
public class MeetingController extends BaseController {
    @Resource
    private IMeetingService meetingService;
    @Resource
    private RemoteCosService remoteCosService;
    @Resource
    private RemoteSysJobService remoteSysJobService;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private IMeetingClipService meetingClipService;
    @Resource
    private IMeetingGeoService meetingGeoService;
    @Resource
    private IMeetingAgendaService meetingAgendaService;


    /**
     * 提供给feign调用的，获取需要的字段，给rec计算rate
     * @return
     */
    @GetMapping("/getListForRec")
    public List<Map<String, Object>> getListForRec() {
        List<Meeting> meetings = meetingService.selectMeetingList(new Meeting());
        List<Map<String, Object>> result = new ArrayList<>();
        meetings.forEach(meeting -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", meeting.getId());
            map.put("title", meeting.getTitle());
            map.put("views", meeting.getViews());
            map.put("type", meeting.getType());
            map.put("meetingType", meeting.getMeetingType());
            result.add(map);
        });
        return result;
    }


    @GetMapping("/lastOne")
    public AjaxResult getLastOneMeeting() {
        Meeting lastOneMeeting = meetingService.getLastOneMeeting();
        return AjaxResult.success(lastOneMeeting);
    }

    @GetMapping("/clipList")
    public AjaxResult clipList() {
        List<MeetingClip> meetingClips = meetingClipService.selectMeetingClipList(new MeetingClip());
        return AjaxResult.success(meetingClips);
    }

    @GetMapping("/getMessageByAgenda")
    public AjaxResult getMessageByAgenda(@RequestParam("id") Long id, @RequestParam("dbType") Long dbType) {
        Map<String, Object> map = new HashMap<>();
        if (Objects.equals(dbType, 1L)) {
            // 查 meeting
            Meeting meeting = meetingService.selectMeetingById(id);
            map.put("title", meeting.getTitle());
            map.put("content", meeting.getRemark());
            map.put("beginTime", meeting.getBeginTime());
            map.put("endTime", meeting.getEndTime());
            map.put("location", meeting.getLocation());
        }
        else {
            // 查 agenda
            MeetingAgenda meetingAgenda = meetingAgendaService.selectMeetingAgendaById(id);
            map.put("title", meetingAgenda.getContent());
            map.put("content", meetingAgenda.getRemark());
            map.put("beginTime", meetingAgenda.getBeginTime());
            map.put("endTime", meetingAgenda.getEndTime());
            map.put("meta", meetingAgenda.getMeta());
        }
        return AjaxResult.success(map);
    }


    @PostMapping("/addClip")
    public AjaxResult addClip(@RequestBody List<MeetingClip> meetingClip) {
        AtomicInteger total = new AtomicInteger();
        meetingClip.forEach(elem -> {
            elem.setFileName(elem.getFileName().split("\\.")[0]);
            elem.setUploadUserId(SecurityUtils.getUserId());
            elem.setCreateTime(DateUtils.getNowDate());
            int i = meetingClipService.insertMeetingClip(elem);
            total.addAndGet(i);
        });
        return AjaxResult.success(total.get() == meetingClip.size());
    }

    @GetMapping("/clips")
    public AjaxResult clips(@RequestParam("id") Long id, @RequestParam("type") Long type) {
        List<MeetingClip> meetingClips = meetingClipService.selectMeetingClipsByCond(id, type);
        return AjaxResult.success(meetingClips);
    }

    @PostMapping("/delClip")
    public AjaxResult delClip(@RequestParam("id") Long id) {
        meetingClipService.delCip(id);
        return AjaxResult.success();
    }



    @GetMapping("/view")
    public AjaxResult view(@RequestParam("id") Long id) {
        Meeting meeting = meetingService.selectMeetingById(id);
        meeting.setViews(meeting.getViews() + 1);
        meetingService.updateMeeting(meeting);
        // 更新redis排行榜(总榜）
        redisTemplate.opsForZSet().incrementScore(MeetingRedisKeyBuilder.MEETING_RANK_KEY, meeting.getId(), 1D);

        return AjaxResult.success();
    }


    /**
     * 排行榜（总榜）
     * @return
     */
    @GetMapping("/rank")
    public AjaxResult rank() {
        // 总榜
        Set<ZSetOperations.TypedTuple<Object>> ranks =  redisTemplate.opsForZSet().reverseRangeWithScores(MeetingRedisKeyBuilder.MEETING_RANK_KEY, 0L, -1L);
        List<Meeting> collect = ranks.stream().map(elem -> {
            Meeting meeting = meetingService.selectMeetingById((Long) elem.getValue());
            // 这个文字太多了，不传
            meeting.setRemark("");
            return meeting;
        }).collect(Collectors.toList());
        return AjaxResult.success(collect);
    }

    @GetMapping("/getPartNumber")
    public AjaxResult getPartNumber(@RequestParam("id") Long id) {
        SimplePartUser partUserAvatarById = meetingService.getPartUserAvatarById(id);
        return AjaxResult.success(partUserAvatarById);
    }


    @GetMapping("/beginTimeList")
    public AjaxResult beginTimeList() {
        List<String> dates = meetingService.selectMeetingBeginTimeForList();
        return AjaxResult.success(dates);
    }

    /**
     * 根据静态推荐获得的会议列表
     * @param
     * @return
     */
    @GetMapping("/static_rec_list")
    public AjaxResult listByStaticRec() {
        List<Meeting> list = meetingService.selectMeetingListByStaticRec();
        List<MeetingResponse> collect = list.stream().map(elem -> {
            MeetingResponse meetingGeo = new MeetingResponse();
            BeanUtils.copyProperties(elem, meetingGeo);
            Long locationId = Long.parseLong(elem.getLocation());
            MeetingGeo meetingGeo1 = meetingGeoService.selectMeetingGeoById(locationId);
            meetingGeo.setLocation(meetingGeo1);
            return meetingGeo;
        }).collect(Collectors.toList());
        return AjaxResult.success(collect);
    }

    /**
     * 查询会议列表
     */
    @RequiresPermissions("meeting:meeting:list")
    @GetMapping("/list")
    public TableDataInfo list(Meeting meeting) {
        startPage();
        List<Meeting> list = meetingService.selectMeetingList(meeting);
        List<MeetingResponse> collect = list.stream().map(elem -> {
            MeetingResponse meetingGeo = new MeetingResponse();
            BeanUtils.copyProperties(elem, meetingGeo);
            Long locationId = Long.parseLong(elem.getLocation());
            MeetingGeo meetingGeo1 = meetingGeoService.selectMeetingGeoById(locationId);
            meetingGeo.setLocation(meetingGeo1);
            return meetingGeo;
        }).collect(Collectors.toList());
        return getDataTable(collect);
    }

    /**
     * 导出会议列表
     */
    @RequiresPermissions("meeting:meeting:export")
    @Log(title = "会议", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Meeting meeting) {
        List<Meeting> list = meetingService.selectMeetingList(meeting);
        ExcelUtil<Meeting> util = new ExcelUtil<Meeting>(Meeting.class);
        util.exportExcel(response, list, "会议数据");
    }

    /**
     * 获取会议详细信息
     */
    @RequiresPermissions("meeting:meeting:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        Meeting meeting = meetingService.selectMeetingById(id);
        MeetingResponse meetingResponse = new MeetingResponse();
        BeanUtils.copyProperties(meeting, meetingResponse);

        long locationId = Long.parseLong(meeting.getLocation());
        MeetingGeo meetingGeo = meetingGeoService.selectMeetingGeoById(locationId);
        meetingResponse.setLocation(meetingGeo);
        return success(meetingResponse);
    }

    /**
     * 新增会议
     */
    @RequiresPermissions("meeting:meeting:add")
    @Log(title = "会议", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody MeetingRequest meetingRequest) {
        // 为了判断路径
        Meeting meeting = new Meeting();
        BeanUtils.copyProperties(meetingRequest, meeting);
        meeting.setLocation(meetingRequest.getPath()); // 获取路径

        // 如果传了图片就调用然后更新
        meeting.setCreateTime(DateUtils.getNowDate());
        return toAjax(meetingService.insertMeeting(meeting));
    }

    @RequiresPermissions("meeting:meeting:add")
    @PostMapping("/addImage")
    public AjaxResult add(@RequestPart(value = "file", required = false) MultipartFile file, @RequestParam("imageId") String imageId) {
        String url = null;
        if (file != null) {
            if (!file.isEmpty()) {
                AjaxResult ajaxResult = remoteCosService.uploadFileCommon(file, imageId);
                if (ajaxResult.get("code").toString().equals("200")) {
                    String filename = file.getOriginalFilename();
                    String extend = filename.substring(filename.lastIndexOf(".") + 1);
                    url = CosConstant.COS_PATH + "common/" + imageId + "." + extend;
                }
            }
        }
        return AjaxResult.success(url);
    }

    /**
     * 修改会议
     */
    @RequiresPermissions("meeting:meeting:edit")
    @Log(title = "会议", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult edit(MeetingInsertQuery meetingEditQuery) {
        int updateMeeting = 0;
        // 如果传了图片就调用然后更新
        if (meetingEditQuery.getFile() != null) {
            if (!meetingEditQuery.getFile().isEmpty()) {
                AjaxResult ajaxResult = remoteCosService.uploadFileCommon(meetingEditQuery.getFile(), meetingEditQuery.getImageId());
                if (ajaxResult.get("code").toString().equals("200")) {
                    // 走到这里image库里面插入了一条新的数据，现在删除老的图片
                    if (meetingEditQuery.getUrl().startsWith("https")) {
                        // 如果url开头，那么就说明开始的时候插入了数据，需要去Image库里面删除
                        remoteCosService.removeImage(meetingEditQuery.getUrl());
                    }
                }
            }
        }

        Meeting meeting = new Meeting();
        BeanUtils.copyProperties(meetingEditQuery, meeting);
        // 修改地图！
        if (meetingEditQuery.getPath() != null) {
            // 1. 先拿到原本map location里面存的meeting_geo id，去把原本的数据删掉
            // System.err.println(meetingEditQuery);
            Long locationId = Long.parseLong(meetingEditQuery.getLocation());
            meetingGeoService.deleteMeetingGeoById(locationId);

            // 2. 调用resttemplate重复新的插入逻辑，获取到最新的数据
            MeetingGeo meetingGeo = meetingService.transferMeetingGeo(meetingEditQuery.getPath());

            // 3. 调换掉新的meeting中的location为最新的location id
            meeting.setLocation(String.valueOf(meetingGeo.getId()));
        }

        if (meetingEditQuery.getFile() != null) {
            String filename = meetingEditQuery.getFile().getOriginalFilename();
            String extend = filename.substring(filename.lastIndexOf(".") + 1);
            if (!meetingEditQuery.getFile().isEmpty())
                meeting.setUrl(CosConstant.COS_PATH + "common/" + meetingEditQuery.getImageId() + "." + extend);
        }
        meeting.setRemark(null);
        updateMeeting = meetingService.updateMeeting(meeting);

        // TODO 修改定时任务
        try {
            if (meetingEditQuery.getBeginTime() != null) {
                // 修改会议自动开始时间
                AjaxResult editJobBeginResult = remoteSysJobService.editByMeetingId(
                        meetingEditQuery.getId(),
                        MeetingServiceImpl.convertToCron(meetingEditQuery.getBeginTime()),
                        "begin");
                log.info("feign更新会议开始时间： {}", editJobBeginResult.toString());
            }
            if (meetingEditQuery.getEndTime() != null) {
                // 修改会议自动结束时间
                AjaxResult editJobEndResult = remoteSysJobService.editByMeetingId(
                        meetingEditQuery.getId(),
                        MeetingServiceImpl.convertToCron(meetingEditQuery.getEndTime()),
                        "end");
                  log.info("feign更新会议结束时间： {}", editJobEndResult.toString());
            }

        }catch (Exception e) {
            throw new RuntimeException(e);
        }
        return toAjax(updateMeeting == 1);
    }

    /**
     * 删除会议
     */
    @RequiresPermissions("meeting:meeting:remove")
    @Log(title = "会议", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(meetingService.deleteMeetingByIds(ids));
    }

    /**
     * ======================================================
     * =============      feign远程接口        ================
     * ======================================================
     */

    /**
     * feign远程接口，用来给 job 调用，自动修改会议状态
     *
     * @param meetingId
     * @param status
     * @return
     */
    @GetMapping("/updateMeetingStatus")
    public AjaxResult updateMeetingStatus(@RequestParam("id") Long meetingId, @RequestParam("status") int status) {
        Meeting meeting = new Meeting();
        meeting.setId(meetingId);
        meeting.setStatus(status == 2 ? MeetingConstant.MEETING_STATUE_IN_PROGRESS : MeetingConstant.MEETING_STATUE_FINISHED);
        int i = meetingService.updateMeeting(meeting);
        return toAjax(i);
    }
}
