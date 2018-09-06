package view.theme;

import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.*;
import model.*;
import view.GlobalController;

import java.nio.file.Paths;
import java.util.stream.IntStream;

/**
 * DefaultTheme manages the drawing of the game board.
 */
public class DefaultTheme implements Theme {
    
    private ColorGenerator colorGenerator;
    private Color[] colors;
    private GlobalController g;
    private Pane gameGrid;
    private AudioClip clickSound;
    private Rectangle pressedRect, hintRect, goalRect;
    private Path hintArrow;
    private IntVec pressStart;

    /**
     * Constructor of default theme sets the colorGenerator and grabs the audio clip for
     * clicking and sets clickSound.
     * @param colorGenerator
     */
    public DefaultTheme(ColorGenerator colorGenerator) {
        this.colorGenerator = colorGenerator;
        clickSound = new AudioClip(Paths.get("src/sounds/click.mp3").toUri().toString());
    }

    /**
     * Returns the path to the FXML for the game screen.
     * @return
     */
    @Override
    public String getFxmlPath() { return "/fxml/game.fxml"; }

    /**
     * Returns the path to the css for the game screen
     * @return
     */
    @Override
    public String getCssPath() { return "/css/game.css"; }

    /**
     * Returns the dimensions of the game screen as an IntVec.
     * @return
     */
    @Override
    public IntVec getDimensions() { return new IntVec(940, 600); }

    /**
     * Initialisation of the game screen. Sets the GlobalController and creates the
     * Cars as rectangles. It adds the rectangles to the scene and also includes the logic
     * which displays which rectangles are being targeted during a mouse click.
     * @param scene
     * @param g
     */
    @Override
    public void init(Scene scene, GlobalController g) {
        /* Sets GlobalController and grabs the gameGrid and state */
        this.g = g;
        gameGrid = (Pane) scene.lookup("#gameGrid");
        State state = g.getModel().getGameState().getCurrentState();

        /* Generate the colours for the car */
        colors = colorGenerator.generateFor(state.getNumCars());
    
        /* Ghost rectangle for the hint */
        hintRect = new Rectangle(0, 0, 0, 0);
        hintRect.setArcWidth(20);
        hintRect.setArcHeight(20);
        gameGrid.getChildren().add(hintRect);
    
        hintArrow = new Path();
//            hintArrow.setFillRule(FillRule.NON_ZERO);
        hintArrow.setFill(Color.grayRgb(0));
        gameGrid.getChildren().add(hintArrow);
    
        /* Create the rectangles used for all the cars */
        for (int i = 0; i < state.getNumCars(); i++) {
            IntRect r = state.getCar(i).getRect();
            
            Rectangle drawRect = new Rectangle(100*r.getX(), 100*r.getY(), 100*r.getW(), 100*r.getH());
            drawRect.setFill(colors[i]);
            drawRect.setArcWidth(20);
            drawRect.setArcHeight(20);
            
            gameGrid.getChildren().add(drawRect);
        }
    
        /* This rectangle is used as an overlay to indicate which rectangle is being pressed. */
        pressedRect = new Rectangle(0, 0, 0, 0);
        pressedRect.setFill(new Color(1, 1, 1, 0.2));
        gameGrid.getChildren().add(pressedRect);

        /* This rectangle is used to tell the user where they want the goal car to reach */
        IntRect goalRectCoord = g.getModel().getGameState().getCurrentState().getGoalRect();
        goalRect = new Rectangle(100*goalRectCoord.getX()+190, 100*goalRectCoord.getY(), 10, 100*goalRectCoord.getH());
        goalRect.setFill(new Color(0,0,0,0.7));
        gameGrid.getChildren().add(goalRect);

        /* These are the mouseEvents which will trigger the overlay to indicate which rectangle is
        being pressed. */
        gameGrid.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
            pressStart = new IntVec((int)e.getX(), (int)e.getY());
            Car car = g.getModel().getGameState().getCurrentState().getCarAt(pressStart.div(100));
            if (car == null) { pressStart = null; return; }
            pressedRect.setX(100*car.getRect().getX());
            pressedRect.setY(100*car.getRect().getY());
            pressedRect.setWidth(100*car.getRect().getW());
            pressedRect.setHeight(100*car.getRect().getH());
        });
        gameGrid.addEventFilter(MouseEvent.MOUSE_DRAGGED, e -> {
            if (pressStart == null) return;
            State s = g.getModel().getGameState().getCurrentState();
            IntVec pressEnd = new IntVec((int)e.getX(), (int)e.getY());
            int carI = s.getCarIndexAt(pressStart.div(100));
            Car car = s.getCar(carI);
            IntVec dragDelta = pressEnd.sub(pressStart).mul(car.getDirection());
            Rectangle r = getGameGridRect(carI);
            IntVec dragCarPos = car.getRect().getPos().mul(100).add(dragDelta).clamp(s.getBoardRect().getPos().mul(100), s.getBoardRect().getEnd().sub(car.getRect().getSize()).mul(100));
            r.setX(dragCarPos.getX());
            r.setY(dragCarPos.getY());
            pressedRect.setX(dragCarPos.getX());
            pressedRect.setY(dragCarPos.getY());
        });
        /* When the mouse is released this will play a sound */
        gameGrid.addEventFilter(MouseEvent.MOUSE_RELEASED, e -> {
            if (pressStart == null) return;
            pressedRect.setWidth(0);
            pressedRect.setHeight(0);
            
            State s = g.getModel().getGameState().getCurrentState();
            int i = s.getCarIndexAt(pressStart.div(100));
            IntVec p = s.getCar(i).getRect().getPos();
            Rectangle r = getGameGridRect(i);
            r.setX(100*p.getX());
            r.setY(100*p.getY());
            pressStart = null;
            
            double volume = g.getModel().getSettings().getVolume();
            System.out.println("Playing sound at " + volume);
            if (0 < volume) {
                clickSound.setVolume(volume);
                clickSound.play();
            }
        });
        
    }
    
    /**
     * @param i the index of the car to get the rect for
     * @return the displayed rectangle for the {@code i}th car
     */
    private Rectangle getGameGridRect(int i) {
        return (Rectangle) gameGrid.getChildren().get(2 + i);
    }
    
    private void addPathQuad(Path p, IntVec a, IntVec b, IntVec c, IntVec d) {
        p.getElements().add(new MoveTo(a.getX(), a.getY()));
        p.getElements().add(new LineTo(b.getX(), b.getY()));
        p.getElements().add(new LineTo(c.getX(), c.getY()));
        p.getElements().add(new LineTo(d.getX(), d.getY()));
        p.getElements().add(new LineTo(a.getX(), a.getY()));
    }
    private void addPathRect(Path p, IntVec a, IntVec b) {
        p.getElements().add(new MoveTo(a.getX(), a.getY()));
        p.getElements().add(new LineTo(a.getX(), b.getY()));
        p.getElements().add(new LineTo(b.getX(), b.getY()));
        p.getElements().add(new LineTo(b.getX(), a.getY()));
        p.getElements().add(new LineTo(a.getX(), a.getY()));
    }
    
    /**
     * Redraws all the Car rectangles based on the state in the model.
     */
    @Override
    public void update() {
        GameState gs = g.getModel().getGameState();
        State state = gs.getCurrentState();
    
        if (gs.getHint() == null) {
            hintRect.setWidth(0);
            hintRect.setHeight(0);
            hintArrow.getElements().clear();
        } else {
            int carI = gs.getHint().getCarI();
            int delta = gs.getHint().getDelta();
            Car c = state.getCar(carI);
            
            IntVec dir = c.getDirection().mul(delta).signum();
            IntVec odir = dir.swap().abs();
            IntVec hintEndPos = c.withMove(delta).getRect().getPos();
            
            hintRect.setX(100*hintEndPos.getX());
            hintRect.setY(100*hintEndPos.getY());
            hintRect.setWidth(100*c.getRect().getW());
            hintRect.setHeight(100*c.getRect().getH());
            Color col = colors[carI];
            hintRect.setFill(Color.color(col.getRed(), col.getGreen(), col.getBlue(), 0.5));
    
            IntVec hintStart = c.getRect().getPos().mul(100).add(odir.mul(50));
            IntVec hintEnd = hintEndPos.mul(100).add(odir.mul(50)).add((dir.manDist() < 0) ? odir.mul(0) : c.getRect().getSize().mul(100).mul(dir));
            
            addPathRect(hintArrow, hintStart.add(odir.mul(-5)), hintEnd.add(odir.mul(5)));
            addPathQuad(hintArrow,
                hintEnd.add(odir.mul(-10)),
                hintEnd.add(odir.mul(-10)).add(dir.mul(-20)),
                hintEnd.add(odir.mul(-50)).add(dir.mul(-100)),
                hintEnd.add(odir.mul(-50)).add(dir.mul(-80))
            );
            addPathQuad(hintArrow,
                hintEnd.add(odir.mul(10)),
                hintEnd.add(odir.mul(10)).add(dir.mul(-20)),
                hintEnd.add(odir.mul(50)).add(dir.mul(-100)),
                hintEnd.add(odir.mul(50)).add(dir.mul(-80))
            );
        }
        
        for (int i = 0; i < state.getNumCars(); i++) {
            IntVec p = state.getCar(i).getRect().getPos();
            Rectangle r = getGameGridRect(i);
            r.setX(100*p.getX());
            r.setY(100*p.getY());
        }
    
    }
}
