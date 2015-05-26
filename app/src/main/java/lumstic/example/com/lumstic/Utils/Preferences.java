package lumstic.example.com.lumstic.Utils;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
public class Preferences {
    private static final String SERVER_URL = "SERVER_URL";
    private static final String ADD_AUTH_IN_HEADER = "ADD_AUTH_IN_HEADER";
    private static final String access_token = "access_token";
    private static final String SEARCH_LOG_ID="SEARCH_LOG_ID";
    private Context context;
    public Preferences(Context context) {
        super();
        this.context = context;
    }
    protected SharedPreferences getSharedPreferences(String key) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
    private String getString(String key, String def) {
        SharedPreferences prefs = getSharedPreferences(key);
        String s = prefs.getString(key, def);
        return s;
    }
    private void setString(String key, String val) {
        SharedPreferences prefs = getSharedPreferences(key);
        Editor e = prefs.edit();
        e.putString(key, val);
        e.commit();
    }
    private boolean getBoolean(String key, boolean def) {
        SharedPreferences prefs = getSharedPreferences(key);
        boolean b = prefs.getBoolean(key, def);
        return b;
    }
    private void setBoolean(String key, boolean val) {
        SharedPreferences prefs = getSharedPreferences(key);
        Editor e = prefs.edit();
        e.putBoolean(key, val);
        e.commit();
    }
    public boolean addAuthInHeader() {
        return getBoolean(ADD_AUTH_IN_HEADER, false);
    }
    public void setAddAuthInHeader(boolean addAuthInHeader) {
        setBoolean(ADD_AUTH_IN_HEADER, addAuthInHeader);
    }
    public String getAccessToken() {
        return getString(access_token, null);
    }
    public void setAccessToken(String authToken) {
        setString(access_token, authToken);
    }
    public String getSearchLogId() {
        return getString(SEARCH_LOG_ID, null);
    }
    public void setSearchLogId(String searchLogId)
    {
        setString(SEARCH_LOG_ID, searchLogId);
    }
    public void logout() {
        setAddAuthInHeader(false);
        setSearchLogId(null);
    }
}