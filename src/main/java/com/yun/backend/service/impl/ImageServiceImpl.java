package com.yun.backend.service.impl;

import com.yun.backend.entity.Image;
import com.yun.backend.mapper.ImageMapper;
import com.yun.backend.service.IImageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LiYueyun
 * @since 2023-03-22
 */
@Service
public class ImageServiceImpl extends ServiceImpl<ImageMapper, Image> implements IImageService {

}
