package view;

import controller.GameController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.theme.*;

import java.io.IOException;

public class Game extends Viewable {
    private GlobalController g;
    private GameController controller;
    
    private model.Settings.Theme themeType;
    private view.theme.Theme theme;
    
    /**
     * This generates the view for the Game, sets the GlobalController and
     * contains code to autosave
     * @param g
     */
    public Game(GlobalController g) {
        this.g = g;
        
        System.out.println("Generating Model Screen \n------------------------------");
    
        themeUpdate();
        
        Stage stage = g.getStage();
        stage.setResizable(false);
        stage.setOnHidden(e -> {
            try {
                controller.autoSave();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
    }

    /**
     * Passes in a theme type, and draws the game board depending on which
     * themeType is stored in settings.
     * @param themeType
     * @return
     */
    private static Theme makeTheme(model.Settings.Theme themeType) {
        switch (themeType) {
            case DEFAULT:
                return new DefaultTheme(new RandomColorGenerator());
            case DEUTERANOPE:
                return new DefaultTheme(new DeuteranopeColorGenerator());
            case TRITANOPE:
                return new DefaultTheme(new TritanopeColorGenerator());
        }
        return null;
    }

    /**
     * This generates the view for the GameBoard and the sidePanel by using an FXML
     * loader and initialises the controller.
     */
    private void rebuildSceneWithTheme() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(theme.getFxmlPath()));
            setScene(new Scene(loader.load(), theme.getDimensions().getX(), theme.getDimensions().getY()));
            getScene().getStylesheets().add(theme.getCssPath());
        
            theme.init(getScene(), g);
            
            controller = loader.getController();
            controller.init(g);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Rebuilds the scene with the new theme if the theme if the themeType in settings
     * is different to the one set in this class. Otherwise just redraw the board.
     */
    private void themeUpdate() {
        // if the theme is different from the one in settings, update it
        if (themeType != g.getModel().getSettings().getTheme()) {
            System.out.println("Updating theme");
            themeType = g.getModel().getSettings().getTheme();
            theme = makeTheme(themeType);
            rebuildSceneWithTheme();
            g.show();
        } else {
            theme.update();
        }
    }

    /**
     * Updates the controller and redraws the board
     */
    public void update() {
        themeUpdate();
        controller.update();
    }
    
}
