package RizpaEngine;

import RizpaEngine.*;
import src.JAXB.RseUser;

import java.util.LinkedList;
import java.util.List;

public class User {
    private final String name;
    private Holdings userHoldings;
    private int funds;
    private boolean isAdmin;

    public User(RseUser rseUser, Stocks stocks) throws FileException {
        this.name = rseUser.getName();
        this.userHoldings = new Holdings(rseUser.getRseHoldings(),stocks);
        this.funds = 0;
    }
    public User(String userName, boolean isAdmin){
        this.name = userName;
        this.isAdmin = isAdmin;
        this.userHoldings = new Holdings();
        this.funds = 0;
    }

    public User(String userName, Holdings userHoldings){
        this.name = userName;
        this.userHoldings = userHoldings;
        this.funds = 0;
    }
    public String getName() {
        return name;
    }

    public Holdings getUserHoldings() {
        return userHoldings;
    }
    public void removeFromHoldings(Stock stock, int stockAmount){
        StockItem stockItem = userHoldings.getUserStock(stock.getStockSymbol());
        if(stockItem.getStockAmount() - stockAmount == 0)
            userHoldings.getUserHoldings().remove(stock.getStockSymbol());
        else
            stockItem.setStockAmount(stockItem.getStockAmount()-stockAmount);
    }
    public void addToHoldings(Stock stock, int stockAmount){
        if(userHoldings.getUserHoldings().containsKey(stock.getStockSymbol())) {
            StockItem stockItem = userHoldings.getUserStock(stock.getStockSymbol());
            stockItem.setStockAmount(stockItem.getStockAmount() + stockAmount);
        }
        else
        {
            StockItem stockItem = new StockItem(stock,stockAmount);
            userHoldings.getUserHoldings().put(stock.getStockSymbol(),stockItem);
        }
    }
    public void addFunds(int fund){
        this.funds += fund;
    }
    public void subFunds(int fund){this.funds -= fund;}
    public int getFunds() {
        return this.funds;
    }

    public boolean isAdmin() {
        return isAdmin;
    }
}
