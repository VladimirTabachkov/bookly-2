package service;

import exception.BookBorrowedException;
import exception.BookNotExistException;
import exception.BookNotFoundException;
import exception.UserNotFoundException;
import model.Book;
import model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LibraryTest {
    private static Library library = Library.createLibrary();

    // проверка добавление книги
    @Test
    void testAddBook() {
        library.addBook("Java для чайников", "Барри Берд", 2019, 1);
        library.displayBooks();
        HashMap<Integer, Book> books = library.findBooks("Java для чайников", "Барри Берд", 2019);
        for (Map.Entry<Integer, Book> entry : books.entrySet()) {
            Assertions.assertEquals("Барри Берд", entry.getValue().getAuthor());
            Assertions.assertEquals("Java для чайников", entry.getValue().getTitle());
            Assertions.assertEquals(1, entry.getValue().getAvailableCopies());
        }
    }

    //проверка добавления читателя
    @Test
    void testAddUser() {
        library.addUser("Пользователь","super@mail.ru");
        library.displayUsers();
        HashMap<Integer, User> users = library.findUsers(0, "Пользователь", "super@mail.ru");
        for (Map.Entry<Integer, User> entry : users.entrySet()) {
            Assertions.assertEquals(4, entry.getValue().getId());
            Assertions.assertEquals("Пользователь", entry.getValue().getName());
            Assertions.assertEquals("super@mail.ru", entry.getValue().getEmail());
        }
    }

    //проверка поиска книги
    @Test
    void testFindBooks() {
        library.addBook("Java для чайников", "Барри Берд", 2019, 1);
        HashMap<Integer, Book> books = library.findBooks("Java для чайников", "Барри Берд", 2019);
        Assertions.assertNotNull(books);
        Assertions.assertEquals(1, books.size());
    }

    //проверка поиска читателя
    @Test
    void testFindUsers() {
        library.addUser("Пользователь","super@mail.ru");
        HashMap<Integer, User> users = library.findUsers(0, "Пользователь", "super@mail.ru");
        Assertions.assertNotNull(users);
        Assertions.assertEquals(2, users.size());
    }

    //проверка выдачи книги читателю
    @Test
    void loanBookTest() throws UserNotFoundException, BookNotExistException, BookNotFoundException, BookBorrowedException {
        Library library1 = new Library();
        UserNotFoundException excp1 = assertThrows(UserNotFoundException.class, () -> library1.addLoan(1, 7));
        Assertions.assertEquals("Пользователь не найден", excp1.getMessage());
        BookNotExistException excp2 = Assertions.assertThrows(BookNotExistException.class, () -> library1.addLoan(1, 1));
        Assertions.assertEquals("Экземпляры книги закончились", excp2.getMessage());
    }

    //проверка возврата книги читателю
    @Test
    void returnBookTest() throws UserNotFoundException, BookNotFoundException, BookBorrowedException {
        Library library1 = new Library();
        UserNotFoundException excp1 = assertThrows(UserNotFoundException.class, () -> library1.removeLoan(1, 7));
        Assertions.assertEquals("Пользователь не найден", excp1.getMessage());
        BookBorrowedException excp2 = assertThrows(BookBorrowedException.class, () -> library1.removeLoan(2, 2));
        Assertions.assertEquals("Эту книгу читателю не выдавали!", excp2.getMessage());
        library1.removeLoan(1, 1);
        assertDoesNotThrow(() -> library1.removeLoan(1, 1));
    }

}