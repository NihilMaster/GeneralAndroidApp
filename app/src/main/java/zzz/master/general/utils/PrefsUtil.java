package zzz.master.general.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefsUtil {

    private static final String PREFS_NAME = "my_app_prefs"; // Nombre del archivo SharedPreferences
    private final SharedPreferences sharedPreferences;

    // Constructor
    public PrefsUtil(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    // Métoodo para guardar un valor String
    public void setString(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    // Métoodo para obtener un valor String
    public String getString(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    // Métoodo para guardar un valor booleano
    public void setBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    // Métoodo para obtener un valor booleano
    public boolean getBoolean(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    // Métoodo para eliminar una clave
    public void deleteKey(String key) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }
}
