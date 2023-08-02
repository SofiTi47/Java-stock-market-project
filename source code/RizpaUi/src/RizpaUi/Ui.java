package RizpaUi;

import RizpaDTO.DTOStock;
import RizpaDTO.DTOStocks;
import RizpaDTO.DTOUser;

import java.io.File;
import java.io.IOException;

public interface Ui {
    /**
     * Gets a full path of an xml file from the user and loads the stock
     * data to the system
     */
    public void loadFile(File loadFile) throws IOException;
    /*
     * Shows the stocks info (if there is no info, an error message will be shown instead):
     * RizpaEngine.Stock symbol, stock company name, current stock rate, total stock transactions,
     * total of completed transactions
     */
    public DTOStocks getStocks();
    /*
     * Gets a stock symbol and shows the stock's data (If there is no info or such stock
     * , an error message will be shown instead):
     * RizpaEngine.Stock symbol, stock company name, current stock rate, total stock transactions,
     * total of completed transactions, completed transactions
     * For each transaction data shown: Date, stock amount, transaction rate, total of transaction,
     * transaction type
     */
    public DTOStock getStock(String symbol);
    /**
     * Gets a stock symbol and an action type and creates a new transaction
     * After creation shows if the transaction is pending, not fully completed or completed
     * if not fully completed shows details about how much is remaining
     */
    public String tradeStocks(String initiator,String action, String actionType, String stockSymbol, String stockAmount
            , String rate) throws IOException;

    /**
     *
     * @param userName - the name of the user that we want to return
     * @return -  a read only object containing all the user data
     */
    public DTOUser getUser(String userName);

}
