package com.qwee.booklib.controller;

import com.qwee.booklib.exception.NotFoundException;
import com.qwee.booklib.model.Book;
import com.qwee.booklib.model.BookPage;
import com.qwee.booklib.model.BookSearchCriteria;
import com.qwee.booklib.service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RestController
@RequestMapping("books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public List<Book> showBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable(value = "id") Integer bookID) throws Throwable {
        Book book = bookService.getBook(bookID)
                .orElseThrow(() -> new NotFoundException("Exception!Book not found for this id: " + bookID));
        return ResponseEntity.ok().body(book);
    }

    @PostMapping
    public Book addBook(@Valid @RequestBody Book book) {
        return bookService.addBook(book);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable(value = "id") Integer bookID,
                                           @Valid @RequestBody Book bookDetails) throws NotFoundException {
        Book book = bookService.getBook(bookID)
                .orElseThrow(() -> new NotFoundException("Exception! Can't UPDATE: Book not found for this id: " + bookID));
        //ID and ISBNCode generate while created first-time
        book.setTitle(bookDetails.getTitle());
        book.setAuthor(bookDetails.getAuthor());
        book.setPagesCount(bookDetails.getPagesCount());
        book.setPublishYear(bookDetails.getPublishYear());
        book.setPrice(bookDetails.getPrice());

        final Book updatedBook = bookService.addBook(book);
        return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping("/{id}")
    public Map<String, Boolean> deleteEmployee(@PathVariable(value = "id") Integer bookID)
            throws NotFoundException {
        Book book = bookService.getBook(bookID)
                .orElseThrow(() -> new NotFoundException("Exception! Can't DELETE: Book not found for this id: " + bookID));

        bookService.deleteBook(book);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Book>> searchBooks(BookPage bookPage,
                                                  BookSearchCriteria bookSearchCriteria) {
        return new ResponseEntity<>(bookService.searchBooks(bookPage, bookSearchCriteria), HttpStatus.OK);
    }

}
