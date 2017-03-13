package net.daum.android.map.openapi.sampleapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.LongSparseArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
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
import java.util.ArrayList;
import java.util.HashMap;


public class SearchHosActivity extends Activity {

    String DataParseUrl = "http://203.252.22.161:8088/search_hos.php";

    JSONArray hospital = null;
    private Spinner choice;
    Boolean Checkspinner;
    private String gchoice;
    private static final String TAG_result = "result";
    private static final String location = "H_location";
    private static final String name = "H_name";
    private static final String tel = "H_tel";
    private static final String kind = "H_kind";
    private static final String weekday = "H_weekday";
    private static final String weekend = "H_weekend";
    private static final String lat = "lat";
    private static final String lon = "lon";
    ArrayList<HashMap<String, String>> HospitalList;
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_hos);
        list = (ListView) findViewById(R.id.H_listView);
        choice = (Spinner) findViewById(R.id.hos_choice);
        HospitalList = new ArrayList<HashMap<String, String>>();

        Button search = (Button) findViewById(R.id.H_search);
        search.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                GetCheckSpinner();
                if (Checkspinner) {
                    new GetDataJSON().execute(gchoice);
                }
            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                HashMap<String,String> listviewitems = HospitalList.get(position);
                Intent intenthos = new Intent(SearchHosActivity.this,HosInfoActivity.class);

                intenthos.putExtra(location, listviewitems.get(location));
                intenthos.putExtra(name, listviewitems.get(name));
                intenthos.putExtra(tel, listviewitems.get(tel));
                intenthos.putExtra(kind, listviewitems.get(kind));
                intenthos.putExtra(weekday, listviewitems.get(weekday));
                intenthos.putExtra(weekend, listviewitems.get(weekend));
                intenthos.putExtra(lat, listviewitems.get(lat));
                intenthos.putExtra(lon, listviewitems.get(lon));


                startActivity(intenthos);
            }
        });
    }

    public void GetCheckSpinner() {
        gchoice = choice.getSelectedItem().toString();

        if (TextUtils.isEmpty(gchoice)) {
            Checkspinner = false;
        } else {
            Checkspinner = true;
        }
    }

    private class GetDataJSON extends AsyncTask<String, String, String> {
        URL url = null;
        HttpURLConnection con;
        ProgressDialog dialog = new ProgressDialog(SearchHosActivity.this);
        protected void onPreExecute() {
            super.onPreExecute();

            dialog.setMessage("리스트를 로딩중입니다");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
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

                Uri.Builder builder = new Uri.Builder().appendQueryParameter("city", params[0]);
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
                    StringBuilder result = new StringBuilder();
                    InputStream input = con.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
                    String json;

                    while ((json = bufferedReader.readLine()) != null) {
                        result.append(json + "\t");
                    }
                    return result.toString();
                } else {
                    return ("병원 찾기 실패");
                }

            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result){
            dialog.dismiss();
            try {

                JSONObject jsonobj = new JSONObject(result);
                hospital = jsonobj. getJSONArray(TAG_result);
                for (int i = 0; i < hospital.length(); i++) {
                    JSONObject c = hospital.getJSONObject(i);
                    String plocation = c.getString(location);
                    String pname = c.getString(name);
                    String ptel = c.getString(tel);
                    String pmanager = c.getString(kind);
                    String pweekday = c.getString(weekday);
                    String pweekend = c.getString(weekend);
                    String plat = c.getString(lat);
                    String plon = c.getString(lon);


                    HashMap<String, String> hospitals = new HashMap<String, String>();

                    hospitals.put(location, plocation);
                    hospitals.put(name, pname);
                    hospitals.put(tel, ptel);
                    hospitals.put(kind, pmanager);
                    hospitals.put(weekday, pweekday);
                    hospitals.put(weekend, pweekend);
                    hospitals.put(lat, plat);
                    hospitals.put(lon, plon);

                    HospitalList.add(hospitals);
                }

                ListAdapter adapter = new SimpleAdapter(
                        SearchHosActivity.this, HospitalList, R.layout.hospital_list,
                        new String[]{location, name, tel},
                        new int[]{R.id.H_location, R.id.H_name, R.id.H_tel}
                );

                list.setAdapter(adapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}