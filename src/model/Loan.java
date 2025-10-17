package model;

import java.time.LocalDate;

public class Loan {
    private int bookId;
    private int userId;
    private LocalDate loanDate;
    private LocalDate returnDate;

    public Loan(int bookId, int userId) {
        this.bookId = bookId;
        this.userId = userId;
        this.loanDate = LocalDate.now();
    }

    public int getBookId() {
        return bookId;
    }

    public int getUserId() {
        return userId;
    }

    public LocalDate getLoanDate() {
        return loanDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }
}
