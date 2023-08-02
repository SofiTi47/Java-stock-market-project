package RizpaControllers;

import RizpaDTO.DTOStock;
import RizpaDTO.DTOStocks;
import RizpaDTO.DTOTransaction;
import RizpaUi.RizpaUi;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class adminTabController {
    @FXML Button cancelStockSearchAdmin;
    @FXML TextField stockSearchBoxAdmin;
    @FXML ListView<String> adminStockList;
    @FXML TableColumn<DTOTransaction, String> buyDate;
    @FXML TableColumn<DTOTransaction, String> buyActionType;
    @FXML TableColumn<DTOTransaction, String> buyAmount;
    @FXML TableColumn<DTOTransaction, String> buyRate;
    @FXML TableColumn<DTOTransaction, String> buyInitiator;

    @FXML TableColumn<DTOTransaction, String> sellDate;
    @FXML TableColumn<DTOTransaction, String> sellActionType;
    @FXML TableColumn<DTOTransaction, String> sellAmount;
    @FXML TableColumn<DTOTransaction, String> sellRate;
    @FXML TableColumn<DTOTransaction, String> sellInitiator;

    @FXML TableColumn<DTOTransaction, String> comDate;
    @FXML TableColumn<DTOTransaction, String> comActionType;
    @FXML TableColumn<DTOTransaction, String> comAmount;
    @FXML TableColumn<DTOTransaction, String> comRate;
    @FXML TableColumn<DTOTransaction, String> comInitiator;
    @FXML TableColumn<DTOTransaction, String> comExecutor;

    @FXML TableView<DTOTransaction> stockPendingBuy;
    @FXML TableView<DTOTransaction> stockPendingSell;
    @FXML TableView<DTOTransaction> stockCompleted;
    private RizpaUi rizpaUi;
    private Stage primaryStage;
    public adminTabController(){
    }
    public void initialize(){
        initBuyStockTableView();
        initSellStockTableView();
        initComStockTableView();
    }
    public void setRizpaUi(RizpaUi rizpaUi){
        this.rizpaUi = rizpaUi;
    }
    public void setPrimaryStage(Stage primaryStage){
        this.primaryStage = primaryStage;
    }
    private void initBuyStockTableView(){
        buyDate.setCellValueFactory(new PropertyValueFactory<>("timeStamp"));
        buyActionType.setCellValueFactory(new PropertyValueFactory<>("transactionType"));
        buyAmount.setCellValueFactory(new PropertyValueFactory<>("stockAmount"));
        buyRate.setCellValueFactory(new PropertyValueFactory<>("rate"));
        buyInitiator.setCellValueFactory(new PropertyValueFactory<>("initiatorName"));
        stockPendingBuy.setPlaceholder(new Label("No rows to display"));
    }
    private void initSellStockTableView(){
        sellDate.setCellValueFactory(new PropertyValueFactory<>("timeStamp"));
        sellActionType.setCellValueFactory(new PropertyValueFactory<>("transactionType"));
        sellAmount.setCellValueFactory(new PropertyValueFactory<>("stockAmount"));
        sellRate.setCellValueFactory(new PropertyValueFactory<>("rate"));
        buyInitiator.setCellValueFactory(new PropertyValueFactory<>("initiatorName"));
        stockPendingSell.setPlaceholder(new Label("No rows to display"));
    }
    private void initComStockTableView(){
        comDate.setCellValueFactory(new PropertyValueFactory<>("timeStamp"));
        comActionType.setCellValueFactory(new PropertyValueFactory<>("transactionType"));
        comAmount.setCellValueFactory(new PropertyValueFactory<>("stockAmount"));
        comRate.setCellValueFactory(new PropertyValueFactory<>("rate"));
        comInitiator.setCellValueFactory(new PropertyValueFactory<>("initiatorName"));
        comExecutor.setCellValueFactory(new PropertyValueFactory<>("executorName"));
        stockPendingSell.setPlaceholder(new Label("No rows to display"));
    }

    @FXML
    public void cancelStockSearchAdminAction(){ stockSearchBoxAdmin.clear(); }

    @FXML
    public void adminStockListAction(){
        if(adminStockList == null){
                Platform.runLater(()->CustomizablePromptDialog.show(primaryStage,
                        "Error! Please load the file again.", "Close"));
                return;
            }
        String symbol = adminStockList.getSelectionModel().getSelectedItem();
        if(symbol == null || symbol.equals(""))
            return;
        DTOStock stock = rizpaUi.getStock(symbol);
        final ObservableList<DTOTransaction> pendingBuy = FXCollections.observableArrayList(stock.getPendingBuy());
        final ObservableList<DTOTransaction> pendingSell = FXCollections.observableArrayList(stock.getPendingSell());
        ObservableList<DTOTransaction> completedTransaction =
                FXCollections.observableArrayList(stock.getCompletedTransactions());

        stockPendingBuy.setItems(pendingBuy);
        stockPendingSell.setItems(pendingSell);
        stockCompleted.setItems(completedTransaction);
    }
    public void createAdminSearchBox(){
        DTOStocks adminStocks = rizpaUi.getStocks();
        if(adminStocks == null) {
            Platform.runLater(()->CustomizablePromptDialog.show(primaryStage,
                    "Error! Please reload the program.", "Close"));
            return;
        }
        ObservableList<String> data = FXCollections.observableArrayList();
        for(DTOStock stock : adminStocks.getStockMap().values())
            data.add(stock.getStockSymbol());
        FilteredList<String> flStock = new FilteredList(data, p -> true);//Pass the data to a filtered list
        adminStockList.setItems(flStock);//Set the table's items using the filtered list
        stockSearchBoxAdmin.textProperty().addListener((obs, oldValue, newValue) -> {
            flStock.setPredicate(p -> p.toLowerCase().startsWith(newValue.toLowerCase().trim()));
        });
    }

}
