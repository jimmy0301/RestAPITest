package com.codepath.getmember.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.codepath.getmember.R;
import com.codepath.getmember.adapter.MemberAdapter;
import com.codepath.getmember.models.Member;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MemberListActivity extends AppCompatActivity {

    RecyclerView rvResults;

    private final String URL = "http://52.197.192.141:3443/member";
    private List<Member> list;
    private MemberAdapter rvAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_list);

        String token = getIntent().getStringExtra("token");

        if (token != null) {
            setupViews();
            showMemberList(token);
        }
    }

    private void setupViews() {
        rvResults = (RecyclerView) findViewById(R.id.rvResults);
        list = new ArrayList<Member>();

        rvAdapter = new MemberAdapter(getApplicationContext(), list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        layoutManager.scrollToPosition(0);

        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rvResults.addItemDecoration(itemDecoration);

        rvResults.setLayoutManager(layoutManager);
        rvResults.setAdapter(rvAdapter);
    }

    private void showMemberList(String token) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .header("Authorization", token)
                .url(URL)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    final JSONObject obj = new JSONObject(response.body().string());
                    JSONArray arr = obj.getJSONArray("data");
                    list.addAll(Member.fromJsonArray(arr));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                MemberListActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rvAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }
}
