package com.ruoyi.meeting.mapper;

import java.util.List;
import com.ruoyi.meeting.domain.News;
import org.apache.ibatis.annotations.Mapper;

/**
 * 新闻管理Mapper接口
 * 
 * @author xiye
 * @date 2025-02-26
 */
@Mapper
public interface NewsMapper 
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
     * 删除新闻管理
     * 
     * @param id 新闻管理主键
     * @return 结果
     */
    public int deleteNewsById(Long id);

    /**
     * 批量删除新闻管理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteNewsByIds(Long[] ids);
}
