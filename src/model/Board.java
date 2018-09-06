package model;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Represents a grid of {@code int}s each with a positive value (or {@link #NONE}).
 */
public class Board {
    /**
     * Represents an empty cell.
     */
    public static final int NONE = -1;
    
    /**
     * The grid of integers.
     */
    private int[] board;
    /**
     * The size of the board.
     */
    private IntRect rect;
    
    /**
     * Constructs a board with exactly the parameters given, no checking.
     * @param board the board to use
     * @param rect the rect to use
     */
    private Board(int[] board, IntRect rect) {
        this.board = board;
        this.rect = rect;
    }
    
    /**
     * Construct a board with the given dimensions.
     * @param rect the dimensions
     */
    public Board(IntRect rect) {
        this(new int[rect.getW() * rect.getH()], rect);
        Arrays.fill(this.board, NONE);
    }
    
    /**
     * Copy constructor.
     * @param o the board to clone
     */
    private Board(Board o) {
        this(Arrays.copyOf(o.board, o.board.length), o.rect);
    }
    
    /**
     * @return the rect of the board
     */
    public final IntRect getRect() { return rect; }
    
    /**
     * @param x the x coordinate
     * @param y the y coordinate
     * @return the value of the cell at ({@code x}, {@code y})
     */
    public final int get(int x, int y) { return board[y*rect.getW() + x]; }
    /**
     * @param p the point
     * @return the value of the cell at {@code p}
     */
    public final int get(IntVec p) { return get(p.getX(), p.getY()); }
    /**
     * Sets the cell at ({@code x}, {@code y}) to value {@code v}.
     * @param x the x coordinate
     * @param y the y coordinate
     * @param v the value to set the cell to
     */
    private final void set(int x, int y, int v) { board[y*rect.getW() + x] = v; }
    
    /**
     * Generates a grid of size {@code rect}, filling each cell with the index
     * of the rectangle present or {@link #NONE} if none exists.
     * @param rect the size or dimensions of the board
     * @param rects the rectangles to fill the board with
     * @return the newly constructed board
     */
    public static Board genFor(IntRect rect, IntRect[] rects) {
        Board board = new Board(rect);
        IntStream.range(0, rects.length).forEach(i ->
            rects[i].forAll((x, y) -> board.set(x, y, i))
        );
        return board;
    }
    
    /**
     * Same as {@link #genFor(IntRect, IntRect[])} using the rects of
     * {@code cars}.
     * @param rect the size or dimensions of the board
     * @param cars the cars to fill the board with
     * @return the newly constructed board
     */
    public static Board genFor(IntRect rect, Car[] cars) {
        return genFor(rect, Arrays.stream(cars).map(Car::getRect).toArray(IntRect[]::new));
    }
    
    /**
     * @param r the rectangle to check within
     * @return true if all cells in the rectangle are {@link #NONE} else false
     */
    public boolean allNone(IntRect r) {
        return r.allOf((x, y) -> get(x, y) == NONE);
    }
    
    /**
     * @param from the rectangle to move
     * @param to the destination rectangle
     * @return a new board where the rectangle {@code from} has been moved to
     *         {@code to}
     */
    public Board withMove(IntRect from, IntRect to) {
        // TODO: optimize
        Board newBoard = new Board(this);
        int carI = get(from.getPos());
        from.forAll((x, y) -> newBoard.set(x, y, Board.NONE));
        to.forAll((x, y) -> newBoard.set(x, y, carI));
        return newBoard;
    }
    
    /**
     * @param rect the rect to set
     * @param i the value to set it to
     * @return a copy of the current board with the supplied rect set to index
     *         {@code i} added in
     */
    public Board withRect(IntRect rect, int i) {
        Board newBoard = new Board(this);
        rect.forAll((x, y) -> newBoard.set(x, y, i));
        return newBoard;
    }
    
    /**
     * Same as {@link #withRect(IntRect, int)} using the supplied car's rect.
     * @param c the car whose rect to set
     * @param i the value to set it to
     * @return a copy of the current board with the supplied car set to index
     *         {@code i} added in
     */
    public Board withCar(Car c, int i) {
        return withRect(c.getRect(), i);
    }
    
    /**
     * Attempts to display a readable version of the board.
     */
    public void print() {
        IntStream.range(0, rect.getH()).forEach(y ->
            System.out.println("  " + IntStream.range(0, rect.getW())
                .map(x -> get(x, y))
                .mapToObj(i -> (i == NONE) ? "." : Integer.toString(i))
                .collect(Collectors.joining(" "))
            )
        );
    }
}
