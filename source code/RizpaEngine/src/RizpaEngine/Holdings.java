package RizpaEngine;

import RizpaEngine.FileException;
import src.JAXB.RseHoldings;
import src.JAXB.RseItem;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Holdings {
    private Map<String, StockItem> userHoldings;

    public Holdings(RseHoldings rseHolding, Stocks stocks) throws FileException {
        userHoldings = new HashMap<>();
        for(RseItem item : rseHolding.getRseItem()){
            StockItem stockItem = new StockItem(item);
            checkStock(stockItem,stocks);
            checkDuplicates(stockItem);
            checkAmount(stockItem);
            stockItem.setStock(stocks.getStock(stockItem.getStockSymbol()));
            userHoldings.put(stockItem.getStockSymbol(), stockItem);
        }
    }
    private void checkDuplicates(StockItem stockItem) throws FileException{
        if(userHoldings.containsKey(stockItem.getStockSymbol()))
            throw new FileException("Double stock symbol found.", new IOException());
    }
    private void checkStock(StockItem stockItem, Stocks stocks) throws FileException{
        if(!stocks.hasStock(stockItem.getStockSymbol()))
            throw new FileException(stockItem.getStockSymbol() + " is not found in stocks.", new IOException());
    }
    private void  checkAmount(StockItem stockItem) throws FileException{
        if(stockItem.getStockAmount() <= 0)
            throw new FileException(stockItem.getStockSymbol() + " has invalid stock amount.", new IOException());
    }

    public StockItem getUserStock(String stockSymbol) {
        return userHoldings.get(stockSymbol);
    }

    public Map<String, StockItem> getUserHoldings() {
        return userHoldings;
    }

}
