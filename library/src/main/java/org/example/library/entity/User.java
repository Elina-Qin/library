package org.example.library.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "user", autoResultMap = true) // 增加autoResultMap确保映射正确
public class User {
    @TableId(value = "name") // 明确指定主键字段名
    private String 用户名;

    @TableField(value = "password") // 明确指定密码字段名
    private String 密码;
}