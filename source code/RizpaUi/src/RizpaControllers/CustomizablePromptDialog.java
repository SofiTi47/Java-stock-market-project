package RizpaControllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;


public class CustomizablePromptDialog extends Stage {

    private static final int WIDTH_DEFAULT = 400;
    private static final int HEIGHT_DEFAULT = 120;

    private static Label label;
    private static CustomizablePromptDialog popup;
    private static String result;

    private final HBox buttonsBox;

    private CustomizablePromptDialog() {
        setResizable(false);
        initModality(Modality.APPLICATION_MODAL);
        initStyle(StageStyle.TRANSPARENT);

        addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode() == KeyCode.ESCAPE) {
                    CustomizablePromptDialog.this.close();
                }
            }
        });

        label = new Label();
        label.setWrapText(true);
        label.setGraphicTextGap(20);

        BorderPane borderPane = new BorderPane();

        BorderPane dropShadowPane = new BorderPane();
        dropShadowPane.getStyleClass().add("content");
        dropShadowPane.setTop(label);

        buttonsBox = new HBox();
        buttonsBox.setSpacing(15);

        dropShadowPane.setBottom(buttonsBox);

        borderPane.setCenter(dropShadowPane);

        Scene scene = new Scene(borderPane);
        URL url = getClass().getResource("/resources/alert.css");
        scene.getStylesheets().add(url.toExternalForm());
        scene.setFill(Color.TRANSPARENT);
        setScene(scene);
    }

    public static String show(Stage owner, String msg, String... buttons) {
        if (popup == null) {
            popup = new CustomizablePromptDialog();
        }
        label.setText(msg);
        popup.refreshButtons(buttons);

        // calculate width of string
        final Text text = new Text(msg);

        text.snapshot(null, null);
        // + 40 because there is padding 10 left and right and there are 2 containers now
        // + 20 because there is text gap between icon and messge
        int width = (int) text.getLayoutBounds().getWidth() + 60;

        if (width < WIDTH_DEFAULT) {
            width = WIDTH_DEFAULT;
        }

        popup.setWidth(width);
        popup.setHeight(HEIGHT_DEFAULT);

        popup.showAndWait();
        return result;
    }

    private void refreshButtons(String[] buttonsNames) {
        buttonsBox.getChildren().clear();
        if (buttonsNames == null || buttonsNames.length == 0) {
            buttonsNames = new String[]{"YES", "NO"};
        }

        for (String buttonName : buttonsNames) {
            final String name = buttonName;
            Button button = new Button(name);
            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    result = name;
                    CustomizablePromptDialog.this.close();
                }
            });
            buttonsBox.getChildren().add(button);
        }
        buttonsBox.setAlignment(Pos.CENTER);
    }
}
