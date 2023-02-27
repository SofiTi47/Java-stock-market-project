import src.JAXB.RizpaStockExchangeDescriptor;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.nio.file.Path;
import java.util.*;

public class RizpaEngine implements Engine{

    private Stocks stocks;
    private final static String JAXB_PACKAGE_NAME = "src.JAXB";

    public RizpaEngine()
    {
        stocks = null;
    }

    @Override
    public void loadDataFromFile(Path filePath) throws FileException {
        File file = filePath.toFile();
        if(!checkIfXml(file)) {
            throw new FileException("Not an xml file.",new IllegalArgumentException());
        }
        if(file.length() == 0)
            throw new FileException("File is empty.",new IOException());
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(JAXB_PACKAGE_NAME);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            RizpaStockExchangeDescriptor temp =
                    (RizpaStockExchangeDescriptor) jaxbUnmarshaller.unmarshal(file);
            stocks = new Stocks(temp.getRseStocks());
        }
        catch (Exception e) {
            if(e.getMessage() == null)
                throw new FileException("No valid data found.",e.getCause());
            throw new FileException(e.getMessage(),e.getCause());
        }

    }
    private boolean checkIfXml(File file)
    {
        String fileName = file.getName();
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf(".")+1).equalsIgnoreCase("xml");
        return false;
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

    @Override
    public List<DTOTransaction> LMTBuy(String stockSymbol, int stockAmount, int maxRate) {
        checkRequestData(stockSymbol,stockAmount,maxRate);
        List<Transaction> createdTransactions = (stocks.getStock(stockSymbol).
                addTransaction(maxRate,stockAmount, Transaction.TransactionType.LMT
                        ,Transaction.ActionType.BUY));
        return (copyList(createdTransactions));
    }

    @Override
    public List<DTOTransaction> LMTSell(String stockSymbol, int stockAmount, int minRate) {
        checkRequestData(stockSymbol,stockAmount,minRate);
        List<Transaction> createdTransactions = (stocks.getStock(stockSymbol).
                addTransaction(minRate,stockAmount, Transaction.TransactionType.LMT
                        ,Transaction.ActionType.SELL));
        return (copyList(createdTransactions));
    }
    @Override
    public List<DTOTransaction> MKTBuy(String stockSymbol, int stockAmount)
    {
        checkRequestData(stockSymbol,stockAmount,1);
        Stock stock= stocks.getStock(stockSymbol);
        List<Transaction> createdTransactions = (stock.addTransaction
                (stock.getStockRate(), stockAmount,
                Transaction.TransactionType.MKT,
                Transaction.ActionType.BUY));
        return (copyList(createdTransactions));
    }
    @Override
    public List<DTOTransaction> MKTSell(String stockSymbol, int stockAmount)
    {
        checkRequestData(stockSymbol,stockAmount,1);
        Stock stock= stocks.getStock(stockSymbol);
        List<Transaction> createdTransactions = (stock.addTransaction
                (stock.getStockRate(), stockAmount,
                        Transaction.TransactionType.MKT,
                        Transaction.ActionType.SELL));
        return (copyList(createdTransactions));
    }

    private void checkRequestData(String stockSymbol, int stockAmount, int rate)
    {
        if(stockAmount == 0)
            throw new IllegalArgumentException("Stock amount has to be higher then 0");
        if(rate == 0)
            throw new IllegalArgumentException("Stock rate has to be higher then 0");
        if(!stocks.hasStock(stockSymbol))
            throw new NoSuchElementException("Stock not found");
    }
    private List<DTOTransaction> copyList(List<Transaction> src) {
        List<DTOTransaction> copiedList = new LinkedList<>();
        for(Transaction transaction : src) {
            copiedList.add(new DTOTransaction(transaction));
        }
        return copiedList;
    }
    @Override
    public void writeToFile (Path path) throws FileException{
        assert path != null;
        if(checkIfTxt(path.toFile()))
            throw new FileException("Not a supported format.\nAllowed formats are: .txt" +
                    ",.dox,.docx",new IllegalArgumentException());
        try {
            stocks.writeToFile(path);
        }
        catch (IOException e)
        {
            throw new FileException(e.getMessage(),e.getCause());
        }
    }

    @Override
    public void readFromFile(Path path) throws FileException
    {
        if(checkIfTxt(path.toFile()))
            throw new FileException("Not a supported format.\nAllowed formats are: .txt" +
                    ",.dox,.docx",new IllegalArgumentException());
        try{stocks = new Stocks(path);}
        catch (FileException e) {
            throw new FileException(e.getMessage(),e.getCause());
        }
    }
    private boolean checkIfTxt(File file)
    {
        String fileName = file.getName();
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
            String name = fileName.substring(fileName.lastIndexOf(".") + 1);
            return !name.equalsIgnoreCase("txt") &&
                    !name.equalsIgnoreCase("doc") &&
                    !name.equalsIgnoreCase("docx");
        }
        return true;
    }
}
