package RizpaEngine;

import RizpaDTO.*;
import jdk.internal.util.Preconditions;
import src.JAXB.RizpaStockExchangeDescriptor;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.util.*;

public class RizpaEngine implements Engine {

    private Stocks stocks;
    private Users users;
    private final static String JAXB_PACKAGE_NAME = "src.JAXB";

    public RizpaEngine() {

        stocks = new Stocks();
        users = new Users();
    }
    @Override
    public Transaction addFunds(String userName, int val){
        User user = users.getUser(userName);
        user.addFunds(val);
        return new Transaction(val,1, Transaction.TransactionType.FUND, Transaction.ActionType.EMPTY
        ,user);
    }
    @Override
    public void addUser(String userName, boolean isAdmin){
        User user = new User(userName,isAdmin);
        users.addUser(user);
    }
    @Override
    public void loadDataFromFile(InputStream loadFile, String userName) throws FileException, IOException {
       loadFile = checkStreamIsNotEmpty(loadFile);
       try {
           JAXBContext jaxbContext = JAXBContext.newInstance(JAXB_PACKAGE_NAME);
           Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
           RizpaStockExchangeDescriptor temp =
                   (RizpaStockExchangeDescriptor) jaxbUnmarshaller.unmarshal(loadFile);
           Stocks tmpStocks = new Stocks(temp.getRseStocks());
           stocks.addStocks(tmpStocks);
           Holdings userHoldings = new Holdings(temp.getRseHoldings(), stocks);
           User user = new User(userName, userHoldings);
           users.addUser(user);
       } catch (Exception e) {
           if (e.getMessage() == null)
               throw new FileException("No valid data found.", e.getCause());
           throw new FileException(e.getMessage(), e.getCause());
       }
    }

    private InputStream checkStreamIsNotEmpty(InputStream inputStream) throws IOException, FileException {
        //Preconditions.checkArgument(inputStream != null,"The InputStream is mandatory");
        PushbackInputStream pushbackInputStream = new PushbackInputStream(inputStream);
        int b;
        b = pushbackInputStream.read();
        if ( b == -1 ) {
            throw new FileException("File is empty.",new IOException());
        }
        pushbackInputStream.unread(b);
        return pushbackInputStream;
    }
    @Override
    public boolean hasLoadedData() {
        return (stocks == null);
    }

    @Override
    public DTOStock getStock(String stockSymbol) {
        Stock stock = stocks.getStock(stockSymbol);
        if(stock == null)
            return null;
        return new DTOStock(stock);
    }

    @Override
    public DTOStocks getStocks() {
        if(hasLoadedData())
            return null;
        return new DTOStocks(stocks);
    }
    public DTOUsers getUsers(){
        if(hasLoadedData())
            return null;
        return new DTOUsers(users);
    }
    public DTOUser getUser(String userName){
        if(hasLoadedData())
            return null;
        return new DTOUser(users.getUser(userName));
    }

    private void
    updateUsers(List<Transaction> createdTransactions, boolean isBuy, String stockSymbol,
                             String initiator, int stockAmount){
        if(isBuy){
            for (Transaction transaction : createdTransactions) { //remove stock from all the sellers
                if (transaction.getInitiator().getName().equals(initiator) && transaction.isCompleted()) {
                    users.getUser(initiator).addToHoldings(stocks.getStock(stockSymbol), stockAmount); //add the buying
                    users.getUser(initiator).addFunds(transaction.getTransactionTotal());
                    System.out.println(users.getUser(initiator).toString());
                }
                if(transaction.isCompleted()) {
                    transaction.getExecutor().removeFromHoldings(stocks.getStock(stockSymbol),
                            transaction.getStockAmount());
                    users.getUser(initiator).subFunds(transaction.getTransactionTotal());
                    System.out.println(users.getUser(transaction.getExecutor().getName()).toString());
                }
            }
        }
        else {
            for (Transaction transaction : createdTransactions) { //add stock to all buyers
                if (transaction.getInitiator().getName().equals(initiator) && transaction.isCompleted()) {
                    users.getUser(initiator).removeFromHoldings(stocks.getStock(stockSymbol), stockAmount);
                    System.out.println(users.getUser(initiator).toString());
                    users.getUser(initiator).subFunds(transaction.getTransactionTotal());
                }//remove sold or to be sold
                if (transaction.isCompleted()) {
                    transaction.getExecutor().addToHoldings(stocks.getStock(stockSymbol),
                            transaction.getStockAmount());
                    users.getUser(initiator).addFunds(transaction.getTransactionTotal());
                    System.out.println(users.getUser(transaction.getExecutor().getName()).toString());
                }
            }
        }
    }
    @Override
    public List<DTOTransaction> LMTBuy(String stockSymbol, int stockAmount, int maxRate, String initiator) {
        checkRequestData(stockSymbol,stockAmount,maxRate);
        List<Transaction> createdTransactions = (stocks.getStock(stockSymbol).
                addTransaction(maxRate,stockAmount, Transaction.TransactionType.LMT
                        , Transaction.ActionType.BUY,users.getUser(initiator)));
        updateUsers(createdTransactions,true,stockSymbol,initiator,stockAmount);
        return (copyList(createdTransactions));
    }

    @Override
    public List<DTOTransaction> LMTSell(String stockSymbol, int stockAmount, int minRate, String initiator) {
        checkRequestData(stockSymbol,stockAmount,minRate);
        List<Transaction> createdTransactions = (stocks.getStock(stockSymbol).
                addTransaction(minRate,stockAmount, Transaction.TransactionType.LMT
                        , Transaction.ActionType.SELL,users.getUser(initiator)));
        updateUsers(createdTransactions,false,stockSymbol,initiator,stockAmount);
        return (copyList(createdTransactions));
    }
    @Override
    public List<DTOTransaction> MKTBuy(String stockSymbol, int stockAmount, String initiator)
    {
        checkRequestData(stockSymbol,stockAmount,1);
        Stock stock= stocks.getStock(stockSymbol);
        List<Transaction> createdTransactions = (stock.addTransaction
                (stock.getStockRate(), stockAmount,
                Transaction.TransactionType.MKT,
                Transaction.ActionType.BUY,users.getUser(initiator)));
        updateUsers(createdTransactions,true,stockSymbol,initiator,stockAmount);
            return (copyList(createdTransactions));
    }
    @Override
    public List<DTOTransaction> MKTSell(String stockSymbol, int stockAmount, String initiator)
    {
        checkRequestData(stockSymbol,stockAmount,1);
        Stock stock= stocks.getStock(stockSymbol);
        List<Transaction> createdTransactions = (stock.addTransaction
                (stock.getStockRate(), stockAmount,
                        Transaction.TransactionType.MKT,
                        Transaction.ActionType.SELL,users.getUser(initiator)));
        updateUsers(createdTransactions,false,stockSymbol,initiator,stockAmount);
        return (copyList(createdTransactions));
    }
    public List<DTOTransaction> IOCBuy(String stockSymbol, int stockAmount, int maxRate, String initiator) {
        checkRequestData(stockSymbol,stockAmount,maxRate);
        List<Transaction> createdTransactions = (stocks.getStock(stockSymbol).
                addTransaction(maxRate,stockAmount, Transaction.TransactionType.IOC
                        , Transaction.ActionType.BUY,users.getUser(initiator)));
        updateUsers(createdTransactions,true,stockSymbol,initiator,stockAmount);
        return (copyList(createdTransactions));
    }
    public List<DTOTransaction> IOCSell(String stockSymbol, int stockAmount, int minRate, String initiator) {
        checkRequestData(stockSymbol,stockAmount,minRate);
        List<Transaction> createdTransactions = (stocks.getStock(stockSymbol).
                addTransaction(minRate,stockAmount, Transaction.TransactionType.IOC
                        , Transaction.ActionType.SELL,users.getUser(initiator)));
        updateUsers(createdTransactions,false,stockSymbol,initiator,stockAmount);
        return (copyList(createdTransactions));
    }
    public List<DTOTransaction> FOKBuy(String stockSymbol, int stockAmount, int maxRate, String initiator) {
        checkRequestData(stockSymbol,stockAmount,maxRate);
        List<Transaction> createdTransactions = (stocks.getStock(stockSymbol).
                addTransaction(maxRate,stockAmount, Transaction.TransactionType.FOK
                        , Transaction.ActionType.BUY,users.getUser(initiator)));
        updateUsers(createdTransactions,true,stockSymbol,initiator,stockAmount);
        return (copyList(createdTransactions));
    }
    public List<DTOTransaction> FOKSell(String stockSymbol, int stockAmount, int minRate, String initiator) {
        checkRequestData(stockSymbol,stockAmount,minRate);
        List<Transaction> createdTransactions = (stocks.getStock(stockSymbol).
                addTransaction(minRate,stockAmount, Transaction.TransactionType.FOK
                        , Transaction.ActionType.SELL,users.getUser(initiator)));
        updateUsers(createdTransactions,false,stockSymbol,initiator,stockAmount);
        return (copyList(createdTransactions));
    }
    private void checkRequestData(String stockSymbol, int stockAmount, int rate)
    {
        if(stockSymbol.equals(""))
            throw new IllegalArgumentException("RizpaEngine.Stock must be chosen.");
        if(stockAmount == 0)
            throw new IllegalArgumentException("RizpaEngine.Stock amount has to be higher then 0.");
        if(rate == 0)
            throw new IllegalArgumentException("RizpaEngine.Stock rate has to be higher then 0.");
    }
    private List<DTOTransaction> copyList(List<Transaction> src) {
        List<DTOTransaction> copiedList = new LinkedList<>();
        for(Transaction transaction : src) {
            copiedList.add(new DTOTransaction(transaction));
        }
        return copiedList;
    }
    @Override
    public boolean addStock(String userName, String symbol, String companyName, int amount, int rate){
        if(stocks.hasStock(symbol))
            return false;
        User user = users.getUser(userName);
        Stock stock = new Stock(symbol,companyName,rate);
        stocks.getStockMap().put(symbol,stock);
        user.addToHoldings(stock,amount);
        return true;
    }
}
