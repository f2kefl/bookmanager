package ru.FL.bm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.FL.bm.domain.Book;
import ru.FL.bm.repository.BookRepository;
import ru.FL.bm.validation.BookValidator;

import java.util.List;

@Controller
public class BookController
{
    private BookRepository bookRepository;
    private BookValidator bookValidator;

    @Autowired
    public BookController(BookRepository bookRepository, BookValidator bookValidator)
    {
        this.bookValidator = bookValidator;
        this.bookRepository = bookRepository;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getBooks(Model model)
    {
        List books = bookRepository.listAll();

        model.addAttribute("books", books);

        return "index";
    }

    @RequestMapping(value = "addBook", method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public String addBook(Model model){
        model.addAttribute("book", new Book());
        return "addBook";
    }

    @RequestMapping(value = "addBook", method = RequestMethod.POST)
    @PreAuthorize("isAuthenticated()")
    public String addBook(@ModelAttribute("book") Book book, BindingResult bindingResult){
        bookValidator.validate(book, bindingResult);
        if (bindingResult.hasErrors()){
            return "addBook";
        }else{
            bookRepository.addBook(book);
            return "redirect:/";
        }
    }

    @RequestMapping(value = "deleteBook/{id}", method = RequestMethod.GET)
    @PreAuthorize("hasRole('admin')")
    public String deleteBook(@PathVariable Integer id){
        bookRepository.removeBook(id);
        return "redirect:/";
    }
}
