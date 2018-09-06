package model;

import org.json.simple.JSONObject;

import java.util.Comparator;

/**
 * Represents a rectangle on an integer grid.
 */
public class IntRect implements Comparable<IntRect> {
    /**
     * The upper left of the rectangle (inclusive).
     */
    private IntVec p;
    /**
     * The size of the rectangle.
     */
    private IntVec size;

//    public IntRect() {}
    /**
     * Constructs a rectangle with the given position and size.
     * @param p the upper left position of the rectangle (inclusive)
     * @param size the size of the rectangle
     */
    public IntRect(IntVec p, IntVec size) {
        this.p = p;
        this.size = size;
    }
    
    /**
     * Constructs a rectangle with the given position and size.
     * @param x the left position of the rectangle (inclusive)
     * @param y the top position of the rectangle (inclusive)
     * @param w the width of the rectangle
     * @param h the height of the rectangle
     */
    public IntRect(int x, int y, int w, int h) {
        this(new IntVec(x, y), new IntVec(w, h));
    }
    
    /**
     * Constructs a rectangles with opposing corners {@code a} and {@code b}.
     * @param a a corner of the rectangle
     * @param b the opposite corner of the rectangle
     * @return the constructed rectangle
     */
    public static IntRect fromUnordered(IntVec a, IntVec b) {
        int x1 = Math.min(a.getX(), b.getX());
        int x2 = Math.max(a.getX(), b.getX());
        int y1 = Math.min(a.getY(), b.getY());
        int y2 = Math.max(a.getY(), b.getY());
        return new IntRect(x1, y1, x2-x1, y2-y1);
    }
    
    /**
     * @return the upper left position of the rectangle (inclusive)
     */
    public final IntVec getPos() { return p; }
    /**
     * @return the size of the rectangle
     */
    public final IntVec getSize() { return size; }
    /**
     * @return the bottom right position of the rectangle (exclusive)
     */
    public final IntVec getEnd() { return p.add(size); }
    /**
     * @return the area of the rectangle
     */
    public final int getArea() { return size.getX() * size.getY(); }
    
    /**
     * @return the left position of the rectangle (inclusive)
     */
    public final int getX() { return p.getX(); }
    /**
     * @return the upper position of the rectangle (inclusive)
     */
    public final int getY() { return p.getY(); }
    /**
     * @return the width of the rectangle
     */
    public final int getW() { return size.getX(); }
    /**
     * @return the height of the rectangle
     */
    public final int getH() { return size.getY(); }
    
    /**
     * @param o the other rectangle to check intersection with
     * @return true if we intersect with {@code o}
     */
    public final boolean intersects(IntRect o) {
        // https://stackoverflow.com/a/306332/3492895
        return (getX() < (o.getX() + o.getW())) && ((getX() + getW()) > o.getX()) &&
                (getY() < (o.getY() + o.getH())) && ((getY() + getH()) > o.getY());
    }
    
    /**
     * @param o the point to check against
     * @return whether {@code o} is fully contained within this rectangle
     */
    public final boolean contains(IntVec o) {
        return (getX() <= o.getX()) && (o.getX() < (getX() + getW())) &&
                (getY() <= o.getY()) && (o.getY() < (getY() + getH()));
    }
    /**
     * @param o the rectangle to check against
     * @return whether {@code o} is fully contained within this rectangle
     */
    public final boolean contains(IntRect o) {
        return (getX() <= o.getX()) && ((o.getX() + o.getW()) <= (getX() + getW())) &&
                (getY() <= o.getY()) && ((o.getY() + o.getH()) <= (getY() + getH()));
    }
    
    /**
     * @param o the object to check against
     * @return true if this rectangle is identical to {@code o}
     */
    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IntRect intRect = (IntRect) o;

        if (!p.equals(intRect.p)) return false;
        return size.equals(intRect.size);
    }
    
    /**
     * @return a hash of this rectangle
     */
    @Override
    public final int hashCode() {
        int result = p.hashCode();
        result = 31 * result + size.hashCode();
        return result;
    }
    
    /**
     * @param o the rectangle to check against
     * @return whether this rectangle is identical to {@code o}
     */
    public final boolean equals(IntRect o) { return p.equals(o.p) && size.equals(o.size); }
    /**
     * @param a a rectangle to check equality with
     * @param b a rectangle to check equality with
     * @return whether {@code a} is identical to {@code b}
     */
    public static boolean equals(IntRect a, IntRect b) { return a.p.equals(b.p) && a.size.equals(b.size); }
    
    /**
     * Provides an arbitrary ordering on rectangles.
     * @param o the rect to compare with
     * @return negative if ({@code this < o}), positive if ({@code this > o})
     *         and zero otherwise
     */
    @Override
    public int compareTo(IntRect o) {
        return Comparator.comparing(IntRect::getPos).thenComparing(IntRect::getSize).compare(this, o);
    }
    
    /**
     * Used by {@link #forAll(ForAllFunc)}
     */
    public interface ForAllFunc {
        void run(int x, int y);
    }
    /**
     * Calls {@code f} for each position in the rectangle.
     * @param f the function to run
     */
    public final void forAll(ForAllFunc f) {
        for (int dy = 0; dy < getH(); dy++) {
            for (int dx = 0; dx < getW(); dx++) {
                f.run(getX() + dx, getY() + dy);
            }
        }
    }
    
    /**
     * Used by {@link #allOf(PredicateFunc)}
     */
    public interface PredicateFunc {
        boolean run(int x, int y);
    }
    /**
     * Calls the predicate on consecutive positions until it returns false
     * @param f the predicate to run on each cell
     * @return true if the predicate never returned false, false otherwise
     */
    public final boolean allOf(PredicateFunc f) {
        for (int dy = 0; dy < getH(); dy++) {
            for (int dx = 0; dx < getW(); dx++) {
                if (!f.run(getX() + dx, getY() + dy)) return false;
            }
        }
        return true;
    }
    
    /**
     * @return a human readable string representing this rectangle.
     */
    @Override
    public final String toString() {
        return "(x:" + getX() + " y:" + getY() + " w:" + getW() + " h:" + getH() + ")";
    }
    
    /**
     * Key to store the rectangle's position under.
     */
    private static final String P_KEY = "pos";
    /**
     * Key to store the rectangle's size under.
     */
    private static final String SIZE_KEY = "size";
    /**
     * @return the rectangle serialized to a {@code JSONObject}
     */
    public JSONObject toJson() {
        JSONObject o = new JSONObject();
        o.put(P_KEY, p.toJson());
        o.put(SIZE_KEY, size.toJson());
        return o;
    }
    /**
     * Given a {@code JSONObject}, tries to deserialize a rectangle from it.
     * @param o the {@code JSONObject} to deserialize from
     * @return the deserialized rectangle
     */
    public static IntRect fromJson(JSONObject o) {
        return new IntRect(IntVec.fromJson((JSONObject) o.get(P_KEY)), IntVec.fromJson((JSONObject) o.get(SIZE_KEY)));
    }
}
