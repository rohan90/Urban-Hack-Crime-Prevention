package urbanhack.reportapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tusharchoudhary on 24/06/15.
 */
public class FacebookLoginService extends AbstractLoginService implements FacebookCallback<LoginResult> {

    private CallbackManager callbackManager;
    private Context context;

    public FacebookLoginService(Context context, LoginServiceCallback loginServiceCallback){
        setContext(context);
        setLoginCode(1);
        setCallback(loginServiceCallback);
        init();
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    protected void init() {
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, this);
    }

    @Override
    public void login() {
        LoginManager.getInstance().logInWithReadPermissions((android.app.Activity) context, getPermissions());
    }

    @Override
    public void onActivityStop() {
        //TODO: handle activity onStop Event here
    }

    @Override
    protected void saveUserOnServer(JSONObject params) {
        //TODO: POST this JSON to the LoginController API of facebook endpoint
    }

    private List<String> getPermissions() {
        List<String> permissions = new ArrayList<>();
        permissions.add("email");
        permissions.add("user_photos");
        return permissions;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void getFbUserDetails() {
        getTaskForFetchingUserDetails().executeAsync();
    }

    @Override
    public void onSuccess(LoginResult loginResult) {
        getFbUserDetails();
    }

    @Override
    public void onCancel() {
        Log.e("fb", "facebook login cancelled!");
    }

    @Override
    public void onError(FacebookException e) {
        Log.e("fb",e.getMessage());
        if (e instanceof FacebookAuthorizationException) {
            if (AccessToken.getCurrentAccessToken() != null) {
                LoginManager.getInstance().logOut();
                 }
        } else
            onLoginFailure();
    }


    private GraphRequest getTaskForFetchingUserDetails() {
        return new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me",
                getRequestBundle(),
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        Log.e("FB RESPONSE","  " + response.getError());
                        try {
                            getImage(response.getJSONObject().getString("id"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //TODO: add validations for successfull response
                        onLoginSuccessful(response.getJSONObject().toString(), getLoginCode());

                    }
                }
        );
    }

    void getImage(String userId){
        /* make the API call */
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/"+userId+"/picture",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                    Logger.logError("get Image "+response.getRawResponse());
                    }
                }
        ).executeAsync();
    }

    private Bundle getRequestBundle() {
        Bundle parameters = new Bundle(1);
        parameters.putString("fields", "id,name,email,first_name,last_name,gender,link,locale,timezone,updated_time,verified");
        return parameters;
    }
}
