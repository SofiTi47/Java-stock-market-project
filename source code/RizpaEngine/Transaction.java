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

    public enum TransactionStatus {
        PENDING,COMPLETED;
    }
    public enum ActionType { //empty action type is used to load completed transactions
        BUY,SELL,EMPTY;
    }
    public enum TransactionType {
        LMT,MKT;
    }

    public Transaction(int rate, int stockAmount,TransactionType type,ActionType actionType) {
        this.rate = rate;
        this.stockAmount = stockAmount;
        transactionTotal = rate * stockAmount;
        timeStamp = DateTimeFormatter.ofPattern("HH:mm:ss:SSSS").format(LocalDateTime.now());
        this.transactionType = type;
        this.transactionStatus = TransactionStatus.PENDING;
        this.actiontype = actionType;
    }

    public Transaction(int rate, int stockAmount, String type, String time, String actionType,
                       String status) {
        this.rate = rate;
        this.stockAmount = stockAmount;
        this.transactionType = TransactionType.valueOf(type);
        transactionTotal = rate*stockAmount;
        this.timeStamp = time;
        this.actiontype = ActionType.valueOf(actionType);
        this.transactionStatus = TransactionStatus.valueOf(status);
    }

    public void setRate(int rate) {
        this.rate = rate;
        transactionTotal = this.rate*this.stockAmount;
    }
    public void setCompleted()
    {
        this.setTransactionStatus(TransactionStatus.COMPLETED);
    }

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
                "Transaction type=" + transactionType +
                ", Transaction rate=" + rate +
                ", Stock amount=" + stockAmount +
                ", Transaction Total=" + transactionTotal +
                ", Transaction time=" + timeStamp +
                ", Transaction status=" + transactionStatus +
                "}";
    }
}
