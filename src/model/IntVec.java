package model;

import org.json.simple.JSONObject;

import java.util.Comparator;

/**
 * Represents a 2D integer vector.
 */
public class IntVec implements Comparable<IntVec> {
    /**
     * The x coordinate of the vector.
     */
    private int x;
    /**
     * The y coordinate of the vector.
     */
    private int y;
    
    /**
     * Constructs a vector from the given {@code x} and {@code y} coordinates.
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public IntVec(int x, int y) {
        this.x = x;
        this.y = y;
    }
    /**
     * Copy constructor.
     * @param o the vector to copy
     */
    public IntVec(IntVec o) {
        this.x = o.x;
        this.y = o.y;
    }
    
    /**
     * @return the x coordinate of this vector
     */
    public final int getX() { return this.x; }
    /**
     * @return the y coordinate of this vector
     */
    public final int getY() { return this.y; }
    
    /**
     * @return the negative of this vector
     */
    public final IntVec neg() { return new IntVec(-x, -y); }
    /**
     * @return the piecewise absolute value of this vector
     */
    public final IntVec abs() { return new IntVec(Math.abs(x), Math.abs(y)); }
    /**
     * @return the piecewise signum of this vector
     */
    public final IntVec signum() { return new IntVec(Integer.signum(x), Integer.signum(y)); }
    /**
     * @return the vector with swapped {@code x} and {@code y} coordinates.
     */
    public final IntVec swap() { return new IntVec(y, x); }
    
    /**
     * @param o the vector to add with
     * @return the piecewise addition of {@code this} and {@code o}
     */
    public final IntVec add(IntVec o) { return new IntVec(x + o.x, y + o.y); }
    /**
     * @param o the vector to subtract with
     * @return the piecewise subtraction of {@code this} and {@code o}
     */
    public final IntVec sub(IntVec o) { return new IntVec(x - o.x, y - o.y); }
    /**
     * @param o the vector to multiply with
     * @return the piecewise multiplication of {@code this} and {@code o}
     */
    public final IntVec mul(IntVec o) { return new IntVec(x * o.x, y * o.y); }
    /**
     * @param o the vector to divide with
     * @return the piecewise division of {@code this} and {@code o}
     */
    public final IntVec div(IntVec o) { return new IntVec(x / o.x, y / o.y); }
    /**
     * @param ox the x coordinate to add
     * @param oy the y coordinate to add
     * @return the piecewise addition of {@code this} and ({@code x}, {@code y})
     */
    public final IntVec add(int ox, int oy) { return new IntVec(x + ox, y + oy); }
    /**
     * @param ox the x coordinate to subtract
     * @param oy the y coordinate to subtract
     * @return the piecewise subtraction of {@code this} and ({@code x}, {@code y})
     */
    public final IntVec sub(int ox, int oy) { return new IntVec(x - ox, y - oy); }
    /**
     * @param ox the x coordinate to multiply with
     * @param oy the y coordinate to multiply with
     * @return the piecewise multiplication of {@code this} and ({@code x}, {@code y})
     */
    public final IntVec mul(int ox, int oy) { return new IntVec(x * ox, y * oy); }
    /**
     * @param ox the x coordinate to divide by
     * @param oy the y coordinate to divide by
     * @return the piecewise division of {@code this} and ({@code x}, {@code y})
     */
    public final IntVec div(int ox, int oy) { return new IntVec(x / ox, y / oy); }
    /**
     * @param n the x and y coordinate to add
     * @return the piecewise addition of {@code this} and ({@code n}, {@code n})
     */
    public final IntVec add(int n) { return new IntVec(x + n, y + n); }
    /**
     * @param n the x and y coordinate to subtract
     * @return the piecewise subtraction of {@code this} and ({@code n}, {@code n})
     */
    public final IntVec sub(int n) { return new IntVec(x - n, y - n); }
    /**
     * @param n the x and y coordinate to multiply with
     * @return the piecewise multiplication of {@code this} and ({@code n}, {@code n})
     */
    public final IntVec mul(int n) { return new IntVec(x * n, y * n); }
    /**
     * @param n the x and y coordinate to divide by
     * @return the piecewise division of {@code this} and ({@code n}, {@code n})
     */
    public final IntVec div(int n) { return new IntVec(x / n, y / n); }
    /**
     * @param a starting vector
     * @param b vector to add to {@code a}
     * @return the piecewise addition of {@code a} and {@code b}
     */
    public static IntVec add(IntVec a, IntVec b) { return new IntVec(a.x + b.x, a.y + b.y); }
    /**
     * @param a starting vector
     * @param b vector to subtract from {@code a}
     * @return the piecewise subtraction of {@code a} and {@code b}
     */
    public static IntVec sub(IntVec a, IntVec b) { return new IntVec(a.x - b.x, a.y + b.y); }
    /**
     * @param a starting vector
     * @param b vector to multiply with {@code a}
     * @return the piecewise multiplication of {@code a} and {@code b}
     */
    public static IntVec mul(IntVec a, IntVec b) { return new IntVec(a.x * b.x, a.y + b.y); }
    /**
     * @param a starting vector
     * @param b vector to divide from {@code a}
     * @return the piecewise division of {@code a} and {@code b}
     */
    public static IntVec div(IntVec a, IntVec b) { return new IntVec(a.x / b.x, a.y / b.y); }
    
    /**
     * @param o the vector to take the the min with
     * @return the piecewise min of {@code this} and {@code o}
     */
    public final IntVec min(IntVec o) { return new IntVec(Math.min(x, o.x), Math.min(y, o.y)); }
    /**
     * @param o the vector to take the the max with
     * @return the piecewise max of {@code this} and {@code o}
     */
    public final IntVec max(IntVec o) { return new IntVec(Math.max(x, o.x), Math.max(y, o.y)); }
    /**
     * @param ox the x coordinate to take the min with
     * @param oy the x coordinate to take the min with
     * @return the piecewise min of {@code this} and ({@code x}, {@code y})
     */
    public final IntVec min(int ox, int oy) { return new IntVec(Math.min(x, ox), Math.min(y, oy)); }
    /**
     * @param ox the x coordinate to take the max with
     * @param oy the x coordinate to take the max with
     * @return the piecewise max of {@code this} and ({@code x}, {@code y})
     */
    public final IntVec max(int ox, int oy) { return new IntVec(Math.max(x, ox), Math.max(y, oy)); }
    /**
     * @param n the x and y coordinate to take the min with
     * @return the piecewise min of {@code this} and ({@code n}, {@code n})
     */
    public final IntVec min(int n) { return new IntVec(Math.min(x, n), Math.min(y, n)); }
    /**
     * @param n the x and y coordinate to take the max with
     * @return the piecewise max of {@code this} and ({@code n}, {@code n})
     */
    public final IntVec max(int n) { return new IntVec(Math.max(x, n), Math.max(y, n)); }
    /**
     * @param a starting vector
     * @param b other vector to take the min with
     * @return the piecewise min of {@code a} and {@code b}
     */
    public static IntVec min(IntVec a, IntVec b) { return new IntVec(Math.min(a.x, b.x), Math.min(a.y, b.y)); }
    /**
     * @param a starting vector
     * @param b other vector to take the max with
     * @return the piecewise max of {@code a} and {@code b}
     */
    public static IntVec max(IntVec a, IntVec b) { return new IntVec(Math.max(a.x, b.x), Math.max(a.y, b.y)); }
    
    /**
     * @param omin the min vector
     * @param omax the max vector
     * @return the piecewise clamp of {@code this} between {@code omin} and {@code omax}
     */
    public final IntVec clamp(IntVec omin, IntVec omax) { return min(omax).max(omin); }
    /**
     * @param omin the min value of x and y
     * @param omax the max value of x and y
     * @return this vector clamped between {@code omin} and {@code omax}
     */
    public final IntVec clamp(int omin, int omax) { return min(omax).max(omin); }
    
    public int manDist() { return x + y; }
    
    /**
     * @param o the object to check against
     * @return true if this vector is identical to {@code o}
     */
    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IntVec intVec = (IntVec) o;

        if (x != intVec.x) return false;
        return y == intVec.y;
    }
    
    /**
     * @return a hash of this vector
     */
    @Override
    public final int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }
    
    /**
     * @param o the vector to check against
     * @return whether this vector is identical to {@code o}
     */
    public final boolean equals(IntVec o) { return x == o.x && y == o.y; }
    /**
     * @param a a vector to check equality with
     * @param b a vector to check equality with
     * @return whether {@code a} is identical to {@code b}
     */
    public static boolean equals(IntVec a, IntVec b) { return a.x == b.x && a.y == b.y; }
    
    /**
     * Provides an arbitrary ordering on vectors.
     * @param o the vector to compare with
     * @return negative if ({@code this < o}), positive if ({@code this > o})
     *         and zero otherwise
     */
    @Override
    public int compareTo(IntVec o) {
        return Comparator.comparing(IntVec::getX).thenComparing(IntVec::getY).compare(this, o);
    }
    
    /**
     * @return a human readable string representing this vector.
     */
    @Override
    public final String toString() {
        return "(" + x + " " + y + ")";
    }
    
    /**
     * Key to store the vector's x coordinate under.
     */
    private static final String X_KEY = "x";
    /**
     * Key to store the vector's y coordinate under.
     */
    private static final String Y_KEY = "y";
    /**
     * @return the vector serialized to a {@code JSONObject}
     */
    public JSONObject toJson() {
        JSONObject o = new JSONObject();
        o.put(X_KEY, x);
        o.put(Y_KEY, y);
        return o;
    }
    /**
     * Given a {@code JSONObject}, tries to deserialize a vector from it.
     * @param o the {@code JSONObject} to deserialize from
     * @return the deserialized vector
     */
    public static IntVec fromJson(JSONObject o) {
        return new IntVec(((Long) o.get(X_KEY)).intValue(), ((Long) o.get(Y_KEY)).intValue());
    }
}
