package RizpaDTO;

import RizpaEngine.Transaction;

public class DTOTransaction{
    private final int rate;
    private final int stockAmount;
    private final int transactionTotal;
    private final String timeStamp;
    private final Transaction.TransactionType transactionType;
    private final Transaction.TransactionStatus transactionStatus;
    private DTOUser initiator;
    private DTOUser executor;
    private int balanceBefore;
    private int balanceAfter;

    public DTOTransaction(Transaction transaction)
    {
        this.rate = transaction.getRate();
        this.stockAmount = transaction.getStockAmount();
        this.transactionTotal = transaction.getTransactionTotal();
        this.timeStamp = transaction.getTimeStamp();
        this.transactionType = transaction.getTransactionType();
        this.transactionStatus = transaction.getTransactionStatus();
        this.initiator = new DTOUser(transaction.getInitiator().getName(),transaction.getInitiator().isAdmin());
        if(transaction.isCompleted() && transactionType != Transaction.TransactionType.FUND)
            this.executor = new DTOUser(transaction.getExecutor().getName(),transaction.getExecutor().isAdmin());
    }

    public int getTransactionTotal() {
        return transactionTotal;
    }
    public boolean isCompleted() {
        return Transaction.TransactionStatus.COMPLETED != this.transactionStatus;
    }

    public int getRate() {
        return rate;
    }

    public int getStockAmount() {
        return stockAmount;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public Transaction.TransactionType getTransactionType() {
        return transactionType;
    }

    public Transaction.TransactionStatus getTransactionStatus() {
        return transactionStatus;
    }

    public DTOUser getInitiator() {
        return initiator;
    }

    public DTOUser getExecutor() {
        return executor;
    }
    public String getInitiatorName(){
        return initiator.getName();
    }
    public String getExecutorName(){
        return executor.getName();
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

    public void setBalanceBefore(int balanceBefore) {
        this.balanceBefore = balanceBefore;
    }

    public void setBalanceAfter(int balanceAfter) {
        this.balanceAfter = balanceAfter;
    }
}
