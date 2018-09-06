package model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;

public class StateGenerator {
    
    /**
     * The size of the board to generate.
     */
    private final static IntRect BOARD = new IntRect(0, 0, 6, 6);
    /**
     * The position of the goal on the board.
     */
    private final static IntVec GOAL = new IntVec(4, 2);
    
    /**
     * Find's the {@code n}th valid position to insert a rectangle of dimensions
     * {@code size} at.
     * @param b the board to look through
     * @param size the size of the rect to insert
     * @param n which position to find
     * @return the {@code n}th valid position on the board or {@code null} if
     *         none exists.
     */
    private static IntVec findNthValidPosition(Board b, IntVec size, int n) {
        for (int y = 0; y < b.getRect().getH(); y++) {
            for (int x = 0; x < b.getRect().getW(); x++) {
                IntVec p = new IntVec(x, y);
                IntRect r = new IntRect(p, size);
                if (b.getRect().contains(r) &&  b.allNone(r)) {
                    if (n == 0) {
                        return p;
                    }
                    n--;
                }
            }
        }
        return null;
    }
    /**
     * @param boardRect the size of the board
     * @param nCars the max number of cars to generate
     * @return an initial finished state with up to {@code nCars} on it
     */
    private static State generateInitialState(IntRect boardRect, int nCars) {
        Board board = new Board(boardRect);
        int boardCellsLeft = boardRect.getArea();
        ArrayList<Car> cars = new ArrayList<>();
        
        Car goalCar = new Car(new IntRect(4, 2, 2, 1), Car.RIGHT);
        
        board = board.withCar(goalCar, 0);

        for (int i = 0; i < nCars; i++) {
            int length = (int) (2 + 2*Math.random());
            IntVec dir = (Math.random() < 0.5) ? Car.RIGHT : Car.DOWN;
            IntVec size = (dir == Car.DOWN) ? (new IntVec(1, length)) : (new IntVec(length, 1));
            IntVec pos = findNthValidPosition(board, size, (int) (boardCellsLeft * Math.random() / 4));

            if (pos != null) {
                IntRect rect = new IntRect(pos, size);
                Car c = new Car(rect, dir);
                cars.add(c);
                board = board.withCar(c, 0);
                boardCellsLeft -= rect.getArea();
            }
        }

        cars.add(goalCar);
        return new State(boardRect, goalCar.getRect().getPos(), cars.toArray(new Car[0]));
    }
    
    /**
     * Represents the furthest state (in approximately the number of moves) from
     * a given state.
     */
    private static class FurthestStateResult {
        /**
         * The furthest state.
         */
        State state;
        /**
         * The max number of moves required.
         */
        int dist;
        /**
         * Constructs self with exactly the parameters given, no checking.
         * @param state the result state
         * @param dist the number of moves
         */
        private FurthestStateResult(State state, int dist) {
            this.state = state;
            this.dist = dist;
        }
    }
    /**
     * Given a starting state, finds the furthest state (in approximately the
     * number of moves from a given state.
     * @param start the state to start from.
     * @return a result dictating the furthest state and the maximum number of
     *         moves required to solve that state.
     */
    private static FurthestStateResult calculateFurthestState(State start) {
        HashMap<State, Integer> seen = new HashMap<>();
        Queue<State> todo = new ArrayDeque<>();

        seen.put(start, 0);
        
        {
            Queue<State> todoStarting = new ArrayDeque<>();
            todoStarting.add(start);
            
            while (!todoStarting.isEmpty()) {
                State curr = todoStarting.poll();
                for (State next : curr.genMoveStates()) {
                    if (!seen.containsKey(next)) {
                        if (next.hasWon()) {
                            seen.put(next, 0);
                            todoStarting.add(next);
                        } else {
                            seen.put(next, 1);
                            todo.add(next);
                        }
                    }
                }
            }
        }

        int furthest = 0;
        State furthestState = start;

        while (!todo.isEmpty()) {
            State curr = todo.poll();
            int dist = seen.get(curr);

            if (furthest < dist) {
                furthest = dist;
                furthestState = curr;
            }

            for (State next : curr.genMoveStates()) {
                if (!seen.containsKey(next)) {
                    seen.put(next, next.hasWon() ? 0 : dist+1);
                    todo.add(next);
                }
            }
        }

        return new FurthestStateResult(furthestState, seen.get(furthestState));
    }
    
    /**
     * Represents to keep trying for {@link #generateWithParams(int, int, int)}.
     */
    private static final int INFINITE = -1;
    /**
     * Tries to generate a board with at most {@code nCars} taking at least
     * {@code nMoves} to solve under {@code nTries} board generations (or with
     * infinite tries of {@code nTries} is {@link #INFINITE}). If it cannot
     * generate such a board within {@code nTries} it returns the current most
     * difficult puzzle (in number of moves to solve).
     * @param nCars the max number of cars on the board
     * @param nMoves the min number of moves
     * @param nTries the number of boards, at most, to generate
     * @return the generated state
     */
    private static State generateWithParams(int nCars, int nMoves, int nTries) {
        int moves = -1;
        State best = null;

        // TODO: show loading screen and perhaps display these inspirational messages to the user
        System.out.println("Hold on! I'm trying my best to generate a board with " + nCars + " cars, takes " + nMoves + " moves to solve, under " + nTries + " generations.");

        for (int tries = 0; ((tries < nTries) || nTries == INFINITE) && moves < nMoves; tries++) {
            System.out.print("Attempt " + tries + ": ");
            State start = generateInitialState(BOARD, nCars);
            FurthestStateResult result = calculateFurthestState(start);
//            if (moves < result.dist) {
//                moves = result.dist;
//                best = result.state;
//            }
            System.out.print("distance of " + result.dist);
            if (moves < result.dist) {
                int currMoves = result.state.solve().length;
                if (moves < currMoves) {
                    moves = currMoves;
                    best = result.state;
                }
                System.out.print(", " + currMoves + " moves to solve");
            }
            System.out.println();
        }

        if (moves < nMoves) {
            System.out.println("Failed! D=, I'll try better next time *tears*");
        } else {
            System.out.println("Success! *takes a bow*");
        }
        System.out.println("Generated board requiring " + moves + " moves, good luck! :)");
        return best;
    }
    
    /**
     * Represents a mapping of [0, 1] => int
     */
    private static class GradientPoint {
        /**
         * The input point.
         */
        float in;
        /**
         * The corresponding output int.
         */
        int out;
        /**
         * Constructs self with exactly the parameters given, no checking.
         * @param in the input value
         * @param out the corresponding output
         */
        private GradientPoint(float in, int out) {
            this.in = in;
            this.out = out;
        }
        /**
         * Same as {@link #GradientPoint(float, int)} but with a double as input.
         */
        private GradientPoint(double in, int out) {
            this((float)in, out);
        }
    }
    
    /**
     * Given a sorted mapping of [0, 1] => int, maps {@code in} linearly between
     * the two points to the left and right of it. If in is outside of [0, 1],
     * the output is either the int mapped to 0 or 1.
     * @param in the input to map
     * @param points the points to map between
     * @return the resultant mapped value
     */
    private int gradient(float in, GradientPoint[] points) {
        if (in < points[0].in) return points[0].out;
        for (int i = 1; i < points.length; i++) {
            if (in < points[i].in) {
                GradientPoint a = points[i-1], b = points[i];
                return a.out + (int)Math.round(((double)(b.out - a.out)) * ((in - a.in) / (b.in - a.in)));
            }
        }
        return points[points.length - 1].out;
    }
    
    /**
     * Generates a state for the given difficulty.
     * @param difficulty the difficulty
     * @return the generate state
     */
    public State generate(double difficulty) {
        float d = (float)difficulty;
        return generateWithParams(gradient(d, new GradientPoint[]{
            new GradientPoint(0, 1),
            new GradientPoint(1, 16),
        }), gradient(d, new GradientPoint[]{
            new GradientPoint(0, 2),
            new GradientPoint(0.2, 5),
            new GradientPoint(0.5, 13),
            new GradientPoint(0.8, 20),
            new GradientPoint(1, 30),
        }), gradient(d, new GradientPoint[]{
            new GradientPoint(0, 100),
            new GradientPoint(1, 10000),
        }));
    }
    
    /**
     * Generates a state for the given level.
     * @param level the level to generate a state for
     * @return the generate state
     */
    public State generate(int level) {
//        return State.makeDefault();
//        return generateInitialState(BOARD, GOAL_CAR, 10);
//        return generateWithParams(8, 20, 100);
        return generate(Math.atan(((double)level) / 20));
    }


}
