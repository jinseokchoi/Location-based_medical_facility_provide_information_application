package net.daum.android.map.openapi.sampleapp;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.internal.widget.TintEditText;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapReverseGeoCoder;
import net.daum.mf.map.api.MapView;

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
import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by seung on 2016-10-26.
 */
public class LocationSearchActivity extends FragmentActivity implements MapView.POIItemEventListener , MapView.CurrentLocationEventListener, MapReverseGeoCoder.ReverseGeoCodingResultListener {


    String DataParseUrl3 = "http://203.252.22.161:8088/Location_Search3.php";
    String DataParseUrl5 = "http://203.252.22.161:8088/Location_Search5.php";
    String DataParseUrl10 = "http://203.252.22.161:8088/Location_Search10.php";
    JSONArray Location = null;
    private static final String TAG_result = "result";
    private static final String name = "name";
    private static final String lat = "lat";
    private static final String lon = "lon";
    String result_name[];
    Double result_lat[], result_lon[];
    MapPOIItem poiItem;
    Boolean CheckLocation = true;
    private static final String LOG_TAG = "LocationSearchActivity";
    private final int MENU_LOCATION = Menu.FIRST;
    private final int MENU_REVERSE_GEO = Menu.FIRST + 1;
    private Button btn_currrent;
    private MapView mMapView;
    private MapReverseGeoCoder mReverseGeoCoder = null;
    private boolean isUsingCustomLocationMarker = false;
    double current_lat, current_lon;
    private String gcurrent_lat="", gcurrent_lon="";
    private Spinner spinnercategory, spinnerdistance;
    private String gspinnerchoice, gspinnerdistance;
    String ItemName;
    // private String gname;
    String[] category = { "병원", "약국", "응급실", "제세동기"};
    String[] distance = { "3km", "5km", "10km"};
    URL url = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_nested_mapview);
        Initialize();
        spinnerAdapter();
        btn_currrent = (Button) findViewById(R.id.btn_current);
        btn_currrent.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("setCurrentLocation","실행전");
                mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
                mMapView.setPOIItemEventListener(LocationSearchActivity.this);
                MapPoint.GeoCoordinate geoCoordinate = mMapView.getMapCenterPoint().getMapPointGeoCoord();
                current_lat = geoCoordinate.latitude; // 위도
                current_lon = geoCoordinate.longitude; // 경도

                //onCurrentLocationUpdate
                Log.e("과연 lat, lon",current_lat+"   ,   "+current_lon);
                GetCheckLocation();
                GetCheckDistance();

                gcurrent_lat = Double.toString(current_lat);
                gcurrent_lon = Double.toString(current_lon);

                Log.e("GetCheckLocation();","실행후 gspinnerchoice값: "+gspinnerchoice);
                if (true) {
                    Log.e("CheckLocation안에 execute","실행전");
                    new GetDataJSON().execute(gspinnerchoice, gcurrent_lat, gcurrent_lon);
                    Log.e("CheckLocation안에 execute","실행후");
                }


            }
        });
    }

    public void GetCheckLocation() {
        gspinnerchoice = spinnercategory.getSelectedItem().toString();
        if(gspinnerchoice == "병원"){
            gspinnerchoice = "hospital";
        }

        else if(gspinnerchoice == "제세동기"){
            gspinnerchoice = "cardioverter";
        }

        else if(gspinnerchoice == "응급실"){
            gspinnerchoice = "emergency";
        }

        else if(gspinnerchoice == "약국"){
            gspinnerchoice = "pharmacy";
        }


        if (current_lat==0.0 && current_lon==0.0) {
            // if(TextUtils.isEmpty(gname)){
            CheckLocation = false;
            // }

        } else {
            CheckLocation = true;
        }
    }

    public void GetCheckDistance(){
        gspinnerdistance = spinnerdistance.getSelectedItem().toString();
        try {
            if (gspinnerdistance == "3km") {
                url = new URL(DataParseUrl3);
            }
            else if(gspinnerdistance == "5km"){
                url = new URL(DataParseUrl5);
            }
            else if(gspinnerdistance == "10km"){
                url = new URL(DataParseUrl10);
            }
        }
        catch(MalformedURLException e){

        }
    }

    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {
        //mapPOIItem.getItemName();
        MapPoint.GeoCoordinate mapPOIItemGeo = mapPOIItem.getMapPoint().getMapPointGeoCoord();
        Log.e(mapPOIItemGeo.latitude+"   item ",mapPOIItemGeo.longitude+"   ");
        Log.e(current_lat+"   current ",current_lon+"   ");

        final Double maplat = mapPOIItemGeo.latitude;
        final Double maplon = mapPOIItemGeo.longitude;

        ItemName = mapPOIItem.getItemName();
        Intent selectgo= new Intent(LocationSearchActivity.this,SelectGoActivity.class);

        selectgo.putExtra("maplat",maplat.toString());
        selectgo.putExtra("maplon",maplon.toString());
        selectgo.putExtra("current_lat",current_lat+"");
        selectgo.putExtra("current_lon",current_lon+"");
        startActivity(selectgo);
        /*AlertDialog.Builder builder = new AlertDialog.Builder(LocationSearchActivity.this);
        // 여기서 부터는 알림창의 속성 설정
        builder.setTitle("Marker Click")        // 제목 설정
                .setMessage("클릭된 정보를 자세히 보시겠습니까?")        // 메세지 설정
                .setCancelable(false)        // 뒤로 버튼 클릭시 취소 가능 설정
                .setPositiveButton("확인", new DialogInterface.OnClickListener(){
                    // 확인 버튼 클릭시 설정
                    public void onClick(DialogInterface dialog, int whichButton){*/

 /*                       startActivity(selectgo);
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener(){
                    // 취소 버튼 클릭시 설정
                    public void onClick(DialogInterface dialog, int whichButton){
                        dialog.cancel();
                    }
                });

        AlertDialog dialog = builder.create();    // 알림창 객체 생성
        dialog.show();    // 알림창 띄우기*/

    }
   /*== private class UrlIntent extends Thread{

        public void run(){

        }
    }*/

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {

    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

    }

    private class GetDataJSON extends AsyncTask<String, String, String> {
        HttpURLConnection con;
        ProgressDialog dialog = new ProgressDialog(LocationSearchActivity.this);
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("탐색 중입니다.");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                con = (HttpURLConnection) url.openConnection();
                con.setReadTimeout(10000);
                con.setConnectTimeout(10000);
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder().appendQueryParameter("choice", params[0]).appendQueryParameter("lat", params[1]).appendQueryParameter("lon", params[2]);
                Log.e("params[0]의 값",params[0]);
                Log.e("params[1]의 값",params[1]);
                Log.e("params[2]의 값",params[2]);

                String query = builder.build().getEncodedQuery();
                Log.e("Uri Builder builder","실행후");
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
                    Log.e("BufferedReader","실행후");
                    while ((json = bufferedReader.readLine()) != null) {
                        result.append(json + "\t");

                        Log.e("readLind 하는중",json);

                    }
                    return result.toString();
                } else {
                    return ("위도 및 경도 찾기 실패");
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
                Location = jsonobj. getJSONArray(TAG_result);
                Log.e("안녕result",result);
                result_lat = new Double[Location.length()];
                result_lon = new Double[Location.length()];
                result_name = new String[Location.length()];

                for (int i = 0; i < Location.length(); i++) {
                    JSONObject c = Location.getJSONObject(i);
                    String pname = c.getString(name);
                    String plat = c.getString(lat);
                    Log.e("c.getString",c.getString(lat));
                    String plon = c.getString(lon);
                    result_name[i] = pname;
                    result_lat[i] = Double.valueOf(plat.trim()).doubleValue();
                    result_lon[i] = Double.valueOf(plon.trim()).doubleValue();

                    poiItem = new MapPOIItem();
                    poiItem.setItemName(pname);
                    poiItem.setTag(1);
                    MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(result_lat[i],result_lon[i]);
                    poiItem.setMapPoint(mapPoint);
                    poiItem.setMarkerType(MapPOIItem.MarkerType.CustomImage);
                    poiItem.setCustomImageResourceId(R.drawable.map_pin_blue);
                    poiItem.setSelectedMarkerType(MapPOIItem.MarkerType.CustomImage);
                    poiItem.setCustomSelectedImageResourceId(R.drawable.map_pin_red);
                    poiItem.setCustomImageAutoscale(false);
                    poiItem.setCustomImageAnchor(0.5f, 1.0f);
                    mMapView.addPOIItem(poiItem);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    //변수초기설정
    protected void Initialize(){
        mMapView = (MapView) findViewById(R.id.map_view);
        mMapView.setDaumMapApiKey(MapApiConst.DAUM_MAPS_ANDROID_APP_API_KEY);
        mMapView.setCurrentLocationEventListener(this);
        spinnercategory =(Spinner)findViewById(R.id.sp_category);
        spinnerdistance = (Spinner) findViewById(R.id.sp_department);
        btn_currrent = (Button) findViewById(R.id.btn_current);
    }
    //위치기반검색>스피너>선택
    protected void spinnerAdapter(){

        ArrayAdapter<String> ad_category= new ArrayAdapter<String>(this,
                R.layout.spinner_textview ,category);
        ad_category.setDropDownViewResource(R.layout.spinner_textview);
        spinnercategory.setAdapter(ad_category);
        spinnercategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
/*
                int cid=spinnercategory.getSelectedItemPosition();
                gspinnerchoice = category[cid];*/
                /*Toast.makeText(getBaseContext(), "You have selected City : " + category[cid],
                        Toast.LENGTH_SHORT).show();*/
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
        mMapView.setShowCurrentLocationMarker(false);
    }




    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint currentLocation, float accuracyInMeters) {
        MapPoint.GeoCoordinate mapPointGeo = currentLocation.getMapPointGeoCoord();
        Log.i(LOG_TAG, String.format("MapView onCurrentLocationUpdate (%f,%f) accuracy (%f)", mapPointGeo.latitude, mapPointGeo.longitude, accuracyInMeters));
      /*  current_lat = mapPointGeo.latitude;
        current_lon = mapPointGeo.longitude;*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, MENU_LOCATION, Menu.NONE, "Location");
        //menu.add(0, MENU_REVERSE_GEO, Menu.NONE, "Reverse Geo-Coding");

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        final int itemId = item.getItemId();

        switch (itemId) {
            case MENU_LOCATION: {
                String[] mapMoveMenuItems = {
                        "User Location On",
                        "User Location On, MapMoving Off",
                        "User Location+Heading On",
                        "User Location+Heading On, MapMoving Off",
                        "Off",
                        (isUsingCustomLocationMarker ? "Default" : "Custom") + " Location Marker",
                        "Show Location Marker",
                        "Hide Location Marker"
                };
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("Location");
                dialog.setItems(mapMoveMenuItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0: // User Location On
                            {
                                mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
                            }
                            break;
                            case 1: // User Location On, MapMoving Off
                            {
                                mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeadingWithoutMapMoving);
                            }
                            break;
                            case 2: // User Location+Heading On
                            {
                                mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading);
                            }
                            break;
                            case 3: // User Location+Heading On, MapMoving Off
                            {
                                mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeadingWithoutMapMoving);
                            }
                            break;
                            case 4: // Off
                            {
                                mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
                                mMapView.setShowCurrentLocationMarker(false);
                            }
                            break;
                            case 5: // Default/Custom Location Marker
                            {
                                if (isUsingCustomLocationMarker) {
                                    mMapView.setCurrentLocationRadius(0);
                                    mMapView.setDefaultCurrentLocationMarker();
                                } else {
                                    mMapView.setCurrentLocationRadius(100); // meter
                                    mMapView.setCurrentLocationRadiusFillColor(Color.argb(77, 255, 255, 0));
                                    mMapView.setCurrentLocationRadiusStrokeColor(Color.argb(77, 255, 165, 0));

                                    MapPOIItem.ImageOffset trackingImageAnchorPointOffset = new MapPOIItem.ImageOffset(16, 16); // 좌하단(0,0) 기준 앵커포인트 오프셋
                                    MapPOIItem.ImageOffset directionImageAnchorPointOffset = new MapPOIItem.ImageOffset(65, 65);
                                    MapPOIItem.ImageOffset offImageAnchorPointOffset = new MapPOIItem.ImageOffset(15, 15);
                                    mMapView.setCustomCurrentLocationMarkerTrackingImage(R.drawable.custom_map_present_tracking, trackingImageAnchorPointOffset);
                                    mMapView.setCustomCurrentLocationMarkerDirectionImage(R.drawable.custom_map_present_direction, directionImageAnchorPointOffset);
                                    mMapView.setCustomCurrentLocationMarkerImage(R.drawable.custom_map_present, offImageAnchorPointOffset);
                                }
                                isUsingCustomLocationMarker = !isUsingCustomLocationMarker;
                            }
                            break;
                            case 6: // Show Location Marker
                            {
                                mMapView.setShowCurrentLocationMarker(true);
                            }
                            break;
                            case 7: // Hide Location Marker
                            {
                                if (mMapView.isShowingCurrentLocationMarker()) {
                                    mMapView.setShowCurrentLocationMarker(false);
                                }
                            }
                            break;
                        }
                    }

                });
                dialog.show();
                return true;
            }

           /* case MENU_REVERSE_GEO: {
                mReverseGeoCoder = new MapReverseGeoCoder(MapApiConst.DAUM_MAPS_ANDROID_APP_API_KEY, mMapView.getMapCenterPoint(), LocationSearchActivity.this, LocationSearchActivity.this);
                mReverseGeoCoder.startFindingAddress();
                return true;
            }*/
        }

        return onOptionsItemSelected(item);

    }

    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {

    }

    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) {

    }

    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {

    }

    @Override
    public void onReverseGeoCoderFoundAddress(MapReverseGeoCoder mapReverseGeoCoder, String s) {
        mapReverseGeoCoder.toString();
        onFinishReverseGeoCoding(s);
    }

    @Override
    public void onReverseGeoCoderFailedToFindAddress(MapReverseGeoCoder mapReverseGeoCoder) {
        onFinishReverseGeoCoding("Fail");
    }

    private void onFinishReverseGeoCoding(String result) {
        Toast.makeText(LocationSearchActivity.this, "Reverse Geo-coding : " + result, Toast.LENGTH_SHORT).show();
    }

}