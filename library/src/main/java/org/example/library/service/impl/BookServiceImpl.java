package org.example.library.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.library.entity.Book;
import org.example.library.mapper.BookMapper;
import org.example.library.service.BookService;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements BookService {

    @Override
    public Book getByNumber(String number) {
        QueryWrapper<Book> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", number);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public boolean updateByNumber(String number, Book book) {
        UpdateWrapper<Book> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", number); // 以图书编号作为更新条件
        return baseMapper.update(book, updateWrapper) > 0;
    }

    @Override
    public boolean removeByNumber(String number) {
        QueryWrapper<Book> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", number); // 以图书编号作为删除条件
        return baseMapper.delete(queryWrapper) > 0;
    }
}
