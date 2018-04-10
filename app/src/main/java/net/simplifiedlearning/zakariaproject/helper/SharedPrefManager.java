package net.simplifiedlearning.zakariaproject.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Belal on 9/25/2017.
 */

public class SharedPrefManager {

    private static final String SHARED_PREF_NAME = "zaksharedpref";
    private static final String KEY_USERNAME = "keyusername";
    private static final String KEY_USERID = "keyuserid";

    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    public void userLogin(int id, String username) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USERNAME, username);
        editor.putInt(KEY_USERID, id);
        editor.apply();
    }

    public User getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        String username = sharedPreferences.getString(KEY_USERNAME, null);
        int userid = sharedPreferences.getInt(KEY_USERID, -1);

        if (username != null && userid != -1) {
            return new User(userid, username);
        }
        return null;
    }

    public void logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}