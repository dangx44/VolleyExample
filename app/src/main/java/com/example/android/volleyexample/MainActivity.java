package com.example.android.volleyexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private List<Item> mItemList;
    private RequestQueue mRequestQue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mItemList = new ArrayList<>();

        mRequestQue = VolleySingleton.getInstance(this).getRequestQue();
        //  mRequestQue = Volley.newRequestQueue(this);
        parseJSON();
    }

    private void parseJSON() {

        String url = "https://pixabay.com/api/?key=8329206-ac2718c33f05d40a024e3289a&q=brown+dogs&image_type=photo&pretty=true";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("hits");

                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject hit = jsonArray.getJSONObject(i);
                        String creatorName = hit.getString("user");
                        String imageUrl = hit.getString("webformatURL");
                        int likes = hit.getInt("likes");

                        mItemList.add(new Item(imageUrl, creatorName, likes));

                        mAdapter = new MyAdapter(MainActivity.this, mItemList);
                        mRecyclerView.setAdapter(mAdapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mRequestQue.add(request);
    }
}
