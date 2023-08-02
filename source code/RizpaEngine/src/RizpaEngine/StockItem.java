package RizpaEngine;

import RizpaEngine.Stock;
import src.JAXB.RseItem;

public class StockItem {
    private final String stockSymbol;
    private int stockAmount;
    private Stock stock;

    public StockItem(RseItem rseItem){
        this.stockSymbol = rseItem.getSymbol();
        this.stockAmount = rseItem.getQuantity();
    }
    public StockItem(Stock stock, int stockAmount){
        this.stockSymbol = stock.getStockSymbol();
        this.stockAmount = stockAmount;
        this.stock = stock;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public int getStockAmount() {
        return stockAmount;
    }

    public void setStockAmount(int stockAmount) {
        this.stockAmount = stockAmount;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

}
