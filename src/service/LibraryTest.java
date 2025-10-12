package service;

import model.Book;
import model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

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
}