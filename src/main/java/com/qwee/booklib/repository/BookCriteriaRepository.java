package com.qwee.booklib.repository;

import com.qwee.booklib.model.Book;
import com.qwee.booklib.model.BookPage;
import com.qwee.booklib.model.BookSearchCriteria;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class BookCriteriaRepository {

    private final EntityManager entityManager;
    private final CriteriaBuilder criteriaBuilder;


    public BookCriteriaRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
    }


    public Page<Book> findAllWithFilters(BookPage bookPage,
                                         BookSearchCriteria bookSearchCriteria) {
        CriteriaQuery<Book> criteriaQuery = criteriaBuilder.createQuery(Book.class);
        Root<Book> bookRoot = criteriaQuery.from(Book.class);
        Predicate predicate = getPredicate(bookSearchCriteria, bookRoot);
        criteriaQuery.where(predicate);
        setOrder(bookPage, criteriaQuery, bookRoot);

        TypedQuery<Book> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult(bookPage.getPageNumber() * bookPage.getPageSize());
        typedQuery.setMaxResults(bookPage.getPageSize());
        Pageable pageable = getPageable(bookPage);

        long bookCount = getBooksCount(predicate);
        return new PageImpl<>(typedQuery.getResultList(), pageable, bookCount);
    }


    private Predicate getPredicate(BookSearchCriteria bookSearchCriteria, Root<Book> bookRoot) {
        List<Predicate> predicates = new ArrayList<>();

        if (Objects.nonNull(bookSearchCriteria.getAuthor())) {
            predicates.add(
                    criteriaBuilder.like(bookRoot.get("author"), "%" + bookSearchCriteria.getAuthor() + "%"));
        }

        if (Objects.nonNull(bookSearchCriteria.getISBNCode())) {

            predicates.add(
                    criteriaBuilder.equal(bookRoot.get("ISBNCode"), bookSearchCriteria.getISBNCode()));
        }
        
        if (Objects.nonNull(bookSearchCriteria.getTitle())) {

            predicates.add(
                    criteriaBuilder.like(bookRoot.get("title"), "%" + bookSearchCriteria.getTitle() + "%"));
        }

        if (Objects.nonNull(bookSearchCriteria.getPrice())) {

            predicates.add(
                    criteriaBuilder.equal(bookRoot.get("price"), bookSearchCriteria.getPrice()));
        }

        if (Objects.nonNull(bookSearchCriteria.getPublishYear())) {

            predicates.add(
                    criteriaBuilder.equal(bookRoot.get("publishYear"), bookSearchCriteria.getPublishYear()));
        }

        if (Objects.nonNull(bookSearchCriteria.getPagesCount())) {

            predicates.add(
                    criteriaBuilder.equal(bookRoot.get("pagesCount"), bookSearchCriteria.getPagesCount()));
        }


        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }


    private void setOrder(BookPage bookPage, CriteriaQuery<Book> criteriaQuery, Root<Book> bookRoot) {

        if (bookPage.getSortDirection().equals(Sort.Direction.ASC)) {
            criteriaQuery.orderBy(criteriaBuilder.asc(bookRoot.get(bookPage.getSortBy())));
        } else {
            criteriaQuery.orderBy(criteriaBuilder.desc(bookRoot.get(bookPage.getSortBy())));
        }
    }

    private Pageable getPageable(BookPage bookPage) {

        Sort sort = Sort.by(bookPage.getSortDirection(), bookPage.getSortBy());
        return PageRequest.of(bookPage.getPageNumber(), bookPage.getPageSize(), sort);
    }

    private long getBooksCount(Predicate predicate) {

        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<Book> countRoot = countQuery.from(Book.class);
        countQuery.select(criteriaBuilder.count(countRoot)).where(predicate);
        return entityManager.createQuery(countQuery).getSingleResult();
    }
}

