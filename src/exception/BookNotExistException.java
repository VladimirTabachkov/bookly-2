package exception;

public class BookNotExistException extends Exception {
    public BookNotExistException() {
        super("Экземпляры книги закончились");
    }
}
