package RizpaDTO;

import RizpaDTO.DTOStock;
import RizpaEngine.Stock;
import RizpaEngine.Stocks;

import java.util.HashMap;
import java.util.Map;

public class DTOStocks{
    private final Map<String, DTOStock> stockMap;

    public DTOStocks(Stocks stocks) {
        stockMap = new HashMap<>();
        for(Stock stock : stocks.getStockMap().values()) {
            stockMap.put(stock.getStockSymbol(),new DTOStock(stock));
        }
    }

    public Map<String, DTOStock> getStockMap() {
        return stockMap;
    }

    //returns a string of minimal data
    public String basicToString() {
        StringBuilder toString = new StringBuilder();
        for(DTOStock stock : stockMap.values()) {
            toString.append(stock.basicToString());
            toString.append("\n");
        }
        return toString.toString();
    }
    //returns a string of full data
    @Override
    public String toString() {
        StringBuilder toString = new StringBuilder();
        for(DTOStock stock : stockMap.values()) {
            toString.append(stock.toString());
            toString.append("\n");
        }
        return toString.toString();
    }
}
