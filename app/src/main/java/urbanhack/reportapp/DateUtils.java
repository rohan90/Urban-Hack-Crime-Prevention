package urbanhack.reportapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;


import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.format.DateTimeFormat;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by rohan on 3/10/15.
 */
public class DateUtils {


    public static String getFormatedDateString(Date date) {
        return DateTimeFormat.forPattern(CustomApp.getAppContext().getString(R.string.date_time_format)).print(new DateTime(date));
    }

    //this overloaded method is not needed anymore.
    public static String getFormatedDateString(Date date, Context context) {
        return DateTimeFormat.forPattern(context.getString(R.string.date_time_format)).print(new DateTime(date));
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // overide this in implementers
        }
    }

    public static String getPostedOnString(Date startDate) {
        DateTime now = new DateTime();
        Duration duration = new Duration(new DateTime(startDate), now);
        if (duration.getStandardMinutes() <= 59)
            return "Posted " + duration.getStandardMinutes() + " m ago";
        else if (duration.getStandardHours() < 24)
            return "Posted " + duration.getStandardHours() + " h ago";
        else
            return "Posted " + duration.getStandardDays() + " d ago";
    }

}
