package apretaste.Helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by cjam on 8/29/2017.
 */

public class DataHelper {
    public String addMinutes(String minutes) {

        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MINUTE, Integer.parseInt(minutes));
        return new SimpleDateFormat("dd/MM/yyyy hh:mmaa").format(c.getTime());

    }

    public static Boolean compareDatime(String datetime1, String datetime2) {
        if (datetime1.compareTo(datetime2) > 0) {
            return true;
        }
        return false;
    }

    public String getNowDateTime() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        return new SimpleDateFormat("dd/MM/yyyy hh:mmaa").format(c.getTime());
    }

    public static boolean compareTwoDates(String currentDateString, String yourDateString) throws ParseException {

        SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy hh:mmaa");
        Date currentDate = sd.parse(currentDateString);
        SimpleDateFormat yourDateFormat = new SimpleDateFormat(
                "dd/MM/yyyy hh:mmaa");

        Date yourDate = yourDateFormat.parse(yourDateString);

        if (yourDate.after(currentDate)) {
            return true;
        } else {
            return false;
        }

    }
}
