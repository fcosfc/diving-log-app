package es.upo.alu.fsaufer.dm.divinglogapp.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utilidad para fechas
 */
public class DateParser {

    private static DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);

    public static  Date getDate(String dateString) {
        Date date = null;

        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

}
