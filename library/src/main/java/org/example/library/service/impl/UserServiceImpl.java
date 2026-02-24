package org.example.library.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.library.entity.User;
import org.example.library.mapper.UserMapper;
import org.example.library.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public User login(String username, String password) {
        log.info("用户登录请求 - 用户名: {}", username);
        try {
            // 参数合法性校验
            if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) {
                log.warn("登录失败：用户名或密码为空 - 用户名: {}", username);
                return null;
            }

            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("name", username)
                    .eq("password", password);

            User user = baseMapper.selectOne(queryWrapper);

            if (user != null) {
                log.info("用户登录成功 - 用户名: {}", username);
            } else {
                log.warn("用户登录失败 - 用户名或密码错误 - 用户名: {}", username);
            }
            return user;
        } catch (Exception e) {
            log.error("用户登录异常 - 用户名: {}", username, e);
            return null;
        }
    }

    @Override
    public boolean register(User user) {
        // 1. 前置参数校验（第一层防御）
        if (user == null) {
            log.error("注册失败：用户对象为空");
            return false;
        }
        String username = user.get用户名();
        String password = user.get密码();

        if (!StringUtils.hasText(username)) {
            log.error("注册失败：用户名为空");
            return false;
        }
        if (!StringUtils.hasText(password)) {
            log.error("注册失败：密码为空 - 用户名: {}", username);
            return false;
        }
        if (username.trim().length() < 3) {
            log.error("注册失败：用户名长度不足3位 - 用户名: {}", username);
            return false;
        }
        if (password.length() < 6) {
            log.error("注册失败：密码长度不足6位 - 用户名: {}", username);
            return false;
        }

        log.info("用户注册请求 - 用户名: {}", username);
        try {
            // 2. 检查用户名是否已存在（避免重复插入）
//            if (existsByUsername(username)) {
//                log.error("注册失败：用户名已存在 - 用户名: {}", username);
//                return false;
//            }

            // 3. 打印SQL参数（脱敏处理密码）
            String maskedPassword = password.replaceAll(".", "*"); // 密码脱敏
            log.debug("注册SQL参数 - 用户名: [{}], 密码: [{}]", username, maskedPassword);
            System.out.println("2");
            // 4. 执行插入操作
            int insertRows = baseMapper.addUser(username,password);
            log.info("注册SQL执行结果 - 影响行数: {}", insertRows);

            return insertRows > 0;
        } catch (org.springframework.dao.DuplicateKeyException e) {
            // 捕获主键冲突异常（双重保障）
            log.error("注册失败：用户名已存在（数据库层面） - 用户名: {}", username, e);
            return false;
        } catch (Exception e) {
            // 捕获所有其他异常
            log.error("注册过程发生异常 - 用户名: {}", username, e);
            return false;
        }
    }

    @Override
    public boolean existsByUsername(String username) {
        log.info("检查用户名是否存在 - 用户名: {}", username);
        try {
            if (!StringUtils.hasText(username)) {
                log.warn("检查用户名存在性：用户名为空");
                return false;
            }

            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("name", username);

            // 注意：selectCount返回Long类型
            Long count = baseMapper.selectCount(queryWrapper);
            boolean exists = count != null && count > 0;

            log.info("用户名存在性检查结果 - 用户名: {}, 存在: {}", username, exists);
            return exists;
        } catch (Exception e) {
            log.error("检查用户名存在性异常 - 用户名: {}", username, e);
            return false;
        }
    }

    @Override
    public boolean updateByUsername(String username, User user) {
        log.info("更新用户信息 - 用户名: {}", username);
        try {
            // 参数校验
            if (!StringUtils.hasText(username) || user == null) {
                log.error("更新失败：用户名或用户信息为空 - 用户名: {}", username);
                return false;
            }

            UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("name", username);

            log.debug("更新用户信息 - 原用户名: {}, 新信息: {}", username, user);

            int updateRows = baseMapper.update(user, updateWrapper);
            boolean success = updateRows > 0;

            if (success) {
                log.info("用户信息更新成功 - 用户名: {}", username);
            } else {
                log.warn("用户信息更新失败 - 未找到用户或信息未变更 - 用户名: {}", username);
            }
            return success;
        } catch (Exception e) {
            log.error("更新用户信息异常 - 用户名: {}", username, e);
            return false;
        }
    }

    @Override
    public boolean removeByUsername(String username) {
        log.info("删除用户 - 用户名: {}", username);
        try {
            // 参数校验
            if (!StringUtils.hasText(username)) {
                log.error("删除失败：用户名为空");
                return false;
            }

            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("name", username);

            int deleteRows = baseMapper.delete(queryWrapper);
            boolean success = deleteRows > 0;

            if (success) {
                log.info("用户删除成功 - 用户名: {}", username);
            } else {
                log.warn("用户删除失败 - 未找到用户 - 用户名: {}", username);
            }
            return success;
        } catch (Exception e) {
            log.error("删除用户异常 - 用户名: {}", username, e);
            return false;
        }
    }
}
