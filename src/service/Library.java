package service;

import model.Book;
import model.User;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Library {

    private static final Map<Integer, Book> books = new HashMap<>();
    private static final Map<Integer, User> users = new HashMap<>();

    private static final String fileNameBook  = "src/data/books.txt";
    private static final String fileNameUser = "src/data/users.txt";

    public Library() {
    }

    public static Library createLibrary() {
        Library library = new Library();
        library.init();
        return library;
    }

    public void init() {
        loadTableFromDB(fileNameBook, "BOOKS");
        loadTableFromDB(fileNameUser, "USERS");
    }

    private void loadTableFromDB(String fileName, String tableName) {
        switch (tableName) {
            case "BOOKS":
                books.clear();
                break;
            case "USERS":
                users.clear();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + tableName);
        }

        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            while ((line = br.readLine()) != null) {
                String[] data = line.split(";");
                if (tableName == "BOOKS") {
                    addBook(data[0], data[1], Integer.parseInt(data[2]), Integer.parseInt(data[3]));
                }
                else {
                    addUser(data[0], data[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addBook(String title, String author, int year, int totalCopies) {
        if (title == null || title.trim().isEmpty() || author == null || author.trim().isEmpty()) {
            throw new IllegalArgumentException("Не заданы автор и название книги");
        }

        Book book = new Book(title, author, year, totalCopies);
        HashMap<Integer, Book> findBooks;
        findBooks = this.findBooks(title, author, year);
        if (findBooks.size() == 0) {
            // если не нашли - то добавляем
            books.put(book.getId(), book);
        }
        //если нашли только одну книгу
        else if (findBooks.size() == 1) {
            for (Book bookFind : findBooks.values()) {
                //увеличиваем общее количество и доступное
                bookFind.setTotalCopies(bookFind.getTotalCopies() + totalCopies);
                bookFind.setAvailableCopies(bookFind.getAvailableCopies() + totalCopies);
                //обновляем мап
                books.put(bookFind.getId(), bookFind);
            }
        }
        //если нашли более одной книги - сообщаем. ничего не делаем
        else {
            System.out.printf("Ошибка. Книга %s, автора %s и годом издания %d не добавлена.", title, author, year);
        }
    }

    public void addUser(String name, String email) {
        if (name == null || name.trim().isEmpty() || email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Не задано ФИО читателя и его e-mail");
        }
        User user = new User(name, email);
        users.put(user.getId(), user);
    }

    public void displayBooks(){
        displayBooks((HashMap<Integer, Book>) books);
    }

    public void displayBooks(HashMap<Integer, Book> booksFind) {
        System.out.printf("Найдено книг: %d\n", booksFind.size());
        System.out.println("Список:");
        for (Map.Entry<Integer, Book> entry : booksFind.entrySet()) {
            System.out.println(entry.getValue().toString());
        }
    }

    public void displayUsers() {
        displayUsers((HashMap<Integer, User>) users);
    }

    public void displayUsers(HashMap<Integer, User> usersFind) {
        System.out.printf("Найдено читателей : %d\n", usersFind.size());
        System.out.println("Список:");
        for (Map.Entry<Integer, User> entry : usersFind.entrySet()) {
            System.out.println(entry.getValue().toString());
        }
    }

    public HashMap<Integer, Book> findBooks(String title, String author, int year) {
        HashMap<Integer, Book> booksFind = new HashMap<>();
        for (Book book : books.values()) {
            if (((title != null && !title.isBlank() && book.getTitle().toUpperCase().contains(title.toUpperCase())) || (title == null || title.isBlank())) &&
                ((author != null && !author.isBlank() && book.getAuthor().toUpperCase().contains(author.toUpperCase())) || (author == null || author.isBlank())) &&
                ((year != 0 && book.getYear() == year) || year == 0)
               ) {
                    booksFind.put(book.getId(), book);
                 }
        }
        return booksFind;
    }

    public HashMap<Integer, User> findUsers(int id, String FIO, String email) {
        HashMap<Integer, User> usersFind = new HashMap<>();
        if (id != 0 && users.containsKey(id)) {
            usersFind.put(id, users.get(id));
        } else {
        for (User user : users.values()) {
            if (((FIO != null && !FIO.isBlank() && user.getName().toUpperCase().contains(FIO.toUpperCase())) || (FIO == null || FIO.isBlank())) &&
                ((email != null && !email.isBlank() && user.getEmail().toUpperCase().contains(email.toUpperCase())) || (email == null || email.isBlank()))
               ) {
                    usersFind.put(user.getId(), user);
               }
            }
        }
        return usersFind;
    }

}
