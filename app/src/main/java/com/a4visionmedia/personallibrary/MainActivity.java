package com.a4visionmedia.personallibrary;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private BookAdapter mAdapter;
    private RecyclerView recyclerView;
    private String mJsonData = "";
    private FloatingActionButton mAddButton;
    private RecyclerView.LayoutManager layoutManager;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private SearchView searchView;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = findViewById(R.id.navigation_drawer);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open,R.string.drawer_close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.books_rv);
        mAddButton = findViewById(R.id.add_book);

        mNavigationView = findViewById(R.id.navigation_view);



        /*if (MainActivity.this.getResources().getConfiguration().orientation == 1) {
            layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        } else {
            layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.HORIZONTAL);
        }*/



        mSwipeRefreshLayout = findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        mSwipeRefreshLayout.setRefreshing(true);

        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("haha", "clicked");
                Intent intent = new Intent(MainActivity.this, InputBookActivity.class);
                intent.putExtra("FLAG", "INPUT");
                startActivity(intent);
            }
        });

        new ListBookAsyncTask().execute();

    }

    @Override
    protected void onResume() {
        new ListBookAsyncTask().execute();
        super.onResume();
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        new ListBookAsyncTask().execute();
    }

    class ListBookAsyncTask extends AsyncTask<URL,Void,String> {
        String BASE_URL="http://dev.beta.4visionmedia.com/dbtojsonbuku.php";
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
            mSwipeRefreshLayout.setRefreshing(false);
            List<Book> bookList = getJsonData();

            mAdapter = new BookAdapter(bookList, MainActivity.this, new OnClickListener() {
                @Override
                public void onItemClick(Book item) {
                    Intent intent = new Intent(MainActivity.this, BookDetailActivity.class);
                    intent.putExtra("judul", item.getTitle());
                    intent.putExtra("pengarang", item.getAuthor());
                    intent.putExtra("penerbit", item.getPublisher());
                    intent.putExtra("kategori", item.getCategory());
                    intent.putExtra("isbn", item.getNoIsbn());
                    intent.putExtra("cover", item.getCover());
                    startActivity(intent);
                }
            });
            recyclerView.setAdapter(mAdapter);
            layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);

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

    @SuppressLint("ResourceAsColor")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                mAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }
        if(mToggle.onOptionsItemSelected(item)){
            mNavigationView.bringToFront();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // close search view on back button pressed
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }

}
