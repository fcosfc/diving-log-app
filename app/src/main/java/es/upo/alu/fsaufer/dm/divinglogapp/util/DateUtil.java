package es.upo.alu.fsaufer.dm.divinglogapp.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utilidad para fechas
 */
public class DateUtil {

    private static final DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);

    public static  Date parseDate(String dateString) {
        Date date = null;

        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    public static String formatDate(Date date) {
        return dateFormat.format(date);
    }

}
