package com.example.newsaggregator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity{
//    final static String ="20b45f3d7df24a49b3da6f9a7addf5d1";





    private RequestQueue mQueue;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private ArrayList<String> sourcesArrList = new ArrayList<>();



    private ViewPager2 viewPager2;
    private ArrayList<ViewPagerItem> viewPagerItemArrayList;

    private ArrayList<SourcesObj> sourcesObjArrayList = new ArrayList<>();

    private ArrayList<SourcesObj> temp = new ArrayList<>();

    private ArrayList<String> test;

    private Menu opt_menu;

    private ListAdapter listAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager2 = findViewById(R.id.viewpager);

        viewPagerItemArrayList = new ArrayList<>();

        mQueue = Volley.newRequestQueue(this);

        //setupPage();
        //getStories("cnn");

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
        Toast.makeText(this, String.valueOf(item), Toast.LENGTH_SHORT).show();
        getByCategory(String.valueOf(item));
        //getSupportActionBar().setTitle(String.valueOf(item) + "( "+temp.size() +" )");
        return super.onOptionsItemSelected(item);
    }

    private void getByCategory(String valueOf) {
        temp.clear();
        for(int i =0;i<sourcesObjArrayList.size();i++){
            String add = sourcesObjArrayList.get(i).getCategory();
            SourcesObj obj = sourcesObjArrayList.get(i);
            if(add.equals(valueOf)){
                temp.add(obj);
            }
            else if (valueOf == "All"){
                temp = (ArrayList<SourcesObj>)sourcesObjArrayList.clone();
            }


        }
        listAdapter = new ListAdapter(this,R.layout.drawer_list_item,temp);
        mDrawerList.setAdapter(listAdapter);
    }

    private void selectItem(int position) {
        //Toast.makeText(this, "Selected "+ sourcesObjArrayList.get(position).getName(), Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, ""+mDrawerList.getAdapter().getItem(position), Toast.LENGTH_SHORT).show();
        if(temp.isEmpty()){
            getStories(sourcesObjArrayList.get(position).getId());
            mDrawerLayout.closeDrawer(mDrawerList);
            getSupportActionBar().setTitle(sourcesObjArrayList.get(position).getName());
        }else{
            getStories(temp.get(position).getId());
            mDrawerLayout.closeDrawer(mDrawerList);
            getSupportActionBar().setTitle(temp.get(position).getName());
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        opt_menu = menu;
        return true;
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
                                String id = s.getString("id");
                                String cat = s.getString("category");
                                //sourcesArrList.add(name);
                                SourcesObj sourcesObj = new SourcesObj(name,id,cat);
                                sourcesObjArrayList.add(sourcesObj);
                            }
                            getSupportActionBar().setTitle("News Gateway( "+sourcesObjArrayList.size()+ " )");
                            test = getAllCategorirs(sourcesObjArrayList);

                            for(String s: test){
                                opt_menu.add(s);
                            }
//
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
        ListAdapter listAdapter = new ListAdapter(this,R.layout.drawer_list_item,sourcesObjArrayList);
        mDrawerList.setAdapter(listAdapter);

        //mDrawerList.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_list_item, array));
    }

    private void getStories(String NewsSource){

        String url ="https://newsapi.org/v2/top-headlines?sources="+NewsSource+"&apiKey=20b45f3d7df24a49b3da6f9a7addf5d1";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {


                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            viewPagerItemArrayList.clear();
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
                                //ViewPagerAdapter testAdapter = new ViewPagerAdapter(viewPagerItemArrayList,listener);
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

    private ArrayList<String> getAllCategorirs(ArrayList<SourcesObj> all){
        ArrayList<String>  temp = new ArrayList<>();
        temp.add("All");
        for(int i =0;i < all.size();i++){
            if(!temp.contains(all.get(i).getCategory())){
                temp.add(all.get(i).getCategory());
            }
        }
        return temp;

    }



}
