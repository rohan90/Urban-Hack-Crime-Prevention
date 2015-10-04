package urbanhack.reportapp;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;

import com.facebook.FacebookSdk;
import com.norbsoft.typefacehelper.TypefaceCollection;
import com.norbsoft.typefacehelper.TypefaceHelper;
import com.parse.Parse;

/**
 * Created by tusharchoudhary on 10/4/15.
 */
public class CustomApp extends Application {
    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, "TwChkB6R8svZvPsdqaXntjFdGhv4cjYHlqFqIjoR", "eb195pkwJbYnomzcj1HeOc7tD189nHG87dm3SE1d");
        FacebookSdk.sdkInitialize(getApplicationContext());
        TypefaceCollection typeface = new TypefaceCollection.Builder()
                .set(Typeface.NORMAL, Typeface.createFromAsset(getAssets(), "fonts/gotham/Gotham-Rounded-Book.ttf"))
                .create();
        TypefaceHelper.init(typeface);
        CustomApp.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return CustomApp.context;
    }
}
