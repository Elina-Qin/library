package org.example.library.controller;

import org.example.library.dto.LoginRequest;
import org.example.library.dto.RegisterRequest;
import org.example.library.dto.Result;
import org.example.library.entity.User;
import org.example.library.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    /**
     * 用户登录接口
     */
    @PostMapping("/login")
    public Result login(@RequestBody LoginRequest loginRequest) {
        // 1. 参数校验
        if (!StringUtils.hasText(loginRequest.getUsername())) {
            return Result.error("用户名不能为空");
        }
        if (!StringUtils.hasText(loginRequest.getPassword())) {
            return Result.error("密码不能为空");
        }

        try {
            // 2. 执行登录逻辑
            User user = userService.login(loginRequest.getUsername().trim(), loginRequest.getPassword().trim());

            // 3. 处理登录结果
            if (user != null) {
                // 登录成功时隐藏密码信息
                User responseUser = new User();
                responseUser.set用户名(user.get用户名());
                return Result.success("登录成功", responseUser);
            } else {
                return Result.error("用户名或密码错误");
            }
        } catch (Exception e) {
            log.error("登录接口异常 - 用户名: {}", loginRequest.getUsername(), e);
            return Result.error("登录失败：" + getErrorMessage(e));
        }
    }

    /**
     * 用户注册接口
     */
    @PostMapping("/register")
    public Result register(@RequestBody RegisterRequest registerRequest) {
        // 1. 详细参数校验
        if (!StringUtils.hasText(registerRequest.getUsername())) {
            return Result.error("用户名不能为空");
        }
        if (registerRequest.getUsername().trim().length() < 3) {
            return Result.error("用户名长度不能少于3个字符");
        }
        if (!StringUtils.hasText(registerRequest.getPassword())) {
            return Result.error("密码不能为空");
        }
        if (registerRequest.getPassword().length() < 6) {
            return Result.error("密码长度不能少于6个字符");
        }
        if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
            return Result.error("两次输入的密码不一致");
        }

        try {
            // 2. 检查用户名是否已存在
            String username = registerRequest.getUsername().trim();
//            if (userService.existsByUsername(username)) {
//                return Result.error("用户名已存在，请更换其他用户名");
//            }

            // 3. 执行注册逻辑
            User user = new User();
            user.set用户名(username);
            user.set密码(registerRequest.getPassword());

            boolean success = userService.register(user);

            // 4. 处理注册结果
            if (success) {
                return Result.success("注册成功，请登录");
            } else {
                log.warn("注册失败 - 服务层返回失败状态 - 用户名: {}", username);
                return Result.error("注册失败，请稍后重试");
            }
        } catch (Exception e) {
            log.error("注册接口异常 - 用户名: {}", registerRequest.getUsername(), e);
            return Result.error("注册失败：" + getErrorMessage(e));
        }
    }

    /**
     * 更新用户信息接口
     */
    @PutMapping("/{username}")
    public Result updateUser(@PathVariable String username, @RequestBody User user) {
        // 1. 参数校验
        if (!StringUtils.hasText(username)) {
            return Result.error("用户名不能为空");
        }
        if (user == null) {
            return Result.error("用户信息不能为空");
        }

        try {
            // 2. 检查用户是否存在
            if (!userService.existsByUsername(username)) {
                return Result.error("用户不存在");
            }

            // 3. 执行更新逻辑
            boolean success = userService.updateByUsername(username, user);

            // 4. 处理更新结果
            if (success) {
                return Result.success("用户信息更新成功");
            } else {
                return Result.error("用户信息更新失败，请稍后重试");
            }
        } catch (Exception e) {
            log.error("更新用户接口异常 - 用户名: {}", username, e);
            return Result.error("更新失败：" + getErrorMessage(e));
        }
    }

    /**
     * 删除用户接口
     */
    @DeleteMapping("/{username}")
    public Result deleteUser(@PathVariable String username) {
        // 1. 参数校验
        if (!StringUtils.hasText(username)) {
            return Result.error("用户名不能为空");
        }

        try {
            // 2. 检查用户是否存在
            if (!userService.existsByUsername(username)) {
                return Result.error("用户不存在");
            }

            // 3. 执行删除逻辑
            boolean success = userService.removeByUsername(username);

            // 4. 处理删除结果
            if (success) {
                return Result.success("用户删除成功");
            } else {
                return Result.error("用户删除失败，请稍后重试");
            }
        } catch (Exception e) {
            log.error("删除用户接口异常 - 用户名: {}", username, e);
            return Result.error("删除失败：" + getErrorMessage(e));
        }
    }

    /**
     * 统一处理异常消息，避免返回null
     */
    private String getErrorMessage(Exception e) {
        if (e.getMessage() == null) {
            // 如果异常消息为null，返回通用消息并记录具体异常类型
            String errorType = e.getClass().getSimpleName();
            log.warn("异常消息为null，异常类型: {}", errorType);
            return "系统异常(" + errorType + ")";
        }
        // 简化数据库异常消息，避免暴露敏感信息
        String message = e.getMessage();
        if (message.contains("Duplicate entry") && message.contains("for key 'PRIMARY'")) {
            return "用户名已存在";
        }
        if (message.contains("SQL syntax")) {
            return "数据操作语法错误";
        }
        return message;
    }
}
