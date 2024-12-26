package com.ruoyi.cos.service.impl;

import java.util.List;
import com.ruoyi.common.core.utils.DateUtils;
import com.ruoyi.cos.domain.Image;
import com.ruoyi.cos.mapper.ImageMapper;
import com.ruoyi.cos.service.IImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 图片cosService业务层处理
 * 
 * @author xiye
 * @date 2024-12-26
 */
@Service
public class ImageServiceImpl implements IImageService
{
    @Autowired
    private ImageMapper imageMapper;

    /**
     * 查询图片cos
     * 
     * @param id 图片cos主键
     * @return 图片cos
     */
    @Override
    public Image selectImageById(Long id)
    {
        return imageMapper.selectImageById(id);
    }

    /**
     * 查询图片cos列表
     * 
     * @param image 图片cos
     * @return 图片cos
     */
    @Override
    public List<Image> selectImageList(Image image)
    {
        return imageMapper.selectImageList(image);
    }

    /**
     * 新增图片cos
     * 
     * @param image 图片cos
     * @return 结果
     */
    @Override
    public int insertImage(Image image)
    {
        image.setCreateTime(DateUtils.getNowDate());
        return imageMapper.insertImage(image);
    }

    /**
     * 修改图片cos
     * 
     * @param image 图片cos
     * @return 结果
     */
    @Override
    public int updateImage(Image image)
    {
        image.setUpdateTime(DateUtils.getNowDate());
        return imageMapper.updateImage(image);
    }

    /**
     * 批量删除图片cos
     * 
     * @param ids 需要删除的图片cos主键
     * @return 结果
     */
    @Override
    public int deleteImageByIds(Long[] ids)
    {
        return imageMapper.deleteImageByIds(ids);
    }

    /**
     * 删除图片cos信息
     * 
     * @param id 图片cos主键
     * @return 结果
     */
    @Override
    public int deleteImageById(Long id)
    {
        return imageMapper.deleteImageById(id);
    }
}
