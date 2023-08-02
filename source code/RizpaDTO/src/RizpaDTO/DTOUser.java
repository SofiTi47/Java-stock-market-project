package RizpaDTO;

import RizpaDTO.DTOHoldings;
import RizpaDTO.DTOStockItem;
import RizpaEngine.User;

import java.util.Map;

public class DTOUser {
    private final String name;
    private DTOHoldings userHoldings;

    public DTOUser(User user){
        this.name = user.getName();
        this.userHoldings = new DTOHoldings(user.getUserHoldings());
    }

    public String getName() {
        return name;
    }

    public DTOHoldings getUserHoldings() {
        return userHoldings;
    }
    public Map<String, DTOStockItem> getUserHoldingsMap(){
        return userHoldings.getUserHoldings();
    }
}
