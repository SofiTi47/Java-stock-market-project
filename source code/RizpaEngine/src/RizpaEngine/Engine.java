package RizpaEngine;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import RizpaDTO.*;

public interface Engine {
    /**
     * This function gets an xml file path, checks if valid and loads it
     * If an error is found with the file, an error message is shown and loaded data
     * is not saved.
     * @param loadFile - the xml file path to load from
     * @throws FileException - throws file exception if the file is empty or has invalid data
     */
    public void loadDataFromFile(InputStream loadFile, String userName) throws FileException, IOException;

    /**
     * @return - if the system has stock data
     */
    public boolean hasLoadedData();

    /**
     * @param stockSymbol - the stock symbol input
     * @return a read only stock object containing its data if found, else returns null
     */
    public DTOStock getStock(String stockSymbol);

    /**
     * @return a read only stocks object containing all stocks data if has data, else returns null
     */
    public DTOStocks getStocks();

    /**
     * This function gets transaction info and creates a buy transaction in the system
     * If a sell transaction is found that the stock rate is equal or bellow the maximum rate,
     * a completed transaction will be created and the sell transaction will be removed accordingly
     * Else, the translation will be inserted to the pending buy list of the stock
     * @param stockSymbol - the stock name
     * @param stockAmount - the number of stocks to buy
     * @param maxRate - the maximum stock pay rate
     * @return a read only transaction list containing the transaction details that were created
     */
    public List<DTOTransaction> LMTBuy(String stockSymbol, int stockAmount, int maxRate, String initiator);

    /**
     * This function gets transaction info and creates a sell transaction in the system
     * If a buy transaction is found that the stock rate is equal or above the minimum rate,
     * a completed transaction will be created and the buy transaction will be removed accordingly
     * Else, the translation will be inserted to the pending sell list of the stock
     * @param stockSymbol - the stock name
     * @param stockAmount - the number of stocks to sell
     * @param minRate - the minimum stock pay rate
     * @return a read only transaction list containing the transaction details that were created
     */
    public List<DTOTransaction> LMTSell(String stockSymbol, int stockAmount, int minRate, String initiator);

    /**
     * This function gets transaction info and creates a buy transaction in the system
     * If a sell transaction is found that has less or equal amount of stock count,
     * a completed transaction will be created and the sell transaction will be removed accordingly
     * The transaction rate will be the rate of the sell transaction
     * Else, the translation will be inserted to the pending buy list of the stock
     * @param stockSymbol - the stock name
     * @param stockAmount - the number of stocks to sell
     * @return a read only transaction list containing the transaction details that were created
     */
    public List<DTOTransaction> MKTBuy(String stockSymbol, int stockAmount, String initiator);

    /**
     * This function gets transaction info and creates a sell transaction in the system
     * If a buy transaction is found that has less or equal amount of stock count,
     * a completed transaction will be created and the buy transaction will be removed accordingly
     * The transaction rate will be the rate of the buy transaction
     * Else, the translation will be inserted to the pending sell list of the stock
     * @param stockSymbol - the stock name
     * @param stockAmount - the number of stocks to sell
     * @return a read only transaction list containing the transaction details that were created
     */
    public List<DTOTransaction> MKTSell(String stockSymbol, int stockAmount, String initiator);

    public List<DTOTransaction> IOCBuy(String stockSymbol, int stockAmount, int maxRate, String initiator);
    public List<DTOTransaction> FOKBuy(String stockSymbol, int stockAmount, int maxRate, String initiator);
    public List<DTOTransaction> IOCSell(String stockSymbol, int stockAmount, int minRate, String initiator);
    public List<DTOTransaction> FOKSell(String stockSymbol, int stockAmount, int minRate, String initiator);

    /**
     *
     * @return a read only object containing all users
     */
    public DTOUsers getUsers();

    /**
     *
     * @param userName - the user name of the user to return
     * @return a read only object containing the user data
     */
    public DTOUser getUser(String userName);

    public Transaction addFunds(String userName, int val);
    public void addUser(String userName, boolean isAdmin);
    public boolean addStock(String userName, String symbol, String companyName, int amount, int rate);
}
