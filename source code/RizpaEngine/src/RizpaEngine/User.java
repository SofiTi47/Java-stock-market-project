package RizpaEngine;

import RizpaEngine.*;
import src.JAXB.RseUser;

public class User {
    private final String name;
    private Holdings userHoldings;

    public User(RseUser rseUser, Stocks stocks) throws FileException {
        this.name = rseUser.getName();
        this.userHoldings = new Holdings(rseUser.getRseHoldings(),stocks);
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

}
