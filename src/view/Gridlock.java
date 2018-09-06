package view;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import model.Model;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Stack;

public class Gridlock extends Application {
    
    private Stack<Viewable> views = new Stack<>();
    private static Stage primaryStage;
    private Model model;

    /**
     * This interface is used throughout the program to manage the views that the stage
     * is displaying and the model of the game.
     */
    private static class GlobalController implements view.GlobalController {
        Gridlock self;
        GlobalController(Gridlock self) { this.self = self; }
        public void replace(Viewable x) { self.replace(x); }
        public void push(Viewable x) { self.push(x); }
        public void pop() { self.pop(); }
        public void show() { self.displayTop(); }
        public void setModel(Model model) { self.setModel(model); }
        public void setModel(Model model, boolean update) { self.setModel(model, update); }
        public Model getModel() { return self.getModel(); }
		public Stage getStage() {return primaryStage;}

    }

    /**
     * Replaces the current view with another view
     * @param x
     */
    private void replace(Viewable x) {
        views.pop();
        push(x);
    }

    /**
     * Pushes a view to the top of the stack and displays it
     * @param x
     */
    private void push(Viewable x) {
        views.push(x);
        updateTop();
        displayTop();
    }

    /**
     * Pops the top view off the stack, and if the stack becomes empty
     * then the application closes. Otherwise we display the next view.
     */
    private void pop() {
        views.pop();
        if (views.empty()) {
            Platform.exit();
            System.exit(0);
        }
        updateTop();
        displayTop();
    }

    private void updateTop() {
        views.get(views.size() - 1).update();
    }

    /**
     * Updates the stage view the top view on the stack.
     */
    private void displayTop() {
        primaryStage.setScene(views.get(views.size() - 1).getScene());
        primaryStage.show();
    }

    /**
     * Sets the model and updates the top view on the stack if the inputted
     * boolean is true.
     * @param model
     * @param update
     */
    public void setModel(Model model, boolean update) {
        this.model = model;
        if (update) views.peek().update();
    }

    /**
     * Sets the model and updates the top view on the stack
     * @param model
     */
    public void setModel(Model model) {
        setModel(model, true);
    }

    /**
     * Getter for the model
     * @return
     */
    public Model getModel() { return model; }

    /**
     * Initialisation of the Game, starts by creating the save file or loadig the file if it already exists.
     * Ends by pushing the Main Menu view to the stack.
     * @param primaryStage
     */
    @Override
    public void start(Stage primaryStage) {
        Gridlock.primaryStage = primaryStage;
        primaryStage.setTitle("Gridlock");
        File savedFile = new File("save.json");
        if(savedFile.exists() && !savedFile.isDirectory()) {
            try {
                @SuppressWarnings("resource")
                String save = new Scanner(new File("save.json")).useDelimiter("\\Z").next();
                model = Model.fromJson((JSONObject) new JSONParser().parse(save));
            } catch (FileNotFoundException | ParseException e) {
                e.printStackTrace();
                model = new Model();
            }
        }
        push(new MainMenu(new GlobalController(this)));
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
}
