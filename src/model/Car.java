package model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collectors;

/**
 * Represents a car in the game
 */
public class Car implements Comparable<Car> {
    /**
     * The value of the car's direction if it is horizontal.
     */
    public static final IntVec RIGHT = new IntVec(1, 0);
    /**
     * The value of the car's direction if it is vertical.
     */
    public static final IntVec DOWN = new IntVec(0, 1);
    
    /**
     * The rect of the car.
     */
    private IntRect rect;
    /**
     * The direction the car is facing.
     */
    private IntVec direction;
    
    /**
     * Constructs a car with the given rect and direction
     * @param rect the rect the car occupies
     * @param direction the direction the car is in
     */
    public Car(IntRect rect, IntVec direction) {
        this.rect = rect;
        this.direction = direction;
    }
    
    /**
     * @return the car's rect
     */
    public final IntRect getRect() { return rect; }
    
    /**
     * @return the car's direction
     */
    public final IntVec getDirection() { return direction; }
    
    /**
     * Check's whether the two car's positions are equal, (we assume we are
     * comparing two valid Car's in the same grid).
     * @param o the other object to compare it to
     * @return true if equal, else false
     */
    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Car car = (Car) o;

        return rect.getPos().equals(car.rect.getPos());
    }
    
    /**
     * Provides a hash based on the Car's position (we assume we are hashing for
     * valid Car's in the same grid, in which case their position is a unique
     * identifier).
     * @return a hash probably uniquely representing this car
     */
    @Override
    public final int hashCode() {
        return rect.getPos().hashCode();
    }
    
    /**
     * Provides an arbitrary ordering on cars.
     * @param o the car to compare with
     * @return negative if ({@code this < o}), positive if ({@code this > o})
     *         and zero otherwise
     */
    @Override
    public int compareTo(Car o) {
        return Comparator.comparing((Car c) -> c.getRect().getPos()).compare(this, o);
    }
    
    /**
     * Returns a copy of this car that has moved {@code d} units in its
     * direction.
     * @param d the number of units to move the car by
     * @return the new, moved car
     */
    public Car withMove(int d) {
        IntVec delta = direction.mul(d);
        return new Car(new IntRect(rect.getPos().add(delta), rect.getSize()), direction);
    }
    
    /**
     * Key to store the car's rect under.
     */
    private static final String RECT_KEY = "rect";
    /**
     * Key to store the car's direction under.
     */
    private static final String DIRECTION_KEY = "direction";
    /**
     * @return the car serialized to a {@code JSONObject}
     */
    public JSONObject toJson() {
        JSONObject o = new JSONObject();
        o.put(RECT_KEY, rect.toJson());
        o.put(DIRECTION_KEY, direction.toJson());
        return o;
    }
    /**
     * Given a {@code JSONObject}, tries to deserialize a car from it.
     * @param o the {@code JSONObject} to deserialize from
     * @return the deserialized car
     */
    public static Car fromJson(JSONObject o) {
        return new Car(IntRect.fromJson((JSONObject) o.get(RECT_KEY)), IntVec.fromJson((JSONObject) o.get(DIRECTION_KEY)));
    }
    /**
     * Given an array of cars, serializes it to a {@code JSONArray}
     * @param cars the array of cars to serialize
     * @return the car array serialized to a {@code JSONArray}
     */
    public static JSONArray arrayToJson(Car[] cars) {
        return Arrays.stream(cars).map(Car::toJson).collect(Collectors.toCollection(JSONArray::new));
    }
    /**
     * Given a {@code JSONArray}, tries to deserialize an array of cars from it.
     * @param a the {@code JSONArray} to deserialize from
     * @return the deserialized car array
     */
    public static Car[] arrayFromJson(JSONArray a) {
        // TODO: figure out why the (Car[]) cast is required
        return (Car[]) a.stream().map(o -> Car.fromJson((JSONObject) o)).toArray(Car[]::new);
    }
}
