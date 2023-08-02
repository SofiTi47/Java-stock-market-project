package RizpaControllers;

import RizpaDTO.DTOStockItem;
import RizpaDTO.DTOUser;
import RizpaUi.RizpaUi;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;

public class userTabController {
    private Tab userTab;
    private DTOUser user;
    private TextField searchBox;
    private TableView<DTOStockItem> stockTable;
    private RizpaUi rizpaUi;
    private Stage primaryStage;
    private SimpleIntegerProperty totalHoldingsSum;

    public userTabController(){ }
    public void initialize(){
        userTab = new Tab();
        userTab.setOnSelectionChanged(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                refreshListView();
            }
        });
        searchBox = new TextField();
        stockTable = new TableView<>();
    }
    public void setRizpaUi(RizpaUi rizpaUi){
        this.rizpaUi = rizpaUi;
    }
    public void setPrimaryStage(Stage primaryStage){
        this.primaryStage = primaryStage;
        userTab.setId("userTab");
        userTab.setText(user.getName());
        this.user = user;
        ToolBar toolBar = new ToolBar();
        BorderPane borderPane = new BorderPane();
        Button cancelSearch = createCancelButton();
        Button refreshButton = createRefreshButton();
        stockTable = createUserTableView();
        Button buyButton = createBuyButton(rizpaUi,primaryStage);
        Button sellButton = createSellButton(rizpaUi,primaryStage);
        toolBar.getItems().addAll(searchBox,cancelSearch,buyButton,sellButton,refreshButton);
        VBox rightVbox = createRightVbox();
        borderPane.setTop(toolBar);
        borderPane.setCenter(stockTable);
        borderPane.setRight(rightVbox);
        userTab.setContent(borderPane);
        cancelSearch.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                searchBox.clear();
            }
        });
    }
    public void setUser(DTOUser user){
        this.user = user;
    }
    private VBox createRightVbox(){
        VBox vBox = new VBox();
        vBox.setPrefHeight(418.0);
        vBox.setPrefWidth(184.0);
        Label totalLabel = new Label();
        totalLabel.setText("Total Holdings: ");
        Label totalHoldings = new Label();
        totalHoldings.textProperty().bind(Bindings.format("%,d",user.getUserHoldings().getHoldingsTotal()));
        Separator separator = new Separator();
        separator.setPrefHeight(15.0);
        separator.setPrefWidth(184.0);
        vBox.getChildren().addAll(totalLabel,totalHoldings,separator);
        return vBox;
    }
    public Tab getTab(){
        return userTab;
    }
    private Button createCancelButton(){
        Button cancelStock = new Button();
        cancelStock.setText("X");
        cancelStock.setFocusTraversable(false);
        cancelStock.setMnemonicParsing(false);
        cancelStock.setPrefHeight(27.0);
        cancelStock.setPrefWidth(28.0);
        cancelStock.setStyle("-fx-border-width: 1 1 1 0; -fx-border-color: gray; -fx-border-radius: 0 5 5 0;");
        cancelStock.setTranslateX(-8.0);
        cancelStock.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                searchBox.clear();
            }
        });
        return cancelStock;
    }
    private Button createBuyButton(RizpaUi rizpaUi, Stage primaryStage){
        Button buyButton = new Button();
        buyButton.setId("buyButton");
        buyButton.setAlignment(Pos.TOP_CENTER);
        buyButton.setMnemonicParsing(false);
        buyButton.setText("Buy");
        buyButton.setTextAlignment(TextAlignment.CENTER);
        Tooltip buyTip = new Tooltip();
        buyTip.setText("Buy stock");
        buyButton.setTooltip(buyTip);
        buyButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                openStockWindow(true,user,rizpaUi,primaryStage);
            }
        });
        return buyButton;
    }
    private Button createRefreshButton(){
        Button refreshButton = new Button();
        refreshButton.setId("refreshButton");
        refreshButton.setAlignment(Pos.TOP_CENTER);
        refreshButton.setMnemonicParsing(false);
        refreshButton.setText("Refresh");
        refreshButton.setTextAlignment(TextAlignment.CENTER);
        Tooltip buyTip = new Tooltip();
        buyTip.setText("Refresh stock table view");
        refreshButton.setTooltip(buyTip);
        refreshButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                refreshListView();
            }
        });
        return refreshButton;
    }
    private Button createSellButton(RizpaUi rizpaUi, Stage primaryStage){
        Button sellButton = new Button();
        sellButton.setId("sellButton");
        sellButton.setAlignment(Pos.TOP_CENTER);
        sellButton.setMnemonicParsing(false);
        //sellButton.setOnAction(sellButtonAction());
        sellButton.setText("Sell");
        sellButton.setTextAlignment(TextAlignment.CENTER);
        Tooltip sellTip = new Tooltip();
        sellTip.setText("Sell stock");
        sellButton.setTooltip(sellTip);
        sellButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                openStockWindow(false,user,rizpaUi,primaryStage);
            }
        });
        return sellButton;
    }
    private void createSearchBox(ObservableList<DTOStockItem> data){
        searchBox.setId("searchBox");
        searchBox.setPrefHeight(27.0);
        searchBox.setPrefWidth(103.0);
        searchBox.setPromptText("Search stock...");
        searchBox.setStyle("-fx-background-color: transparent; -fx-border-width: 1 1 1 1; -fx-border-color: gray;" +
                "-fx-border-radius: 5 5 0 5");
        FilteredList<DTOStockItem> flStock = new FilteredList(data, p -> true);//Pass the data to a filtered list
        stockTable.setItems(flStock);//Set the table's items using the filtered list
        searchBox.textProperty().addListener((obs, oldValue, newValue) -> {
            flStock.setPredicate(p -> p.getStockSymbol().toLowerCase().startsWith(newValue.toLowerCase().trim()));
        });
    }
    private TableView<DTOStockItem> createUserTableView(){
        stockTable.setId("stockTable");
        stockTable.setFocusTraversable(false);
        stockTable.setPrefHeight(418.0);
        stockTable.prefWidth(549.0);
        TableColumn<DTOStockItem, String> symbol = new TableColumn<>();
        symbol.setEditable(false);
        symbol.setText("Symbol");
        symbol.setCellValueFactory(new PropertyValueFactory<>("stockSymbol"));
        TableColumn<DTOStockItem, String> amount = new TableColumn<>();
        amount.setEditable(false);
        amount.setText("Amount");
        amount.setCellValueFactory(new PropertyValueFactory<>("stockAmount"));
        TableColumn<DTOStockItem, String> currentRate = new TableColumn<>();
        currentRate.setEditable(false);
        currentRate.setText("Current rate");
        currentRate.setPrefWidth(105.0);
        currentRate.setCellValueFactory(new PropertyValueFactory<>("currentRate"));
        TableColumn<DTOStockItem, String> totalHolding = new TableColumn<>();
        totalHolding.setEditable(false);
        totalHolding.setText("Holding total");
        totalHolding.setPrefWidth(108.0);
        totalHolding.setCellValueFactory(new PropertyValueFactory<>("itemTotal"));
        stockTable.getColumns().addAll(symbol,amount,currentRate,totalHolding);
        stockTable.setFocusTraversable(false);
        fillTableView();
        return stockTable;
    }
    private void fillTableView(){
        final ObservableList<DTOStockItem> data = FXCollections.observableArrayList(
                rizpaUi.getUser(user.getName()).getUserHoldingsMap().values());
        totalHoldingsSum = new SimpleIntegerProperty(user.getUserHoldings().getHoldingsTotal());
        createSearchBox(data);
    }
    private void openStockWindow(Boolean isBuy, DTOUser user, RizpaUi rizpaUi, Stage primaryStage){
        FXMLLoader loader = new FXMLLoader();
        URL url = getClass().getResource("/resources/popoutstock.fxml");
        try {
            GridPane layout = loader.load(url.openStream());
            Scene secondScene = new Scene(layout);
            rizpaStockController stockController = loader.getController();
            stockController.setUiLogic(rizpaUi);
            stockController.setBuyAction(isBuy);
            stockController.setUser(user);
            Stage newWindow = new Stage();
            newWindow.setTitle("RizpaEngine.Transaction Form");
            newWindow.setScene(secondScene);
            newWindow.initModality(Modality.APPLICATION_MODAL);
            newWindow.initOwner(primaryStage);
            newWindow.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void refreshListView(){
        fillTableView();
    }
}
