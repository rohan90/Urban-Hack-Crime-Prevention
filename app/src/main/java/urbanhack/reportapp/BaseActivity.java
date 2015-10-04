package urbanhack.reportapp;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;


/**
 * Created by rohan on 2/10/15.
 */
public abstract class BaseActivity extends AppCompatActivity implements TaskFragment.AsyncTaskListener{

    protected TaskFragment mTaskFragment;
    private final String fragmentTag = "task";
    private ProgressDialog barProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentManager fm = getSupportFragmentManager();
        mTaskFragment = (TaskFragment) fm.findFragmentByTag(fragmentTag);

        // If the Fragment is non-null, then it is currently being
        // retained across a configuration change.
        if (mTaskFragment == null) {
            mTaskFragment = new TaskFragment();
            fm.beginTransaction().add(mTaskFragment, fragmentTag).commit();
        }
    }

    protected AsyncTask executeTask(int taskCode, Object... params) {
        if (Utils.isConnectedToInternet(this)) {
            mTaskFragment.createAsyncTaskManagerObject(taskCode)
                    .execute(params);
        }else {
            toast("Internet not available, Please try again later!");
            stopLoader();
        }

        return null;
    }
    public void toastLong(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPreExecute(int taskCode) {
        Logger.logInfo("on Pre Execute, task code" + taskCode);
    }

    @Override
    public void onPostExecute(Object response, int taskCode, Object... params) {
        Logger.logInfo("onPostExecute-> taskcode:" + taskCode + ", response" + response);
    }

    @Override
    public void onBackgroundError(RestException re, Exception e, int taskCode, Object... params) {
        stopLoader();
        Logger.logError("onBackgroundError : "+e);

    }

    protected void startLoader(String message,String title) {
        barProgressDialog = new ProgressDialog(this);
        barProgressDialog.setTitle(title);
        barProgressDialog.setMessage(message);
        barProgressDialog.setCancelable(false);
        barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
        barProgressDialog.show();
    }

    protected  void stopLoader(){
        if(barProgressDialog!=null) barProgressDialog.dismiss();
    }

    protected void toast(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}
