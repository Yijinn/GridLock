package view;

import javafx.scene.Scene;

/**
 * Abstract class for dealing with Views. Contains basic functionality to get and set scenes.
 */
public abstract class Viewable {
    private Scene scene;
    void setScene(Scene scene) { this.scene = scene; }
    Scene getScene() { return scene; }
    void update() {}
}
