package urbanhack.reportapp;


import org.json.JSONException;

import java.io.IOException;

/**
 * Created by rohan on 3/10/15.
 */
public class GetReportsService extends GenericService {
    @Override
    public Object getData(Object... params) throws JSONException, NullPointerException, IOException {
        Logger.logInfo("in GET : Get Reports , fetchin...");
        String response = null;
        if (params != null && params.length > 1) {
            try {
                response = doGet(params[0], params[1]);
            } catch (RestException e) {
                e.printStackTrace();
            }
        }
        return response;
    }
}
