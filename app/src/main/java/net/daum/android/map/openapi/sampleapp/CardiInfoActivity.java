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

public class CardiInfoActivity extends FragmentActivity implements MapView.CurrentLocationEventListener, MapReverseGeoCoder.ReverseGeoCodingResultListener {

    private static final String LOG_TAG = "CardiInfoActivity";
    private MapView cardiMapView;
    private MapReverseGeoCoder mReverseGeoCoder = null;
    private TextView cardiname,cardiaddr,carditel,cardimanager,cardimanagertel;
    private String cname,caddr,ctel,cmanager,cmanagertel,clat,clon;
    private Button btn_map;
    private Double doubleclat,doubleclon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_cardi);
        Intent intent = getIntent();
        init();
        btn_map = (Button)findViewById(R.id.btn_infocardi_map);
        cname = intent.getStringExtra("C_name");
        caddr = intent.getStringExtra("C_location");
        ctel = intent.getStringExtra("C_tel");
        cmanager = intent.getStringExtra("C_manager");
        cmanagertel = intent.getStringExtra("C_manager_tel");

        clat = intent.getStringExtra("lat");
        clon = intent.getStringExtra("lon");

        doubleclat = Double.valueOf(clat).doubleValue();
        doubleclon = Double.valueOf(clon).doubleValue();

        //Log.e("위도","  "+ doubleclat);

        cardiname.setText(cname);
        cardiaddr.setText(caddr);
        carditel.setText(ctel);
        cardimanager.setText(cmanager);
        cardimanagertel.setText(cmanagertel);


        cardiMapView  = (MapView) findViewById(R.id.cardi_mapview);
        cardiMapView .setDaumMapApiKey(MapApiConst.DAUM_MAPS_ANDROID_APP_API_KEY);
        cardiMapView .setCurrentLocationEventListener(this);
        btn_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //cardiMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
                cardiMapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(doubleclat,doubleclon), 2, true);
                MapPOIItem poiItem = new MapPOIItem();
                poiItem.setItemName(cname);
                poiItem.setTag(1);
                MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(doubleclat,doubleclon);
                poiItem.setMapPoint(mapPoint);
                poiItem.setMarkerType(MapPOIItem.MarkerType.CustomImage);
                poiItem.setCustomImageResourceId(R.drawable.map_pin_blue);
                poiItem.setSelectedMarkerType(MapPOIItem.MarkerType.CustomImage);
                poiItem.setCustomSelectedImageResourceId(R.drawable.map_pin_red);
                poiItem.setCustomImageAutoscale(false);
                poiItem.setCustomImageAnchor(0.5f, 1.0f);

                cardiMapView.addPOIItem(poiItem);

            }
        });
    }
    protected void init(){
        cardiname = (TextView) findViewById(R.id.cardiname);
        cardiaddr = (TextView)findViewById(R.id.cardiaddr);
        carditel = (TextView)findViewById(R.id.carditel);
        cardimanager = (TextView)findViewById(R.id.cardimanager);
        cardimanagertel = (TextView)findViewById(R.id.cardimanagertel);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cardiMapView .setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
        cardiMapView .setShowCurrentLocationMarker(false);
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
        Toast.makeText(CardiInfoActivity.this, "Reverse Geo-coding : " + result, Toast.LENGTH_SHORT).show();
    }
}