package com.ruoyi.meeting.service;

import java.util.List;
import com.ruoyi.meeting.domain.News;
import com.ruoyi.meeting.domain.NewsEditor;
import com.ruoyi.meeting.entity.NewsEditorRequest;

/**
 * 新闻管理Service接口
 * 
 * @author xiye
 * @date 2025-02-26
 */
public interface INewsService 
{
    /**
     * 查询新闻管理
     * 
     * @param id 新闻管理主键
     * @return 新闻管理
     */
    public News selectNewsById(Long id);

    /**
     * 查询新闻管理列表
     * 
     * @param news 新闻管理
     * @return 新闻管理集合
     */
    public List<News> selectNewsList(News news);

    /**
     * 新增新闻管理
     * 
     * @param news 新闻管理
     * @return 结果
     */
    public int insertNews(News news);

    /**
     * 修改新闻管理
     * 
     * @param news 新闻管理
     * @return 结果
     */
    public int updateNews(News news);

    /**
     * 批量删除新闻管理
     * 
     * @param ids 需要删除的新闻管理主键集合
     * @return 结果
     */
    public int deleteNewsByIds(Long[] ids);

    /**
     * 删除新闻管理信息
     * 
     * @param id 新闻管理主键
     * @return 结果
     */
    public int deleteNewsById(Long id);

    boolean modifyNewsEditorInMongodb(NewsEditorRequest newsEditorRequest);

    NewsEditor selectNewEditor(Long newsId);
}
