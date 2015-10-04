package urbanhack.reportapp;


public interface LoginServiceCallback {
   public void onLoginSuccessful(String response, int loginCode);

   public void onLoginFailure();
}
