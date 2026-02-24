package org.example.library.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("book")
public class Book {
    // 关键修复：指定图书编号为表主键
    @TableId(value = "id")  // 映射数据库表的"编号"字段作为主键
    private String 编号;
    @TableField(value = "book_name")
    private String 书名;
    @TableField(value = "author")
    private String 作者;
}
