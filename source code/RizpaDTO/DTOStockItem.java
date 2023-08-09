package RizpaDTO;

import RizpaDTO.DTOStock;
import RizpaEngine.Stock;
import RizpaEngine.StockItem;

public class DTOStockItem {
    private final String stockSymbol;
    private int stockAmount;
    private Stock stock;
    private int itemTotal;

    public DTOStockItem(StockItem item){
        this.stockSymbol = item.getStockSymbol();
        this.stockAmount = item.getStockAmount();
        this.stock = item.getStock();
        this.itemTotal = this.stock.getStockRate() * this.stockAmount;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public int getStockAmount() {
        return stockAmount;
    }

    public DTOStock getStock() {
        return new DTOStock(stock);
    }

    public int getItemTotal() {
        return itemTotal;
    }

    public int getCurrentRate() {
        return stock.getStockRate();
    }
}
