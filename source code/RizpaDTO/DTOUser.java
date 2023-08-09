package RizpaDTO;
import RizpaEngine.Transaction;
import RizpaEngine.User;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DTOUser {
    private final String name;
    private DTOHoldings userHoldings;
    private boolean userType;
    private int userFunds;
    private List<DTOTransaction> completedUserTransactions;
    private List<String> userAlerts;

    public DTOUser(User user){
        this.name = user.getName();
        this.userHoldings = new DTOHoldings(user.getUserHoldings());
        this.userFunds = user.getFunds();
        this.userType = user.isAdmin();
        this.completedUserTransactions = new LinkedList<>();
        this.userAlerts = new LinkedList<>();
    }

    public DTOUser(String name, boolean type){
        this.name = name;
        this.userType = type;
        this.userHoldings = new DTOHoldings();
        this.userFunds = 0;
        this.completedUserTransactions = new LinkedList<>();
        this.userAlerts = new LinkedList<>();
    }
    public String getName() {
        return name;
    }

    public DTOHoldings getUserHoldings() {
        return userHoldings;
    }
    public int getUserHoldings(String symbol){
        return userHoldings.getUserHoldings().get(symbol).getStockAmount();
    }
    public Map<String, DTOStockItem> getUserHoldingsMap(){
        return userHoldings.getUserHoldings();
    }
    public String toSimpleString(){
        if(userType)
            return name+' '+"Admin";
        return name+' '+"Broker";
    }
    public int getUserFunds() {
        return userFunds;
    }

    public String addUserFunds(Transaction t){
        t.setCompleted();
        DTOTransaction transaction = new DTOTransaction(t);
        this.completedUserTransactions.add(transaction);
        transaction.setBalanceBefore(this.userFunds);
        this.userFunds+=t.getRate();
        transaction.setBalanceAfter(this.userFunds);
        return Integer.toString(userFunds);
    }

    public List<DTOTransaction> getCompletedUserTransactions() {
        return completedUserTransactions;
    }

    public List<String> getUserAlerts() {
        return userAlerts;
    }
}
