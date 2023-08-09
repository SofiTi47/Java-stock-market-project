package RizpaEngine;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Transaction{
    private int rate;
    private int stockAmount;
    private int transactionTotal;
    private String timeStamp;
    private TransactionStatus transactionStatus;
    private TransactionType transactionType;
    private ActionType actiontype;
    private User initiator;
    private User executor;

    public enum TransactionStatus {
        PENDING,COMPLETED,INCOMPLETE;
    }
    public enum ActionType { //empty action type is used to load completed transactions
        BUY,SELL,EMPTY;
    }
    public enum TransactionType {
        LMT,MKT,FOK,IOC,FUND;
    }

    public Transaction(int rate, int stockAmount,TransactionType type,ActionType actionType, User initiator) {
        this.rate = rate;
        this.stockAmount = stockAmount;
        transactionTotal = rate * stockAmount;
        timeStamp = DateTimeFormatter.ofPattern("HH:mm:ss:SSSS").format(LocalDateTime.now());
        this.transactionType = type;
        this.transactionStatus = TransactionStatus.PENDING;
        this.actiontype = actionType;
        this.initiator = initiator;
    }

    public void setRate(int rate) {
        this.rate = rate;
        transactionTotal = this.rate*this.stockAmount;
    }
    public void setCompleted()
    {
        this.setTransactionStatus(TransactionStatus.COMPLETED);
    }

    public void setIncomplete() {this.setTransactionStatus(TransactionStatus.INCOMPLETE);}

    public void setStockAmount(int stockAmount) { this.stockAmount = stockAmount; }

    public void setTransactionStatus(TransactionStatus transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public int getRate() {
        return rate;
    }

    public int getStockAmount() {
        return stockAmount;
    }

    public int getTransactionTotal() {
        return transactionTotal;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public TransactionStatus getTransactionStatus() {
        return transactionStatus;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public ActionType getActionType() {
        return actiontype;
    }

    public boolean isCompleted() {
        return transactionStatus == Transaction.TransactionStatus.COMPLETED;
    }

    public User getInitiator() { return initiator; }

    public User getExecutor() { return executor;  }

    public void setExecutor(User executor) { this.executor = executor; }

    public void setInitiator(User initiator) { this.initiator = initiator;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transaction)) return false;
        Transaction that = (Transaction) o;
        return getRate() == that.getRate() &&
                getStockAmount() == that.getStockAmount() &&
                getTransactionTotal() == that.getTransactionTotal() &&
                getTimeStamp().equals(that.getTimeStamp()) &&
                getTransactionStatus() == that.getTransactionStatus() &&
                getTransactionType() == that.getTransactionType() &&
                actiontype == that.actiontype;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRate(),
                getStockAmount(),
                getTransactionTotal(),
                getTimeStamp(),
                getTransactionStatus(),
                getTransactionType(),
                actiontype);
    }

    @Override
    public String toString() {
        return "{" +
                "RizpaEngine.Transaction type=" + transactionType +
                ", RizpaEngine.Transaction rate=" + rate +
                ", RizpaEngine.Stock amount=" + stockAmount +
                ", RizpaEngine.Transaction Total=" + transactionTotal +
                ", RizpaEngine.Transaction time=" + timeStamp +
                ", RizpaEngine.Transaction status=" + transactionStatus +
                "}";
    }
}
