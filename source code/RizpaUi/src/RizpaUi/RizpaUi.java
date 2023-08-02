package RizpaUi;

import RizpaDTO.*;
import RizpaEngine.Engine;
import RizpaControllers.rizpaUiController;
import RizpaEngine.RizpaEngine;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class RizpaUi implements Ui {
    private final Engine engine;

    private enum StockActions { //cancel always last
        LMT,MKT;
    }
    private enum ActionTypes { //cancel always last
        BUY,SELL;
    }
    public RizpaUi() {
        this.engine = new RizpaEngine();
    }

    public RizpaUi(rizpaUiController rizpaUiController){
        this.engine = new RizpaEngine();
    }
    @Override
    public void loadFile(File loadFile) throws IOException {
        if(loadFile == null)
            return;
        try{engine.loadDataFromFile(loadFile);}
        catch (Exception e)
        {
            throw new IOException("Loaded file is not valid reason:\n"+e.toString());
        }
    }

    @Override
    public DTOStocks getStocks() {
        return engine.getStocks();
    }
    public DTOUsers getUsers(){
        return engine.getUsers();
    }

    @Override
    public DTOStock getStock(String symbol) {
        DTOStock stock = engine.getStock(symbol);
        System.out.println(stock.toString());
        return stock;
    }

    @Override
    public String tradeStocks(String initiator, String inputAction, String inputActionType, String stockSymbol,
                            String inputStockAmount, String rate){
        ActionTypes action = getActionType(inputAction);
        StockActions actionType = getAction(inputActionType);
        String res = "";
        int stockAmount;
        try {
            stockAmount = Integer.parseInt(inputStockAmount);
        }
        catch (Exception e){
            throw new IllegalArgumentException("Amount cannot be empty");
        }
        try {
            switch (actionType) {
                case LMT:
                    res = stockLMT(action, stockSymbol, stockAmount, initiator, rate);
                    break;
                case MKT:
                    res = stockMKT(action, stockSymbol, stockAmount, initiator);
                    break;
            }
        }
        catch (Exception e) {
            throw e;
        }
        return res;
    }
    public DTOUser getUser(String userName){
        return engine.getUser(userName);
    }
    private String stockLMT(ActionTypes action, String stockSymbol, int stockAmount, String initiator, String stockRate) {
        List<DTOTransaction> createdTransactions = null;
        int rate = 0;
        if(stockRate == null || stockRate.equals(""))
            throw new IllegalArgumentException("Rate cannot be empty.");
        try {
            rate = Integer.parseInt(stockRate);
        }
        catch (Exception e){
            throw new IllegalArgumentException("Rate must be a numerical value.");
        }
        try {
           switch (action) {
               case BUY:
                   createdTransactions = engine.LMTBuy(stockSymbol, stockAmount, rate,initiator);
                   break;
               case SELL:
                   createdTransactions = engine.LMTSell(stockSymbol, stockAmount, rate,initiator);
                   break;
           }
       }
        catch (Exception e) {
            throw e;
        }
       if(createdTransactions != null) {
           return printCreatedTransactions(createdTransactions);
       }
       return null;
    }
    private String stockMKT(ActionTypes action, String stockSymbol, int stockAmount, String initiator) {
        List<DTOTransaction> createdTransactions = null;
        try {
            switch (action) {
                case BUY:
                    createdTransactions = engine.MKTBuy(stockSymbol, stockAmount,initiator);
                    break;
                case SELL:
                    createdTransactions = engine.MKTSell(stockSymbol, stockAmount,initiator);
                    break;
            }
        }
        catch (Exception e) {
            throw e;
        }
        if(createdTransactions != null)
           return printCreatedTransactions(createdTransactions);
        return null;
    }

    private String printCreatedTransactions(List<DTOTransaction> createdTransactions)
    {
        Iterator<DTOTransaction> itr = createdTransactions.iterator();
        DTOTransaction transaction = itr.next(); //the original request always in the list
        StringBuilder res = new StringBuilder();
        int i = 1;
        if(transaction.isCompleted() && !itr.hasNext())
            res.append("Transaction pending!");
        else if(transaction.isCompleted())
            res.append("Transaction not fully completed!");
        else
            res.append("Transaction completed!");
        if(itr.hasNext())
            res.append("Completed transactions:").append("\n");
        while(itr.hasNext()) {
            if(i == createdTransactions.size()-1 && transaction.isCompleted())
                res.append("Remaining transaction:\n").append(itr.next().getBasicData());
            else {
                i++;
                res.append(itr.next().toString()).append("\n");
            }
        }
        return res.toString();
    }
    private StockActions getAction(String action){
        if(action == null || action.equals(""))
            throw new IllegalArgumentException("Action type cannot be empty.");
        if(action.equals("Limit"))
            return StockActions.LMT;
        else
            return StockActions.MKT;
    }
    private ActionTypes getActionType(String actionType){
        if(actionType.equals("Buy"))
            return ActionTypes.BUY;
        else
            return ActionTypes.SELL;
    }
}

