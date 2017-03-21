package net.daum.android.map.openapi.sampleapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
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


public class SearchCardiActivity extends Activity {

    String DataParseUrl = "http://203.252.22.161:8088/search_car.php";

    String myjson;
    JSONArray cardioverter = null;
    private Spinner choice;
    Boolean Checkspinner;
    private String gchoice;
    private static final String TAG_result = "result";
    private static final String location = "C_location";
    private static final String name = "C_name";
    private static final String tel = "C_tel";
    private static final String manager = "C_manager";
    private static final String manager_tel = "C_manager_tel";
    private static final String lat = "lat";
    private static final String lon = "lon";
    ArrayList<HashMap<String, String>> cardioverterList;
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_cardi);
        list = (ListView) findViewById(R.id.C_listView);
        choice = (Spinner) findViewById(R.id.cardi_choice);
        cardioverterList = new ArrayList<HashMap<String, String>>();

        Button search = (Button) findViewById(R.id.C_search);
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

                HashMap<String,String> listviewitems = cardioverterList.get(position);
                Intent intentcardi = new Intent(SearchCardiActivity.this,CardiInfoActivity.class);

                intentcardi.putExtra(location, listviewitems.get(location));
                intentcardi.putExtra(name, listviewitems.get(name));
                intentcardi.putExtra(tel, listviewitems.get(tel));
                intentcardi.putExtra(manager, listviewitems.get(manager));
                intentcardi.putExtra(manager_tel, listviewitems.get(manager_tel));
                intentcardi.putExtra(lat, listviewitems.get(lat));
                intentcardi.putExtra(lon, listviewitems.get(lon));


                startActivity(intentcardi);
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
        ProgressDialog dialog = new ProgressDialog(SearchCardiActivity.this);
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
                    return ("제세동기 찾기 실패");
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
                cardioverter = jsonobj. getJSONArray(TAG_result);
                for (int i = 0; i < cardioverter.length(); i++) {
                    JSONObject c = cardioverter.getJSONObject(i);
                    String plocation = c.getString(location);
                    String pname = c.getString(name);
                    String ptel = c.getString(tel);
                    String pmanager = c.getString(manager);
                    String pmanager_tel = c.getString(manager_tel);
                    String plat = c.getString(lat);
                    String plon = c.getString(lon);


                    HashMap<String, String> cardioverters = new HashMap<String, String>();

                    cardioverters.put(location, plocation);
                    cardioverters.put(name, pname);
                    cardioverters.put(tel, ptel);
                    cardioverters.put(manager, pmanager);
                    cardioverters.put(manager_tel, pmanager_tel);
                    cardioverters.put(lat, plat);
                    cardioverters.put(lon, plon);


                    cardioverterList.add(cardioverters);
                }

                ListAdapter adapter = new SimpleAdapter(
                        SearchCardiActivity.this, cardioverterList, R.layout.cardioverter_list,
                        new String[]{location, name, tel},
                        new int[]{R.id.C_location, R.id.C_name, R.id.C_tel}
                );

                list.setAdapter(adapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}