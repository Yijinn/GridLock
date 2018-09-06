package model;

import org.json.simple.JSONObject;

public class Settings {
    /**
     * An enum of the available themes.
     */
    public enum Theme {
        DEFAULT,
        DEUTERANOPE,
        TRITANOPE
    }
    /**
     * The volume the user has set from 0 to 1.
     */
    private float volume;
    /**
     * The theme the user has set.
     */
    private Theme theme;
    
    /**
     * Constructs a settings with exactly the parameters given, no checking.
     * @param volume the volume
     * @param theme the theme
     */
    private Settings(float volume, Theme theme) {
        this.volume = volume;
        this.theme = theme;
    }
    /**
     * Constructs a settings object with default values.
     */
    public Settings() { this(0.5f, Theme.DEFAULT); }
    
    /**
     * @return the volume
     */
    public float getVolume() { return volume; }
    /**
     * @return the theme
     */
    public Theme getTheme() { return theme; }
    
    /**
     * @param volume the new volume
     * @return a new settings object with the volume or {@code null} if the
     *         {@code volume} was outside the correct range or has not changed.
     */
    public Settings withVolume(float volume) {
        return (0 <= volume && volume <= 1 && this.volume != volume) ? new Settings(volume, theme) : null;
    }
    /**
     * @param theme the new theme
     * @return a new settings object with the theme or {@code null} if the
     *         {@code theme} has not changed.
     */
    public Settings withTheme(Theme theme) {
        return (this.theme != theme) ? new Settings(volume, theme) : null;
    }
    
    /**
     * Key to store the settings' volume under.
     */
    private static final String VOLUME_KEY = "volume";
    /**
     * Key to store the settings' theme under.
     */
    private static final String THEME_KEY = "theme";
    /**
     * @return the settings serialized to a {@code JSONObject}
     */
    public JSONObject toJson() {
        JSONObject o = new JSONObject();
        o.put(VOLUME_KEY, volume);
        o.put(THEME_KEY, theme.toString());
        return o;
    }
    /**
     * Given a {@code JSONObject}, tries to deserialize a settings from it.
     * @param o the {@code JSONObject} to deserialize from
     * @return the deserialized settings
     */
    public static Settings fromJson(JSONObject o) {
        return new Settings(((Double) o.get(VOLUME_KEY)).floatValue(), Theme.valueOf((String) o.get(THEME_KEY)));
    }
}
