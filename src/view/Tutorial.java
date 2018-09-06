package view;

import controller.TutorialController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

public class Tutorial extends Viewable {
    private GlobalController g;
    private TutorialController controller;
    
    public Tutorial(GlobalController g) {
        /*
            Load the FXML and CSS files, throw an exception if not loaded. Also initialises
            the controller
         */
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/tutorial.fxml"));
            setScene(new Scene(loader.load()));
            getScene().getStylesheets().add("/css/tutorial.css");
    
            controller = loader.getController();
            controller.init(g);
        } catch (IOException e) {
            System.out.println("Could not load view, check FXML Loader");
        }
    }
}
