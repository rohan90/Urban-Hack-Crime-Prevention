package urbanhack.reportapp;

/**
 * Created by rohan on 2/10/15.
 */
public class AppConstants {

    public static final String PREFS_NAME = "PreferencesFile";

    public static class APIs {
        public static final String URL = "http://192.168.1.19:3000";
        public static final String BASE_URL = URL+"/api/v1";
        public static final String SEARCH_REPORTS_API = BASE_URL+"/articles";
        public static String SEARCH_REPORTS_BYLOCATION_API = SEARCH_REPORTS_API;
    }

    public static class TASK_CODES {
        public static final int FB_LOGIN_OBJECT = 1;
        public static final int GOOGLE_LOGIN_OBJECT = 2;

        public static final int GET_REPORTS = 3;
        public static final int GET_REPORTS_BY_LOCATION = 4;
    }

    public static class INTENT_KEYS {
    }

    public static class ACITIVITY_REQUEST_CODES {
        public static final int PREVIEW_REPORT = 1;
        //Follows src->destination syntax
    }

    public static class ACITIVITY_RESULT_CODES {
        //Follows src->destination syntax
    }

    public static class LOGIN_CODES {
        public static final int FACEBOOK_LOGIN = 1;
        public static final int GOOGLE_LOGIN = 2;
    }

    public static class BUNDLE_KEYS {
        public static final String AUTH_TOKEN = "AUTH-TOKEN";
        public static final String TOKEN = "token";
        public static final String LATITUDE_CURRENT = "lattitude";
        public static final String LONGITUDE_CURRENT = "longitude";
    }

    public static class GOOGLE_PLACES_API {
        public static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
        public static final String TYPE_AUTOCOMPLETE = "/autocomplete";
        public static final String OUT_JSON = "/json";
        public static final String API_KEY = "AIzaSyDoscZUBmkqzgZ6Q-5hHJKZ5LMWRCj38-I";
    }
}

