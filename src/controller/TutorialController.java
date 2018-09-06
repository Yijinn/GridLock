package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import view.GlobalController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class TutorialController {
    private GlobalController g;
    private ArrayList<Image> images;
    private String[] slideInstructions;
    private int imageNumber;
    
    @FXML private Button nextButton;
    @FXML private ImageView imageView;
    @FXML private Button prevButton;
    @FXML private Label instructionLabel;
    
    public void init(GlobalController g) {
        this.g = g;
        loadImages();
    }
    
    /**
     * Loads images for the tutorial
     */
    public void loadImages() {
        String[] imageFilePaths = new String[]{
                "/images/tutorial1.png",
                "/images/tutorial2.png",
                "/images/tutorial3.png",
                "/images/tutorial4.png",
                "/images/tutorial5.png",
        };

        slideInstructions = new String[]{
                "This is what your game board looks like.",
                "Click on a box to select it, the box you select will be highlighted.",
                "Drag your click to where you want the box to go.",
                "Your aim is to get the red box to the door indicated by the two arrows",
                "Congratulations, when you win you can start the next level. \n Be careful! The levels get harder and harder."
        };

        images = Arrays.stream(imageFilePaths).map(filePath -> new Image(filePath)).collect(Collectors.toCollection(ArrayList::new));
        this.imageNumber = 0;
        imageView.setImage(images.get(imageNumber));
        instructionLabel.setText(slideInstructions[imageNumber]);
        /* Cannot initially go back */
        prevButton.setDisable(true);
    }
    
    /**
     * Go back a slide when the back button is pressed
     * @param event
     */
    @FXML
    void loadPrevImage(ActionEvent event) {
        System.out.println("Loading previous tutorial slide...");
        nextButton.setDisable(false);
        imageNumber--;
        imageView.setImage(images.get(imageNumber));
        instructionLabel.setText(slideInstructions[imageNumber]);
        if (imageNumber == 0) {
            prevButton.setDisable(true);
        }
    }

    /**
     * Go to next slide when the next button is pressed
     * @param event
     */
    @FXML
    void loadNextImage(ActionEvent event) {
        System.out.println("Loading next tutorial slide...");
        prevButton.setDisable(false);
        imageNumber++;
        imageView.setImage(images.get(imageNumber));
        instructionLabel.setText(slideInstructions[imageNumber]);
        if (imageNumber == images.size() - 1) {
            nextButton.setDisable(true);
        }
    }
    
    /**
     * Return to the menu view when back button is pressed
     * @param event
     */
    @FXML
    void returnToMenu(ActionEvent event) {
        System.out.println("Returning to main menu");
        g.pop();
    }
}
