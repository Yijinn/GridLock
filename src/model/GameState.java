package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import javafx.beans.Observable;
import javafx.scene.paint.Color;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Represents an entire game (including history).
 */
public class GameState extends java.util.Observable {
    /**
     * The history of the game;
     */
    private List<State> states;
    /**
     * The current index in the history we are in.
     */
    private int currState;
    private State.Move hint;
    
    /**
     * Constructs a game state with exactly the parameters given, no checking.
     * @param states the history
     * @param currState the index of the current state in the history
     * @param hint a hint, if any
     */
    private GameState(List<State> states, int currState, State.Move hint) {
        this.states = states;
        this.currState = currState;
        this.hint = hint;
    }
    
    /**
     * Constructs a new game with the given starting state.
     * @param startingState the starting state
     */
    public GameState(State startingState) {

        System.out.println("Start creation of GameState");
    
        this.states = new ArrayList<>();
        this.states.add(startingState);
        this.currState = 0;
        
        System.out.println("GameState Created");
        System.out.println("------------------------------");
    }
    
    /**
     * @return the current state
     */
    public State getCurrentState() { return states.get(currState); }
    /**
     * @return the current move
     */
    public int getMoveNumber() { return currState; }
    
    /**
     * @return a hint, if any
     */
    public State.Move getHint() { return hint; }
    
    /**
     * @return whether we can perform a undo
     */
    public boolean canUndo() { return 0 < currState; }
    /**
     * @return whether we can perform a redo
     */
    public boolean canRedo() { return currState < (states.size() - 1); }
    
    /**
     * Tries to construct a new state with the history moved {@code delta}
     * forward (negative {@code delta} is backwards).
     * @param delta the steps to move
     * @return the new state
     */
    private GameState withHistoryDelta(int delta) {
        int newCurrState = currState + delta;
        return ((0 <= newCurrState) && (newCurrState < states.size())) ? new GameState(states, newCurrState, null) : null;
    }
    
    /**
     * @return a new game state with the history moved back one (returns
     * {@code null} if not possible)
     */
    public GameState withUndo() { return withHistoryDelta(-1); }
    /**
     * @return a new game state with the history moved forward one (returns
     * {@code null} if not possible)
     */
    public GameState withRedo() { return withHistoryDelta( 1); }
    
    /**
     * Tries to construct a new game state with a move from cell {@code from} to
     * {@code to}.
     * @param from the cell to move frmo
     * @param to the cell to move to
     * @return the new game state or null on invalid move
     */
    public GameState withMove(IntVec from, IntVec to) {
        State newState = getCurrentState().withMove(from, to);
        if (newState == null) return null;
    
        ArrayList<State> newStates = new ArrayList<>(states.subList(0, currState+1));
        newStates.add(newState);

        return new GameState(newStates, currState+1, null);
    }
    
    /**
     * @return a new game state with a hint or {@code null} if not possible
     */
    public GameState withHint() {
        State.Move[] solution = getCurrentState().solve();
        if (solution.length < 1) return null;
        
        return new GameState(states, currState, solution[0]);
    }
    
    /**
     * Key to store the history under.
     */
    private static final String STATES_KEY = "states";
    /**
     * Key to store the current history's index under.
     */
    private static final String CURRSTATE_KEY = "currState";
    /**
     * Key to store the current hunt under.
     */
    private static final String HINT_KEY = "hint";
    /**
     * @return the game state serialized to a {@code JSONObject}
     */
    public JSONObject toJson() {
        JSONObject o = new JSONObject();
        o.put(STATES_KEY, State.arrayToJson(states.toArray(new State[0])));
        o.put(CURRSTATE_KEY, currState);
        o.put(HINT_KEY, (hint == null) ? null : hint.toJson());
        return o;
    }
    /**
     * Given a {@code JSONObject}, tries to deserialize a game state from it.
     * @param o the {@code JSONObject} to deserialize from
     * @return the deserialized game state
     */
    public static GameState fromJson(JSONObject o) {
        JSONObject hint = (JSONObject) o.get(HINT_KEY);
        return new GameState(Arrays.asList(State.arrayFromJson((JSONArray) o.get(STATES_KEY))), ((Long) o.get(CURRSTATE_KEY)).intValue(), (hint == null) ? null : State.Move.fromJson(hint));
    }
}
