package urbanhack.reportapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.norbsoft.typefacehelper.TypefaceHelper;

import java.util.List;

/**
 * Created by rohan on 3/10/15.
 */
public class HomeFragment extends Fragment implements OnMapReadyCallback, HomeActivity.SearchListener {

    private static HomeFragment instance;
    private View view;
    private SharedPreferenceUtil preferenceUtil;
    private List<Report> mReports;
    private GoogleMap mMap;

    public static Fragment getInstance() {
        if (instance == null)
            instance = new HomeFragment();
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, null);
        TypefaceHelper.typeface(view);
        preferenceUtil = SharedPreferenceUtil.getInstance(getActivity());
        initView();
        return view;
    }

    private void initView() {
        initMap();
    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.mMap = map;
        LatLng city = new LatLng(12.58, 77.38); //TODO make this dynamic? BLORE
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                city, 16));
        //map.moveCamera(CameraUpdateFactory.newLatLng(city));
        CameraPosition cameraPosition = new CameraPosition.Builder().target(
                city).zoom(12).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(false);

        LatLng myLocation = new LatLng(Double.parseDouble(preferenceUtil.getData(AppConstants.BUNDLE_KEYS.LATITUDE_CURRENT, "12.58")), Double.parseDouble(preferenceUtil.getData(AppConstants.BUNDLE_KEYS.LONGITUDE_CURRENT, "77.38")));
        ((HomeActivity) getActivity()).fetchMarkers(myLocation.latitude, myLocation.longitude, 10);

    }

    private void initMarkers(GoogleMap map) {
        for (Report r : mReports) {
            Logger.logInfo("rendering marker on " + r.getLattitude() + "," + r.getLongitude() + "for Role:" + r.getRole());
            if (r.getRole() == Role.USER)
                map.addMarker(new MarkerOptions().position(new LatLng(r.getLattitude(), r.getLongitude())).title(r.getTitle()).snippet(r.getCategory()));
            else {
                map.addMarker(new MarkerOptions().position(new LatLng(r.getLattitude(), r.getLongitude())).title(r.getTitle()).snippet(r.getCategory()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
            }
        }
    }


    @Override
    public void onSearchCompleted(List<Report> report) {
        mReports = report;
        initMarkers(mMap);
    }

    @Override
    public void setTotalPages(int totalPages) {

    }
}
