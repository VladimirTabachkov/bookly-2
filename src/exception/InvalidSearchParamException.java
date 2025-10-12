package exception;

public class InvalidSearchParamException extends Exception  {
    public InvalidSearchParamException() {
        super("Не задано ни одного параметра поиска. Укажите параметр поиска");
    }
}
