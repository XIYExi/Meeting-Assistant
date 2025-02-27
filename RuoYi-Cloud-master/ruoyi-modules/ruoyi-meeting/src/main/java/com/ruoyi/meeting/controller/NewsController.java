package com.ruoyi.meeting.controller;

import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.ruoyi.cos.api.RemoteCosService;
import com.ruoyi.meeting.constant.CosConstant;
import com.ruoyi.meeting.domain.NewsEditor;
import com.ruoyi.meeting.entity.NewsEditorRequest;
import com.ruoyi.meeting.entity.NewsRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.security.annotation.RequiresPermissions;
import com.ruoyi.meeting.domain.News;
import com.ruoyi.meeting.service.INewsService;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 新闻管理Controller
 * 
 * @author xiye
 * @date 2025-02-26
 */
@RestController
@RequestMapping("/news")
public class NewsController extends BaseController
{
    @Autowired
    private INewsService newsService;
    @Resource
    private RemoteCosService remoteCosService;



    @PostMapping("/submitNewEditor")
    public AjaxResult submitNewEditor(@RequestBody String newsEditorRequest) {
        // System.err.println(newsEditorRequest.getContent());
        NewsEditorRequest newsEditorRequest1 = JSON.parseObject(newsEditorRequest, NewsEditorRequest.class);
        boolean b = newsService.modifyNewsEditorInMongodb(newsEditorRequest1);
        return b ? AjaxResult.success() : AjaxResult.error();
    }

    @GetMapping("/selectNewEditor")
    public AjaxResult selectNewEditor(@RequestParam("newsId") Long newsId) {
        NewsEditor newsEditor = newsService.selectNewEditor(newsId);
        return newsEditor == null ? AjaxResult.error() : AjaxResult.success(newsEditor);
    }

    @GetMapping("/getNewsList")
    public AjaxResult getNewsList() {
        List<News> news = newsService.selectNewsList(new News());
        return AjaxResult.success(news);
    }

    @GetMapping("/getNewsDetails")
    public AjaxResult getNewsDetails(@RequestParam("newsId") Long newsId) {
        News news = newsService.selectNewsById(newsId);
        return AjaxResult.success(news);
    }

    /**
     * 查询新闻管理列表
     */
    @RequiresPermissions("meeting:news:list")
    @GetMapping("/list")
    public TableDataInfo list(News news)
    {
        startPage();
        List<News> list = newsService.selectNewsList(news);
        return getDataTable(list);
    }

    /**
     * 导出新闻管理列表
     */
    @RequiresPermissions("meeting:news:export")
    @Log(title = "新闻管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, News news)
    {
        List<News> list = newsService.selectNewsList(news);
        ExcelUtil<News> util = new ExcelUtil<News>(News.class);
        util.exportExcel(response, list, "新闻管理数据");
    }

    /**
     * 获取新闻管理详细信息
     */
    @RequiresPermissions("meeting:news:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(newsService.selectNewsById(id));
    }


    @RequiresPermissions("meeting:news:add")
    @PostMapping("/addImage")
    public AjaxResult addImage(@RequestPart(value = "file", required = false) MultipartFile file, @RequestParam("imageId") String imageId) {
        String url = null;
        if (file != null) {
            if (!file.isEmpty()) {
                AjaxResult ajaxResult = remoteCosService.uploadFileSocial(file, imageId);
                if (ajaxResult.get("code").toString().equals("200")) {
                    String filename = file.getOriginalFilename();
                    String extend = filename.substring(filename.lastIndexOf(".") + 1);
                    url = CosConstant.COS_PATH + "article/" + imageId + "." + extend;
                }
            }
        }
        return AjaxResult.success(url);
    }

    /**
     * 新增新闻管理
     */
    @RequiresPermissions("meeting:news:add")
    @Log(title = "新闻管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody News news)
    {
        return toAjax(newsService.insertNews(news));
    }

    /**
     * 修改新闻管理
     */
    @RequiresPermissions("meeting:news:edit")
    @Log(title = "新闻管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult edit(NewsRequest newsRequest)
    {
        int updateNewsItem = 0;
        // 如果传了图片就调用然后更新
        if (newsRequest.getFile() != null) {
            if (!newsRequest.getFile().isEmpty()) {
                AjaxResult ajaxResult = remoteCosService.uploadFileSystem(newsRequest.getFile(), newsRequest.getImageId());
                if (ajaxResult.get("code").toString().equals("200")) {
                    // 走到这里image库里面插入了一条新的数据，现在删除老的图片
                    if (newsRequest.getUrl().startsWith("https")) {
                        // 如果url开头，那么就说明开始的时候插入了数据，需要去Image库里面删除
                        remoteCosService.removeImage(newsRequest.getUrl());
                    }
                }
            }
        }
        News newsItem = new News();
        BeanUtils.copyProperties(newsRequest, newsItem);

        if (newsRequest.getFile() != null) {
            String filename = newsRequest.getFile().getOriginalFilename();
            String extend = filename.substring(filename.lastIndexOf(".") + 1);
            if (!newsRequest.getFile().isEmpty())
                newsItem.setUrl(CosConstant.COS_PATH + "article/" + newsRequest.getImageId() + "." + extend);
        }
        updateNewsItem = newsService.updateNews(newsItem);
        return toAjax(updateNewsItem == 1);
    }

    /**
     * 删除新闻管理
     */
    @RequiresPermissions("meeting:news:remove")
    @Log(title = "新闻管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(newsService.deleteNewsByIds(ids));
    }
}
