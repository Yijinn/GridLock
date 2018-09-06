package controller;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.scene.input.DragEvent;
import model.Model;
import model.Settings;
import view.GlobalController;

import java.util.Arrays;
import java.util.stream.Collectors;

public class SettingsController {
    
    private GlobalController g;
    
    @FXML private Slider soundSlider;
    @FXML private ChoiceBox<String> themeSelector;
    
    private ObservableList<String> THEME_STRINGS = FXCollections.observableList(Arrays.stream(model.Settings.Theme.values()).map(model.Settings.Theme::toString).collect(Collectors.toList()));
    
    /**
     * Initialises listeners for setting elements and sets the GlobalController
     * @param g
     */
    public void init(GlobalController g) {
        this.g = g;

        /* Set bounds for soundSlider */
        soundSlider.setMin(0);
        soundSlider.setMax(1);

        /* Grab all available themes and add them to the choicebox */
        themeSelector.getItems().addAll(THEME_STRINGS);

        /* Listener for soundSlider */
        soundSlider.valueProperty().addListener(new ChangeListener<Number>() {
				@Override
				public void changed(ObservableValue<? extends Number>  ov, Number oldVal, Number newVal) {
					if (oldVal != newVal) {
		                System.out.println("Volume changed to " + soundSlider.getValue());
		                Model model = g.getModel().withSettingsVolume((float) soundSlider.getValue());
		                if (model != null) g.setModel(model);
		            }
				}
	   	});

        /* Listener for themeSelector */
        themeSelector.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (!oldValue.equals(newValue)) {
                System.out.println("Theme changed to " + newValue);
                Model model = g.getModel().withSettingsTheme(Settings.Theme.valueOf(newValue));
                if (model != null) g.setModel(model);
            }
        });

    }
    
    /**
     * Returns to the previous scene when back button is pressed
     * @param event
     */
    @FXML
    void backButtonPressed(ActionEvent event) {
        System.out.println("Back button pressed, returning to previous...");
        g.pop();
    }

    /**
     * Updates the model with new volume settings when there is a drag event
     * @param e
     */
    @FXML
    void onVolumeChange(DragEvent e) {
        System.out.println("Volume changed");
        // TODO: remove this or figure out why it doesn't work
        Model model = g.getModel().withSettingsVolume((float) soundSlider.getValue());
        if (model != null) g.setModel(model);
    }
}
