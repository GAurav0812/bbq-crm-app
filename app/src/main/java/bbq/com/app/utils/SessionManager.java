package bbq.com.app.utils;

/**
 * Created by System-2 on 11/10/2016.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import bbq.com.app.ConfigureActivity;
//import com.app.bbq.crm.ConfigureActivity;

import java.util.HashMap;


public class SessionManager {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "BBQNSAMPref_v1.0";


    public static final String OUTLET_DETAILS = "outletDetails";
    public static final String DEVICE_ID = "deviceId";
    private static final String IS_STORE_ID = "IsStoreId";
    public static final String QUESTION_LIST = "OuestionList";


    // Constructor
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     */
    public void registerDevice(String deviceId){
        editor.putString(DEVICE_ID, deviceId);
        editor.apply();
    }

    public void configureStore(String outletDetails) {
        // Storing login value as TRUE
        editor.putBoolean(IS_STORE_ID, true);

        editor.putString(OUTLET_DETAILS, outletDetails);

        // commit changes
        editor.apply();
    }

    public void configureQuestions(String questions) {
        editor.putString(QUESTION_LIST, questions);
        // commit changes
        editor.apply();
    }


    public Boolean isStoredId() {
        return pref.getBoolean(IS_STORE_ID, false);

    }


    /**
     * Get stored session data
     */
    public HashMap<String, String> getStoreDetails()
    {
        HashMap<String, String> store = new HashMap<String, String>();

        store.put(OUTLET_DETAILS, pref.getString(OUTLET_DETAILS, null));
        store.put(DEVICE_ID, pref.getString(DEVICE_ID, null));

        store.put(QUESTION_LIST, pref.getString(QUESTION_LIST, null));

        return store;
    }




    public void clearAccount() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.apply();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, ConfigureActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }
}
