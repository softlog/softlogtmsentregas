package br.eti.softlog.softlogtmsentregas;

import androidx.appcompat.app.AppCompatActivity;
import br.eti.softlog.model.Documento;
import br.eti.softlog.model.ImagemOcorrencia;
import br.eti.softlog.model.ImagemOcorrenciaDao;
import br.eti.softlog.model.OcorrenciaDocumento;
import br.eti.softlog.utils.Util;
import butterknife.BindView;
import butterknife.ButterKnife;
import me.echodev.resizer.Resizer;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ActivityUtils;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import static org.osmdroid.tileprovider.util.StreamUtils.copy;

public class RegistroCanhotoActivity extends AppCompatActivity {

    static Bitmap mImage;

    String path;
    String pathRoot;

    private Long idDocumento;
    private Long idOcorrencia;
    private String dataOcorrencia;
    private String horaOcorrencia;
    private String nomeRecebedor;
    private String docRecebedor;
    private String observacao;
    public Double latitude;
    public Double longitude;
    public Long idImagem;

    EntregasApp app;
    Manager manager;
    Util util;

    Intent inCall;

    @BindView(R.id.txt_nota_fiscal)
    TextView txtNotaFiscal;

    @BindView(R.id.txt_ocorrencia)
    TextView txtOcorrencia;

    @BindView(R.id.img_canhoto)
    ImageView imgOcorrencia;

    @BindView(R.id.btn_capturar)
    ImageButton btnCapturar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_canhoto);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Registro Imagem/Canhoto");

//        ActionBar actionBar = getActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);
        app = (EntregasApp)getApplicationContext();
        util = new Util();

        manager = new Manager(app);

        Intent inCall = getIntent();
        idOcorrencia = inCall.getLongExtra("id_ocorrencia", Long.valueOf(0));
        idDocumento = inCall.getLongExtra("id_documento", 0);
        dataOcorrencia = inCall.getStringExtra("data_ocorrencia");
        horaOcorrencia = inCall.getStringExtra("hora_ocorrencia");
        nomeRecebedor = inCall.getStringExtra("nome_recebedor");
        docRecebedor = inCall.getStringExtra("doc_recebedor");
        observacao = inCall.getStringExtra("observacao");
        latitude = inCall.getDoubleExtra("latitude",Double.valueOf(0.00));
        longitude = inCall.getDoubleExtra("longitude", Double.valueOf(0.00));
        idImagem = inCall.getLongExtra("id_imagem",-1);


        pathRoot = app      .getApplicationContext().getFilesDir().toString();

        btnCapturar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = CropImage.activity()
                        .setRequestedSize(800,0)
                        .getIntent(getApplicationContext());

                startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_result_crop, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        boolean substituir;

        if (idImagem == Long.valueOf(-1))
            substituir = false;
        else
            substituir = true;


        int id = item.getItemId();

        if (id == R.id.menu_finalizar && !(substituir)) {


            EntregasApp app = (EntregasApp)getApplicationContext();
            Manager manager = new Manager(app);

            Documento documento = manager.findDocumentoById(idDocumento);

            documento.setIdOcorrencia(idOcorrencia);
            documento.setDataOcorrencia(dataOcorrencia + " " + horaOcorrencia);
            app.getDaoSession().update(documento);

            OcorrenciaDocumento oco = manager.addOcorrenciaDocumento(documento.getId(),idOcorrencia,dataOcorrencia,
                    horaOcorrencia,null,docRecebedor,nomeRecebedor,observacao,
                    String.valueOf(latitude),String.valueOf(longitude));


            //Gravar o arquivo de imagem e registrar na tabela de imagens
            if (mImage != null) {



                ImagemOcorrencia imagemOcorrencia = new ImagemOcorrencia();
                imagemOcorrencia.setOcorrenciaDocumentoId(oco.getId());
                imagemOcorrencia.setSincronizado(false);
                app.getDaoSession().insert(imagemOcorrencia);


                String path = getApplicationContext().getFilesDir().toString();
                OutputStream fOut = null;
                Integer counter = 0;


                //Gerar nome do arquivo
                Util util = new Util();
                Date date = new Date();
                String cData = util.getDateTimeFormatYMD(date);

                String nameFile = app.getUsuario().getCpf().toString() + "_" +
                        app.getUsuario().getCodigoAcesso() + "_" +
                        documento.getChaveNfe().toString() + "_"
                        + String.valueOf(imagemOcorrencia.getId()) + ".jpg";


                // the File to save , append increasing numeric counter to prevent files from getting overwritten.
                File file = new File(path, nameFile);



                try {
                    fOut = new FileOutputStream(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }



                //Bitmap resized = Bitmap.createScaledBitmap(mImage,(int)(mImage.getWidth()*0.8), (int)(mImage.getHeight()*0.8), true);
                Bitmap pictureBitmap = mImage;
                pictureBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                //Saving the Bitmap to a file compressed as a JPEG with 85% compression rate

                pictureBitmap.recycle();

                //resized.recycle();
                //resized= null;


                imagemOcorrencia.setNomeArquivo(nameFile);
                app.getDaoSession().update(imagemOcorrencia);


                mImage.recycle();
                mImage = null;

                try {

                    fOut.flush(); // Not really required
                    fOut.close(); // do not forget to close the stream
                    // pictureBitmap.recycle();
                    //pictureBitmap = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }


//                try {
//                    MediaStore.Images.Media.insertImage(getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//
//                }

                File file2 = null;
                try {
                    file2 = file_from(Uri.fromFile(new File(file.getAbsolutePath())));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String nameFile2 = app.getUsuario().getCpf().toString() + "_" +
                        app.getUsuario().getCodigoAcesso() + "_" +
                        documento.getChaveNfe().toString() + "_"
                        + String.valueOf(imagemOcorrencia.getId());

                File file3;
//                try {
//                    fOut = new FileOutputStream(file);
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }

                try {
                    int width = app.getConfigResolucao();
                    int compress = app.getConfigCompressao();

                    File resizedImage = new Resizer(this)
                            .setTargetLength(width)
                            .setQuality(compress)
                            .setOutputFormat("JPEG")
                            .setOutputFilename(nameFile2)
                            .setOutputDirPath(path)
                            .setSourceImage(file2)
                            .getResizedFile();


//                    file3 = new Compressor(this)
//                            .setMaxWidth(640)
//                            .setMaxHeight(480)
//                            .setQuality(75)
//                            .setCompressFormat(Bitmap.CompressFormat.WEBP)
//                            .setDestinationDirectoryPath(path)
//                            .compressToFile(file2);

//                    try {
//                        MediaStore.Images.Media.insertImage(getContentResolver(), file3.getAbsolutePath(), file3.getName(), file3.getName());
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//
//                    }

                } catch (IOException e) {
                    //Log.d("erro",e.getMessage());
                    e.printStackTrace();
                }





            } else {

            }

            releaseBitmap();
            Toast.makeText(this,"Ocorrência de Entrega gravada com sucesso!",Toast.LENGTH_LONG).show();



            Intent mainIntent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(mainIntent);

            finish();


            //OcorrenciaDocumento oco = new OcorrenciaDocumento(idDocumento,)


        }

        if (id == R.id.menu_finalizar && substituir) {

            EntregasApp app = (EntregasApp)getApplicationContext();
            Manager manager = new Manager(app);


            //Gravar o arquivo de imagem e registrar na tabela de imagens
            if (mImage != null) {


                ImagemOcorrencia imagemOcorrencia = app.getDaoSession().getImagemOcorrenciaDao().queryBuilder()
                        .where(ImagemOcorrenciaDao.Properties.Id.eq(idImagem)).unique();




                String path = getApplicationContext().getFilesDir().toString();
                OutputStream fOut = null;
                Integer counter = 0;


                //Gerar nome do arquivo
                Util util = new Util();
                Date date = new Date();
                String cData = util.getDateTimeFormatYMD(date);

                String nameFile = imagemOcorrencia.getNomeArquivo();


                // the File to save , append increasing numeric counter to prevent files from getting overwritten.
                File file = new File(path, nameFile);

                if (file.exists())
                    file.delete();

                try {
                    fOut = new FileOutputStream(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }



                //Bitmap resized = Bitmap.createScaledBitmap(mImage,(int)(mImage.getWidth()*0.8), (int)(mImage.getHeight()*0.8), true);
                Bitmap pictureBitmap = mImage;
                pictureBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                //Saving the Bitmap to a file compressed as a JPEG with 85% compression rate

                pictureBitmap.recycle();

                //resized.recycle();
                //resized= null;


                imagemOcorrencia.setNomeArquivo(nameFile);
                imagemOcorrencia.setSincronizado(false);
                app.getDaoSession().update(imagemOcorrencia);


                mImage.recycle();
                mImage = null;

                try {

                    fOut.flush(); // Not really required
                    fOut.close(); // do not forget to close the stream
                    // pictureBitmap.recycle();
                    //pictureBitmap = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }



                File file2 = null;
                try {
                    file2 = file_from(Uri.fromFile(new File(file.getAbsolutePath())));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String nameFile2 = app.getUsuario().getCpf().toString() + "_" +
                        app.getUsuario().getCodigoAcesso() + "_" +
                        imagemOcorrencia.getOcorrenciaDocumento().getDocumento().getChaveNfe().toString() + "_"
                        + String.valueOf(imagemOcorrencia.getId());

                File file3;
//                try {
//                    fOut = new FileOutputStream(file);
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }

                try {
                    int width = app.getConfigResolucao();
                    int compress = app.getConfigCompressao();

                    File resizedImage = new Resizer(this)
                            .setTargetLength(width)
                            .setQuality(compress)
                            .setOutputFormat("JPEG")
                            .setOutputFilename(nameFile2)
                            .setOutputDirPath(path)
                            .setSourceImage(file2)
                            .getResizedFile();

                } catch (IOException e) {
                    //Log.d("erro",e.getMessage());
                    e.printStackTrace();
                }
            }

            releaseBitmap();



            if (substituir){
                Toast.makeText(this,"Imagem Substituida com sucesso!",Toast.LENGTH_LONG).show();
                Intent mainIntent = new Intent(getApplicationContext(),ImagensOcorrencias.class);
                mainIntent.putExtra("id_documento",idDocumento);
                startActivity(mainIntent);
            } else {
                Toast.makeText(this,"Ocorrência de Entrega gravada com sucesso!",Toast.LENGTH_LONG).show();
                Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                ActivityUtils.finishAllActivities();
                startActivity(mainIntent);
            }
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onBackPressed() {
        releaseBitmap();
        super.onBackPressed();
    }

    public void onImageViewClicked(View view) {
        releaseBitmap();
        finish();
    }

    private void releaseBitmap() {
        if (mImage != null) {
            mImage.recycle();
            mImage = null;
        }
    }

    private File file_from(Uri uri) throws IOException {
        InputStream inputStream = this.getApplicationContext().getContentResolver().openInputStream(uri);
        String fileName = getFileName(uri);
        String[] splitName = splitFileName(fileName);
        File tempFile = File.createTempFile(splitName[0], splitName[1]);

        tempFile = rename(tempFile, fileName);
        tempFile.deleteOnExit();
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(tempFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (inputStream != null) {
            copy(inputStream, out);
            inputStream.close();
        }

        if (out != null) {
            out.close();
        }
        return tempFile;
    }

    private String[] splitFileName(String fileName) {
        String name = fileName;
        String extension = "";
        int i = fileName.lastIndexOf(".");
        if (i != -1) {
            name = fileName.substring(0, i);
            extension = fileName.substring(i);
        }

        return new String[]{name, extension};
    }

    private File rename(File file, String newName) {
        File newFile = new File(file.getParent(), newName);
        if (!newFile.equals(file)) {
            if (newFile.exists() && newFile.delete()) {
                // Log.d("FileUtil", "Delete old " + newName + " file");
            }
            if (file.renameTo(newFile)) {
                // Log.d("FileUtil", "Rename file to " + newName);
            }
        }
        return newFile;
    }

    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = this.getApplicationContext().getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf(File.separator);
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();

                /*


                Picasso.get().load(new File(path)).networkPolicy(NetworkPolicy.NO_CACHE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .into(img);
                 */

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }


}
