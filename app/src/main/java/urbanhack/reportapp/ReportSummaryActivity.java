package urbanhack.reportapp;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class ReportSummaryActivity extends BaseActivity {

    private Toolbar toolbar;
    private Report report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_summary);
        report = (Report) getIntent().getParcelableExtra("report");
        init();
    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle(report.getTitle());
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
