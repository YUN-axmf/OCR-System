package com.yun.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yun.backend.entity.User;
import com.yun.backend.service.IUserService;
import com.yun.backend.util.Result;
import io.jsonwebtoken.lang.Strings;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author LiYueyun
 * @since 2023-03-20
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private IUserService userService;

    @Resource
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody User user) {
        Map<String, Object> data = userService.login(user);
        if (Objects.isNull(data))
            return Result.unauthorized();
        return Result.success("登陆成功", data);
    }

    @PostMapping("/logout")
    public Result<?> login() {
        return Result.success("退出成功");
    }

    @GetMapping("/info")
    public Result<Map<String, Object>> queryUserInfo(@RequestParam("token") String token) {
        User data = userService.getUserInfo(token);
        if (Objects.isNull(data))
            return Result.unauthorized();
        Map<String, Object> map = new HashMap<>();
        map.put("id", data.getId());
        map.put("username", data.getUsername());
        map.put("phone", data.getPhone());
        map.put("email", data.getEmail());
        map.put("avatar", data.getAvatar());
        List<String> roles = new ArrayList<>();
        roles.add(data.getRole());
        map.put("roles", roles);
        return Result.success("登陆成功", map);
    }

    @GetMapping("/list")
    public Result<Map<String, Object>> queryUserList(@RequestParam(value = "username", required = false) String username,
                                                     @RequestParam(value = "phone", required = false) String phone,
                                                     @RequestParam(value = "pageNo") Long pageNo,
                                                     @RequestParam(value = "pageSize") Long pageSize) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(Strings.hasText(username), User::getUsername, username);
        wrapper.like(Strings.hasText(phone), User::getPhone, phone);
        Page<User> userPage = new Page<>(pageNo, pageSize);
        userService.page(userPage, wrapper);
        Map<String, Object> data = new HashMap<>();
        data.put("total", userPage.getTotal());
        data.put("rows", userPage.getRecords());
        return Result.success("用户列表查询成功", data);
    }

    @PostMapping("")
    public Result<?> addUser(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (userService.save(user)) {
            return Result.success("新增用户成功");
        }
        return Result.badRequest("新增用户失败");
    }

    @PostMapping("/register")
    public Result<?> Register(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (userService.save(user)) {
            return Result.success("注册成功");
        }
        return Result.badRequest("注册失败");
    }

    @PutMapping("")
    public Result<?> modifyUser(@RequestBody User user) {
        user.setPassword(null);
        if (userService.updateById(user)) {
            return Result.success("修改用户成功");
        }
        return Result.badRequest("修改用户失败");
    }

    @GetMapping("/{id}")
    public Result<User> queryUserById(@PathVariable("id") Integer id) {
        User user = userService.getById(id);
        if (user != null) {
            return Result.success(user);
        }
        return Result.badRequest("查询用户失败");
    }

    @DeleteMapping("/{id}")
    public Result<User> deleteUserById(@PathVariable("id") Integer id) {
        if (userService.removeById(id)) {
            return Result.success("用户删除成功");
        }
        return Result.badRequest("用户删除失败");
    }
}
