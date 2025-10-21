package exception;

public class BookNotFoundException extends Exception {
    public BookNotFoundException() {
        super("Книга не найдена");
    }
}
