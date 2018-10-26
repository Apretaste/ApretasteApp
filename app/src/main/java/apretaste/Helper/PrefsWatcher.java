package apretaste.Helper;

import android.content.Context;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Created  by Raymond Arteaga on 13/07/2017.
 */
public class PrefsWatcher {
    public static void bindWatcher(final Context ctx, EditText editText, final String prefName, String defaultValue) {
        editText.setText(PreferenceManager.getDefaultSharedPreferences(ctx).getString(prefName, defaultValue));
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                PreferenceManager.getDefaultSharedPreferences(ctx).edit().putString(prefName, s.toString()).apply();
            }
        });
    }
}
