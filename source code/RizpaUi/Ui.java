public interface Ui {
    /**
     * Gets a full path of an xml file from the user and loads the stock
     * data to the system
     */
    public void loadFile();
    /*
     * Shows the stocks info (if there is no info, an error message will be shown instead):
     * Stock symbol, stock company name, current stock rate, total stock transactions,
     * total of completed transactions
     */
    public void getStocks();
    /*
     * Gets a stock symbol and shows the stock's data (If there is no info or such stock
     * , an error message will be shown instead):
     * Stock symbol, stock company name, current stock rate, total stock transactions,
     * total of completed transactions, completed transactions
     * For each transaction data shown: Date, stock amount, transaction rate, total of transaction,
     * transaction type
     */
    public void getStock();
    /**
     * Gets a stock symbol and an action type and creates a new transaction
     * After creation shows if the transaction is pending, not fully completed or completed
     * if not fully completed shows details about how much is remaining
     */
    public void tradeStocks();

    /**
     * Shows all pending an completed transaction for each of the stock
     * (If there is no data an error message is shows instead)
     * For each transaction data shown: Date, stock amount, transaction rate, total of transaction,
     * transaction type
     */
    public void getPendingTransactions();

    /**
     * Gets a full file path of a .txt, .doc or .docx and saves the system data
     * If the file is not valid an error is shown
     */
    public void saveSystemData();

    /**
     * Gets a full file path of a .txt, .doc or .docx and loads system data
     * If the file is not valid an error is shown
     */
    public void loadSavedData();

    /**
     * Exits the system without saving any data
     */
    public void exitSystem() throws InterruptedException;

    /**
     * Prints the system menu
     */
    public void printMenu();

    /**
     * This function gets an enum menu and returns an enum from 1 to last enum
     * ex. for a menu with 3 options the function will return an enum from 1-3
     * @param clazz- an enum menu class
     * @param <E> - an enum menu
     * @return - an enum
     */
    public <E extends Enum<E>> E getCommand(Class<E> clazz);
}
