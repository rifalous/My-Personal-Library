package com.a4visionmedia.personallibrary;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

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

public class RegisterActivity extends AppCompatActivity {

    private String mUsername, mEmail, mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText etEmail = (EditText) findViewById(R.id.etEmail);
        final EditText etUsername = (EditText) findViewById(R.id.etUserName);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);
        final Button bRegister = (Button) findViewById(R.id.bRegister);

        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEmail = etEmail.getText().toString();
                mUsername = etUsername.getText().toString();
                mPassword = etPassword.getText().toString();

                new UserRegisAsyncTask().execute();

            }
        });
    }

    class UserRegisAsyncTask extends AsyncTask<URL,Void,String> {
        String BASE_URL="http://dev.beta.4visionmedia.com/regis_android.php";
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
            if(strings.toLowerCase().equals("sukses")){
                Toast.makeText(RegisterActivity.this, "Daftar Berhasil", Toast.LENGTH_SHORT);
                Intent tempIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(tempIntent);
            }

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

                data += "&" + URLEncoder.encode("password", "UTF-8") + "="
                        + URLEncoder.encode(mPassword, "UTF-8");

                data += "&" + URLEncoder.encode("email", "UTF-8") + "="
                        + URLEncoder.encode(mEmail, "UTF-8");

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