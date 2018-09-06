package view.theme;

import javafx.scene.paint.Color;

import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * A color generator for the Tritanope theme type
 */
public class TritanopeColorGenerator implements ColorGenerator {
    @Override
    /**
     * Returns an array of colors when given an integer input of number of cars. Always ensures that
     * the goalCar is red.
     * generates colours that avoid red-yellow-purple contrasts
     */
    public Color[] generateFor(int nCars) {
        return Stream.concat(IntStream.range(0, nCars-1).mapToObj(i -> {
            int value = 120 + (int)(120*Math.random());
            return Color.hsb(value ,0.3 + 0.7*Math.random() ,1.0);
        }), Stream.of(Color.RED)).toArray(Color[]::new);
    }
}
