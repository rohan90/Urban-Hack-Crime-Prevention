package urbanhack.reportapp;

/**
 * Created by rohan on 3/10/15.
 */
public class RestException extends Exception {

    private static final long serialVersionUID = 1L;
    protected int httpStatusCode;



    public RestException(int httpStatusCode, String message) {
        super(message);
        this.httpStatusCode = httpStatusCode;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

}