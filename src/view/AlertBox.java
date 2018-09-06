package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertBox {

    static boolean response;

    /**
     * Creates an AlertBox window with a given Title and Message. Returns a boolean true/false
     * depending on if Yes or No is pressed. This window cannot be closed until it has been
     * dealt with.
     * @param title
     * @param message
     */
    public static boolean display(String title, String message) {
        /* Initialise the Stage window */
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(350);

        /* Initialise components on the Scene */
        Label label = new Label();
        label.setText(message);
        Button yesButton = new Button("Yes");
        Button noButton = new Button("No");

        /* Styles for buttons */
        yesButton.setStyle("-fx-background-color: #00CEAE");
        noButton.setStyle("-fx-background-color: #E93F3F");

        /* Set actions for AlertBox */
        yesButton.setOnAction(e -> {
            response = true;
            window.close();
        });

        noButton.setOnAction(e -> {
            response = false;
            window.close();
        });

        /* Create the layout for the Scene */
        HBox buttons = new HBox(15);
        VBox layout = new VBox(10);

        /* Add all the components to the layout */
        buttons.getChildren().addAll(yesButton, noButton);
        layout.getChildren().addAll(label, buttons);

        /* Set layout */
        layout.setAlignment(Pos.CENTER);
        buttons.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(15,15,15,15));

        /* Set the scene to the window */
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();

        return response;
    }
}
