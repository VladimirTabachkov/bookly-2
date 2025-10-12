package exception;

public class InvalidSearchUserIDException extends Exception {
    public InvalidSearchUserIDException() {
        super("Не задан ID читателя. Поиск прекращен");
    }
}
