package com.ruoyi.meeting.service.impl;

import java.util.Arrays;
import java.util.List;
import com.ruoyi.common.core.utils.DateUtils;
import com.ruoyi.common.security.utils.SecurityUtils;
import com.ruoyi.cos.api.RemoteCosService;
import com.ruoyi.meeting.domain.NewsEditor;
import com.ruoyi.meeting.domain.PointsItems;
import com.ruoyi.meeting.entity.NewsEditorRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import com.ruoyi.meeting.mapper.NewsMapper;
import com.ruoyi.meeting.domain.News;
import com.ruoyi.meeting.service.INewsService;

import javax.annotation.Resource;

/**
 * 新闻管理Service业务层处理
 * 
 * @author xiye
 * @date 2025-02-26
 */
@Service
public class NewsServiceImpl implements INewsService 
{
    private static final Logger logger = LoggerFactory.getLogger(NewsServiceImpl.class);
    @Autowired
    private NewsMapper newsMapper;
    @Resource
    private RemoteCosService remoteCosService;
    @Resource
    private MongoTemplate mongoTemplate;

    /**
     * 查询新闻管理
     * 
     * @param id 新闻管理主键
     * @return 新闻管理
     */
    @Override
    public News selectNewsById(Long id)
    {
        return newsMapper.selectNewsById(id);
    }

    /**
     * 查询新闻管理列表
     * 
     * @param news 新闻管理
     * @return 新闻管理
     */
    @Override
    public List<News> selectNewsList(News news)
    {
        return newsMapper.selectNewsList(news);
    }

    /**
     * 新增新闻管理
     * 
     * @param news 新闻管理
     * @return 结果
     */
    @Override
    public int insertNews(News news)
    {
        news.setCreateTime(DateUtils.getNowDate());
        news.setCreateTime(DateUtils.getNowDate());
        news.setAuthor(SecurityUtils.getUsername());
        return newsMapper.insertNews(news);
    }

    /**
     * 修改新闻管理
     * 
     * @param news 新闻管理
     * @return 结果
     */
    @Override
    public int updateNews(News news)
    {
        news.setUpdateTime(DateUtils.getNowDate());
        news.setAuthor(SecurityUtils.getUsername());
        return newsMapper.updateNews(news);
    }

    /**
     * 批量删除新闻管理
     * 
     * @param ids 需要删除的新闻管理主键
     * @return 结果
     */
    @Override
    public int deleteNewsByIds(Long[] ids)
    {
        Arrays.stream(ids).forEach(newsId -> {
            News news = newsMapper.selectNewsById(newsId);
            String url = news.getUrl();
            if (!url.equals("null")) {
                remoteCosService.removeImage(url);
            }
        });
        return newsMapper.deleteNewsByIds(ids);
    }

    /**
     * 删除新闻管理信息
     * 
     * @param id 新闻管理主键
     * @return 结果
     */
    @Override
    public int deleteNewsById(Long id)
    {
        News news = newsMapper.selectNewsById(id);
        String url = news.getUrl();
        if (!url.equals("null")) {
            remoteCosService.removeImage(url);
        }
        return newsMapper.deleteNewsById(id);
    }

    @Override
    public boolean modifyNewsEditorInMongodb(NewsEditorRequest newsEditorRequest) {
        NewsEditor newsEditors = mongoTemplate.findById(newsEditorRequest.getNewsId(), NewsEditor.class);
        if (newsEditors == null) {
            NewsEditor newsEditor = new NewsEditor();
            newsEditor.setNewsId(newsEditorRequest.getNewsId());
            newsEditor.setContent(newsEditorRequest.getContent());
            mongoTemplate.save(newsEditor);
        }
        else {
            mongoTemplate.remove(newsEditors);
            NewsEditor _newsEditor = new NewsEditor();
            _newsEditor.setNewsId(newsEditorRequest.getNewsId());
            _newsEditor.setContent(newsEditorRequest.getContent());
            mongoTemplate.save(_newsEditor);
        }
        return true;
    }

    @Override
    public NewsEditor selectNewEditor(Long newsId) {
        return mongoTemplate.findById(newsId, NewsEditor.class);
    }
}
