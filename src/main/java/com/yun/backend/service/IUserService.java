package com.yun.backend.service;

import com.yun.backend.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LiYueyun
 */
public interface IUserService extends IService<User> {
    Map<String, Object> login(User user);

    void logout(String token);

    User getUserInfo(String token);
}
