package br.eti.softlog.utils;

import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;

import com.pixplicity.easyprefs.library.Prefs;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.blankj.utilcode.util.ActivityUtils.startActivity;

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

    public static void appendLog(String text, String pathFile)
    {

        if (text == null)
            return ;
        if (!Prefs.getBoolean("modo_debug",false)){
            return ;
        }
        File logFile;
        try{
            logFile = new File(pathFile);
        } catch (Error e){
            return ;
        }

        if (!logFile.exists())
        {
            try
            {
                logFile.createNewFile();
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try
        {
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(getDateTimeFormatYMD(new Date()) + "\t" + text);
            buf.newLine();
            buf.close();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public static void appendLog(String tag, String text, String pathFile)
    {

        if (text == null)
            return ;
        if (!Prefs.getBoolean("modo_debug",false)){
            return ;
        }
        File logFile;
        try{
            logFile = new File(pathFile);
        } catch (Error e){
            return ;
        }


        if (!logFile.exists())
        {
            try
            {
                logFile.createNewFile();
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try
        {
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(getDateTimeFormatYMD(new Date()) + "\t" + tag + "\t" + text);
            buf.newLine();
            buf.close();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void sendLogMail(){

        // save logcat in file
        File outputFile = new File("/sdcard/sconfirmei/log_sconfirmei.txt");
        /*
        try {
            Runtime.getRuntime().exec(
                    "logcat -f " + outputFile.getAbsolutePath());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        */

        //send file using email
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        // Set type to "email"
        emailIntent.setType("vnd.android.cursor.dir/email");
        String to[] = {"paulo.sergio.softlog@gmail.com"};
        emailIntent.putExtra(Intent.EXTRA_EMAIL, to);

        // the attachment
        emailIntent.putExtra(Intent.EXTRA_STREAM, outputFile.getAbsolutePath());

        // the mail subject
        emailIntent .putExtra(Intent.EXTRA_SUBJECT, "Log do Aplicativo de Entregas SConfirmei");
        startActivity(Intent.createChooser(emailIntent , "Envio de Email..."));

    }


}

