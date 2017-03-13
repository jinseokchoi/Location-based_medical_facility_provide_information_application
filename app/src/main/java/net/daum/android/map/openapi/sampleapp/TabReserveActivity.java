package net.daum.android.map.openapi.sampleapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import net.daum.android.map.openapi.sampleapp.ListView.IconTextItem;
import net.daum.android.map.openapi.sampleapp.ListView.IconTextListAdapter;

/**
 * Created by seung on 2016-10-26.
 */
public class TabReserveActivity extends Fragment {

    ListView listView1;
    IconTextListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.tab_fragment_reserve, container, false);

        // 리스트뷰 객체 참조
        listView1 = (ListView) rootView.findViewById(R.id.listView);
        adapter = new IconTextListAdapter(getActivity(), 1);    // 두번째 인자는 탭 구분 (예약페이지)

        //Resources res = getResources();
        adapter.addItem(new IconTextItem("한신 병원", "경기도 화성시 양산동", "2016년 12월 5일 7시 50분"));
        adapter.addItem(new IconTextItem("서울 병원", "서울시 용산구 청파동", "2016년 1월 27일 13시 30분"));
        adapter.addItem(new IconTextItem("수원 병원", "경기도 수원시 정자동", "2017년 8월 13일 14시 00분"));
        adapter.addItem(new IconTextItem("강남 병원", "서울시 강남구 양재동", "2016년 9월 14일 5시 30분"));
        adapter.addItem(new IconTextItem("동탄 병원", "경기도 동탄시 매탄동", "2013년 4월 15일 11시 30분"));
        adapter.addItem(new IconTextItem("부산 병원", "부산시 팔달구 인계동", "2011년 8월 22일 8시 00분"));
        adapter.addItem(new IconTextItem("인천 병원", "인천시 연수구 조원동", "2016년 6월 5일 11시 10분"));
        adapter.addItem(new IconTextItem("송파 병원", "서울시 송파구 잠실동", "2018년 1월 20일 3시 30분"));
        adapter.addItem(new IconTextItem("서초 병원", "서울시 서초구 방배동", "2016년 2월 3일 20시 20분"));
        listView1.setAdapter(adapter);

        // 리스트뷰의 한 객체 클릭 시 이벤트
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                IconTextItem curItem = (IconTextItem) adapter.getItem(position);

                String[] curData = curItem.getData();
                //Toast.makeText(getActivity(), "Selected : " + curData[0], Toast.LENGTH_LONG).show();

                Intent i_hos_info = new Intent(getActivity(),HosInfoActivity.class);
                i_hos_info.putExtra("hosname",curData[0].toString());
                i_hos_info.putExtra("hosaddr",curData[1].toString());

                startActivity(i_hos_info);



                /*i_hos_info.putExtra("hosname",curData[0].toString());
                i_hos_info.putExtra("hosaddr",curData[1].toString());
                setResult(RESULT_OK,i_hos_info);*/


            }
        });

        final EditText input = (EditText) rootView.findViewById(R.id.input);
        // 검색창에 입력되는 텍스트에 변화가 있을 때
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("input", "입력중 - " + input.getText().toString().trim());
                //adapter = new IconTextListAdapter(getActivity(), 1);
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }

        });
        return rootView;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
//        TextView name_view = (TextView)findViewById(R.id.name_view);
//        TextView digit_view = (TextView)findViewById(R.id.digit_view);
//            if(resultCode==RESULT_OK) // 액티비티가 정상적으로 종료되었을 경우
//            {
//                 if(requestCode==1) // InformationInput에서 호출한 경우에만 처리합니다.
//                 {               // 받아온 이름과 전화번호를 InformationInput 액티비티에 표시합니다.
//                          name_view.setText(data.getStringExtra("hosname"));
//                          digit_view.setText(data.getStringExtra("hosaddr"));
//                 }
//            }

    }
}
