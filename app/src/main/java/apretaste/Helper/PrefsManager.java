package apretaste.Helper;

import android.content.Context;
import android.preference.PreferenceManager;
import android.widget.EditText;

import apretaste.ProfileInfo;


/**
 * Created by CJAM on 27/7/2017.
 */

public class PrefsManager {
    public void show_value(final Context ctx, EditText editText, final String prefName, String defaultValue) {
        editText.setText(PreferenceManager.getDefaultSharedPreferences(ctx).getString(prefName, defaultValue));

    }

    public void change_value(final Context ctx, final String prefName, EditText editText) {
        PreferenceManager.getDefaultSharedPreferences(ctx).edit().putString(prefName, editText.getText().toString()).apply();

    }

    public void change_value2(final Context ctx, final String prefName, String editText) {
        PreferenceManager.getDefaultSharedPreferences(ctx).edit().putString(prefName, editText).apply();

    }

    public String getData(String key, Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx).getString(key, "");
    }

    public void saveData(String key, Context ctx, String value) {
        PreferenceManager.getDefaultSharedPreferences(ctx).edit().putString(key, value).apply();

    }

    public void saveInt(Context ctx, String key, int value) {
        PreferenceManager.getDefaultSharedPreferences(ctx).edit().putInt(key, value).apply();

    }

    public int readInt(Context ctx, String key) {
        return PreferenceManager.getDefaultSharedPreferences(ctx).getInt(key, 0);
    }

    public boolean getBoolean(Context ctx, String key) {
        return PreferenceManager.getDefaultSharedPreferences(ctx).getBoolean(key, false);
    }


    public void saveBoolean(Context ctx, String key, boolean value) {
        PreferenceManager.getDefaultSharedPreferences(ctx).edit().putBoolean(key, value).apply();
    }

    public float getFloat(Context ctx, String key) {
        return PreferenceManager.getDefaultSharedPreferences(ctx).getFloat(key, 0);
    }

    public void saveFloat(Context ctx, String key, Float value) {
        PreferenceManager.getDefaultSharedPreferences(ctx).edit().putFloat(key, value).apply();
    }


    public void SaveSettingsApp(Context context, ProfileInfo pi) {

        saveData("username", context, pi.username);
        if (pi.profile.picture != "") {
            saveData("picture", context, pi.profile.picture);
        }
        saveFloat(context, "credit", pi.credit);
        saveData("mailbox", context, pi.mailbox);
        saveData("type_img", context, pi.img_quality);
        saveData("token", context, pi.token);
        saveData("timestamp", context, pi.timestamp);
    }
}
