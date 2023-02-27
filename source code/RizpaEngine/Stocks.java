import src.JAXB.RseStock;
import src.JAXB.RseStocks;
import java.io.*;
import java.nio.file.Path;
import java.util.*;

public class Stocks{
    private Map<String, Stock> stockMap;

    public Stocks(RseStocks rseStocks) throws FileException {
        stockMap = new HashMap<>();
        for(RseStock rseStock : rseStocks.getRseStock())
        {
            Stock stock = new Stock(rseStock);
            checkDuplicates(stock);
            stockMap.put(stock.getStockSymbol(),stock);
        }
    }
    public Stocks(Path path) throws FileException {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream
                (path.toFile())));){
            stockMap = new HashMap<>();
            Scanner read = new Scanner(in);
            boolean first = true;
            read.useDelimiter("\n");
            String stockData;
            while (read.hasNext()) {
                if(!first) //skips the "\n" in between each stock
                    read.next();
                if(read.hasNext())
                    stockData = read.next();
                else
                    return;
                Stock stock = new Stock(stockData);
                checkDuplicates(stock); //Checks for stock and company duplicates
                getTransactions(read, stock.getPendingBuy(), "BUY");
                getTransactions(read, stock.getPendingSell(), "SELL");
                getTransactions(read, stock.getCompletedTransactions(), "EMPTY");
                stockMap.put(stock.getStockSymbol(),stock);
                if(first)
                    first = false;
            }
            read.close();
        }
        catch (Exception e) {
            throw new FileException("File data is not valid.",new IOException());
        }
    }
    private void getTransactions(Scanner read, List<Transaction> transactionList, String action) {
        boolean first = true;
        while(read.hasNext()) {
            String data = read.next();
            String type = data.split("=")[1];
            String[] values = data.split(", ");
            if(type.indexOf(']') != -1)
                return;
            else if (first) {
                type = data.split("=")[2].split(",")[0];
                first = false;
            }
            else
                type = values[0].split("=")[1];
            int stockRate = Integer.parseInt(values[1].split("=")[1]);
            int stockAmount = Integer.parseInt(values[2].split("=")[1]);
            String time = values[4].split("=")[1];
            String status = values[5].split("=")[1].split("}")[0];
            transactionList.add(new Transaction(stockRate,stockAmount,type,time,action,status));
        }
    }
    public boolean hasStock(String symbol)
    {
        return stockMap.containsKey(symbol.toUpperCase());
    }

    public Stock getStock(String symbol) {
        if(!stockMap.containsKey(symbol.toUpperCase()))
            return null;
        return stockMap.get(symbol.toUpperCase());
    }

    public Map<String, Stock> getStockMap() {
        return stockMap;
    }

    public void writeToFile (Path path) throws IOException {
        try (Writer out = new BufferedWriter(
                new OutputStreamWriter(
                        new FileOutputStream(path.toString())))) {
            out.write(this.toString());
        }
    }

    private void checkDuplicates(Stock stock) throws FileException {
        if(stockMap.containsKey(stock.getStockSymbol()))
            throw new FileException("Double stock symbol found.", new IOException());
        if(checkDuplicateCompany(stock.getCompanyName()))
            throw new FileException("Double company found.",new IOException());
    }
    private boolean checkDuplicateCompany(String companyName) {
        for(Stock stock : stockMap.values())
            if(stock.getCompanyName().equals(companyName))
                return true;
        return false;
    }
    //used to write to files
    @Override
    public String toString() {
        StringBuilder toString = new StringBuilder();
        for(Stock stock : stockMap.values()) {
            toString.append(stock.toString());
            toString.append("\n");
        }
        return toString.toString();
    }

}
