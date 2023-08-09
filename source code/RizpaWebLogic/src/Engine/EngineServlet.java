package Engine;

import RizpaDTO.*;
import RizpaEngine.RizpaEngine;
import RizpaEngine.FileException;
import Users.UserManager;
import utils.ServletUtils;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class EngineServlet extends HttpServlet {
    private RizpaEngine engine = new RizpaEngine();

    public RizpaEngine getEngine(){
        return this.engine;
    }

    public DTOStocks getStock() { return this.engine.getStocks();}


    public void addUser(String userName, boolean isAdmin){
        engine.addUser(userName,isAdmin);
    }

    public int getUserFunds(String userName){
        return engine.getUser(userName).getUserFunds();
    }

    public List<String> getTransactionTable(String userName){
        List<String> transactions = new LinkedList<>();
        List<DTOTransaction> ls = engine.getUser(userName).getCompletedUserTransactions();
        for(DTOTransaction dtoTransaction : ls){
            transactions.add(dtoTransaction.toString());
        }
        return transactions;
    }
    public boolean addStock(String userName, String symbol, String companyName, int amount, int rate){
        return engine.addStock(userName,symbol,companyName,amount,rate);
    }
    public List<DTOTransaction> createTransaction(String symbol, int amount, int rate, String initiator,
                                                  String action, String bs){
        List<DTOTransaction> transactionList = new LinkedList<>();
        switch (action) {
            case "LMT":
                if(bs.equals("buy"))
                    transactionList = engine.LMTBuy(symbol,amount, rate,initiator);
                else
                    transactionList = engine.LMTSell(symbol,amount,rate,initiator);
                break;
            case "MKT":
                if(bs.equals("buy"))
                    transactionList = engine.MKTBuy(symbol,amount,initiator);
                else
                    transactionList = engine.MKTSell(symbol,amount,initiator);
                break;
            case "IOC":
                if(bs.equals("buy"))
                    transactionList = engine.IOCBuy(symbol,amount,rate,initiator);
                else
                    transactionList = engine.IOCSell(symbol,amount,rate,initiator);
                break;
            case "FOK":
                if(bs.equals("buy"))
                    transactionList = engine.FOKBuy(symbol,amount,rate,initiator);
                else
                    transactionList = engine.FOKSell(symbol,amount,rate,initiator);
                break;
        }
        return transactionList;
    }
}
