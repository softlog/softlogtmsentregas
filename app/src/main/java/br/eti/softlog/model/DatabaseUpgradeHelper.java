package br.eti.softlog.model;

import android.content.Context;
import android.util.Log;

import org.greenrobot.greendao.database.Database;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Paulo Sérgio Alves on 2018/04/10.
 */

public class DatabaseUpgradeHelper extends DaoMaster.OpenHelper {

    public DatabaseUpgradeHelper(Context context, String name) {
        super(context, name);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {

        List<Migration> migrations = getMigrations();

        // Only run migrations past the old version
        for (Migration migration : migrations) {
            if (oldVersion < migration.getVersion()) {
                migration.runMigration(db);
            }
        }
    }

    private List<Migration> getMigrations() {
        List<Migration> migrations = new ArrayList<>();
        migrations.add(new MigrationV7());
        migrations.add(new MigrationV8());
        migrations.add(new MigrationV10());
        migrations.add(new MigrationV11());
        migrations.add(new MigrationV13());
        migrations.add(new MigrationV14());
        migrations.add(new MigrationV17());
        migrations.add(new MigrationV18());
        migrations.add(new MigrationV20());
        migrations.add(new MigrationV25());
        migrations.add(new MigrationV26());
        migrations.add(new MigrationV27());
        migrations.add(new MigrationV28());
        migrations.add(new MigrationV29());
        migrations.add(new MigrationV31());

        // Sorting just to be safe, in case other people add migrations in the wrong order.
//        Comparator<Migration> migrationComparator = new Comparator<Migration>() {
//            @Override
//            public int compare(Migration m1, Migration m2) {
//                return m1.getVersion().compareTo(m2.getVersion());
//            }
//        };
//        Collections.sort(migrations, migrationComparator);

        return migrations;
    }

    private static class MigrationV7 implements Migration {

        @Override
        public Integer getVersion() {
            return 7;
        }

        @Override
        public void runMigration(Database db) {
            //Adding new table
              //Log.d("Versao BD","7");
              db.execSQL("ALTER TABLE " + DocumentoDao.TABLENAME + " ADD COLUMN " +
                      DocumentoDao.Properties.IdConhecimentoNotasFiscais.columnName + " INTEGER");
              db.execSQL("ALTER TABLE " + DocumentoDao.TABLENAME + " ADD COLUMN " +
                      DocumentoDao.Properties.IdConhecimento.columnName + " INTEGER");

            //UsuarioDao.createTable(db, false);
        }
    }

    private static class MigrationV8 implements Migration {

        @Override
        public Integer getVersion() {
            return 8;
        }

        @Override
        public void runMigration(Database db) {
            //Adding new table

            db.execSQL("CREATE TABLE locations (day integer, hour integer, milliseconds integer, " +
                    "latitude real, longitude real, altitude real, gpsStart integer, accuracy real, " +
                    "bearing real, speed real, provider text, best integer);");

//            db.execSQL("CREATE TABLE tarefas_executadas(_id integer, tipo_tarefa integer, " +
//                    "data_hora_check_in text, latitude_check_in text, longitude_check_in text, " +
//                    "data_hora_check_out text, latitude_check_out text, longitude_check_out text);");

            db.execSQL("CREATE INDEX day_hour on locations (day, hour);");
            db.execSQL("ALTER TABLE documentos ADD COLUMN distance real");
            db.execSQL("ALTER TABLE documentos ADD COLUMN tempo_estimado real");
            db.execSQL("ALTER TABLE ocorrencias_documento ADD COLUMN tarefa_executada_id integer");

            TarefasExecutadasDao.createTable(db, false);
//
//
        // db.execSQL("ALTER TABLE " + ProtocoloRemetenteDao.TABLENAME + " ADD COLUMN " + ProtocoloRemetenteDao.Properties.Observacao.columnName + " TEXT");
            //UsuarioDao.createTable(db, false);
        }
    }

    private static class MigrationV10 implements Migration {

        @Override
        public Integer getVersion() {
            return 10;
        }

        @Override
        public void runMigration(Database db) {
            //Adding new table

            TrackingGpsDao.createTable(db, false);
//
//
            // db.execSQL("ALTER TABLE " + ProtocoloRemetenteDao.TABLENAME + " ADD COLUMN " + ProtocoloRemetenteDao.Properties.Observacao.columnName + " TEXT");
            //UsuarioDao.createTable(db, false);
        }
    }

    private static class MigrationV11 implements Migration {

        @Override
        public Integer getVersion() {
            return 11;
        }

        @Override
        public void runMigration(Database db) {
            //Adding new table


//
//
            db.execSQL("ALTER TABLE " + OcorrenciaDao.TABLENAME + " ADD COLUMN " + OcorrenciaDao.Properties.Ativo.columnName + " INTEGER");

            //UsuarioDao.createTable(db, false);
        }
    }

    private static class MigrationV13 implements Migration {

        @Override
        public Integer getVersion() {
            return 13;
        }

        @Override
        public void runMigration(Database db) {
            //Adding new table
            db.execSQL("ALTER TABLE " + UsuarioDao.TABLENAME + " ADD COLUMN " +  UsuarioDao.Properties.LastLogin.columnName + " TEXT");
            db.execSQL("ALTER TABLE " + UsuarioDao.TABLENAME + " ADD COLUMN " +  UsuarioDao.Properties.Status.columnName + " INTEGER");
            db.execSQL("ALTER TABLE " + UsuarioDao.TABLENAME + " ADD COLUMN " +  UsuarioDao.Properties.Sincronizar.columnName + " INTEGER");
            db.execSQL("ALTER TABLE " + UsuarioDao.TABLENAME + " ADD COLUMN " +  UsuarioDao.Properties.Uuid.columnName + " INTEGER");
            //UsuarioDao.createTable(db, false);
        }
    }

    private static class MigrationV14 implements Migration {

        @Override
        public Integer getVersion() {
            return 14;
        }

        @Override
        public void runMigration(Database db) {
            //Adding new table
            db.execSQL("ALTER TABLE " + OcorrenciaDao.TABLENAME + " ADD COLUMN " +  OcorrenciaDao.Properties.ExigeRecebedor.columnName + " INTEGER");
            db.execSQL("ALTER TABLE " + OcorrenciaDao.TABLENAME + " ADD COLUMN " +  OcorrenciaDao.Properties.ExigeDocumento.columnName + " INTEGER");
            db.execSQL("ALTER TABLE " + OcorrenciaDao.TABLENAME + " ADD COLUMN " +  OcorrenciaDao.Properties.ExigeImagem.columnName + " INTEGER");
            //UsuarioDao.createTable(db, false);
        }
    }


    private static class MigrationV16 implements Migration {

        @Override
        public Integer getVersion() {
            return 16;
        }

        @Override
        public void runMigration(Database db) {
            //Adding new table

            //UsuarioDao.createTable(db, false);
        }
    }

    private static class MigrationV17 implements Migration {

        @Override
        public Integer getVersion() {
            return 17;
        }

        @Override
        public void runMigration(Database db) {
            //Adding new table
            db.execSQL("ALTER TABLE " + DocumentoDao.TABLENAME + " ADD COLUMN " +  DocumentoDao.Properties.Cep.columnName + " TEXT");
            //UsuarioDao.createTable(db, false);
        }
    }

    private static class MigrationV18 implements Migration {

        @Override
        public Integer getVersion() {
            return 18;
        }

        @Override
        public void runMigration(Database db) {
            //Adding new table
            db.execSQL("ALTER TABLE " + OcorrenciaDocumentoDao.TABLENAME + " ADD COLUMN " +  OcorrenciaDocumentoDao.Properties.IdServidor.columnName + " INTEGER");
            //UsuarioDao.createTable(db, false);
        }
    }

    private static class MigrationV20 implements Migration {

        @Override
        public Integer getVersion() {
            return 20;
        }

        @Override
        public void runMigration(Database db) {
            //Adding new table
            db.execSQL("ALTER TABLE " + DocumentoDao.TABLENAME + " ADD COLUMN " +  DocumentoDao.Properties.Selected.columnName + " INTEGER");
            //UsuarioDao.createTable(db, false);
        }
    }

    private static class MigrationV25 implements Migration {

        @Override
        public Integer getVersion() {
            return 25;
        }

        @Override
        public void runMigration(Database db) {
            //Adding new table
            try{
                db.execSQL("ALTER TABLE " + DocumentoDao.TABLENAME + " ADD COLUMN " +  DocumentoDao.Properties.EntregaId.columnName + " INTEGER");
            } catch (Exception e){

            }

            try{
                db.execSQL("CREATE TABLE entregas (" +
                        EntregasDao.Properties.Id.columnName + " INTEGER, " +
                        EntregasDao.Properties.DestinatarioId.columnName + " TEXT, " +
                        EntregasDao.Properties.DataExpedicao.columnName + " TEXT, " +
                        EntregasDao.Properties.Status.columnName + " TEXT, " +
                        EntregasDao.Properties.Latitude.columnName + " TEXT, " +
                        EntregasDao.Properties.Longitude.columnName + " TEXT, " +
                        EntregasDao.Properties.OrdemEntrega.columnName + " TEXT, " +
                        EntregasDao.Properties.TemPendencia + " INTEGER" +
                        ")");
            } catch (Exception e){

            }
            //UsuarioDao.createTable(db, false);
        }
    }

    private static class MigrationV26 implements Migration {

        @Override
        public Integer getVersion() {
            return 26;
        }

        @Override
        public void runMigration(Database db) {
            //Adding new table
            try{
                db.execSQL("ALTER TABLE " + EntregasDao.TABLENAME + " ADD COLUMN " +  EntregasDao.Properties.IdOcorrenciaPadrao.columnName + " INTEGER");
            } catch (Exception e){

            }

        }
    }

    private static class MigrationV27 implements Migration {

        @Override
        public Integer getVersion() {
            return 27;
        }

        @Override
        public void runMigration(Database db) {
            //Adding new table
            try{
                db.execSQL("ALTER TABLE " + EntregasDao.TABLENAME + " ADD COLUMN " +  EntregasDao.Properties.DataOcorrencia.columnName + " TEXT");
                db.execSQL("ALTER TABLE " + EntregasDao.TABLENAME + " ADD COLUMN " +  EntregasDao.Properties.HoraOcorrencia.columnName + " TEXT");
                db.execSQL("ALTER TABLE " + EntregasDao.TABLENAME + " ADD COLUMN " +  EntregasDao.Properties.NomeRecebedor.columnName + " TEXT");
                db.execSQL("ALTER TABLE " + EntregasDao.TABLENAME + " ADD COLUMN " +  EntregasDao.Properties.DocumentoRecebedor.columnName + " TEXT");
                db.execSQL("ALTER TABLE " + EntregasDao.TABLENAME + " ADD COLUMN " +  EntregasDao.Properties.Observacoes.columnName + " TEXT");
            } catch (Exception e){

            }

        }
    }

    private static class MigrationV28 implements Migration {

        @Override
        public Integer getVersion() {
            return 28;
        }

        @Override
        public void runMigration(Database db) {
            //Adding new table
            try{
                db.execSQL("ALTER TABLE " + DocumentoDao.TABLENAME + " ADD COLUMN " +  DocumentoDao.Properties.TemCanhoto.columnName + " INTEGER");
            } catch (Exception e){

            }

        }
    }

    private static class MigrationV29 implements Migration {

        @Override
        public Integer getVersion() {
            return 29;
        }

        @Override
        public void runMigration(Database db) {
            //Adding new table
            try{
                db.execSQL("ALTER TABLE " + OcorrenciaDocumentoDao.TABLENAME + " ADD COLUMN " +  OcorrenciaDocumentoDao.Properties.Finalizado.columnName + " INTEGER");
                db.execSQL("ALTER TABLE " + OcorrenciaDocumentoDao.TABLENAME + " ADD COLUMN " +  OcorrenciaDocumentoDao.Properties.EntregaId.columnName + " INTEGER");
            } catch (Exception e){

            }

        }
    }

    private static class MigrationV31 implements Migration {

        @Override
        public Integer getVersion() {
            return 31;
        }

        @Override
        public void runMigration(Database db) {
            //Adding new table
            try{
                db.execSQL("ALTER TABLE " + OcorrenciaDocumentoDao.TABLENAME + " ADD COLUMN " +  OcorrenciaDocumentoDao.Properties.TemCanhoto.columnName + " INTEGER");
            } catch (Exception e){

            }

        }
    }

    private interface Migration {
        Integer getVersion();
        void runMigration(Database db);
    }
}