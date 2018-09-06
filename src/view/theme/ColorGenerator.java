package view.theme;

import javafx.scene.paint.Color;

/**
 * This interface is used to manage the layout of how colors are generated
 */
public interface ColorGenerator {
    Color[] generateFor(int nCars);
}
