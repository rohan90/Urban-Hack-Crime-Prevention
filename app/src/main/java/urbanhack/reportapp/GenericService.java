package urbanhack.reportapp;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import org.json.JSONException;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

abstract public class GenericService implements Service {

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    OkHttpClient client;

    private Response post(String url, String json) throws RestException, IOException {
        Logger.logError("Making a POST to url:" + url + ", params " + json);
        client.setConnectTimeout(15, TimeUnit.SECONDS); // connect timeout
        client.setReadTimeout(15, TimeUnit.SECONDS);    // socket timeout
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response;
    }

    private Response put(String url, String json, String token) throws RestException, IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .header(AppConstants.BUNDLE_KEYS.AUTH_TOKEN, token)
                .put(body)
                .build();
        Response response = client.newCall(request).execute();
        return response;
    }


    private Response postLogout(String url, String token) throws RestException, IOException {
        RequestBody body = RequestBody.create(JSON, "");
        Request request = new Request.Builder()
                .url(url)
                .header(AppConstants.BUNDLE_KEYS.AUTH_TOKEN, token)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response;
    }

    private Response get(String url, String token) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .header(AppConstants.BUNDLE_KEYS.AUTH_TOKEN, token)
                .build();
        Response response = client.newCall(request).execute();
        return response;
    }

    protected GenericService() {
        client = new OkHttpClient();
        client.setConnectTimeout(10, TimeUnit.SECONDS); // connect timeout
        client.setReadTimeout(10, TimeUnit.SECONDS);    // socket timeout

    }
    protected final String doPost(Object url,
                                  Object postBody) throws RestException, JSONException, IOException {

        Response response =post((String)url,(String)postBody);
        String json =response.body().string();
        Logger.logError("POST response "+json);
        return json;
    }

    protected final String doPost(Object url,
                                  Object postBody, Object token) throws RestException, JSONException, IOException {

        Response response =post((String)url,(String)postBody,(String) token);
        String json =response.body().string();
        Logger.logError("POST response "+json);
        return json;
    }

    private Response post(String url, String postBody, String token) throws IOException {
        Logger.logError("Making a POST to url:"+url+", params "+postBody+", token "+token);
        RequestBody body = RequestBody.create(JSON, postBody);
        Request request = new Request.Builder()
                .url(url)
                .header(AppConstants.BUNDLE_KEYS.AUTH_TOKEN,token)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response;
    }

    protected final String doPut(Object url,
                                 Object putBody,Object token) throws RestException, JSONException, IOException {
        Logger.logError(" Making a PUT call to url:"+url+", params "+putBody+" , token "+token);

        Response response = put((String) url, (String) putBody, (String) token);
        String json =null;
        ResponseBody body = response.body();
        if(body!=null)
            json =response.body().string();

        Logger.logInfo(""+json);
        return json;
    }

    protected final String doLogout(Object url,
                                    Object token) throws RestException, JSONException, IOException {
        Logger.logError("url:"+url+", token "+token);

        Response response =postLogout((String) url, (String) token);
        String json =response.body().string();
        Logger.logInfo(json);
        return json;
    }

    protected final String doGet(Object url,
                                 Object token) throws RestException, JSONException, IOException {
        Logger.logError("url:"+url+", token "+token);
        Response response = get((String) url, (String) token);
        String json =response.body().string();
        return json;
    }

    protected final String doDelete(Object url,
                                    Object token) throws RestException, JSONException, IOException {
        Logger.logError("url:"+url+", token "+token);
        Response response = delete((String) url, (String) token);
        String json =response.body().string();
        return json;
    }

    private Response delete(String url, String token) throws IOException {
        Logger.logError("Making a DELETE to url:" + url + ", token " + token);
        Request request = new Request.Builder()
                .url(url)
                .header(AppConstants.BUNDLE_KEYS.AUTH_TOKEN,token)
                .delete()
                .build();
        Response response = client.newCall(request).execute();
        return response;
    }

    protected RestException getRestException(int code) {
        return new RestException(code, "");
    }


    protected void handleResponse(Response response)
            throws JSONException, RestException, IOException {

        if (response == null) {
            throw new RestException(ErrorCodes.UNKNOWN_ERROR,
                    "Some technical error occurred !");
        } else {
            if (response.isSuccessful()) {
                Logger.logInfo(response.body().string());
                return;

            }else if (response.code() == ErrorCodes.NETWORK_SLOW_ERROR) {
                throw new RestException(ErrorCodes.NETWORK_SLOW_ERROR,
                        "Connection timed out");

            } else if (response.code() == ErrorCodes.URL_INVALID) {
                throw new RestException(ErrorCodes.URL_INVALID,
                        "URL Not Valid");
            } else {
                throw new RestException(ErrorCodes.UNKNOWN_ERROR,
                        "Unknown Error");
            }

        }

    }


    public static Response saveReport(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response;
    }
}
