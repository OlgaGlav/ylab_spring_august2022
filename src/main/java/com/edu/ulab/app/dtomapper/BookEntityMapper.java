package com.edu.ulab.app.dtomapper;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.entity.Book;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookEntityMapper {
    Book bookDtoToBookEntity(BookDto bookDto);

    BookDto bookEntityToBookDto(Book book);
}
