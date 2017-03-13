package net.daum.android.map.openapi.sampleapp;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import net.daum.android.map.openapi.sampleapp.demos.SearchDemoActivity;


/**
 * Created by seung on 2016-09-20.
 */

public class HomeFragment extends Fragment {
    public static final String ARG_NUMBER = "Fragment_number";
    Button search_hos,search_pharm,search_emer,search_cardi,location_search;

    public HomeFragment() {
        // Empty constructor required for fragment subclasses
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
/////////
        Button search_demo = (Button) rootView.findViewById(R.id.btn_search_demo);
        search_demo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchdemoAct = new Intent(getActivity(), SearchDemoActivity.class);
                startActivity(searchdemoAct);
            }
        });
  ///////////
        search_hos = (Button) rootView.findViewById(R.id.btn_search_hos);
        search_hos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchHosAct = new Intent(getActivity(), SearchHosActivity.class);
                startActivity(searchHosAct);
            }
        });

        search_pharm = (Button) rootView.findViewById(R.id.btn_search_pharm);
        search_pharm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchPharmAct = new Intent(getActivity(), SearchPharmActivity.class);
                startActivity(searchPharmAct);
            }
        });
        search_emer = (Button) rootView.findViewById(R.id.btn_search_emer);
        search_emer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchEmerAct = new Intent(getActivity(), SearchEmerActivity.class);
                startActivity(searchEmerAct);
            }
        });
        search_cardi = (Button) rootView.findViewById(R.id.btn_search_cardi);
        search_cardi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchCardiAct = new Intent(getActivity(), SearchCardiActivity.class);
                startActivity(searchCardiAct);
            }
        });
        location_search = (Button) rootView.findViewById(R.id.btn_location_search);
        location_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent LocationAct = new Intent(getActivity(), LocationSearchActivity.class);
                startActivity(LocationAct);
            }
        });

        return rootView;

    }


}
