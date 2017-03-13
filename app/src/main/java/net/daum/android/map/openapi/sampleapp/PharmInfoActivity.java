package net.daum.android.map.openapi.sampleapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import net.daum.android.map.openapi.sampleapp.MapApiConst;
import net.daum.android.map.openapi.sampleapp.R;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapReverseGeoCoder;
import net.daum.mf.map.api.MapView;

public class PharmInfoActivity extends FragmentActivity implements MapView.CurrentLocationEventListener, MapReverseGeoCoder.ReverseGeoCodingResultListener {

    private static final String LOG_TAG = "PharmInfoActivity";
    private MapView pharmMapView;
    private MapReverseGeoCoder mReverseGeoCoder = null;
    private TextView pharmname, pharmaddr ,pharmtel;
    private String pname,paddr,ptel,plat,plon;
    private Button btn_map;
    private Double doubleclat,doubleclon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_pharm);
        Intent intent = getIntent();
        init();
        btn_map = (Button)findViewById(R.id.btn_infopharm_map);
        pname = intent.getStringExtra("P_name");
        paddr = intent.getStringExtra("P_location");
        ptel = intent.getStringExtra("P_tel");

        plat = intent.getStringExtra("lat");
        plon = intent.getStringExtra("lon");

        doubleclat = Double.valueOf(plat).doubleValue();
        doubleclon = Double.valueOf(plon).doubleValue();

        //Log.e("위도","  "+ doubleclat);

        pharmname.setText(pname);
        pharmaddr.setText(paddr);
        pharmtel.setText(ptel);


        pharmMapView  = (MapView) findViewById(R.id.pharm_mapview);
        pharmMapView .setDaumMapApiKey(MapApiConst.DAUM_MAPS_ANDROID_APP_API_KEY);
        pharmMapView .setCurrentLocationEventListener(this);
        btn_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //cardiMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
                pharmMapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(doubleclat,doubleclon), 2, true);
                MapPOIItem poiItem = new MapPOIItem();
                poiItem.setItemName(pname);
                poiItem.setTag(1);
                MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(doubleclat,doubleclon);
                poiItem.setMapPoint(mapPoint);
                poiItem.setMarkerType(MapPOIItem.MarkerType.CustomImage);
                poiItem.setCustomImageResourceId(R.drawable.map_pin_blue);
                poiItem.setSelectedMarkerType(MapPOIItem.MarkerType.CustomImage);
                poiItem.setCustomSelectedImageResourceId(R.drawable.map_pin_red);
                poiItem.setCustomImageAutoscale(false);
                poiItem.setCustomImageAnchor(0.5f, 1.0f);

                pharmMapView.addPOIItem(poiItem);

            }
        });
    }
    protected void init(){
        pharmname = (TextView) findViewById(R.id.pharmname);
        pharmaddr = (TextView)findViewById(R.id.pharmaddr);
        pharmtel = (TextView)findViewById(R.id.pharmtel);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        pharmMapView .setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
        pharmMapView .setShowCurrentLocationMarker(false);
    }

    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint currentLocation, float accuracyInMeters) {
        MapPoint.GeoCoordinate mapPointGeo = currentLocation.getMapPointGeoCoord();
        Log.i(LOG_TAG, String.format("MapView onCurrentLocationUpdate (%f,%f) accuracy (%f)", mapPointGeo.latitude, mapPointGeo.longitude, accuracyInMeters));
    }


    /*@Override
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

            case MENU_REVERSE_GEO: {
                mReverseGeoCoder = new MapReverseGeoCoder(MapApiConst.DAUM_MAPS_ANDROID_APP_API_KEY, mMapView.getMapCenterPoint(), CardiInfoActivity.this, CardiInfoActivity.this);
                mReverseGeoCoder.startFindingAddress();
                return true;
            }
        }

        return onOptionsItemSelected(item);

    }
*/
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
        Toast.makeText(PharmInfoActivity.this, "Reverse Geo-coding : " + result, Toast.LENGTH_SHORT).show();
    }
}