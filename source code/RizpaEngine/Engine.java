
import java.io.File;
import java.nio.file.Path;
import java.util.List;

public interface Engine {
    /**
     * This function gets an xml file path, checks if valid and loads it
     * If an error is found with the file, an error message is shown and loaded data
     * is not saved.
     * @param file - the xml file path to load from
     * @throws FileException - throws file exception if the file is empty or has invalid data
     */
    public void loadDataFromFile(Path file) throws FileException;

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
    public List<DTOTransaction> LMTBuy(String stockSymbol, int stockAmount, int maxRate);

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
    public List<DTOTransaction> LMTSell(String stockSymbol, int stockAmount, int minRate);

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
    public List<DTOTransaction> MKTBuy(String stockSymbol, int stockAmount);

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
    public List<DTOTransaction> MKTSell(String stockSymbol, int stockAmount);

    /**
     *This function gets a file path to save the system data into
     * and creates or updates the file with the data
     * supported file types: .txt, .doc, .docx
     * @param path - the file path to save data to
     * @throws FileException - throws a file exception if the file type is not supported
     */
    public void writeToFile (Path path) throws FileException;

    /**
     * This function gets a file path to a text format file to load data from
     * supported file types: .txt, .doc, .docx
     * @param path - the file path to load data from
     * @throws FileException - throws a file exception if the file is not found, is empty
     * or has incorrect data
     */
    public void readFromFile(Path path) throws FileException;
}
