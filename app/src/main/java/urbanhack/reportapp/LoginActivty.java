package urbanhack.reportapp;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.norbsoft.typefacehelper.TypefaceHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginActivty extends AppCompatActivity implements LoginServiceCallback,View.OnClickListener {


    private ImageButton fbLoginButton;

    private Activity activity = this;

    private AbstractLoginService loginService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activty);
        TypefaceHelper.typeface(this);
        initView();
        printKeyHashSignatures(this);
    }

    public static void printKeyHashSignatures(Activity activity) {
        try {
            PackageInfo info = activity.getPackageManager().getPackageInfo(
                    "urbanhack.reportapp",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("", "KeyHash:" + Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("", "error: " + e);
        } catch (NoSuchAlgorithmException e) {
            Log.e("", "error: " + e);
        }
    }

    private void initView() {
        initFacebook();
    }


    private void initFacebook() {
        fbLoginButton = (ImageButton) findViewById(R.id.btn_fb_login);
        fbLoginButton.setOnClickListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (loginService != null)
            loginService.onActivityStop();
    }

    @Override
    public void onLoginSuccessful(String response, int loginCode) {
        Log.e("", "Logged in Successfully! reposne" + response);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(response);

            String name = jsonObject.getString("name");
            SharedPreferenceUtil preferenceUtil = SharedPreferenceUtil.getInstance(this);
            preferenceUtil.saveData("author", "name");
            preferenceUtil.saveData("authorUrl", "url");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        startActivity(new Intent(activity, HomeActivity.class));
        finish();
    }


    @Override
    public void onLoginFailure() {
        Log.e("", "Logged in Failure!");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (loginService != null)
            loginService.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_fb_login:
                onFbButtonClicked();
                break;
        }
    }

    private void onFbButtonClicked() {
        loginService = new FacebookLoginService(this, this);
        login();
    }

    private void login() {
        loginService.login();
    }


}