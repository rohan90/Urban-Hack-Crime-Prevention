package urbanhack.reportapp;

import android.util.Log;

/**
 * Created by tusharchoudhary on 24/06/15.
 */
public class Logger {
    private static final boolean isLoggerOn = true;
    private static final String TAG = "HunterLogs";

    public static void logError(String message){
        if(isLoggerOn)
            Log.e(TAG,message);
    }
    public static void logInfo(String message){
        if(isLoggerOn)
            Log.d(TAG,message);
    }
}
