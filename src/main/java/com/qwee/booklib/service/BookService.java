package com.qwee.booklib.service;

import com.qwee.booklib.model.Book;
import com.qwee.booklib.model.BookPage;
import com.qwee.booklib.model.BookSearchCriteria;
import com.qwee.booklib.repository.BookCriteriaRepository;
import com.qwee.booklib.repository.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final BookCriteriaRepository bookCriteriaRepository;

    public BookService(BookRepository bookRepository, BookCriteriaRepository bookCriteriaRepository) {
        this.bookRepository = bookRepository;
        this.bookCriteriaRepository = bookCriteriaRepository;
    }

    public Page<Book> searchBooks(BookPage bookPage, BookSearchCriteria bookSearchCriteria) {
        return bookCriteriaRepository.findAllWithFilters(bookPage, bookSearchCriteria);
    }


    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> getBook(Integer bookID) {
        return bookRepository.findById(bookID);
    }

    public Book addBook(Book book) {
        StringBuilder ISBNBuilder = new StringBuilder();
        Random random = new Random();
        int prefix = random.nextInt(998)+1;
        int countryNum = random.nextInt(192)+1;
        int registrationPublisherNum = random.nextInt(999_999)+10;
        int publisherNum = random.nextInt(98)+1;
        int controlNum = random.nextInt(10);

        ISBNBuilder
                .append(prefix)
                .append("-")
                .append(countryNum)
                .append("-")
                .append(registrationPublisherNum)
                .append("-")
                .append(publisherNum)
                .append("-")
                .append(controlNum);

        book.setISBNCode(ISBNBuilder.toString());
        return bookRepository.save(book);
    }

    public void deleteBook(Book book) {
        bookRepository.delete(book);
    }
}
