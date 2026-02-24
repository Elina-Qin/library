package org.example.library.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.library.entity.Book;

public interface BookService extends IService<Book> {
    // 新增：根据图书编号查询图书
    Book getByNumber(String number);
    // 新增：根据图书编号更新图书
    boolean updateByNumber(String number, Book book);
    // 新增：根据图书编号删除图书
    boolean removeByNumber(String number);
}