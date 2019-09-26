package br.eti.softlog.utils;

import android.os.Build;
import android.text.TextUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrador on 2018/03/03.
 */

public final class Util {

    public static Date getDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }


    public static String getDateFormat(Date data) {
        DateFormat formatador = DateFormat.getDateInstance(DateFormat.FULL, new Locale("pt", "BR"));
        String dataExtenso = formatador.format(data);
        return dataExtenso;
    }

    public static String getDateFormatDMY(String data) {


        Date dateData;
        String dataFormatada;

        try {
            SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
            dateData = formato.parse(data);
            formato.applyPattern("dd/MM/yyyy");
            dataFormatada = formato.format(dateData);
        } catch (ParseException e) {
            dataFormatada = data;
        }

        return dataFormatada;
    }

    public static  String getDateFormatYMD(Date data) {

        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formato.applyPattern("yyyy-MM-dd");

        String dataFormatada = formato.format(data);
        return dataFormatada;
    }

    public static  String getDateTimeFormatYMD(Date data) {

        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formato.applyPattern("yyyy-MM-dd HH:mm:ss");

        String dataFormatada = formato.format(data);
        return dataFormatada;
    }

    /** Returns the consumer friendly device name */
    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;

        StringBuilder phrase = new StringBuilder();
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase.append(Character.toUpperCase(c));
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase.append(c);
        }

        return phrase.toString();
    }

}

