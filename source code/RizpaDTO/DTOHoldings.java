package RizpaDTO;

import RizpaEngine.Holdings;
import RizpaEngine.StockItem;

import java.util.HashMap;
import java.util.Map;

public class DTOHoldings {

    private Map<String, DTOStockItem> userHoldings;

    public DTOHoldings(Holdings holdings){
        userHoldings = new HashMap<>();
        for(StockItem item : holdings.getUserHoldings().values()){
            userHoldings.put(item.getStockSymbol(),new DTOStockItem(item));
        }
    }
    public DTOHoldings(){
        userHoldings = new HashMap<>();
    }
    public Map<String, DTOStockItem> getUserHoldings() {
        return userHoldings;
    }
    public int getHoldingsTotal(){
        int total = 0;
        for(DTOStockItem item : userHoldings.values())
            total+= item.getItemTotal();
        return total;
    }
}
