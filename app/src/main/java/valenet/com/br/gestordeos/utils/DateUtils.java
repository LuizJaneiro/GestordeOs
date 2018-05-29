package valenet.com.br.gestordeos.utils;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import valenet.com.br.gestordeos.application.GestorDeOsApplication;

public class DateUtils{
    private static final String TAG = "DateUtils";

    private static final SimpleDateFormat[] dateFormats;
    private static final int dateFormat_default = 7;

    private DateUtils() { }

    static
    {
        final String[] possibleDateFormats =
                {
                        "EEE, dd MMM yyyy HH:mm:ss z", // RFC_822
                        "EEE, dd MMM yyyy HH:mm zzzz",
                        "yyyy-MM-dd'T'HH:mm:ssZ",
                        "yyyy-MM-dd'T'HH:mm:ss.SSSzzzz", // Blogger Atom feed has millisecs also
                        "yyyy-MM-dd'T'HH:mm:sszzzz",
                        "yyyy-MM-dd'T'HH:mm:ss z",
                        "yyyy-MM-dd'T'HH:mm:ssz", // ISO_8601
                        "yyyy-MM-dd'T'HH:mm:ss",
                        "yyyy-MM-dd'T'HHmmss.SSSz",
                        "yyyy-MM-dd"
                };

        dateFormats = new SimpleDateFormat[possibleDateFormats.length];
        TimeZone gmtTZ = TimeZone.getTimeZone("GMT");

        for (int i = 0; i < possibleDateFormats.length; i++)
        {
            dateFormats[i] = new SimpleDateFormat(possibleDateFormats[i],
                    GestorDeOsApplication.myLocale);

            dateFormats[i].setTimeZone(gmtTZ);
        }
    }

    /**
     * Parse a date string.  The format of RSS/Atom dates come in many
     * different forms, so this method is extremely flexible and attempts to
     * understand many different formats.
     *
     * Copied verbatim from Informa 0.7.0-alpha2, ParserUtils.java.
     *
     * @param strdate
     *   Date string to attempt to parse.
     *
     * @return
     *   If successful, returns a {@link Date} object representing the parsed
     *   date; otherwise, null.
     */
    public static Date parseDate(String strdate)
    {
        Date result = null;
        strdate = strdate.trim();
        if (strdate.length() > 10) {

            if ((strdate.substring(strdate.length() - 5).indexOf("+") == 0 || strdate
                    .substring(strdate.length() - 5).indexOf("-") == 0)
                    && strdate.substring(strdate.length() - 5).indexOf(":") == 2) {

                String sign = strdate.substring(strdate.length() - 5,
                        strdate.length() - 4);

                strdate = strdate.substring(0, strdate.length() - 5) + sign + "0"
                        + strdate.substring(strdate.length() - 4);
                // logger.debug("CASE1 : new date " + strdate + " ? "
                //    + strdate.substring(0, strdate.length() - 5));

            }

            String dateEnd = strdate.substring(strdate.length() - 6);

            // try to deal with -05:00 or +02:00 at end of date
            // replace with -0500 or +0200
            if ((dateEnd.indexOf("-") == 0 || dateEnd.indexOf("+") == 0)
                    && dateEnd.indexOf(":") == 3) {
                // nda
                if ("GMT".equals(strdate.substring(strdate.length() - 9, strdate
                        .length() - 6))) {
                    Log.d(TAG, "General time zone with offset, no change");
                } else {
                    // continue treatment
                    String oldDate = strdate;
                    String newEnd = dateEnd.substring(0, 3) + dateEnd.substring(4);
                    strdate = oldDate.substring(0, oldDate.length() - 6) + newEnd;
                    // logger.debug("!!modifying string ->"+strdate);
                }
            }
        }
        int i = 0;
        while (i < dateFormats.length) {
            try {
                result = dateFormats[i].parse(strdate);
                // logger.debug("******Parsing Success "+strdate+"->"+result+" with
                // "+dateFormats[i].toPattern());
                break;
            } catch (java.text.ParseException eA) {
                i++;
            }
        }

        return result;
    }

    /**
     * Format a date in a manner that would be most suitable for serialized
     * storage.
     *
     * @param date
     *   {@link Date} object to format.
     *
     * @return
     *   Robust, formatted date string.
     */
    public static String formatDate(Date date)
    {
        return dateFormats[dateFormat_default].format(date);
    }

    public static boolean areDatesSameDay(Date date1, Date date2){
        if(date1 == null || date2 == null)
            return false;

        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        boolean sameYear = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
        boolean sameMonth = cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
        boolean sameDay = cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);

        if(sameDay && sameMonth && sameYear)
            return true;
        else
            return false;
    }

    public static boolean isDateTomorrow(Date currentDate, Date nextDate){
        if(currentDate == null || nextDate == null)
            return false;

        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(currentDate);
        cal2.setTime(nextDate);
        boolean sameYear = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
        boolean sameMonth = cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
        boolean sameDay = cal1.get(Calendar.DAY_OF_YEAR) == (cal2.get(Calendar.DAY_OF_YEAR) - 1);

        if(sameDay && sameMonth && sameYear)
            return true;
        else
            return false;
    }

    public static boolean isDateNextDays(Date currentDate, Date nextDate){
        if(currentDate == null || nextDate == null)
            return false;

        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(currentDate);
        cal2.setTime(nextDate);
        boolean sameDay = (cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH)) || (cal1.get(Calendar.DAY_OF_YEAR) == (cal2.get(Calendar.DAY_OF_YEAR) - 1)
                           || (cal1.get(Calendar.DAY_OF_YEAR) > cal2.get(Calendar.DAY_OF_YEAR)));

        if(!sameDay)
            return true;
        else
            return false;
    }

    public static boolean isDateNextDaysWithTomorrow(Date currentDate, Date nextDate){
        if(currentDate == null || nextDate == null)
            return false;

        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(currentDate);
        cal2.setTime(nextDate);
        boolean sameDay = (cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH)) || (cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));

        if(!sameDay)
            return true;
        else
            return false;
    }
}
