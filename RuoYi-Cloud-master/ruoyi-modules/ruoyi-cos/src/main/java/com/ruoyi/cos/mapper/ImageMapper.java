package com.ruoyi.cos.mapper;

import com.ruoyi.cos.domain.Image;

import java.util.List;

/**
 * 图片cosMapper接口
 * 
 * @author xiye
 * @date 2024-12-26
 */
public interface ImageMapper 
{
    /**
     * 查询图片cos
     * 
     * @param id 图片cos主键
     * @return 图片cos
     */
    public Image selectImageById(Long id);

    /**
     * 查询图片cos列表
     * 
     * @param image 图片cos
     * @return 图片cos集合
     */
    public List<Image> selectImageList(Image image);

    /**
     * 新增图片cos
     * 
     * @param image 图片cos
     * @return 结果
     */
    public int insertImage(Image image);

    /**
     * 修改图片cos
     * 
     * @param image 图片cos
     * @return 结果
     */
    public int updateImage(Image image);

    /**
     * 删除图片cos
     * 
     * @param id 图片cos主键
     * @return 结果
     */
    public int deleteImageById(Long id);

    /**
     * 批量删除图片cos
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteImageByIds(Long[] ids);
}
