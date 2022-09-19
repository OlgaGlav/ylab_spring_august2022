package com.edu.ulab.app.storage;

import com.edu.ulab.app.entity.Book;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("book_storage")
public class BookStorage extends AbstractStorage<Book> {

    public List<Book> findByUser(Long userId) {
        List<Book> books = getRepository().values().stream().toList();
        List<Book> result = new ArrayList<>();
        for (Book book : books) {
            if (book.getUserId().equals(userId)) {
                result.add(book);
            }
        }
        return result;
    }
}
