public class DTOTransaction{
    private final int rate;
    private final int stockAmount;
    private final int transactionTotal;
    private final String timeStamp;
    private final Transaction.TransactionType transactionType;
    private final Transaction.TransactionStatus transactionStatus;

    public DTOTransaction(Transaction transaction)
    {
        this.rate = transaction.getRate();
        this.stockAmount = transaction.getStockAmount();
        this.transactionTotal = transaction.getTransactionTotal();
        this.timeStamp = transaction.getTimeStamp();
        this.transactionType = transaction.getTransactionType();
        this.transactionStatus = transaction.getTransactionStatus();
    }

    public int getTransactionTotal() {
        return transactionTotal;
    }
    public boolean isCompleted() {
        return transactionStatus != Transaction.TransactionStatus.COMPLETED;
    }

    @Override
    public String toString() {
        return "{" +
                "Transaction time=" + timeStamp +
                ", Stock amount=" + stockAmount +
                ", Transaction rate=" + rate +
                ", Transaction total=" + transactionTotal +
                ", Transaction type="+ transactionType+
                "}";
    }
    public String getBasicData() {
        return "Stock amount=" + stockAmount +
             ", Transaction rate="  + rate +
             ", Transaction total=" + transactionTotal;
    }
}
