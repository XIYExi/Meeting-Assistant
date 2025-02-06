package com.ruoyi.meeting.controller;

import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.core.utils.DateUtils;
import com.ruoyi.cos.api.RemoteCosService;
import com.ruoyi.job.api.RemoteSysJobService;
import com.ruoyi.job.api.domain.SysJob;
import com.ruoyi.meeting.constant.CosConstant;
import com.ruoyi.meeting.constant.MeetingConstant;
import com.ruoyi.meeting.mapper.MeetingMapper;
import com.ruoyi.meeting.qo.MeetingInsertQuery;
import com.ruoyi.meeting.service.impl.MeetingServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private IMeetingService meetingService;
    @Autowired
    private RemoteCosService remoteCosService;
    @Autowired
    private RemoteSysJobService remoteSysJobService;

    /**
     * 查询会议列表
     */
    @RequiresPermissions("meeting:meeting:list")
    @GetMapping("/list")
    public TableDataInfo list(Meeting meeting) {
        startPage();
        List<Meeting> list = meetingService.selectMeetingList(meeting);
        return getDataTable(list);
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
        return success(meetingService.selectMeetingById(id));
    }

    /**
     * 新增会议
     */
    @RequiresPermissions("meeting:meeting:add")
    @Log(title = "会议", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody Meeting meeting) {
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
        if (meetingEditQuery.getFile() != null) {
            String filename = meetingEditQuery.getFile().getOriginalFilename();
            String extend = filename.substring(filename.lastIndexOf(".") + 1);
            if (!meetingEditQuery.getFile().isEmpty())
                meeting.setUrl(CosConstant.COS_PATH + "common/" + meetingEditQuery.getImageId() + "." + extend);
        }
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
