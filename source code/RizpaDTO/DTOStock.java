import java.util.LinkedList;
import java.util.List;

public class DTOStock {
    private final String stockSymbol;
    private final String CompanyName;
    private final int stockRate;

    private final List<DTOTransaction> pendingBuy;
    private final List<DTOTransaction> pendingSell;
    private final List<DTOTransaction> completedTransactions;
    public DTOStock(Stock stock)
    {
        this.stockSymbol = stock.getStockSymbol();
        this.CompanyName = stock.getCompanyName();
        this.stockRate = stock.getStockRate();
        this.pendingBuy = new LinkedList<>();
        copyLists(stock.getPendingBuy(),this.pendingBuy);
        this.pendingSell = new LinkedList<>();
        copyLists(stock.getPendingSell(),this.pendingSell);
        this.completedTransactions = new LinkedList<>();
        copyLists(stock.getCompletedTransactions(),this.completedTransactions);
    }
    private void copyLists(List<Transaction> src, List<DTOTransaction> dest) {
        for(Transaction transaction : src) {
            dest.add(new DTOTransaction(transaction));
        }
    }
    //returns a string of minimal data
    public String basicToString() {
        int totalCompleted = completedTransactions.stream()
                .mapToInt(DTOTransaction::getTransactionTotal).sum();
        int totalTransactions = completedTransactions.size();
        return  "Stock symbol=" + stockSymbol +
                ", Company name=" + CompanyName +
                ", Stock rate=" + stockRate +
                ", Total transactions=" + totalTransactions +
                ", Total of completed transactions="+totalCompleted;
    }

    @Override
    //returns a string of full data
    public String toString() {
        return  basicToString() +
                "\ncompletedTransactions=" + basicListToString(completedTransactions);
    }
    public String getStockSymbol() {
        return stockSymbol;
    }

    public List<DTOTransaction> getPendingBuy() {
        return pendingBuy;
    }

    public List<DTOTransaction> getPendingSell() {
        return pendingSell;
    }

    public List<DTOTransaction> getCompletedTransactions() {
        return completedTransactions;
    }

    //returns a string of full data
    public String listToString(List<DTOTransaction> transactionList) {
        return basicListToString(transactionList) +
                "\nTotal of transactions: " +
                transactionList.stream().mapToInt(DTOTransaction::getTransactionTotal).sum();
    }
    //returns a string of minimal data
    public String basicListToString(List<DTOTransaction> transactionList) {
        StringBuilder toString = new StringBuilder();
        int i = 0;
        toString.append("[");
        for(DTOTransaction transaction : transactionList) {
            toString.append(transaction.toString());
            if(i != transactionList.size()-1)
                toString.append("\n");
            i++;
        }
        if(transactionList.isEmpty())
            toString.append("none");
        toString.append("]");
        return toString.toString();
    }
}
