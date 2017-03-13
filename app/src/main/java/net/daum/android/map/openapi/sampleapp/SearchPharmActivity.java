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


public class SearchPharmActivity extends Activity {

    String DataParseUrl = "http://203.252.22.161:8088/search_pharm.php";

    JSONArray pharmacy = null;
    private Spinner choice;
    Boolean Checkspinner;
    private String gchoice;
    private static final String TAG_result = "result";
    private static final String location = "P_location";
    private static final String name = "P_name";
    private static final String tel = "P_tel";
    private static final String lat = "lat";
    private static final String lon = "lon";
    ArrayList<HashMap<String, String>> pharmacyList;
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_pharm);
        list = (ListView) findViewById(R.id.P_listView);
        choice = (Spinner) findViewById(R.id.pharm_choice);
        pharmacyList = new ArrayList<HashMap<String, String>>();

        Button search = (Button) findViewById(R.id.P_search);
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

                HashMap<String,String> listviewitems = pharmacyList.get(position);
                Intent intentpharm = new Intent(SearchPharmActivity.this,PharmInfoActivity.class);

                intentpharm.putExtra(location, listviewitems.get(location));
                intentpharm.putExtra(name, listviewitems.get(name));
                intentpharm.putExtra(tel, listviewitems.get(tel));
                intentpharm.putExtra(lat, listviewitems.get(lat));
                intentpharm.putExtra(lon, listviewitems.get(lon));


                startActivity(intentpharm);
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
        ProgressDialog dialog = new ProgressDialog(SearchPharmActivity.this);
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
                    return ("약국 찾기 실패");
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
                pharmacy = jsonobj. getJSONArray(TAG_result);
                for (int i = 0; i < pharmacy.length(); i++) {
                    JSONObject c = pharmacy.getJSONObject(i);
                    String plocation = c.getString(location);
                    String pname = c.getString(name);
                    String ptel = c.getString(tel);
                    String plat = c.getString(lat);
                    String plon = c.getString(lon);


                    HashMap<String, String> pharmacys = new HashMap<String, String>();

                    pharmacys.put(location, plocation);
                    pharmacys.put(name, pname);
                    pharmacys.put(tel, ptel);
                    pharmacys.put(lat, plat);
                    pharmacys.put(lon, plon);

                    pharmacyList.add(pharmacys);
                }

                ListAdapter adapter = new SimpleAdapter(
                        SearchPharmActivity.this, pharmacyList, R.layout.pharmacy_list,
                        new String[]{location, name, tel},
                        new int[]{R.id.P_location, R.id.P_name, R.id.P_tel}
                );

                list.setAdapter(adapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}