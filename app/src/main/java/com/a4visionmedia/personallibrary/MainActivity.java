package com.a4visionmedia.personallibrary;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private BookAdapter mAdapter;
    private RecyclerView recyclerView;
    private String mJsonData = "";
    private ImageView mAddButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.books_rv);
        mAddButton = findViewById(R.id.add_book);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("haha", "clicked");
                Intent intent = new Intent(MainActivity.this, InputBookActivity.class);
                startActivity(intent);
            }
        });

        new ListBookAsyncTask().execute();

    }

    class ListBookAsyncTask extends AsyncTask<URL,Void,String> {
        String BASE_URL="http://dev.beta.4visionmedia.com/booklist.json";
        @Override
        protected String doInBackground(URL... urls) {
            String jsonString = "";
            URL url = null;
            try{
                url = createUrl(BASE_URL);
            }catch(MalformedURLException e){
                e.printStackTrace();
            }
            try{
                jsonString = makeHttpRequest(url);
                Log.d("haha2", jsonString);
                mJsonData = jsonString;
            } catch (IOException e) {
                e.printStackTrace();
            }

            return jsonString;
        }

        @Override
        protected void onPostExecute(String strings) {
            List<Book> bookList = getJsonData();

            mAdapter = new BookAdapter(bookList, MainActivity.this, new OnClickListener() {
                @Override
                public void onItemClick(Book item) {
                    Intent intent = new Intent(MainActivity.this, BookDetailActivity.class);
                    startActivity(intent);
                }
            });

            recyclerView.setAdapter(mAdapter);
        }

        private String makeHttpRequest (URL url) throws IOException{
            String jsonResponse = "";
            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;
            try{
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000);
                urlConnection.setConnectTimeout(15000);
                urlConnection.connect();
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }catch (IOException e){

            } finally {
                if (urlConnection != null){
                    urlConnection.disconnect();
                }
                if(inputStream != null){
                    inputStream.close();
                }
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

    private ArrayList<Book> getJsonData(){
        ArrayList<Book> books = new ArrayList<>();

        try {
            JSONObject root = new JSONObject(mJsonData);
            //JSONArray features = root.getJSONArray("daftar");
            JSONObject book = root.getJSONObject("buku");
            JSONArray data = book.getJSONArray("data");
            for(int i=0 ; i<data.length() ; i++){
                //JSONObject properties = hutan.getJSONObject(i).getJSONObject("properties");
                JSONObject properties = data.getJSONObject(i);
                books.add(new Book(properties.getString("judul"),
                        properties.getString("pengarang"),
                        properties.getString("penerbit"),
                        properties.getString("kategori"),
                        properties.getString("noisbn"),
                        properties.getString("cover")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return books;
    }
}
