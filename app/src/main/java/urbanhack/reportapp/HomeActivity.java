package urbanhack.reportapp;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity {

    private Toolbar mToolbar;
    private NavigationDrawerFragment mDrawerFragment;

    private Fragment mFragment;
    private SharedPreferenceUtil preferenceUtil;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        preferenceUtil = SharedPreferenceUtil.getInstance(this);

        init();
    }

    private void init() {
        initToolbar();
        initNavDrawer();
        getUserLocation();
        drawerItemSelected(1);
    }

    private void getUserLocation() {
        locationManager = (LocationManager) this.getSystemService(this.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            toast("Please enable your location for full functionality");
        } else {
            getLocationOfDevice();
        }
    }

    private void getLocationOfDevice() {
        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (lastKnownLocation != null) {
            makeUseOfLocation(lastKnownLocation);
        } else {
            requestForLocationUpdates();
        }
    }

    private void requestForLocationUpdates() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);

        locationManager.requestSingleUpdate(criteria, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                makeUseOfLocation(location);
            }

            @Override
            public void onProviderDisabled(String provider) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onStatusChanged(String provider, int status,
                                        Bundle extras) {
            }
        }, null);
    }

    private void makeUseOfLocation(Location lastKnownLocation) {
        String lat = String.valueOf(lastKnownLocation.getLatitude());
        String lng = String.valueOf(lastKnownLocation.getLongitude());
        Logger.logInfo("lat: " + lat + ", long: " + lng);
        preferenceUtil.saveData(AppConstants.BUNDLE_KEYS.LATITUDE_CURRENT, lat);
        preferenceUtil.saveData(AppConstants.BUNDLE_KEYS.LONGITUDE_CURRENT, lng);
    }

    private void initNavDrawer() {
        mDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        mDrawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);

    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(mToolbar);
    }




    public void drawerItemSelected(int position){
        String title = "";
        switch (position){
            case 0:
                title = "Maps"; //TODO string resource this;
                mFragment = HomeFragment.getInstance();
                replaceFragment(mFragment);
                break;
            case 1:
                title = "Search";
                mFragment = SearchFragment.getInstance();
                replaceFragment(mFragment);
                break;
            case 2:
                break;
            default:
                mFragment = HomeFragment.getInstance();
                replaceFragment(mFragment);
                break;
        }
        mToolbar.setTitle(title);
    }
    private void replaceFragment(Fragment instance) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container_home, instance).commit();
    }


    @Override
    public void onPostExecute(Object response, int taskCode, Object... params) {
        super.onPostExecute(response, taskCode, params);
        switch (taskCode){
            case AppConstants.TASK_CODES.GET_REPORTS:
                handleOnReportsFetched(response);
                break;
            case AppConstants.TASK_CODES.GET_REPORTS_BY_LOCATION:
                handleOnReportsFetchedByLocation(response);
                break;
        }
    }

    private void handleOnReportsFetchedByLocation(Object response) {
        Logger.logInfo("responses- " + response.toString());
        try {
            JSONObject root = new JSONObject((String) response);
            //int totalPages = data.getInt("totalPages");
            JSONArray content = root.getJSONArray("data");
            Report report;
            List<Report> reports = new ArrayList<>();
            for (int i = 0; i < content.length(); i++) {
                report = Utils.getObjectFromJsonUnique(content.get(i).toString(), Report.class);
                Logger.logError("POJO made " + report.toString());
                reports.add(report);
            }
            ((SearchListener) mFragment).onSearchCompleted(reports);

        } catch (JSONException e) {//TODO json exception
            e.printStackTrace();
        }
    }

    private void handleOnReportsFetched(Object response) {
        Logger.logInfo("responses- " + response.toString());
        try {
            JSONObject root = new JSONObject((String) response);
            //int totalPages = data.getInt("totalPages");
            JSONArray content = root.getJSONArray("data");
            Report report;
            List<Report> reports = new ArrayList<>();
            for (int i = 0; i < content.length(); i++) {
                report = Utils.getObjectFromJsonUnique(content.get(i).toString(), Report.class);
                Logger.logError("POJO made " + report.toString());
                reports.add(report);
            }
            ((SearchListener) mFragment).setTotalPages(1);
            ((SearchListener) mFragment).onSearchCompleted(reports);

        } catch (JSONException e) {//TODO json exception
            e.printStackTrace();
        }
    }

    public void getReports(int i) {
        executeTask(AppConstants.TASK_CODES.GET_REPORTS, AppConstants.APIs.SEARCH_REPORTS_API/* + "/" + currentPage + "/10"*/, preferenceUtil.getData(AppConstants.BUNDLE_KEYS.TOKEN, ""));
    }

    public void fetchMarkers(double latitude, double longitude, int radius) {
        executeTask(AppConstants.TASK_CODES.GET_REPORTS_BY_LOCATION, AppConstants.APIs.SEARCH_REPORTS_BYLOCATION_API+"/"+latitude+"/"+longitude+"/"+radius, preferenceUtil.getData(AppConstants.BUNDLE_KEYS.TOKEN, ""));
    }

    public interface SearchListener {

        public void onSearchCompleted(List<Report> report);
        public void setTotalPages(int totalPages);
    }



    /*
     on back pressed twice exits app
     */
    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        toast("Please click BACK again to exit");

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
