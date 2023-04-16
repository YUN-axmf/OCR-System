package com.yun.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yun.backend.entity.User;
import com.yun.backend.mapper.UserMapper;
import com.yun.backend.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yun.backend.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LiYueyun
 * @since 2023-03-20
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private JwtUtil jwtUtil;

    @Override
    public Map<String, Object> login(User user) {
        // 根据用户名查询
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, user.getUsername());
        User loginUser = this.baseMapper.selectOne(wrapper);
        // 结果不为空且密码匹配则生成token
        if (!Objects.isNull(loginUser) && passwordEncoder.matches(user.getPassword(), loginUser.getPassword())) {
            // 创建JWT
            loginUser.setPassword(null);
            String token = jwtUtil.createToken(loginUser);
            // 返回数据
            Map<String, Object> data = new HashMap<>();
            data.put("token", token);
            return data;
        }
        return null;
    }

    @Override
    public void logout(String token) {

    }

    @Override
    public User getUserInfo(String token) {
        // 根据token获取用户信息
        User loginUser = null;
        try {
            loginUser = jwtUtil.parseToken(token, User.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (loginUser != null) {
            return loginUser;
        }
        return null;
    }
}
