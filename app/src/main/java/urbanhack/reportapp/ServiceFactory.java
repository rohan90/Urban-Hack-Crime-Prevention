package urbanhack.reportapp;

/**
 * Created by rohan on 2/10/15.
 */
public class ServiceFactory {

    public static Service getInstance(int taskCode) {
        Service service = null;
        switch (taskCode) {
            case AppConstants.TASK_CODES.GET_REPORTS:
                service = new GetReportsService();
                break;
            case AppConstants.TASK_CODES.GET_REPORTS_BY_LOCATION:
                service = new GetReportsService();
                break;

        }
        return service;

    }
}
