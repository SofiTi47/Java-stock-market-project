import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class RizpaUi implements Ui{
    private Engine engine;

    public enum MenuOptions { //exit always last
        LOADFILE,GETSTOCKS,GETSTOCK,STOCKACTION,PENDINGACTIONS,SAVEFILE,LOADSAVED,EXIT;
    }
    private enum StockActions { //cancel always last
        LMT,MKT,CANCEL;
    }
    private enum ActionTypes { //cancel always last
        BUY,SELL,CANCEL;
    }
    public static final String cancel = "-1"; //cancellation escape char

    public RizpaUi() {
        this.engine = new RizpaEngine();
        System.out.println("Welcome to the Rizpa exchange system!");
    }

    @Override
    public void loadFile() {
        Path path = getLoadPath();
        if(path == null)
            return;
        try{engine.loadDataFromFile(path);}
        catch (Exception e)
        {
            System.out.println("Loaded file is not valid reason: "+e.toString()+" Please try again.\n" +
                    "Returning to main menu...");
            return;
        }
        System.out.println("File loaded successfully!");
    }

    @Override
    public void getStocks() {
        if(hasLoadedData()) {
            DTOStocks stocks = engine.getStocks();
            System.out.println(stocks.basicToString());
        }
    }

    @Override
    public void getStock() {
        if(hasLoadedData()) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Please enter the stock symbol or "+cancel+" to cancel");
            String input = scanner.next();
            if(input.equals(cancel))
                return;
            DTOStock stock = engine.getStock(input);
            while (stock == null) {
                System.out.println("Stock not found! Try again or enter "+cancel+" to cancel");
                input = scanner.next();
                if (input.equals(cancel))
                    return;
                stock = engine.getStock(input);
            }
            System.out.println(stock.toString());
        }
    }

    @Override
    public void tradeStocks() {
        if(hasLoadedData()) {
            printStockActions();
            StockActions action = getCommand(StockActions.class);
            if(action == StockActions.CANCEL)
                return;
            Scanner scanner = new Scanner(System.in);
            System.out.println("Please choose the following: \n" +
                    "1)Buy a stock\n" +
                    "2)Sell a stock\n" +
                    "3)Cancel");
            ActionTypes actionType = getCommand(ActionTypes.class);
            if(actionType == ActionTypes.CANCEL)
                return;
            System.out.println("Please enter the stock symbol or "+cancel+" to cancel");
            String stockSymbol = scanner.next();
            if(stockSymbol.equals(cancel))
                return;
            System.out.println("Please enter the stock amount or "+cancel+" to cancel");
            int stockAmount = getInteger();
            if(stockAmount == Integer.parseInt(cancel))
                return;
            switch (action) {
                case LMT:
                    stockLMT(actionType,stockSymbol,stockAmount);
                    break;
                case MKT:
                    stockMKT(actionType,stockSymbol,stockAmount);
                    break;
            }
        }
    }
    private void stockLMT(ActionTypes action, String stockSymbol, int stockAmount) {
        List<DTOTransaction> createdTransactions = null;
        int rate;
        try {
           switch (action) {
               case BUY:
                   System.out.println("Please enter maximum stock rate or "+cancel+" to cancel");
                   rate = getValue();
                   if(rate == Integer.parseInt(cancel))
                       return;
                   createdTransactions = engine.LMTBuy(stockSymbol, stockAmount, rate);
                   break;
               case SELL:
                   System.out.println("Please enter minimum stock rate or "+cancel+" to cancel");
                   rate = getValue();
                   if(rate == Integer.parseInt(cancel))
                       return;
                   createdTransactions = engine.LMTSell(stockSymbol, stockAmount, rate);
                   break;
           }
       }
        catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Returning to main menu...");
        }
       if(createdTransactions != null) {
            printCreatedTransactions(createdTransactions);
       }
    }
    private void stockMKT(ActionTypes action, String stockSymbol, int stockAmount) {
        List<DTOTransaction> createdTransactions = null;
        try {
            switch (action) {
                case BUY:
                    createdTransactions = engine.MKTBuy(stockSymbol, stockAmount);
                    break;
                case SELL:
                    createdTransactions = engine.MKTSell(stockSymbol, stockAmount);
                    break;
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Returning to main menu...");
        }
        if(createdTransactions != null)
            printCreatedTransactions(createdTransactions);
    }

    private void printCreatedTransactions(List<DTOTransaction> createdTransactions)
    {
        Iterator<DTOTransaction> itr = createdTransactions.iterator();
        DTOTransaction transaction = itr.next(); //the original request always in the list
        int i = 1;
        if(transaction.isCompleted() && !itr.hasNext())
            System.out.println("Transaction pending!");
        else if(transaction.isCompleted())
            System.out.println("Transaction not fully completed!");
        else
            System.out.println("Transaction completed!");
        System.out.println("Transaction details:"+transaction);
        if(itr.hasNext())
            System.out.println("Completed transactions:");
        while(itr.hasNext()) {
            if(i == createdTransactions.size()-1 && transaction.isCompleted())
                System.out.println("Remaining transaction:"+itr.next().getBasicData());
            else {
                i++;
                System.out.println(itr.next().toString());
            }
        }
    }
    @Override
    public void getPendingTransactions() {
        if(hasLoadedData()) {
            DTOStocks stocks = engine.getStocks();
            for(DTOStock stock : stocks.getStockMap().values())
            {
                System.out.println(stock.getStockSymbol() +"\n"
                        +"Pending buy transaction:"+stock.listToString(stock.getPendingBuy())+"\n"
                        +"Pending sell transaction:"+stock.listToString(stock.getPendingSell())+"\n"
                        +"Completed transactions:"+stock.listToString(stock.getCompletedTransactions())+"\n");
            }
        }
    }
    @Override
    public void saveSystemData() {
        if(hasLoadedData()) {
            Path path = getSavePath();
            if (path == null)
                return;
            try {
                engine.writeToFile(path);
            } catch (FileException ex) {
                System.out.println(ex.getMessage());
            }
            System.out.println("File created successfully!");
        }
    }
    @Override
    public void loadSavedData() {
        Path path = getLoadPath();
        if(path == null)
            return;
        try {engine.readFromFile(path);}
        catch(FileException e) {
            System.out.println("Loaded file is not valid reason: "+e.toString()+" Please try again.\n" +
                    "Returning to main menu...");
            return;
        }
        System.out.println("File loaded successfully!");
    }

    @Override
    public void exitSystem() throws InterruptedException {
        System.out.println("Thank you for using Rizpa exchange system! We hope to see you soon!");
        TimeUnit.SECONDS.sleep(4);
        System.exit(0);
    }

    @Override
    public void printMenu() {
        System.out.println("Please enter the number of option: \n" +
                "1)Load data from file\n" +
                "2)Get all stock information\n" +
                "3)Get stock information\n" +
                "4)Trade stocks\n" +
                "5)Get all pending transactions\n" +
                "6)Save system data\n" +
                "7)Load saved system data from file\n"+
                "8)Exit system");
    }
    private void printStockActions() {
        System.out.println("Please choose from the following: \n" +
                "1)Limit\n" +
                "2)Market\n" +
                "3)Cancel");
    }
    private int getInteger() {
        Scanner scanner = new Scanner(System.in);
        while(!scanner.hasNextInt()) {
            System.out.println("Invalid input! Please insert a numerical value.\n");
            scanner.next();
        }
        return(scanner.nextInt());
    }
    private int getValue() {
        int num = -2;
        while(num <= 0) {
            num = getInteger();
            if(num <= 0 && num != Integer.parseInt(cancel))
                System.out.println("Please enter a value bigger then 0.\n");
        }
        return num;
    }
    @Override
    public <E extends Enum<E>> E getCommand(Class<E> clazz) {
        int numAction = -1;
        int enumSize = clazz.getEnumConstants().length;
        while(numAction <= 0 || numAction >enumSize) {
            numAction = getInteger();
            if(numAction <= 0 || numAction > enumSize)
                System.out.println("Invalid input! Please enter a command in range : 1-"+
                        enumSize);
        }
        return clazz.getEnumConstants()[numAction-1];
    }
    private boolean hasLoadedData() {
        if(engine.hasLoadedData()) {
            System.out.println("No data in system! Please load a file first\n" +
                    "Returning to main menu...");
            return false;
        }
        return true;
    }

    private Path getSavePath() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please input the full file path (supported formats are: .txt,.doc,.docx): ");
        Path path = Paths.get(scanner.nextLine());
        while(Files.isRegularFile(path)) {
            System.out.println("File with the same name found! Try again or choose from the following: " +
                    "\n1)Save anyway" +
                    "\n2)Cancel");
            scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            if(input.equals("2"))
                return null;
            if(input.equals("1"))
                return path;
            path = Paths.get(input);
        }
        return path;
    }
    private Path getLoadPath() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please input the full file path: ");
        Path path = Paths.get(scanner.nextLine());
        while(!Files.isRegularFile(path)) {
            System.out.println("File not found! Please try again or enter 0 to go back to main menu.");
            scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            if(input.equals("0"))
                return null;
            path = Paths.get(input);
        }
        return path;
    }
}

