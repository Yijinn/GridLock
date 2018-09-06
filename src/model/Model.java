package model;

import org.json.simple.JSONObject;

public class Model {
    /**
     * The generator to generate states.
     */
    private StateGenerator generator;
    /**
     * The current state of the game.
     */
    private GameState gameState;
    /**
     * The settings of the user.
     */
    private Settings settings;
    /**
     * The level the user is on.
     */
    private int level;
    
    
    
    /**
     * Constructs a model with exactly the parameters given, no checking.
     * @param gameState the game state
     * @param settings the current settings
     * @param level the level of the user
     */
    private Model(GameState gameState, Settings settings, int level) {
        this.generator = new StateGenerator();
        this.gameState = gameState;
        this.settings = settings;
        this.level = level;
    }
    /**
     * Constructs a new model for a new user.
     */
    public Model() { this(null, new Settings(), 1); }
    
    
    
    /**
     * @return the game state
     */
    public GameState getGameState() { return gameState; }
    /**
     * @return the current settings
     */
    public Settings getSettings() { return settings; }
    /**
     * @return the current level the user is on
     */
    public int getLevel() { return level; }
    
    
    
    /**
     * Constructs a model with exactly the parameters given, only checks for
     * {@code null}.
     * @param gameState the game state
     * @param settings the current settings
     * @param level the level of the user
     * @return the new model or {@code null} if any of the parameters were
     *         {@code null}.
     */
    private static Model withNew(GameState gameState, Settings settings, int level) {
        if (gameState == null || settings == null) return null;
        return new Model(gameState, settings, level);
    }
    /**
     * @param newSettings the new settings
     * @return a copy of the current model with the new settings or {@code null}
     *         if the supplied settings was {@code null}.
     */
    private Model withSettings(Settings newSettings) { return withNew(gameState, newSettings, level); }
    /**
     * @param volume the new volume
     * @return a copy of the current game state with the new volume (if it is
     *         valid) else {@code null}
     */
    public Model withSettingsVolume(float volume) { return withSettings(settings.withVolume(volume)); }
    /**
     * @param theme the new theme
     * @return a copy of the current game state with the new theme (if it is
     *         valid) else {@code null}
     */
    public Model withSettingsTheme(Settings.Theme theme) { return withSettings(settings.withTheme(theme)); }
    
    /**
     * @param newGameState the new game state
     * @return a copy of the current model with the new game state or
     *         {@code null} if the supplied game state was {@code null}.
     */
    private Model withGameState(GameState newGameState) { return withNew(newGameState, settings, level); }
    /**
     * @return a copy of the current model after an undo (if it is valid to
     *         do so) else {@code null}
     */
    public Model withGameStateUndo() { return withGameState(gameState.withUndo()); }
    /**
     * @return a copy of the current model after a redo (if it is valid to
     *         do so) else {@code null}
     */
    public Model withGameStateRedo() { return withGameState(gameState.withRedo()); }
    /**
     * @param from the position to move from
     * @param to the position to move to
     * @return a copy of the current model after a move (from {@code from} to
     *         {@code to}) (if it is valid to do so) else {@code null}
     */
    public Model withGameStateMove(IntVec from, IntVec to) { return withGameState(gameState.withMove(from, to)); }
    /**
     * @return a copy of the current model with a hint (if it is valid to do so)
     *         else {@code null}
     */
    public Model withGameStateHint() { return withGameState(gameState.withHint()); }
    
    /**
     * @param level the new level
     * @return a copy of the current model with a new game for the new level
     */
    private Model withLevel(int level) { return withNew(new GameState(generator.generate(level)), settings, level); }
    /**
     * @return a copy of the current model with a new game for level 1
     */
    public Model withGameStateNew() { return withLevel(1); }
    /**
     * @return a copy of the current model with a new game for the next level
     */
    public Model withGameStateNext() { return withLevel(level + 1); }
    
    
    
    /**
     * Key to store the model's game state under.
     */
    private static final String GAMESTATE_KEY = "gameState";
    /**
     * Key to store the model's settings under.
     */
    private static final String SETTINGS_KEY = "settings";
    /**
     * Key to store the model's level under.
     */
    private static final String LEVEL_KEY = "level";
    /**
     * @return the model serialized to a {@code JSONObject}
     */
    public JSONObject toJson() {
        JSONObject o = new JSONObject();
        o.put(GAMESTATE_KEY, gameState.toJson());
        o.put(SETTINGS_KEY, settings.toJson());
        o.put(LEVEL_KEY, level);
        return o;
    }
    /**
     * Given a {@code JSONObject}, tries to deserialize a model from it.
     * @param o the {@code JSONObject} to deserialize from
     * @return the deserialized model
     */
    public static Model fromJson(JSONObject o) {
        return new Model(GameState.fromJson((JSONObject) o.get(GAMESTATE_KEY)), Settings.fromJson((JSONObject) o.get(SETTINGS_KEY)), ((Long) o.get(LEVEL_KEY)).intValue());
    }
}
