package view;

import controller.MainMenuController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

public class MainMenu extends Viewable {

    /**
     * Loads the main menu view using an FXML Loader and sets the scene.
     */
    public MainMenu(GlobalController g) {
        /*
            The file paths for the FXML and CSS files as well as the width and height
            of the scene.
         */
        String fxmlPath = "/fxml/mainMenu.fxml";
        String cssPath = "/css/mainMenu.css";
        double sceneWidth = 300;
        double sceneHeight = 600;

        /*
            Load the FXML and CSS files, throw an exception if not loaded. Also initialise
            the controller
         */
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            setScene(new Scene(root, sceneWidth, sceneHeight));
            getScene().getStylesheets().add(cssPath);

            MainMenuController controller = loader.getController();
            controller.init(g);
            controller.update();
        } catch (IOException e) {
            System.out.println("Could not load view, check FXML Loader");
        }
    }
}
