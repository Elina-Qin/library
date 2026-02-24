package org.example.library.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.library.entity.User;

public interface UserService extends IService<User> {
    User login(String username, String password);
    boolean register(User user);
    boolean existsByUsername(String username);
    // 新增：根据用户名更新用户信息
    boolean updateByUsername(String username, User user);
    // 新增：根据用户名删除用户
    boolean removeByUsername(String username);
}
