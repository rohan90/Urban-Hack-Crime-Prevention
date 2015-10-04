package urbanhack.reportapp;

import android.content.Intent;

import org.json.JSONObject;


public abstract class AbstractLoginService {
    protected LoginServiceCallback callback;

    protected int loginCode;

    protected abstract void init();

    public abstract void login();

    public abstract void onActivityStop();

    protected abstract void saveUserOnServer(JSONObject jsonObject);

    public abstract void onActivityResult(int requestCode, int resultCode, Intent data);

    protected void onLoginSuccessful(String response,int loginCode){
        callback.onLoginSuccessful(response,loginCode);
    }

    protected void onLoginFailure(){
        callback.onLoginFailure();
    }

    protected void setCallback(LoginServiceCallback callback) {
        this.callback = callback;
    }

    public int getLoginCode() {return loginCode;}

    public void setLoginCode(int loginCode) {this.loginCode = loginCode;}


}
