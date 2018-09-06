package view.theme;

import javafx.scene.paint.Color;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public class DeuteranopeColorGenerator implements ColorGenerator {
	/**
   * Returns an array of colors when given an integer input of number of cars. Always ensures that
   * the goalCar is red.
   * generates colours that avoid green-red contrasts
	 */
    @Override
    public Color[] generateFor(int nCars) {
        return Stream.concat(IntStream.range(0, nCars-1).mapToObj(i -> {
            int value = 15 + (int)(210*Math.random());
            if (value > 60) value += 120;
            return Color.hsb(value ,0.3 + 0.7*Math.random() ,1.0);
        }), Stream.of(Color.RED)).toArray(Color[]::new);
    }
}
