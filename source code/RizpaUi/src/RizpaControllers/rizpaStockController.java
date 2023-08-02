package RizpaControllers;

import RizpaDTO.DTOHoldings;
import RizpaDTO.DTOStock;
import RizpaDTO.DTOStockItem;
import RizpaDTO.DTOUser;
import RizpaUi.RizpaUi;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class rizpaStockController {
    @FXML GridPane popOutStock;
    @FXML TextField userField;
    @FXML TextField actionField;
    @FXML ComboBox<String> stockComboBox;
    @FXML TextField stockRateField;
    @FXML TextField stockAmountField;
    @FXML ComboBox<String> stockActionType;
    @FXML Button confirmButton;
    @FXML Button cancelButton;

    private Stage primaryStage;
    private RizpaUi rizpaUi;
    private SimpleBooleanProperty isBuy;

    public rizpaStockController(){ }
    public void setUiLogic(RizpaUi rizpaUi) {
        this.rizpaUi = rizpaUi;
    }
    public void setBuyAction(boolean val) {
        if (val) {
            actionField.setText("Buy");
            for(DTOStock stock : rizpaUi.getStocks().getStockMap().values()){
                stockComboBox.getItems().add(stock.getStockSymbol());
            }
        }
        else {
            actionField.setText("Sell");
        }

        isBuy = new SimpleBooleanProperty(val);
    }
    public void setUser(DTOUser user){
        userField.setText(user.getName());
        if(!isBuy.getValue()) { //we can sell only what we have
            DTOHoldings holdings = user.getUserHoldings();
            for (DTOStockItem stockItem : holdings.getUserHoldings().values()) {
                stockComboBox.getItems().add(stockItem.getStockSymbol());
            }
        }
    }
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
    @FXML
    public void initialize() {
       // userField.setText(user.get());
        stockActionType.getItems().add("Limit");
        stockActionType.getItems().add("Market");
        stockActionType.setOnAction(e -> {
            stockRateField.setDisable(stockActionType.getValue().equals("Market"));
        });
    }
    @FXML
    public void cancelButtonAction(){
        Stage stage = (Stage)cancelButton.getScene().getWindow();
        stage.close();
    }
    @FXML
    public void confirmButtonAction(){
        String res;
            try {
                res = rizpaUi.tradeStocks(userField.getText(), actionField.getText(), stockActionType.getValue(),
                        stockComboBox.getValue(), stockAmountField.getText(), stockRateField.getText());
            }
            catch (Exception e) {
                CustomizablePromptDialog.show(primaryStage,e.getMessage(),"Close");
                return;
            };
        CustomizablePromptDialog.show(primaryStage,res,"Close");
        Stage stage = (Stage)confirmButton.getScene().getWindow();
        stage.close();
        };
    }

