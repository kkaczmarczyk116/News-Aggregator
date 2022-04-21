package com.example.newsaggregator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
//    final static String ="20b45f3d7df24a49b3da6f9a7addf5d1";





    private RequestQueue mQueue;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private ArrayList<String> sourcesArrList = new ArrayList<>();

    private String[] array;

    private ViewPager2 viewPager2;
    private ArrayList<ViewPagerItem> viewPagerItemArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        viewPager2 = findViewById(R.id.viewpager);

        viewPagerItemArrayList = new ArrayList<>();

        mQueue = Volley.newRequestQueue(this);

        //setupPage();
        getStories("cnn");

        getChannels();



        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerList = findViewById(R.id.left_drawer);

//        mDrawerList.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_list_item, array));

        mDrawerList.setOnItemClickListener(
                (parent, view, position, id) -> selectItem(position)
        );

        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                R.string.drawer_open,
                R.string.drawer_close
        );

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        //mDrawerList.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_list_item, array));
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void selectItem(int position) {
        Toast.makeText(this, "Selected "+position, Toast.LENGTH_SHORT).show();
        mDrawerLayout.closeDrawer(mDrawerList);
    }




    private void getChannels(){

        String url ="https://newsapi.org/v2/sources?apiKey=20b45f3d7df24a49b3da6f9a7addf5d1";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {


                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray sources = response.getJSONArray("sources");
                            int numberOfSources = sources.length();

                            for(int i = 0;i <sources.length();i++){
                                JSONObject s = sources.getJSONObject(i);
                                String name = s.getString("name");
                                sourcesArrList.add(name);
                            }
                            array = new String[sourcesArrList.size()];
                            for(int j =0;j<sourcesArrList.size();j++){
                                array[j] = sourcesArrList.get(j);
                            }
                            adapt();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

            }

        }){@Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            Map<String, String> headers = new HashMap<>();
            headers.put("User-Agent", "");
            return headers;
        }};


        mQueue.add(request);

    }
    private void adapt(){
        mDrawerList.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_list_item, array));
    }

    private void getStories(String NewsSource){

        String url ="https://newsapi.org/v2/top-headlines?sources="+NewsSource+"&apiKey=20b45f3d7df24a49b3da6f9a7addf5d1";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {


                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String title;
                            String date ;
                            String page_source;
                            String picUrl ;
                            String desc ;
                            String articleUrl;
                            JSONArray articles = response.getJSONArray("articles");
                            int numberOfSources = articles.length();

                            for(int i = 0;i <articles.length();i++){
                                JSONObject s = articles.getJSONObject(i);
                                JSONObject sourceObj = s.getJSONObject("source");
                                if(s.has("title")){
                                    title = s.getString("title");
                                }else{
                                    title = " ";
                                }
                                if(s.has("publishedAt")){
                                    date = s.getString("publishedAt");
                                }else{
                                    date = " ";
                                }

                                if(s.has("author")){
                                    page_source = s.getString("author");
                                }else{
                                    page_source = " ";
                                }

                                if(s.has("urlToImage")){
                                    picUrl = s.getString("urlToImage");
                                }else{
                                    picUrl = " ";
                                }

                                if(s.has("description")){
                                    desc = s.getString("description");
                                }else{
                                    desc = " ";
                                }

                                if(s.has("url")){
                                    articleUrl = s.getString("url");
                                }else{
                                    articleUrl = " ";
                                }

                                if(page_source == "null" || page_source.length() > 100){
                                    page_source = " ";
                                }

                                ViewPagerItem viewPagerItem = new ViewPagerItem(title,date,page_source,picUrl,desc,articleUrl);
                                viewPagerItemArrayList.add(viewPagerItem);




                                ViewPagerAdapter vpAdapter = new ViewPagerAdapter(viewPagerItemArrayList);
                                viewPager2.setAdapter(vpAdapter);
                                viewPager2.setClipToPadding(false);
                                viewPager2.setClipChildren(false);
                                viewPager2.setOffscreenPageLimit(2);
                                viewPager2.getChildAt(0).setOverScrollMode(View.OVER_SCROLL_NEVER);

                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

            }

        }){@Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            Map<String, String> headers = new HashMap<>();
            headers.put("User-Agent", "");
            return headers;
        }};


        mQueue.add(request);

    }





}
