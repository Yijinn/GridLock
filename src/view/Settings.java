package view;

import controller.SettingsController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Settings extends Viewable {

    private GlobalController g;
    private SettingsController controller;
    
    private Slider soundSlider;
    private ChoiceBox<String> themeSelector;
    
    /**
     * Generates the Scene for the Settings Menu. While it is doing this, the controller also receives the
     * current scene so that it can return back to it when the back button is pressed.
     */
    public Settings(GlobalController g) {
        this.g = g;
        
        /*
            Load the FXML and CSS files, throw an exception if not loaded. Adds a slider and choicebox
            to manage.
         */
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/settings.fxml"));
            setScene(new Scene(loader.load()));
            getScene().getStylesheets().add("/css/settingsMenu.css");
    
            soundSlider = (Slider) getScene().lookup("#soundSlider");
            themeSelector = (ChoiceBox<String>) getScene().lookup("#themeSelector");
            
            update();
            
            controller = loader.getController();
            controller.init(g);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Could not load view, check FXML Loader");
        }
    }

    /**
     * Updates the settings view with the settings that are stored in the Model
     */
    @Override
    public void update() {
        soundSlider.setValue(g.getModel().getSettings().getVolume());
        themeSelector.setValue(g.getModel().getSettings().getTheme().toString());
    }
}
