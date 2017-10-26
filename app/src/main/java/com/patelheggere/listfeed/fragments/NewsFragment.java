package com.patelheggere.listfeed.fragments;

/**
 * Created by Talkative Parents on 10/11/2017.
 */

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import com.patelheggere.listfeed.activities.MainActivity;
import com.patelheggere.listfeed.R;
import com.patelheggere.listfeed.adapter.FeedListAdapter;
import com.patelheggere.listfeed.app.AppController;
import com.patelheggere.listfeed.data.FeedItem;



public class NewsFragment extends Fragment{
    private static final String TAG = MainActivity.class.getSimpleName();
    private ListView listView;
    private FeedListAdapter listAdapter;
    private List<FeedItem> feedItems;
    private String URL_FEED = "https://rajeev-8a4b5.firebaseio.com/News.json";
    public NewsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.news_fragment, container, false);
                listView = (ListView)linearLayout.findViewById(R.id.list);

        feedItems = new ArrayList<FeedItem>();
        listAdapter = new FeedListAdapter(getActivity(), feedItems);
        listView.setAdapter(listAdapter);
        // These two lines not needed,
        // just to get the look of facebook (changing background color & hiding the icon)
        //getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3b5998")));
        //getActionBar().setIcon( new ColorDrawable(getResources().getColor(android.R.color.transparent)));

        // We first check for cached request
        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(URL_FEED);
        if (entry != null) {
            // fetch the data from cache
            try {
                String data = new String(entry.data, "UTF-8");
                try {
                    parseJsonFeed(new JSONObject(data));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        } else {
            // making fresh volley request and getting json
            JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.GET,
                    URL_FEED, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    VolleyLog.d(TAG, "Response: " + response.toString());
                    if (response != null) {
                        parseJsonFeed(response);
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                }
            });

            // Adding request to volley request queue
            AppController.getInstance().addToRequestQueue(jsonReq);
        }

        // Inflate the layout for this fragment
        return linearLayout;
    }


    /**
     * Parsing json reponse and passing the data to feed view list adapter
     * */
    private void parseJsonFeed(JSONObject response) {

        try {
            System.out.println("parsedJson:"+response.toString());
            JSONObject object = new JSONObject(response.toString());
            Iterator<String> iterator = object.keys();
            while (iterator.hasNext()) {
                String fbKey = iterator.next();
                JSONObject feedObj = object.getJSONObject(fbKey);
                FeedItem item = new FeedItem();
                item.setFbKey(fbKey);
                item.setId(feedObj.getInt("id"));
                item.setName(feedObj.getString("name"));

                // Image might be null sometimes
                String image = feedObj.isNull("image") ? null : feedObj.getString("image");
                item.setImge(image);
                item.setStatus(feedObj.getString("status"));
                item.setProfilePic(feedObj.getString("profilePic"));
                item.setTimeStamp(feedObj.getLong("timeStamp"));

                // url might be null sometimes
                String feedUrl = feedObj.isNull("url2") ? null : feedObj.getString("url2");
                String CommentCount = feedObj.isNull("commentCount") ? null : feedObj.getString("commentCount");
                String LikeCount = feedObj.isNull("LikeCount") ? null : feedObj.getString("LikeCount");
                item.setCommentCount(CommentCount);
                item.setLikeCount(LikeCount);
                item.setUrl(feedUrl);
                //item.setFbKey(iterator.next());
                feedItems.add(item);
            }

            // notify data changes to list adapater
            listAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
