package com.a4visionmedia.personallibrary;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class InputBookActivity extends AppCompatActivity {

    private EditText mJudul, mPengarang, mPenerbit, mKategori, mNoISBN;
    private Button mSubmit, mSelectImage;
    private String mConvertedImageString, judul, pengarang, penerbit, kategori, noISBN;
    private String ImagePath = "image_path";
    private String ImageName = "image_name";
    private boolean check = true;
    private Bitmap FixBitmap;
    private ImageView mShowSelectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        initialize();

        mSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();

                intent.setType("image/*");

                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent, "Select Image From Gallery"), 1);
            }
        });

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mConvertedImageString = getStringImage(FixBitmap);
                getText();
            }
        });

    }

    @Override
    protected void onActivityResult(int RC, int RQC, Intent I) {

        super.onActivityResult(RC, RQC, I);

        if (RC == 1 && RQC == RESULT_OK && I != null && I.getData() != null) {

            Uri uri = I.getData();

            try {

                FixBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                mShowSelectedImage.setImageBitmap(FixBitmap);

            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }

    private String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    public void getText(){
        judul = mJudul.getText().toString();
        pengarang = mPengarang.getText().toString();
        penerbit = mPenerbit.getText().toString();
        kategori = mKategori.getText().toString();
        noISBN = mNoISBN.getText().toString();

        PostData sendData = new PostData();
        sendData.execute(judul,pengarang,penerbit, kategori, noISBN);
        finish();
    }

    public void initialize(){
        mJudul = findViewById(R.id.judul_buku);
        mPengarang = findViewById(R.id.pengarang_buku);
        mPenerbit = findViewById(R.id.penerbit_buku);
        mKategori = findViewById(R.id.kategori_buku);
        mNoISBN = findViewById(R.id.isbn_buku);

        mSubmit = findViewById(R.id.submit_button);
        mSelectImage = findViewById(R.id.select_cover);
        mShowSelectedImage = findViewById(R.id.preview_image);
    }

    class PostData extends AsyncTask<String, Void, String> {

        String add_url;

        @Override
        protected void onPreExecute() {

            add_url = "http://dev.beta.4visionmedia.com/add_buku_android.php";
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... args) {
            Log.d("StartData", "Starting");
            String judul, pengarang, penerbit, kategori, no_isbn;
            judul = args[0];
            pengarang = args[1];
            penerbit = args[2];
            kategori = args[3];
            no_isbn = args[4];

            ImageProcessClass imageProcessClass = new ImageProcessClass();

            HashMap<String,String> HashMapParams = new HashMap<String,String>();

            HashMapParams.put("noisbn", no_isbn);
            HashMapParams.put("judul", judul);
            HashMapParams.put("pengarang", pengarang);
            HashMapParams.put("penerbit", penerbit);
            HashMapParams.put("kategori", kategori);
            HashMapParams.put(ImageName, judul);
            HashMapParams.put(ImagePath, mConvertedImageString);
            Log.d("MidData", no_isbn + " " + judul + " " + pengarang + " " + penerbit + " " +  kategori);
            Log.d("MiddData", mConvertedImageString);

            String FinalData = imageProcessClass.ImageHttpRequest(add_url, HashMapParams);
            Log.d("FinalData", "data final : " + FinalData);
            return FinalData;

        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(), "AsnycTask Done", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public class ImageProcessClass{

        public String ImageHttpRequest(String requestURL,HashMap<String, String> PData) {

            StringBuilder stringBuilder = new StringBuilder();

            try {

                URL url;
                HttpURLConnection httpURLConnectionObject ;
                OutputStream OutPutStream;
                BufferedWriter bufferedWriterObject ;
                BufferedReader bufferedReaderObject ;
                int RC ;

                url = new URL(requestURL);

                httpURLConnectionObject = (HttpURLConnection) url.openConnection();

                httpURLConnectionObject.setReadTimeout(19000);

                httpURLConnectionObject.setConnectTimeout(19000);

                httpURLConnectionObject.setRequestMethod("POST");

                httpURLConnectionObject.setDoInput(true);

                httpURLConnectionObject.setDoOutput(true);

                OutPutStream = httpURLConnectionObject.getOutputStream();

                bufferedWriterObject = new BufferedWriter(

                        new OutputStreamWriter(OutPutStream, "UTF-8"));

                bufferedWriterObject.write(bufferedWriterDataFN(PData));

                bufferedWriterObject.flush();

                bufferedWriterObject.close();

                OutPutStream.close();

                RC = httpURLConnectionObject.getResponseCode();

                if (RC == HttpsURLConnection.HTTP_OK) {

                    bufferedReaderObject = new BufferedReader(new InputStreamReader(httpURLConnectionObject.getInputStream()));

                    stringBuilder = new StringBuilder();

                    String RC2;

                    while ((RC2 = bufferedReaderObject.readLine()) != null){

                        stringBuilder.append(RC2);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return stringBuilder.toString();
        }

        private String bufferedWriterDataFN(HashMap<String, String> HashMapParams) throws UnsupportedEncodingException {

            StringBuilder stringBuilderObject;

            stringBuilderObject = new StringBuilder();

            for (Map.Entry<String, String> KEY : HashMapParams.entrySet()) {

                if (check)
                    check = false;
                else
                    stringBuilderObject.append("&");

                stringBuilderObject.append(URLEncoder.encode(KEY.getKey(), "UTF-8"));

                stringBuilderObject.append("=");

                stringBuilderObject.append(URLEncoder.encode(KEY.getValue(), "UTF-8"));
            }

            return stringBuilderObject.toString();
        }

    }
}
