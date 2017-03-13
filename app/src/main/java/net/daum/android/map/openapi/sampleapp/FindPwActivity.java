package net.daum.android.map.openapi.sampleapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONArray;
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

/**
 * Created by seung on 2016-10-30.
 */
public class FindPwActivity extends Activity {
    String DataParseUrl = "http://203.252.22.161:8088/Find_pw.php";
    Boolean CheckEditText;
    private EditText username, id, tel;
    private String gusername, gid, gtel;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_pw);

        username = (EditText) findViewById(R.id.find_pw_input_name);
        tel = (EditText) findViewById(R.id.find_pw_input_tel);
        id = (EditText) findViewById(R.id.find_pw_input_id);

        Button find = (Button) findViewById(R.id.btn_pw_find);
        find.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                GetCheckpwEditTextIsEmptyOrNot();

                if (CheckEditText) {
                    new AsyncFpw().execute(gid, gusername, gtel);
                } else {
                    Toast.makeText(FindPwActivity.this, "빈 항목을 입력해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button cancel = (Button) findViewById(R.id.btn_pw_close);
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }


    public void GetCheckpwEditTextIsEmptyOrNot() {
        gid = id.getText().toString();
        gusername = username.getText().toString();
        gtel = tel.getText().toString();

        if (TextUtils.isEmpty(gid) || TextUtils.isEmpty(gusername) || TextUtils.isEmpty(gtel)) {
            CheckEditText = false;
        } else {
            CheckEditText = true;
        }
    }

    private class AsyncFpw extends AsyncTask<String, String, String> {
        ProgressDialog dialog = new ProgressDialog(FindPwActivity.this);
        HttpURLConnection con;
        URL url = null;

        protected void onPreExecute() {
            super.onPreExecute();

            dialog.setMessage("비밀번호를 찾는중입니다");
            dialog.setCancelable(false);
            dialog.show();

        }

        protected String doInBackground(String... params) {
            try {
                url = new URL(DataParseUrl);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "Exception Error";
            }

            try {
                con = (HttpURLConnection) url.openConnection();
                con.setReadTimeout(100000);
                con.setConnectTimeout(100000);
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder().appendQueryParameter("U_id", params[0]).appendQueryParameter("U_name", params[1]).appendQueryParameter("U_tel", params[2]);
                String query = builder.build().getEncodedQuery();

                OutputStream os = con.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

                writer.write(query);
                writer.flush();
                writer.close();
                os.flush();
                os.close();
                con.connect();
            } catch (IOException e) {
                e.printStackTrace();
                return "Exception Error";
            }

            try {
                int response = con.getResponseCode();

                if (response == HttpURLConnection.HTTP_OK) {
                    InputStream input = con.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    return (result.toString());
                } else {
                    return ("비밀번호 찾기 실패");
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "Exceiption Error";
            } finally {
                con.disconnect();
            }
        }

        protected void onPostExecute(String result) {
            dialog.dismiss();
            if (result != null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(FindPwActivity.this);
                builder.setTitle("비밀번호");
                builder.setMessage(result);
                builder.setCancelable(false);
                builder.setNegativeButton("닫기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }


    }
}