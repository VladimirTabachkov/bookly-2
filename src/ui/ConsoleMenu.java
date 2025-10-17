package ui;

import exception.BookBorrowedException;
import exception.BookNotExistException;
import exception.BookNotFoundException;
import exception.InvalidSearchParamException;
import exception.InvalidSearchUserIDException;
import exception.UserNotFoundException;
import model.Book;
import model.User;
import service.Library;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ConsoleMenu {
    private final Scanner scanner = new Scanner(System.in);
    Library library = Library.createLibrary();

    public void start() {
        System.out.println("Добро пожаловать в электронную библиотеку!");
        displayMenu();
        while (true) {
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> addBook();
                case "2" -> addUser();
                case "3" -> library.displayBooks();
                case "4" -> library.displayUsers();
                case "5" -> findBooks();
                case "6" -> findUsers();
                case "7" -> bookLoan();
                case "8" -> bookReturn();
                case "9" -> bookLoanExpired();
                case "L", "l" -> bookLoanList();
                case "U", "u" -> bookLoanListUser();
                case "B", "b" -> userLoanListBook();
                case "Q", "q" -> System.exit(0);
                default -> displayMenu();
             }
        }
    }

    private static void displayMenu() {
        System.out.println("1. Добавить книгу");
        System.out.println("2. Добавить читателя");
        System.out.println("3. Просмотр всех книг");
        System.out.println("4. Просмотр всех читателей");
        System.out.println("5. Поиск книг по: названию, автору, году");
        System.out.println("6. Поиск читателя по ID");
        System.out.println("7. Выдача книги читателю");
        System.out.println("8. Возврат книги читателем");
        System.out.println("9. Просроченные выдачи");
        System.out.println("L. Список всех выданных книг");
        System.out.println("U. Список выданных книг читателя");
        System.out.println("B. Список читателей взявших книгу");
        System.out.println("Q. Выход");
    }

    // Добавить книгу
    private void addBook() {
        String title, author;
        int year, totalCopies;
        try {
            title = getStringValue("Название книги:");
            author = getStringValue("ФИО автора:");
            year = Integer.parseInt(getStringValue("Год издания:"));
            totalCopies = Integer.parseInt(getStringValue("Количество копий:"));
            library.addBook(title, author, year, totalCopies);
            System.out.println("Книга добавлена");
        } catch (NumberFormatException e) {
            System.out.println("Не верные год издания или количество копий");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    // Добавить пользователя
    private void addUser() {
        String name, email;
        try {
            name = getStringValue("ФИО читателя:");
            email = getStringValue("E-Mail читателя:");
            library.addUser(name, email);
            System.out.println("Читатель добавлен");
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
        }
        catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    // Найти книгу по параметрам. Если параметр не известен, вводим пустую строку
    private void findBooks() {
        HashMap<Integer, Book> booksFind;
        try {
            booksFind = checkBooks();
            if (booksFind != null) {
                library.displayBooks(booksFind);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private HashMap<Integer, Book> checkBooks() {
        String title, author;
        int year;
        HashMap<Integer, Book> booksFind = null;
        try {
            title = getStringValue("Название книги:");
            author = getStringValue("ФИО автора:");
            try {
                year = Integer.parseInt(getStringValue("Год издания:"));
            } catch (NumberFormatException e) {
                year = 0;
            }
            if ((title == null || title.isBlank()) && (author == null || author.isBlank()) && year == 0) {
                throw new InvalidSearchParamException();
            }
            booksFind = library.findBooks(title, author, year);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return booksFind;
    }

    // Найти пользователя по ID.
    private void findUsers() {
        HashMap<Integer, User> usersFind;
        try {
            usersFind = checkUser();
            if (usersFind != null) {
                library.displayUsers(usersFind);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void bookLoan() {
        HashMap<Integer, User> usersFind;
        try {
            usersFind = checkUser();
            if (usersFind == null) {
                return;
                }
            Map.Entry<Integer, User> firstEntry = usersFind.entrySet().iterator().next();
            User firstUser = firstEntry.getValue();
            int countBook = firstUser.CountLoansBook();
            if (countBook >= 3) {
              System.out.println(firstUser.getName() + " не может взять еще книг. У него максимальное колличество.");
                return;
            }
            else {
                countBook = 3 - countBook;
                System.out.println(firstUser.getName() + " может взять еще " + countBook + " книг");
            }
            System.out.println("Выбор книг (не более " + countBook + ", окончание - пустая строка)");

            int bookId = 0;
            for (int i = 0; i < countBook; i++) {
                try {
                    bookId = Integer.parseInt(getStringValue("ID книги:"));
                } catch (NumberFormatException e) {
                    bookId = 0;
                }
                if (bookId == 0) {
                    break; // Выход из цикла
                }
                try {
                    library.addLoan(bookId, firstUser.getId());
                } catch (BookBorrowedException | BookNotExistException | BookNotFoundException | UserNotFoundException e) {
                  System.out.println(e.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void bookReturn() {
        HashMap<Integer, User> usersFind;
        try {
            usersFind = checkUser();
            if (usersFind == null) {
                return;
            }
            Map.Entry<Integer, User> firstEntry = usersFind.entrySet().iterator().next();
            User firstUser = firstEntry.getValue();
            int countBook = firstUser.CountLoansBook();
            if (countBook == 0) {
                System.out.println(firstUser.getName() + " не взял еще ни одной книги.");
                return;
            }
            else {
                System.out.println(firstUser.getName() + " может вернуть " + countBook + " книги");
            }

            int bookId = 0;
            try {
                bookId = Integer.parseInt(getStringValue("ID книги:"));
            } catch (NumberFormatException e) {
                bookId = 0;
            }
            if (bookId == 0) {
                return;
            }
            try {
                library.removeLoan(bookId, firstUser.getId());
            } catch (BookBorrowedException | BookNotFoundException | UserNotFoundException e) {
                System.out.println(e.getMessage());
            }
        } catch (Exception e) {
        System.out.println(e.getMessage());
        }
    }

    private void bookLoanExpired() {
        try {
            library.displayLoanExpiredBook();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void bookLoanList() {
        try {
            library.displayAllLoanBook();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    private void bookLoanListUser() {
        HashMap<Integer, User> usersFind;
        usersFind = checkUser();
        if (usersFind == null) {
            return;
        }
        Map.Entry<Integer, User> firstEntry = usersFind.entrySet().iterator().next();
        User firstUser = firstEntry.getValue();
        try {
            library.displayUserLoanBook(firstEntry.getValue().getId());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void userLoanListBook() {
        int bookId = 0;
        try {
            bookId = Integer.parseInt(getStringValue("ID книги:"));
        } catch (NumberFormatException e) {
            bookId = 0;
        }
        if (bookId == 0) {
            return;
        }
        try {
            Book book = library.findBookById(bookId); // Проверка что книга есть в каталоге
            library.displayUserLoanBook(bookId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private HashMap<Integer, User> checkUser() {
        HashMap<Integer, User> usersFind = null;
        int id;
        try {
            try {
                id = Integer.parseInt(getStringValue("ID читателя:"));
            } catch (NumberFormatException e) {
                id = 0;
            }
            if (id == 0) {
                throw new InvalidSearchUserIDException();
            }
            usersFind = library.findUsers(id, null, null);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return usersFind;
    }

    private String getStringValue(String s) {
        System.out.print(s);
        return scanner.nextLine();
    }
}
