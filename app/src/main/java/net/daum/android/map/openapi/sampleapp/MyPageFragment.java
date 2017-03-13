package net.daum.android.map.openapi.sampleapp;


import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by seung on 2016-10-16.
 */


//http://comostudio.tistory.com/107 fragment 설명
//http://stackoverflow.com/questions/20469877/adding-tab-inside-fragment-in-android
public class MyPageFragment extends Fragment {
    private FragmentTabHost mTabHost;

    //Mandatory Constructor
    public MyPageFragment(){ }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.mypage,container, false);
        mTabHost = (FragmentTabHost)rootView.findViewById(android.R.id.tabhost);
        //mTabHost.setup(getActivity().getApplicationContext(), getChildFragmentManager(), R.id.realtabcontent);
        mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.realtabcontent);


    mTabHost.addTab(mTabHost.newTabSpec("fragmentb").setIndicator("마이 페이지"),
                TabMyPageActivity.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("fragmentc").setIndicator("별점 목록"),
                TabStarRatingActivity.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("fragmentd").setIndicator("예약 목록"),
                TabReserveActivity.class, null);

        return rootView;

    }

}















