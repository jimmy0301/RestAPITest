package com.codepath.getmember.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.getmember.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences mSettings;
    private EditText etName;
    private EditText etPwd;
    private Button btnSubmit;
    private JSONObject data;
    private final String URL = "http://52.197.192.141:3443";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSettings = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        String token = mSettings.getString("token", "");

        if (token.isEmpty()) {
            etName = (EditText) findViewById(R.id.etName);
            etPwd = (EditText) findViewById(R.id.etPwd);
            btnSubmit = (Button) findViewById(R.id.btnSubmit);

            View.OnClickListener btnListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    data = new JSONObject();
                    JSONObject tokenStruct;

                    try {
                        data.put("name", etName.getText().toString());
                        data.put("pwd", etPwd.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    getToken(data);
                }
            };

            btnSubmit.setOnClickListener(btnListener);
        }
        else {
            Log.d("getfromShare", token);
            Integer exp_time = mSettings.getInt("exp", 0);

            if (!is_token_exp(exp_time)) {
                Intent i = new Intent(MainActivity.this, MemberListActivity.class);
                i.putExtra("token", token);
                startActivity(i);
            }
            else {
                data = new JSONObject();
                String name = mSettings.getString("name", "");
                String pwd = mSettings.getString("pwd", "");

                if (!name.isEmpty() && !pwd.isEmpty()) {
                    try {
                        data.put("name", name);
                        data.put("pwd", pwd);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    getToken(data);
                }
            }
        }
    }

    private boolean is_token_exp(Integer exp_time) {
        long now = System.currentTimeMillis()/1000;
        if (now > exp_time)
            return true;

        return false;
    }

    public void getToken(final JSONObject data) {
        RequestBody body = RequestBody.create(JSON, data.toString());
        Request request = new Request.Builder().url(URL).post(body).build();
        OkHttpClient client = new OkHttpClient();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("failure", e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    final JSONObject obj = new JSONObject(response.body().string());
                    boolean hasCode = obj.has("code");

                    if (hasCode) {
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "Invalid password or account", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    else {
                        boolean hasToken = obj.has("token");

                        if (hasToken) {
                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        String inputName = data.getString("name");
                                        String inputPwd = data.getString("pwd");
                                        String token = obj.getJSONObject("token").getString("token");
                                        Integer iat = obj.getJSONObject("token").getInt("iat");
                                        Integer exp = obj.getJSONObject("token").getInt("exp");

                                        SharedPreferences.Editor editor = mSettings.edit();

                                        editor.putString("token", token);
                                        editor.putString("name", inputName);
                                        editor.putString("pwd", inputPwd);
                                        editor.putInt("iat", iat);
                                        editor.putInt("exp", exp);

                                        editor.apply();

                                        Intent i = new Intent(MainActivity.this, MemberListActivity.class);
                                        i.putExtra("token", token);
                                        startActivity(i);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
