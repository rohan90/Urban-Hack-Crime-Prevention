package urbanhack.reportapp;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;



/**
 * Created by rohan on 3/10/15.
 */
public class TaskFragment extends Fragment {

    private Context ctx;

    public interface AsyncTaskListener {

        public void onPreExecute(int taskCode);

        public void onPostExecute(Object response, int taskCode,
                                  Object... params);

        public void onBackgroundError(RestException re, Exception e,
                                      int taskCode, Object... params);

    }
    private AsyncTaskListener asyncTaskListener;

    @Override
    public void onAttach(Activity activity) {
        Logger.logInfo(asyncTaskListener + "");
        super.onAttach(activity);
        asyncTaskListener = (AsyncTaskListener) activity;
        ctx = activity;
        Logger.logInfo("Check AsyncTask Listener");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retain this fragment across configuration changes.
        setRetainInstance(true);
        asyncTaskListener = (AsyncTaskListener) getActivity();
    }


    /**
     * Set the callback to null so we don't accidentally leak the Activity
     * instance.
     */
	/*
	 * @Override public void onDetach() { super.onDetach(); asyncTaskListener =
	 * null; }
	 */

    public AsyncManager createAsyncTaskManagerObject(int taskCode) {
        return new AsyncManager(taskCode);
    }

    // generic task that does the work asynchronously and returns result to the calling activty
    public class AsyncManager extends AsyncTask<Object, Object, Object> {

        private int taskCode;
        private Object[] params;
        private Exception e;
        private long startTime;

        public AsyncManager(int taskCode) {
            this.taskCode = taskCode;
        }

        @Override
        protected void onPreExecute() {
            startTime = System.currentTimeMillis();
            Logger.logInfo("On Preexecute AsyncTask");
            //checking if activity has not been killed btw process
            if (asyncTaskListener != null)
                asyncTaskListener.onPreExecute(this.taskCode);
        }

        @Override
        protected Object doInBackground(Object... params) {
            Service service = ServiceFactory.getInstance(taskCode);
            Logger.logInfo("DoinBackGround");
            String response = null;
            this.params = params;
            try {
                response = (String) service.getData(params);
            } catch (Exception e) {
                e.printStackTrace();
                this.e= e;
            }
            return response;
        }

        @Override
        protected void onPostExecute(Object result) {
            //checking if activity has not been killed btw process
            if (getActivity() != null) {
                Logger.logError(
                        asyncTaskListener.getClass().getName()
                                + " , time taken in ms: "
                                + (System.currentTimeMillis() - startTime));
                if (e != null) {
                    Logger.logError("Ecxetion in do in background: "+e);
                    if (e instanceof RestException) {
                        asyncTaskListener.onBackgroundError((RestException) e,
                                null, this.taskCode, this.params);
                    } else {
                        asyncTaskListener.onBackgroundError(null, e,
                                this.taskCode, this.params);
                    }
                } else {
                    asyncTaskListener.onPostExecute(result, this.taskCode,
                            this.params);
                }



                super.onPostExecute(result);
            }
        }

        public int getCurrentTaskCode() {
            return this.taskCode;
        }

    }
}
