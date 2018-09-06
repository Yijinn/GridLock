package view;

import javafx.stage.Stage;
import model.Model;

/**
 * Interface for managing views and models for the game
 */
public interface GlobalController {
    void replace(Viewable x);
    void push(Viewable x);
    void pop();
    void show();
    void setModel(Model model);
    void setModel(Model model, boolean update);
    Model getModel();
    Stage getStage();
}
