package com.a4visionmedia.personallibrary;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.List;

public class BookDetailActivity extends AppCompatActivity {
    private String mPengarang;
    private String mPenerbit;
    private String mKategori;
    private String mISBN;
    private String mCover;
    private String mJudul;
    private String mUsername;
    private LinearLayout mEditButton, mDeleteButton;
    private TextView mTitleBook, mAuthorBook, mPubBook, mCatBook, mNoISBN;
    private ImageView mCoverBook;
    private Intent getIntentData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        initialize();

        getIntentData = getIntent();

        mJudul = getIntentData.getStringExtra("judul");
        mPengarang = getIntentData.getStringExtra("pengarang");
        mPenerbit = getIntentData.getStringExtra("penerbit");
        mKategori = getIntentData.getStringExtra("kategori");
        mISBN = getIntentData.getStringExtra("isbn");
        mCover = getIntentData.getStringExtra("cover");
        mUsername = getIntentData.getStringExtra("username");

        mTitleBook.setText(mJudul);
        mAuthorBook.setText(mPengarang);
        mPubBook.setText(mPenerbit);
        mCatBook.setText(mKategori);
        mNoISBN.setText(mISBN);
        Picasso.with(getApplicationContext()).load(mCover).into(mCoverBook);

        mEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BookDetailActivity.this, InputBookActivity.class);
                intent.putExtra("FLAG", "EDIT");
                intent.putExtra("judul", mJudul);
                intent.putExtra("pengarang", mPengarang);
                intent.putExtra("penerbit", mPenerbit);
                intent.putExtra("kategori", mKategori);
                intent.putExtra("isbn", mISBN);
                intent.putExtra("cover", mCover);
                intent.putExtra("username", mUsername);
                startActivity(intent);
            }
        });

        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DeleteBookAsyncTask().execute();
            }
        });

    }

    public void initialize(){
        mTitleBook = findViewById(R.id.titleBook);
        mAuthorBook = findViewById(R.id.authorBook);
        mPubBook = findViewById(R.id.pubBook);
        mCatBook = findViewById(R.id.catBook);
        mNoISBN = findViewById(R.id.isbn);
        mCoverBook = findViewById(R.id.coverBook);
        mEditButton = findViewById(R.id.edit_buku);
        mDeleteButton = findViewById(R.id.hapus_buku);
    }

    class DeleteBookAsyncTask extends AsyncTask<URL,Void,String> {
        String BASE_URL="http://dev.beta.4visionmedia.com/delete_buku.php";
        ProgressDialog dialog = new ProgressDialog(BookDetailActivity.this);

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Menghapus Buku");
            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(URL... urls) {
            String serverResponse = "";
            URL url = null;
            try{
                url = createUrl(BASE_URL);
            }catch(MalformedURLException e){
                e.printStackTrace();
            }
            try{
                serverResponse = makeHttpRequest(url);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return serverResponse;
        }

        @Override
        protected void onPostExecute(String strings) {
            Log.d("webrespondd", strings);
            if(dialog.isShowing()){
                dialog.dismiss();
            }
            Toast.makeText(getApplicationContext(),strings, Toast.LENGTH_SHORT).show();
            finish();
        }

        private String makeHttpRequest (URL add_url) throws IOException{
            String jsonResponse = "";
            try{
                URL url = add_url;
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                String data = URLEncoder.encode("username", "UTF-8")
                        + "=" + URLEncoder.encode(mUsername, "UTF-8");

                data += "&" + URLEncoder.encode("noisbn", "UTF-8") + "="
                        + URLEncoder.encode(mISBN, "UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
                Log.d("resp", jsonResponse);
                inputStream.close();
                httpURLConnection.disconnect();
                return jsonResponse;


            }catch (MalformedURLException e){
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return jsonResponse;
        }

        private URL createUrl(String url) throws MalformedURLException {
            return new URL(url);
        }

        private String readFromStream(InputStream inputStream) throws IOException {
            StringBuilder output = new StringBuilder();
            if(inputStream != null){
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while(line != null){
                    output.append(line);
                    line = reader.readLine();
                }
            }
            return  output.toString();
        }

    }

}
