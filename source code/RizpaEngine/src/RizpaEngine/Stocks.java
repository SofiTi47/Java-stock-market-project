package RizpaEngine;

import RizpaEngine.FileException;
import RizpaEngine.Stock;
import src.JAXB.RseStock;
import src.JAXB.RseStocks;
import java.io.*;
import java.util.*;

public class Stocks{
    private Map<String, Stock> stockMap;

    public Stocks(RseStocks rseStocks) throws FileException {
        stockMap = new HashMap<>();
        for(RseStock rseStock : rseStocks.getRseStock())
        {
            Stock stock = new Stock(rseStock);
            checkDuplicates(stock);
            stockMap.put(stock.getStockSymbol(),stock);
        }
    }
    public boolean hasStock(String symbol)
    {
        return stockMap.containsKey(symbol.toUpperCase());
    }

    public Stock getStock(String symbol) {
        if(!stockMap.containsKey(symbol.toUpperCase()))
            return null;
        return stockMap.get(symbol.toUpperCase());
    }

    public Map<String, Stock> getStockMap() {
        return stockMap;
    }

    private void checkDuplicates(Stock stock) throws FileException {
        if(stockMap.containsKey(stock.getStockSymbol()))
            throw new FileException("Double stock symbol found.", new IOException());
        if(checkDuplicateCompany(stock.getCompanyName()))
            throw new FileException("Double company found.",new IOException());
    }
    private boolean checkDuplicateCompany(String companyName) {
        for(Stock stock : stockMap.values())
            if(stock.getCompanyName().equals(companyName))
                return true;
        return false;
    }

}
