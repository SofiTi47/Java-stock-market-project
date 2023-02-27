import src.JAXB.RseStock;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Stock{
    private String stockSymbol;
    private String companyName;
    private int stockRate;
    private List<Transaction> pendingBuy;
    private List<Transaction> pendingSell;
    private List<Transaction> completedTransactions;

    private final Comparator<Transaction> compareTransactionsRate= (t1, t2)-> t1.getRate()-t2.getRate();

    public Stock(RseStock rseStock) {
        this.stockSymbol = rseStock.getRseSymbol();
        this.companyName = rseStock.getRseCompanyName();
        this.stockRate = rseStock.getRsePrice();
        pendingBuy = new LinkedList<>();
        pendingSell = new LinkedList<>();
        completedTransactions = new LinkedList<>();
    }
    public Stock(String data) {
        String[] fields = data.split(", ");
        for(int i = 0; i< fields.length;i++)
        {
            String[] fieldValue = fields[i].split("=");
            switch(i)
            {
                case 0: this.stockSymbol = fieldValue[1]; break;
                case 1: this.companyName = fieldValue[1]; break;
                case 2: this.stockRate = Integer.parseInt(fieldValue[1]); break;
            }
        }
        this.pendingBuy = new LinkedList<>();
        this.pendingSell = new LinkedList<>();
        this.completedTransactions = new LinkedList<>();
    }

    public List<Transaction> addTransaction(int rate, int stockAmount, Transaction.TransactionType type,
    Transaction.ActionType actionType) {
        List<Transaction> createdTransactions = new LinkedList<>();
        Transaction request = new Transaction(rate,stockAmount,type,actionType);
        Transaction originalRequest = new Transaction(rate,stockAmount,type,actionType); //creates a copy to save in the list
        originalRequest.setTimeStamp(request.getTimeStamp());
        createdTransactions.add(originalRequest);
        boolean found;
        List<Transaction> searchList;
        if(actionType == Transaction.ActionType.BUY)
            searchList = pendingSell;
        else
            searchList = pendingBuy;
        found = checkStock(searchList, request, createdTransactions);
        if(!found && actionType == Transaction.ActionType.BUY)
            insertTransaction(request, pendingBuy, actionType);
        else if(!found)
            insertTransaction(request, pendingSell, actionType);
        removeCompleted(request); //removes the request from all pending lists if completed
        if(request.isCompleted())
            originalRequest.setTransactionStatus(Transaction.TransactionStatus.COMPLETED);
        else if(!request.equals(originalRequest)) //If the transaction not fully completed
            createdTransactions.add(request);
        return createdTransactions;
    }

    private boolean checkStock(List<Transaction> searchList, Transaction request,
                               List<Transaction> createdTransactions) {
        boolean found = false;
        Iterator<Transaction> itr = searchList.iterator();
        while(itr.hasNext() && request.getTransactionStatus() != Transaction.TransactionStatus.COMPLETED) {
            Transaction pendingTransaction = itr.next();
            if(request.getTransactionType() == Transaction.TransactionType.MKT)
                request.setRate(pendingTransaction.getRate());//Market transaction get the rate from the transaction
            if((request.getActionType() == Transaction.ActionType.BUY && //The found transaction rate is equal or lesser
                    compareTransactionsRate.compare(request,pendingTransaction) >= 0)) {
                getStock(pendingTransaction, request,createdTransactions);
                found = true;
            }
            if((request.getActionType()== Transaction.ActionType.SELL && //The found transaction rate is equal or bigger
                    compareTransactionsRate.compare(pendingTransaction,request) >= 0)) {
                getStock(pendingTransaction,request,createdTransactions);
                found = true;
            }
            if(pendingTransaction.getTransactionStatus() == Transaction.TransactionStatus.COMPLETED)
                itr.remove();
        }
        return found;
    }
    private void getStock(Transaction pendingTransaction, Transaction request,
                          List<Transaction> createdTransactions) {
        if (pendingTransaction.getStockAmount() >= request.getStockAmount())
            addCompletedRequest(pendingTransaction,request,createdTransactions);
        else
            addIncompleteRequest(pendingTransaction,request,createdTransactions);
    }

    private void addCompletedRequest(Transaction pendingTransaction, Transaction request,
                                     List<Transaction> createdTransactions) {
        if (pendingTransaction.getStockAmount() > request.getStockAmount()) {
            switch (request.getActionType()) {
                case BUY: //A sell transaction is created like sort of a 'receipt' for the incomplete buy request
                    createdTransactions.add(createCompletedSellTransaction(pendingTransaction.getRate(),
                            request.getStockAmount(),
                            request.getTimeStamp(), request.getTransactionType()));
                    break;
                case SELL: //Here a 'receipt' is not needed because the rate is from the pending buy transactions
                    if(request.getTransactionType() == Transaction.TransactionType.LMT)
                        request.setRate(pendingTransaction.getRate());
                    setStockRate(request.getRate());
                    completedTransactions.add(0,request);
            }
            if(request.getTransactionType()== Transaction.TransactionType.MKT)
                request.setRate(getStockRate());
            request.setCompleted();
            pendingTransaction.setStockAmount(pendingTransaction.getStockAmount()
                    - request.getStockAmount());
        }
        else {
            request.setCompleted();
            pendingTransaction.setCompleted();
            completedTransactions.add(pendingTransaction);
            setStockRate(request.getRate());
        }
    }
    private void addIncompleteRequest(Transaction pendingTransaction, Transaction request,
                                      List<Transaction> createdTransactions) {
        request.setStockAmount(request.getStockAmount() - pendingTransaction.getStockAmount());
        pendingTransaction.setCompleted();
        completedTransactions.add(0,pendingTransaction);
        createdTransactions.add(pendingTransaction);
        setStockRate(pendingTransaction.getRate());
        if(request.getTransactionType() == Transaction.TransactionType.MKT)
            request.setRate(getStockRate());
        switch (request.getActionType()) {
            case BUY:
                insertTransaction(request, pendingBuy, Transaction.ActionType.BUY);
                break;
            case SELL:
                insertTransaction(request, pendingSell, Transaction.ActionType.SELL);
        }
    }
    private Transaction createCompletedSellTransaction(int rate, int stockAmount, String timeStamp,
                                             Transaction.TransactionType type) {
        Transaction completed = new Transaction(rate,stockAmount,type,
                Transaction.ActionType.SELL);
        completed.setCompleted();
        completed.setTimeStamp(timeStamp);
        completedTransactions.add(0,completed);
        return completed;
    }

    private void insertTransaction(Transaction transaction,List<Transaction> transactionList,
                                   Transaction.ActionType comparisonType) {
        int i = 0;
        for(Transaction pendingTransaction : transactionList) {
            switch(comparisonType) { //pending buy- from highest to lowest stock rate, pending sell opposite
                case BUY:
                    if(compareTransactionsRate.compare(transaction,pendingTransaction) > 0) {
                        transactionList.add(i, transaction);
                        return;
                    }
                    break;
                case SELL:
                    if(compareTransactionsRate.compare(transaction,pendingTransaction) < 0) {
                        transactionList.add(i, transaction);
                        return;
                    }
                    break;
            }
            //if they have the same rate, the later transaction will be after the earlier
           if(pendingTransaction.getRate() == transaction.getRate()) {
               i++;
               transactionList.add(i, transaction);
               return;
           }
            i++;
        }
        transactionList.add(transaction);
    }

    private void removeCompleted(Transaction transaction)
    {
        if(transaction.getTransactionStatus() != Transaction.TransactionStatus.COMPLETED)
            return;
        pendingBuy.remove(transaction);
        pendingSell.remove(transaction);
    }
    public String getStockSymbol() {
        return stockSymbol;
    }

    public String getCompanyName() {
        return companyName;
    }

    public int getStockRate() {
        return stockRate;
    }

    public void setStockRate(int stockRate) {
        this.stockRate = stockRate;
    }

    public List<Transaction> getPendingBuy() {
        return pendingBuy;
    }

    public List<Transaction> getPendingSell() {
        return pendingSell;
    }

    public List<Transaction> getCompletedTransactions() {
        return completedTransactions;
    }

    private String listToString(List<Transaction> transactionList)
    {
        StringBuilder toString = new StringBuilder();
        toString.append("[");
        for(Transaction transaction : transactionList) {
            toString.append(transaction.toString());
            toString.append("\n");
        }
        if(transactionList.isEmpty())
            toString.append("none");
        else {
            toString.append("Total of transactions= ");
            toString.append(transactionList.stream().mapToInt(Transaction::getTransactionTotal).sum());
        }
        toString.append("]");
        return toString.toString();
    }
    //Used to write to files
    @Override
    public String toString() {
        return  "Stock symbol=" + stockSymbol +
                ", Company name=" + companyName +
                ", Stock rate=" + stockRate +
                "\npendingBuy=" + listToString(pendingBuy) +
                "\npendingSell=" + listToString(pendingSell) +
                "\ncompletedTransactions=" + listToString(completedTransactions) +
                "\n";
    }
}
