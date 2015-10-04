package urbanhack.reportapp;


import org.json.JSONException;

import java.io.IOException;

public interface Service {

	public Object getData(Object... params) throws JSONException, NullPointerException, IOException;
	
	
}
