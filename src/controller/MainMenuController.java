package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import model.Model;
import view.*;

import java.io.File;
import java.io.IOException;

public class MainMenuController {
    
    private GlobalController g;

    @FXML private Button resumeGameButton;

    /**
     * Sets the GlobalController so Main Menu can access it
     * @param g the GlobalController
     */
    public void init(GlobalController g) {
        this.g = g;
    }

    /**
     * Opens the tutorial view when the tutorial button is pressed.
     * @param event
     */
    @FXML
    void openTutorial(ActionEvent event) {
        System.out.println("Clicked Tutorial button, opening tutorial...");
        g.push(new Tutorial(g));
    }

    /**
     * Method is run when the new game button is pressed on the main menu. If a save file
     * already exists, double check prompts the user before starting a new game. Else it starts a new game.
     * @param event
     */
    @FXML
    void startNewGame(ActionEvent event) throws IOException {
        System.out.println("Clicked New Model Button, starting a new game...");
        File savedFile = new File("save.json");
        if(savedFile.exists()) {
	        if (g.getModel().getGameState() == null || AlertBox.display("", "We detected a saved game. Are you sure you want to start a new game?")) {
	            g.setModel(g.getModel().withGameStateNew());
	            g.push(new Game(g));
	        }
        }else {
        	g.setModel(new Model().withGameStateNew());
            g.push(new Game(g));
        }
    }

    /**
     * Method is called when Resume Button is pressed.
     * @param event
     * @throws IOException
     */
    @FXML
    void loadGame(ActionEvent event) throws IOException {
    	File savedFile = new File("save.json");
    	if(savedFile.exists() && !savedFile.isDirectory()) {
	        if (g.getModel().getGameState() != null) {
	            g.push(new Game(g));
	        } else {
	            System.out.println("No game was saved!");
	        }
    	} else {
    		System.out.println("No game was saved!");
    	}
    }

    /**
     * When the settings button is pressed, open the settings view.
     * @param event
     */
    @FXML
    void openSettings(ActionEvent event) {
        System.out.println("Clicked Settings Button. opening settings...");
        g.push(new Settings(g));
    }

    public void update() {
        File savedFile = new File("save.json");
        if (!savedFile.exists() || savedFile.isDirectory()) {
            resumeGameButton.setDisable(true);
        }
    }
}
