package br.eti.softlog.softlogtmsentregas;

import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Environment;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;

import android.util.Log;
import android.widget.Toast;

import com.blankj.utilcode.util.Utils;
import com.pixplicity.easyprefs.library.Prefs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.UUID;

import androidx.multidex.MultiDexApplication;
import br.eti.softlog.model.DaoMaster;
import br.eti.softlog.model.DaoSession;
import br.eti.softlog.model.DatabaseUpgradeHelper;
import br.eti.softlog.model.Usuario;
import br.eti.softlog.model.UsuarioDao;
import br.eti.softlog.utils.Util;


/**
 * Created by Paulo Sérgio Alves on 2018/03/13.
 */

public class EntregasApp extends MultiDexApplication {
    private static EntregasApp singleton;

    private DaoSession mDaoSession;
    //DaoMaster.DevOpenHelper helper;
    DatabaseUpgradeHelper helper;
    private Usuario usuario;
    private boolean status;
    private int CodigoAcesso;
    private Date data_corrente;
    private int tipoStatus;
    private String nameDb;
    private String fileLog;

    private static final boolean DEBUG = true;
    public static final String TAG = "EntregasApp";

    protected static SharedPreferences sharedPreferences;

    // Status and Preferences
    public static boolean trackingOn = false; // not running
    public static int prefInterval = 0;
    public static int prefTimeout = 0;
    public static double prefThreshold = 0.30;

    // Wake Locks
    protected static PowerManager mPowerManager;
    protected static PowerManager.WakeLock mWakeLock1;
    protected static PowerManager.WakeLock mWakeLock2;

    public static EntregasApp getInstance() {
        return singleton;
    }

    @Override
    public void onCreate() {
        super.onCreate();




        Utils.init(this);


        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();

        singleton = this;
        tipoStatus = 1;
        data_corrente = new Date();


        //Recupera as configuracoes em SharedPreferences
        String db = this.getConfigDb();
        String login = this.getConfigLogin();
        boolean status = this.getConfigStatus();

        //this.setBD("tms_entregas.db");
        if (status){
            this.setBD(db);
            Usuario usuario = this.mDaoSession.getUsuarioDao().queryBuilder()
                    .where(UsuarioDao.Properties.Cpf.eq(login)).unique();


            if (!(usuario == null)){
                this.setUsuario(usuario);
            }
        } else {
            //Log.d("Status", "Nenhum Usuario esta logado");
            this.setStatus(false);
        }


        mPowerManager = (PowerManager)getApplicationContext().getSystemService(Context.POWER_SERVICE);
        // Save a handle to our SharedPreferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Reset tracking state when application/activity is created
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("pref_onoff", false);
        editor.commit();



        //initLogManager();// init LogManager before you use it to print log
        //LogUtil.writeLogs(DEBUG);

        //mDaoSession = new DaoMaster(new DaoMaster.DevOpenHelper(this, "conference.db").getWritableDb()).newSession();
    }

    public String getNameDb() {
        return nameDb;
    }

    public int getTipoStatus(){
        return tipoStatus;
    }

    public void setTipoStatus(int tipoStatus){
        this.tipoStatus = tipoStatus;
    }
    public Date getDate(){
        return data_corrente;
    }

    public void setDate(Date p_data_corrente){
        data_corrente = p_data_corrente;
    }

    public void setBD(String nome_bd){
        //helper = new DaoMaster.DevOpenHelper(this,nome_bd,null);
        helper = new DatabaseUpgradeHelper(getApplicationContext(),nome_bd);
        SQLiteDatabase db = helper.getWritableDatabase();

        //db.execSQL("DROP TABLE tracking_gps");
//        db.execSQL("CREATE TABLE tracking_gps(_id INTEGER, placa_veiculo TEXT, motorista_cpf TEXT, " +
//                "data_localizacao TEXT, latitude REAL, longitude REAL, sincronizado INTEGER)");

//        db.execSQL("DROP TABLE tarefas_executadas;");
//        db.execSQL("CREATE TABLE tarefas_executadas(_id integer, data_tarefa text, tipo_tarefa integer, " +
//                "data_hora_inicio text, latitude_inicio text, longitude_inicio text, " +
//                   "data_hora_check_in text, latitude_check_in text, longitude_check_in text, " +
//                   "data_hora_check_out text, latitude_check_out text, longitude_check_out text, " +
//                "endereco text, numero text, bairro text, id_cidade integer, cep text, " +
//                "telefone text, whatsapp text, id_regiao integer, distance real, duracao real);");
//
//        db.execSQL("ALTER TABLE ocorrencias_documento ADD COLUMN tarefa_executada_id integer");

        DaoMaster daoMaster = new DaoMaster(db);
        mDaoSession = daoMaster.newSession();
        nameDb = nome_bd;

        String dirMain = getApplicationContext().getFilesDir().toString();

        File folder = new File(dirMain);
        if (!folder.exists()) {
            folder.mkdir();
        }
        folder = null;

        fileLog = dirMain + "/" + nameDb.replace(".db","") + "_log_sconfirmei.txt";
        File logFile = new File(fileLog);

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

        logFile = null;
    }

    public String getFileLog(){
        return fileLog;
    }

    public static boolean doesDatabaseExist(Context context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }

    public String getPathDocs(){
        PackageManager m = getPackageManager();
        String s = getPackageName();
        PackageInfo p = null;
        try {
            p = m.getPackageInfo(s, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return p.applicationInfo.dataDir;
    }


    public String getUUID(){
        return UUID.randomUUID().toString();
    }


    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    public void setUsuario(Usuario p_usuario) {
        this.usuario = p_usuario;
        this.status = true;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void logout() {
        SharedPreferences pref;
        pref = getSharedPreferences("info", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
        this.status = false;
        this.usuario = null;

        Intent s1 = new Intent(getApplicationContext(),ServiceMain.class);
        stopService(s1);

        Intent s2 = new Intent(getApplicationContext(),LocationService.class);
        stopService(s2);

    }


    public void setConfigDb(String nome_db) {
        //Create a object SharedPreferences from getSharedPreferences("name_file",MODE_PRIVATE) of Context
        SharedPreferences pref;
        pref = getSharedPreferences("info", MODE_PRIVATE);
        //Using putXXX - with XXX is type data you want to write like: putString, putInt...   from      Editor object
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("banco_dados",nome_db);

        //finally, when you are done saving the values, call the commit() method.
        editor.commit();
    }

    public String getConfigDb() {
        //get SharedPreferences from getSharedPreferences("name_file", MODE_PRIVATE)
        SharedPreferences shared = getSharedPreferences("info",MODE_PRIVATE);
        //Using getXXX- with XX is type date you wrote to file "name_file"
        String db = shared.getString("banco_dados","");
        //Log.d("Banco Dados",db);
        return db;
    }

    public void setConfigLogin(String login) {
        //Create a object SharedPreferences from getSharedPreferences("name_file",MODE_PRIVATE) of Context
        SharedPreferences pref;
        pref = getSharedPreferences("info", MODE_PRIVATE);
        //Using putXXX - with XXX is type data you want to write like: putString, putInt...   from      Editor object
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("login",login);

        //finally, when you are done saving the values, call the commit() method.
        editor.commit();
    }

    public String getConfigLogin() {
        //get SharedPreferences from getSharedPreferences("name_file", MODE_PRIVATE)
        SharedPreferences shared = getSharedPreferences("info",MODE_PRIVATE);
        //Using getXXX- with XX is type date you wrote to file "name_file"
        String login = shared.getString("login","");
        //Log.d("Login",login);
        return login;
    }

    public void setConfigStatus(boolean status) {
        //Create a object SharedPreferences from getSharedPreferences("name_file",MODE_PRIVATE) of Context
        SharedPreferences pref;
        pref = getSharedPreferences("info", MODE_PRIVATE);
        //Using putXXX - with XXX is type data you want to write like: putString, putInt...   from      Editor object
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("status",status);

        //finally, when you are done saving the values, call the commit() method.
        editor.commit();
    }

    public boolean getConfigStatus() {
        //get SharedPreferences from getSharedPreferences("name_file", MODE_PRIVATE)
        SharedPreferences shared = getSharedPreferences("info",MODE_PRIVATE);
        //Using getXXX- with XX is type date you wrote to file "name_file"
        boolean status = shared.getBoolean("status",false);
        return status;
    }


    public boolean getConfigUploadImageMobile() {
        //get SharedPreferences from getSharedPreferences("name_file", MODE_PRIVATE)
        SharedPreferences shared = getSharedPreferences("br.eti.softlog.softlogtmsentregas_preferences",MODE_PRIVATE);
        //Using getXXX- with XX is type date you wrote to file "name_file"
        boolean r = shared.getBoolean("upload_img_mobile",false);
        return r;
    }

    public boolean getConfigUploadOcorrenciaMobile() {
        //get SharedPreferences from getSharedPreferences("name_file", MODE_PRIVATE)
        SharedPreferences shared = getSharedPreferences("br.eti.softlog.softlogtmsentregas_preferences",MODE_PRIVATE);
        //Using getXXX- with XX is type date you wrote to file "name_file"
        boolean r = shared.getBoolean("upload_oco_mobile",true);
        return r;
    }

    public boolean getConfigDownloadMobile() {
        //get SharedPreferences from getSharedPreferences("name_file", MODE_PRIVATE)
        SharedPreferences shared = getSharedPreferences("br.eti.softlog.softlogtmsentregas_preferences",MODE_PRIVATE);
        //Using getXXX- with XX is type date you wrote to file "name_file"
        boolean r = shared.getBoolean("download_mobile",true);
        return r;
    }

    public int getConfigIntervalSync() {
        //get SharedPreferences from getSharedPreferences("name_file", MODE_PRIVATE)
        SharedPreferences shared = getSharedPreferences("br.eti.softlog.softlogtmsentregas_preferences",MODE_PRIVATE);
        //Using getXXX- with XX is type date you wrote to file "name_file"
        String r = shared.getString("interval_sync","5");

        return Integer.valueOf(r);
    }

    public int getConfigIntervalTracking() {
        //get SharedPreferences from getSharedPreferences("name_file", MODE_PRIVATE)
        SharedPreferences shared = getSharedPreferences("br.eti.softlog.softlogtmsentregas_preferences",MODE_PRIVATE);
        //Using getXXX- with XX is type date you wrote to file "name_file"
        String r = shared.getString("interval_tracking","2");

        return Integer.valueOf(r);
    }

    public boolean getFiltroOcorrencias() {
        //get SharedPreferences from getSharedPreferences("name_file", MODE_PRIVATE)
        SharedPreferences shared = getSharedPreferences("br.eti.softlog.softlogtmsentregas_preferences",MODE_PRIVATE);
        //Using getXXX- with XX is type date you wrote to file "name_file"
        Boolean r = shared.getBoolean("filtrar_ocorrencias",true);

        return r;
    }



    public int getConfigResolucao() {
        //get SharedPreferences from getSharedPreferences("name_file", MODE_PRIVATE)
        SharedPreferences shared = getSharedPreferences("br.eti.softlog.softlogtmsentregas_preferences",MODE_PRIVATE);
        //Using getXXX- with XX is type date you wrote to file "name_file"
        String r = shared.getString("img_width","1000");
        return Integer.valueOf(r);
    }

    public int getConfigCompressao() {
        //get SharedPreferences from getSharedPreferences("name_file", MODE_PRIVATE)
        SharedPreferences shared = getSharedPreferences("br.eti.softlog.softlogtmsentregas_preferences",MODE_PRIVATE);
        //Using getXXX- with XX is type date you wrote to file "name_file"
        String r = shared.getString("img_compress","75");
        return Integer.valueOf(r);
    }



    public boolean getConfigCanhotoObrigatorio() {
        //get SharedPreferences from getSharedPreferences("name_file", MODE_PRIVATE)
        SharedPreferences shared = getSharedPreferences("br.eti.softlog.softlogtmsentregas_preferences",MODE_PRIVATE);
        //Using getXXX- with XX is type date you wrote to file "name_file"
        boolean r = shared.getBoolean("scanner_obrigatorio",true);
        //Log.d("Canhoto obrigatorio",String.valueOf(r));
        return r;
    }

    public boolean getModoConsulta() {
        //get SharedPreferences from getSharedPreferences("name_file", MODE_PRIVATE)
        SharedPreferences shared = getSharedPreferences("br.eti.softlog.softlogtmsentregas_preferences",MODE_PRIVATE);
        //Using getXXX- with XX is type date you wrote to file "name_file"
        boolean modoConsulta = shared.getBoolean("modo_consulta",false);
        return modoConsulta;
    }

    public boolean getModoDaltonico() {
        //get SharedPreferences from getSharedPreferences("name_file", MODE_PRIVATE)
        SharedPreferences shared = getSharedPreferences("br.eti.softlog.softlogtmsentregas_preferences",MODE_PRIVATE);
        //Using getXXX- with XX is type date you wrote to file "name_file"
        boolean modoDaltonico = shared.getBoolean("modo_daltonico",false);
        return modoDaltonico;
    }

    public boolean backupBD(Context context, String nome_bd) {


        String dirBackup = Environment.getExternalStorageDirectory() + "/sconfirmei";

        //String dirBackup = dirMain + "/backup";
        File folderBackup = new File(dirBackup);
        if (!folderBackup.exists()) {
            folderBackup.mkdir();
        }


        Date date = new Date();
        String cDate = Util.getDateTimeFormatYMD(date);

        //Código de Backup
        try {
            // Caminho de Origem do Seu Banco de Dados
            InputStream in = new FileInputStream(
                    new File(String.valueOf(context.getDatabasePath(nome_bd))));

            // Caminho de Destino do Backup do Seu Banco de Dados
            OutputStream out = new FileOutputStream(new File(
                    dirBackup + "/" + nome_bd));

            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        } catch (FileNotFoundException e) {
            Log.d("Backup DB",e.getMessage());
            return false;
        } catch (IOException e) {
            Log.d("Backup DB",e.getMessage());
            return false;
        }
        return true;
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    public static void Debug(String message) {
        //Log.d("MainActivity", message);
    }


    public static SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

}