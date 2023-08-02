package RizpaEngine;

import RizpaDTO.*;
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

        stocks = null;
        users = null;
    }

    @Override
    public void loadDataFromFile(File loadFile) throws FileException {
        if(loadFile.length() == 0)
            throw new FileException("File is empty.",new IOException());
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(JAXB_PACKAGE_NAME);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            RizpaStockExchangeDescriptor temp =
                    (RizpaStockExchangeDescriptor) jaxbUnmarshaller.unmarshal(loadFile);
            stocks = new Stocks(temp.getRseStocks());
            users = new Users(temp.getRseUsers(), stocks);
        }
        catch (Exception e) {
            if(e.getMessage() == null)
                throw new FileException("No valid data found.",e.getCause());
            throw new FileException(e.getMessage(),e.getCause());
        }

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

    private void updateUsers(List<Transaction> createdTransactions, boolean isBuy, String stockSymbol,
                             String initiator, int stockAmount){
        if(isBuy){
            for (Transaction transaction : createdTransactions) { //remove stock from all the sellers
                if (transaction.getInitiator().getName().equals(initiator) && transaction.isCompleted()) {
                    users.getUser(initiator).addToHoldings(stocks.getStock(stockSymbol), stockAmount); //add the buying
                    System.out.println(users.getUser(initiator).toString());
                }
                if(transaction.isCompleted()) {
                    transaction.getExecutor().removeFromHoldings(stocks.getStock(stockSymbol),
                            transaction.getStockAmount());
                    System.out.println(users.getUser(transaction.getExecutor().getName()).toString());
                }
            }
        }
        else {
            for (Transaction transaction : createdTransactions) { //add stock to all buyers
                if (transaction.getInitiator().getName().equals(initiator) && transaction.isCompleted()) {
                    users.getUser(initiator).removeFromHoldings(stocks.getStock(stockSymbol), stockAmount);
                    System.out.println(users.getUser(initiator).toString());
                }//remove sold or to be sold
                if (transaction.isCompleted()) {
                    transaction.getExecutor().addToHoldings(stocks.getStock(stockSymbol),
                            transaction.getStockAmount());
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
}
