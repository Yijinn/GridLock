package view.theme;

import javafx.scene.Scene;
import model.IntVec;
import view.GlobalController;

/**
 * This general interface manages the drawing of the gameboard
 */
public interface Theme {
    
    String getFxmlPath();
    String getCssPath();
    
    IntVec getDimensions();
    
    void init(Scene scene, GlobalController g);
    void update();
}
