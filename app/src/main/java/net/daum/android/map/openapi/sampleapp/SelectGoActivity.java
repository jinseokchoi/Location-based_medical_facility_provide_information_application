package net.daum.android.map.openapi.sampleapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

/**
 * Created by seung on 2016-11-01.
 */
public class SelectGoActivity extends Activity{

    RadioButton rd,rb_foot,rb_publictransit,rb_car;
    RadioGroup rg;
    private String maplat,maplon,current_lat,current_lon;
    private double d_maplat,d_maplon,d_current_lat,d_current_lon;
    Intent idaum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectgo);
        Intent in = getIntent();
        maplat = in.getStringExtra("maplat");
        maplon = in.getStringExtra("maplon");
        current_lat = in.getStringExtra("current_lat");
        current_lon = in.getStringExtra("current_lon");

        d_maplat = Double.valueOf(maplat).doubleValue();
        d_maplon = Double.valueOf(maplon).doubleValue();
        d_current_lat = Double.valueOf(current_lat).doubleValue();
        d_current_lon = Double.valueOf(current_lon).doubleValue();



        idaum = new Intent();
        idaum.setAction(Intent.ACTION_VIEW);
        idaum.addCategory(Intent.CATEGORY_DEFAULT);
        idaum.addCategory(Intent.CATEGORY_BROWSABLE);
        // idaum.setData(Uri.parse("daummaps://search?q="+ItemName+"&p=mapPOIItemGeo.latitude,mapPOIItemGeo.longitude"));
        /*idaum.setData(Uri.parse("daummaps://route?sp="+current_lat+","+current_lon+"&ep="+maplat+","+maplon+"&by=PUBLICTRANSIT"));
        LocationSearchActivity.this.startActivity(idaum);*/
        rg = (RadioGroup) findViewById(R.id.rg) ;
     /*   rb_foot = (RadioButton) findViewById(R.id.foot);
        rb_publictransit = (RadioButton) findViewById(R.id.publictransit);
        rb_car = (RadioButton) findViewById(R.id.car);*/


        rd = (RadioButton) findViewById(rg.getCheckedRadioButtonId());

        Button find = (Button) findViewById(R.id.btn_select_go);
        find.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.e("dfsf","sdfsf");
                switch (rg.getCheckedRadioButtonId()){
                    case R.id.foot:
                        idaum.setData(Uri.parse("daummaps://route?sp="+d_current_lat+","+d_current_lon+"&ep="+d_maplat+","+d_maplon+"&by=FOOT"));
                        break;
                    case R.id.publictransit:
                        idaum.setData(Uri.parse("daummaps://route?sp="+d_current_lat+","+d_current_lon+"&ep="+d_maplat+","+d_maplon+"&by=PUBLICTRANSIT"));
                        break;
                    case R.id.car:
                        idaum.setData(Uri.parse("daummaps://route?sp="+d_current_lat+","+d_current_lon+"&ep="+d_maplat+","+d_maplon+"&by=CAR"));
                        break;
                    default:
                }

            }
        });

        Button cancel = (Button) findViewById(R.id.btn_select_close);
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }
}