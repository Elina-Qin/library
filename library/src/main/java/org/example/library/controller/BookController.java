package org.example.library.controller;

import org.example.library.dto.Result;
import org.example.library.entity.Book;
import org.example.library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/book")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping("/list")
    public Result list() {
        try {
            List<Book> books = bookService.list();
            return Result.success(books);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("获取图书列表失败：" + e.getMessage());
        }
    }

    @PostMapping
    public Result add(@RequestBody Book book) {
        try {
            if (bookService.getByNumber(book.get编号()) != null) {
                return Result.error("图书编号已存在");
            }
            if (bookService.save(book)) {
                return Result.success("添加成功");
            } else {
                return Result.error("添加失败：数据库操作未成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("添加失败：" + e.getMessage());
        }
    }

    @PutMapping("/{number}")
    public Result update(@PathVariable String number, @RequestBody Book book) {
        try {
            Book existingBook = bookService.getByNumber(number);
            if (existingBook == null) {
                return Result.error("图书不存在");
            }
            if (!number.equals(book.get编号()) && bookService.getByNumber(book.get编号()) != null) {
                return Result.error("新图书编号已存在");
            }
            if (bookService.updateByNumber(number, book)) {
                return Result.success("更新成功");
            } else {
                return Result.error("更新失败：数据库操作未成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("更新失败：" + e.getMessage());
        }
    }

    @DeleteMapping("/{number}")
    public Result delete(@PathVariable String number) {
        try {
            if (bookService.getByNumber(number) == null) {
                return Result.error("图书不存在");
            }
            if (bookService.removeByNumber(number)) {
                return Result.success("删除成功");
            } else {
                return Result.error("删除失败：数据库操作未成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("删除失败：" + e.getMessage());
        }
    }

    @GetMapping("/{number}")
    public Result getBook(@PathVariable String number) {
        try {
            Book book = bookService.getByNumber(number);
            if (book != null) {
                return Result.success(book);
            } else {
                return Result.error("图书不存在");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("获取图书信息失败：" + e.getMessage());
        }
    }
}
