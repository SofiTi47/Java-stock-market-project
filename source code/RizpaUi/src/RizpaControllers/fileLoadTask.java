package RizpaControllers;

import RizpaDTO.DTOUser;
import RizpaDTO.DTOUsers;
import RizpaUi.RizpaUi;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

import java.io.File;

public class fileLoadTask extends Task<Boolean> {
    private File file;
    private RizpaUi rizpaUi;
    private adminTabController adminTab;
    private Stage primaryStage;
    private TabPane rizpaTabPane;

    public fileLoadTask(File file, RizpaUi rizpaUi, adminTabController adminTab, Stage primaryStage,
                        TabPane rizpaTabPane) {
        this.adminTab = adminTab;
        this.file = file;
        this.rizpaUi = rizpaUi;
        this.primaryStage = primaryStage;
        this.rizpaTabPane = rizpaTabPane;
    }
    public fileLoadTask(){}

    @Override
    protected Boolean call() throws Exception {
        try {
            updateMessageLabel("Fetching file...");
            sleepForAWhile(20);
            updateMessageLabel("Fetching stocks...");
            rizpaUi.loadFile(file);
            if(rizpaUiController.hasStockData)
                Platform.runLater(()->rizpaTabPane.getTabs().remove(1,rizpaTabPane.getTabs().size()));
            updateLoadingBar(0, rizpaUi.getStocks().getStockMap().size());
            updateLoadingBar(rizpaUi.getStocks().getStockMap().size(), rizpaUi.getStocks().getStockMap().size());
            updateMessageLabel("Getting users...");
            sleepForAWhile(20);
            DTOUsers users = rizpaUi.getUsers();
            updateLoadingBar(1, rizpaUi.getUsers().getUserMap().size());
            int progress = 0;
            for (DTOUser user : users.getUserMap().values()) {
                Platform.runLater(() -> {
                    userTabController newTab = new userTabController();
                    newTab.initialize();
                    newTab.setRizpaUi(rizpaUi);
                    newTab.setUser(user);
                    newTab.setPrimaryStage(primaryStage);
                    rizpaTabPane.getTabs().add(newTab.getTab());
                    sleepForAWhile(5);
                });
                progress++;
                updateLoadingBar(progress, rizpaUi.getUsers().getUserMap().size());
            }
            updateMessageLabel("Done!");
            adminTab.createAdminSearchBox();
            if(!rizpaUiController.hasStockData)
                Platform.runLater(()->rizpaTabPane.getTabs().remove(0,1));
            rizpaUiController.hasStockData = true;
            Platform.runLater(()-> rizpaUiController.closeLoadingButtonStatic.setDisable(false));
            return Boolean.TRUE;
        }
        catch (Exception e){
            Platform.runLater(()-> rizpaUiController.closeLoadingButtonStatic.setDisable(false));
            Platform.runLater(()-> rizpaUiController.currentLoadingStatic.setText(e.getMessage()));
            Platform.runLater(()-> rizpaUiController.currentLoadingStatic.setStyle("-fx-text-fill: red"));
            if(!rizpaUiController.hasStockData)
                Platform.runLater(()->rizpaTabPane.getTabs().remove(1,rizpaTabPane.getTabs().size()));
            else
                Platform.runLater(()->rizpaTabPane.getTabs().remove(
                        rizpaTabPane.getTabs().size(),rizpaTabPane.getTabs().size()));
            return Boolean.FALSE;
        }
    }
    private void updateMessageLabel(String message){
        Platform.runLater(() -> rizpaUiController.currentLoadingStatic.setText(message));
    }
    private void updateLoadingBar(long currWork, long totalWork){
        updateProgress(currWork,totalWork);
        Platform.runLater(() -> rizpaUiController.loadingBarStatic.setProgress(getProgress()));
    }
    public static void sleepForAWhile(long sleepTime) {
        if (sleepTime != 0) {
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException ignored) {

            }
        }
    }
}


