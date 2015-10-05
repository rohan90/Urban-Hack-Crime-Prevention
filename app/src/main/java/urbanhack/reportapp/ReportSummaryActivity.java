package urbanhack.reportapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.norbsoft.typefacehelper.TypefaceHelper;
import com.squareup.picasso.Picasso;

public class ReportSummaryActivity extends BaseActivity {


    private Toolbar toolbar;
    private Report report;

    private ImageView reportIV;
    private TextView descriptionTV;
    private TextView dateOfIncidentTV;
    private ImageView reporterPicTV;
    private TextView reporterNameTV;
    private TextView dateOfReportTV;
    private TextView firStatusTV;
    private TextView divisionTV;
    private TextView policeStationTV;
    private ImageView addressIV;
    private TextView addressTV;
    private TextView timeOfIncidentTV;
    private TextView categoryTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_summary);
        TypefaceHelper.typeface(this);
        report = (Report) getIntent().getParcelableExtra("report");
        init();
    }

    private void init() {
        initToolbar();
        initViews();
        populateViews();

    }

    private void populateViews() {
        Picasso.with(this).load(report.getAuthorImgUrl()).placeholder(R.drawable.logo_bangalore_police).into(reporterPicTV);
        if(report.getImgUrls().size()>0)
            Picasso.with(this).load(report.getImgUrls().get(0)).placeholder(R.drawable.logo_bangalore_police).into(reportIV);//TODO
        String mapUrl = "https://maps.googleapis.com/maps/api/staticmap?center=" + report.getLattitude() + "," + report.getLongitude() + "&zoom=14&size=500x400&markers=color:red%7C" + report.getLattitude() + "," + report.getLongitude() + "&key=AIzaSyAjzh2HxRAWmzb73LpSxarQsaOy6rHWHTk";
        Logger.logInfo("MAP URL:: " + mapUrl);
        Picasso.with(this).load(mapUrl).placeholder(R.drawable.logo_bangalore_police).into(addressIV);
        Logger.logError("report " + report);
        if(report.getAssailantVehicleType()!=null)
            descriptionTV.setText(Utils.getSeperatedStringWithNullChecks(" ", report.getContent(), report.getAddressOfIncident(),""+report.getVictimsAge(), report.getTypeOfPlace(), report.getAssailantVehicleType(), report.getAssailantVehicleModel()));
        else
            descriptionTV.setText(Utils.getSeperatedStringWithNullChecks(" ", report.getContent(), report.getAddressOfIncident()));
        categoryTV.setText(report.getCategory());
        dateOfIncidentTV.setText(DateUtils.getFormatedDateString(report.getDateOfIncident()));
        timeOfIncidentTV.setText(report.getTimeOfOccurencce());
        addressTV.setText(report.getAddressOfIncident());
        policeStationTV.setText(report.getPoliceStation());
        firStatusTV.setText(report.getFirStatus());
        divisionTV.setText("");//TODO
        dateOfReportTV.setText(DateUtils.getFormatedDateString(report.getDateOfReport()));
        reporterNameTV.setText(report.getAuthor());

    }

    private void initViews() {
        reportIV = (ImageView) findViewById(R.id.iv_summarry_top);
        descriptionTV = (TextView) findViewById(R.id.tv_summary_description);
        dateOfIncidentTV = (TextView) findViewById(R.id.tv_summary_dateofincident);
        categoryTV = (TextView) findViewById(R.id.tv_summary_category);
        timeOfIncidentTV = (TextView) findViewById(R.id.tv_summary_timeofincident);
        addressTV = (TextView) findViewById(R.id.tv_address_value);
        addressIV = (ImageView) findViewById(R.id.map_summary_map);
        policeStationTV = (TextView) findViewById(R.id.tv_summary_station);
        divisionTV = (TextView) findViewById(R.id.tv_summary_division);
        firStatusTV = (TextView) findViewById(R.id.tv_summary_fir_status);
        dateOfReportTV = (TextView) findViewById(R.id.tv_summary_dateofreport);
        reporterNameTV = (TextView) findViewById(R.id.tv_summary_reporter_name);
        reporterPicTV = (ImageView) findViewById(R.id.iv_summary_profilepic);
        addressIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGoogleMaps();
            }
        });
    }

    private void openGoogleMaps() {
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?daddr=" + report.getLattitude() + "," + report.getLongitude()));
        startActivity(intent);
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle(report.getTitle());
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
