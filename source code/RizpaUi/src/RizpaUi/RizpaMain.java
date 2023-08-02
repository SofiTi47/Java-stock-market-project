package RizpaUi;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import RizpaControllers.rizpaUiController;

public class RizpaMain extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    private RizpaUi rizpaUi;


    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Rizpa stock exchange");
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(RizpaMain.class.getResource("/resources/rizpaMain.fxml"));
            rootLayout = (BorderPane) loader.load();
            rizpaUiController rizpaController = loader.getController();
            RizpaUi uiLogic = new RizpaUi();
            rizpaController.setPrimaryStage(primaryStage);
            rizpaController.setUiLogic(uiLogic);
            rizpaController.initialize();
            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {

    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}