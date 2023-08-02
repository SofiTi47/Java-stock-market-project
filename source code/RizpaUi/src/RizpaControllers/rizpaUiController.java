package RizpaControllers;

import RizpaUi.RizpaUi;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;

public class rizpaUiController {
    @FXML MenuItem fileDialog;
    @FXML BorderPane rizpaBorderPane;
    @FXML TabPane rizpaTabPane;
    @FXML Label currentLoading;
    @FXML ProgressBar loadingBar;
    @FXML Button closeLoadingButton;
    @FXML MenuItem close;

    private Stage primaryStage;
    private RizpaUi rizpaUi;
    public static boolean hasStockData;
    private RizpaControllers.adminTabController adminTabController;
    public static ProgressBar loadingBarStatic;
    public static Label currentLoadingStatic;
    public static Button closeLoadingButtonStatic;

    public rizpaUiController(){
    }
    public void setUiLogic(RizpaUi rizpaUi) { this.rizpaUi = rizpaUi; }
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void initialize() {
        loadingBarStatic = loadingBar;
        currentLoadingStatic = currentLoading;
        closeLoadingButtonStatic = closeLoadingButton;
    }
    @FXML
    public void fileDialogAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select XML file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML files", "*.xml"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile == null) {
            return;
        }
        loadFileData(selectedFile);
    }
    private void loadFileData(File selectedFile){
        fileLoadTask fileLoadTask = new fileLoadTask();
        loadingBar = new ProgressBar();
        currentLoading = new Label();
        closeLoadingButton = new Button();
        closeLoadingButton.setDisable(true);
        openLoadingScreen();
        FXMLLoader loader = new FXMLLoader();
        URL url = getClass().getResource("/resources/adminTab.fxml");
       try{
           Tab tabLayout = (Tab) loader.load(url.openStream());
           rizpaTabPane.getTabs().add(tabLayout);
           adminTabController = loader.getController();
           adminTabController.setRizpaUi(rizpaUi);
           adminTabController.setPrimaryStage(primaryStage);
           fileLoadTask = new fileLoadTask(selectedFile,rizpaUi,adminTabController,primaryStage,rizpaTabPane);
           new Thread(fileLoadTask).start();
       }
       catch (Exception e) {
       }
    }
    private void openLoadingScreen(){
        FXMLLoader loader = new FXMLLoader();
        URL url = getClass().getResource("/resources/loadingScreen.fxml"); //define string later
        try {
            GridPane layout = loader.load(url.openStream());
            Scene secondScene = new Scene(layout);
            Stage newWindow = new Stage();
            newWindow.setTitle("Loading...");
            newWindow.setScene(secondScene);
            newWindow.initModality(Modality.APPLICATION_MODAL);
            newWindow.initOwner(primaryStage);
            newWindow.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void closeLoadingAction(){
        Stage stage = (Stage) closeLoadingButton.getScene().getWindow();
        stage.close();
    }
    @FXML
    public void closeAction(){
        primaryStage.close();
    }
}
