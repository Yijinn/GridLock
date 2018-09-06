package model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Represents a state of the board.
 */
public class State implements Comparable<State> {
    /**
     * The grid of cars.
     */
    private Board board;
    /**
     * A list of all cars on the board. The last car is the goal car.
     */
    private Car[] cars;
    /**
     * The position of the goal.
     */
    private IntVec goal;
    
    /**
     * Prints debugging messages prepended with the class's name.
     * @param msg the message to print
     */
    private static void printDebug(String msg) {
        System.out.println(State.class.getCanonicalName() + ": " + msg);
    }
    
    /**
     * Checks whether:
     *  - all the cars are inside the board
     *  - the goal is inside the board
     *  - none of the cars intersect
     * @param boardRect the board
     * @param goal the goal position
     * @param cars the array of cars
     * @return true if everything is ok, false otherwise
     */
    private static boolean checkCars(IntRect boardRect, IntVec goal, Car[] cars) {
        if (!boardRect.contains(new IntRect(goal, cars[cars.length-1].getRect().getSize()))) {
            printDebug("The board " + boardRect + " does not contain the goal " + goal);
            return false;
        }
        for (Car c : cars) {
            if (!boardRect.contains(c.getRect())) {
                printDebug("The board " + boardRect + " does not contain piece " + c.getRect());
                return false;
            }
        }
        // TODO: check car can reach goal maybe
        for (int i = 0; i < cars.length; i++) {
            for (int j = i+1; j < cars.length; j++) {
                if (cars[i].getRect().intersects(cars[j].getRect())) {
                    printDebug("Rects " + cars[i].getRect() + " and " + cars[j].getRect() + " intersect");
                    return false;
                }
            }
        }
        return true;
    }
    
    /**
     * Checks the supplied arguments with
     * {@link #checkCars(IntRect, IntVec, Car[])} and throws if it returns
     * {@code false}.
     * @param boardRect the board
     * @param goal the goal position
     * @param cars the array of cars
     */
    private static void throwIfNotOk(IntRect boardRect, IntVec goal, Car[] cars) {
        if (!checkCars(boardRect, goal, cars)) {
            throw new RuntimeException(
                State.class.getCanonicalName() + ": Invalid cars supplied for boardRect " + boardRect + ", their rects are: " +
                Arrays.stream(cars).map(Car::getRect).map(IntRect::toString).collect(Collectors.joining(", "))
            );
        }
    }
    
    /**
     * Constructs a state with exactly the parameters given, no checking.
     * @param board the board
     * @param goal the goal position
     * @param cars the array of cars
     */
    private State(Board board, IntVec goal, Car[] cars) {
        this.board = board;
        this.goal = goal;
        this.cars = cars;
    }
    /**
     * Constructs a state given the board size, the goal and the cars on the
     * board. Throws an exception if the supplied arguments are invalid.
     * @param boardRect the board
     * @param goal the goal position
     * @param cars the array of cars
     */
    public State(IntRect boardRect, IntVec goal, Car[] cars) {
        this((Board) null, goal, cars);
        throwIfNotOk(boardRect, goal, cars);
        this.board = Board.genFor(boardRect, cars);
    }
    /**
     * Constructs a state given the board size, the goal and the cars on the
     * board. Throws an exception if the supplied arguments are invalid.
     * @param size the size of the board
     * @param goal the goal position
     * @param cars the array of cars
     */
    public State(IntVec size, IntVec goal, Car[] cars) {
        this(new IntRect(0, 0, size.getX(), size.getX()), goal, cars);
    }
    
    /**
     * @return makes the default state (from the assignment page)
     */
    public static State makeDefault() {
        return new State(new IntVec(6, 6), new IntVec(4, 2), new Car[]{
            new Car(new IntRect(0, 0, 2, 1), Car.RIGHT),
            new Car(new IntRect(0, 1, 1, 3), Car.DOWN),
            new Car(new IntRect(0, 4, 1, 2), Car.DOWN),
            new Car(new IntRect(1, 2, 2, 1), Car.RIGHT),
            new Car(new IntRect(3, 1, 1, 3), Car.DOWN),
            new Car(new IntRect(2, 5, 3, 1), Car.RIGHT),
            new Car(new IntRect(4, 4, 2, 1), Car.RIGHT),
            new Car(new IntRect(5, 0, 1, 3), Car.DOWN),
        });
    }
    
//    public static State genRandomFinished(IntRect boardRect, int nCars) {
//        State s = new State(new Board(boardRect), null);
//    }
    
    /**
     * @return the board rect
     */
    public IntRect getBoardRect() { return board.getRect(); }
    /**
     * @return the goal rect
     */
    public IntRect getGoalRect() { return new IntRect(goal, cars[cars.length-1].getRect().getSize()); }
    
    /**
     * @return whether the goal car is in the goal
     */
    public boolean hasWon() { return cars[cars.length-1].getRect().getPos().equals(goal); }
    
    /**
     * @return the number of cars in the state
     */
    public int getNumCars() { return cars.length; }
    /**
     * @param i the index of the car to retrieve
     * @return the {@code i}th car
     */
    public Car getCar(int i) { return cars[i]; }
    /**
     * @param p the position to query for a car at
     * @return the car index at {@code p} or -1 if none exists
     */
    public int getCarIndexAt(IntVec p) {
        return board.get(p);
    }
    /**
     * @param p the position to query for a car at
     * @return the car at {@code p} or {@code null} if none exists
     */
    public Car getCarAt(IntVec p) {
        int carI = getCarIndexAt(p);
        return (carI == Board.NONE) ? null : cars[carI];
    }
    
    /**
     * @param o the object to check against
     * @return true if this state is identical to {@code o}
     */
    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        State state = (State) o;

        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(cars, state.cars);
    }
    
    /**
     * @return a hash of this state
     */
    @Override
    public final int hashCode() {
        return Objects.hash(board.getRect(), goal, Arrays.hashCode(cars));
    }
    
    /**
     * @param o the vector to check against
     * @return whether this state is identical to {@code o}
     */
    public final boolean equals(State o) {
        // not sure if faster to compare entire board or to compare cars
        return board.getRect().equals(o.board.getRect()) && Arrays.equals(cars, o.cars);
    }
    
    /**
     * Provides an arbitrary ordering on states.
     * @param o the state to compare with
     * @return negative if ({@code this < o}), positive if ({@code this > o})
     *         and zero otherwise
     */
    @Override
    public int compareTo(State o) {
        return IntStream.range(0, cars.length)
            .map(i -> cars[i].compareTo(o.cars[i]))
            .filter(cmp -> cmp != 0).findFirst().orElse(0);
    }
    
    /**
     * @param carI the index of the car to move
     * @param d the delta to move the car by
     * @return a new state with the car moved by {@code d} units, or
     *         {@code null} if this is not possible.
     */
    private State withMove(int carI, int d) {
        Car c = cars[carI];

        IntVec delta = c.getDirection().mul(d);
        IntVec deltaSize = delta.signum().add(1).div(2);
        IntVec pointInCar = deltaSize.mul(c.getRect().getSize()).add(c.getRect().getPos());
        IntVec pointDest = pointInCar.add(delta).add(c.getRect().getSize().mul(c.getDirection().swap()));

        // area we move over
        IntRect movementRect = IntRect.fromUnordered(pointInCar, pointDest);
        if (!board.getRect().contains(movementRect)) return null;
        if (!board.allNone(movementRect)) return null;

        Car newC = c.withMove(d);
        Car[] newCars = cars.clone();
        newCars[carI] = newC;

        Board newBoard = board.withMove(c.getRect(), newC.getRect());

        return new State(newBoard, goal, newCars);
    }
    
    /**
     * @param from the position to move from
     * @param to the position to move to
     * @return a new state with the car moved from {@code from} to {@code to},
     *         or {@code null} if this is not possible.
     */
    public State withMove(IntVec from, IntVec to) {
        if (from.equals(to)) return null;
        int carI = board.get(from);
        if (carI == Board.NONE) return null;

        return withMove(carI, to.sub(from).manDist());
    }
    
    /**
     * Represents a move of {@code carI} of {@code delta} units ending up at
     * {@code state}.
     */
    public static class Move {
        /**
         * The index of the car that was moved.
         */
        private int carI;
        /**
         * The amount of units the car was moved by.
         */
        private int delta;
        /**
         * The state that resulted from the move.
         */
        private State state;
        /**
         * Constructs a move with exactly the parameters given, no checking.
         * @param carI the index of the car that was moved
         * @param delta the amount of units the car was moved by
         * @param state the state that resulted from the move
         */
        private Move(int carI, int delta, State state) {
            this.carI = carI;
            this.delta = delta;
            this.state = state;
        }
        /**
         * @return the index of the car that was moved
         */
        public int getCarI() { return carI; }
        /**
         * @return the amount of units the car was moved by
         */
        public int getDelta() { return delta; }
        /**
         * @return the state that resulted from the move
         */
        public State getState() { return state; }
        /**
         * Key to store the move's board under.
         */
        private static final String CARI_KEY = "carI";
        /**
         * Key to store the move's cars under.
         */
        private static final String DELTA_KEY = "delta";
        /**
         * Key to store the move's goal under.
         */
        private static final String STATE_KEY = "state";
        /**
         * @return the move serialized to a {@code JSONObject}
         */
        public JSONObject toJson() {
            JSONObject o = new JSONObject();
            o.put(CARI_KEY, carI);
            o.put(DELTA_KEY, delta);
            o.put(STATE_KEY, state.toJson());
            return o;
        }
        /**
         * Given a {@code JSONObject}, tries to deserialize a move from it.
         * @param o the {@code JSONObject} to deserialize from
         * @return the deserialized move
         */
        public static Move fromJson(JSONObject o) {
            return new Move(((Long) o.get(CARI_KEY)).intValue(), ((Long) o.get(DELTA_KEY)).intValue(), State.fromJson((JSONObject) o.get(STATE_KEY)));
        }
    }
    
    /**
     * @return an array of moves representing all possible states which are one
     *         move away from the current one
     */
    public Move[] genMoves() {
        ArrayList<Move> moves = new ArrayList<>();
        for (int i = 0; i < cars.length; i++) {
            for (int d = 1; ; d++) {
                State state = withMove(i, d);
                if (state == null) break;
                moves.add(new Move(i, d, state));
            }
            for (int d = -1; ; d--) {
                State state = withMove(i, d);
                if (state == null) break;
                moves.add(new Move(i, d, state));
            }
        }
        return moves.toArray(new Move[0]);
    }
    /**
     * @return an array of states representing all possible states which are one
     *         move away from the current one
     */
    public State[] genMoveStates() {
        return Arrays.stream(genMoves()).map(Move::getState).toArray(State[]::new);
    }
    
    /**
     * @return an array of moves representing the steps needed to solve the
     *         current state.
     */
    public Move[] solve() {
        HashMap<State, Move> from = new HashMap<>();
        Queue<Move> todo = new ArrayDeque<>();
    
        from.put(this, null);
        todo.add(new Move(-1, 0, this));
    
        while (!todo.isEmpty()) {
            Move curr = todo.poll();
        
            if (curr.getState().hasWon()) {
                ArrayDeque<Move> moves = new ArrayDeque<>();
                for (Move move = curr; move != null; move = from.get(move.getState())) {
                    moves.addFirst(move);
                }
                moves.removeFirst();
                return moves.toArray(new Move[0]);
            }
        
            for (Move next : curr.getState().genMoves()) {
                if (!from.containsKey(next.getState())) {
                    from.put(next.getState(), curr);
                    todo.add(next);
                }
            }
        }
    
        return null;
    }
    
    /**
     * Key to store the state's board under.
     */
    private static final String BOARDRECT_KEY = "board";
    /**
     * Key to store the state's cars under.
     */
    private static final String CARS_KEY = "cars";
    /**
     * Key to store the state's goal under.
     */
    private static final String GOAL_KEY = "rect";
    /**
     * @return the state serialized to a {@code JSONObject}
     */
    public JSONObject toJson() {
        JSONObject o = new JSONObject();
        o.put(BOARDRECT_KEY, board.getRect().toJson());
        o.put(CARS_KEY, Car.arrayToJson(cars));
        o.put(GOAL_KEY, goal.toJson());
        return o;
    }
    /**
     * Given a {@code JSONObject}, tries to deserialize a state from it.
     * @param o the {@code JSONObject} to deserialize from
     * @return the deserialized state
     */
    public static State fromJson(JSONObject o) {
        return new State(IntRect.fromJson((JSONObject) o.get(BOARDRECT_KEY)), IntVec.fromJson((JSONObject) o.get(GOAL_KEY)), Car.arrayFromJson((JSONArray) o.get(CARS_KEY)));
    }
    /**
     * Given an array of states, serializes it to a {@code JSONArray}
     * @param states the array of states to serialize
     * @return the states array serialized to a {@code JSONArray}
     */
    public static JSONArray arrayToJson(State[] states) {
        return Arrays.stream(states).map(State::toJson).collect(Collectors.toCollection(JSONArray::new));
    }
    /**
     * Given a {@code JSONArray}, tries to deserialize an array of states from it.
     * @param a the {@code JSONArray} to deserialize from
     * @return the deserialized state array
     */
    public static State[] arrayFromJson(JSONArray a) {
        // TODO: figure out why the (State[]) cast is required
        return (State[]) a.stream().map(o -> State.fromJson((JSONObject) o)).toArray(State[]::new);
    }
}
