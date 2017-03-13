package net.daum.android.map.openapi.sampleapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import org.json.JSONArray;
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
import java.util.Calendar;

/**
 * Created by Jinseok on 2016-10-29.
 */
public class FindIdActivity extends Activity{

    String DataParseUrl = "http://203.252.22.161:8088/Find_id.php";
    Boolean CheckEditText ;
    private EditText username, birth, tel;
    private String gusername, gbirth, gtel;
    private ImageButton findid_mDate;
    private int mYear,mMonth,mDay;
    static  final int DATE_DOALOG_ID = 0; //정적변수 선언
    private View dateView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_id);

        username = (EditText) findViewById(R.id.find_id_input_name);
        tel = (EditText) findViewById(R.id.find_id_input_tel);
        birth = (EditText) findViewById(R.id.find_id_input_birth);
        findid_mDate = (ImageButton) findViewById(R.id.find_id_mb_date);

        findid_mDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dateView = v;
                showDialog(DATE_DOALOG_ID);
            }
        });

        calendarInit();
        updateDisplay();
        Button find = (Button)findViewById(R.id.btn_id_find);
        find.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                GetCheckidEditTextIsEmptyOrNot();

                if (CheckEditText) {
                    new AsyncFid().execute(gusername, gbirth, gtel);
                } else {
                    Toast.makeText(FindIdActivity.this, "빈 항목을 입력해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button cancel = (Button)findViewById(R.id.btn_id_close);
        cancel.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                finish();
            }
        });
    }


    private void calendarInit(){
        final Calendar objTime = Calendar.getInstance();
        mYear = objTime.get(Calendar.YEAR);
        mMonth = objTime.get(Calendar.MONTH);
        mDay = objTime.get(Calendar.DAY_OF_MONTH);

    }

    private void updateDisplay()
    {
        // Month is 0 based so add 1
        birth.setText(new StringBuilder().append(mYear).append("-").append(mMonth + 1).append("-").append(mDay));

    }
    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                    // TODO Auto-generated method stub
                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;

                    updateDisplay();
                }

            };

    //Dialog 생성 메소드
    @Override
    protected Dialog onCreateDialog(int id){
        switch(id){
            case DATE_DOALOG_ID:
                return new DatePickerDialog(this, mDateSetListener, mYear, mMonth, mDay);
        }
        return null;
    }
    public void GetCheckidEditTextIsEmptyOrNot(){
        gusername = username.getText().toString();
        gbirth= birth.getText().toString();
        gtel = tel.getText().toString();

        if(TextUtils.isEmpty(gusername) || TextUtils.isEmpty(gbirth) || TextUtils.isEmpty(gtel)){
            CheckEditText = false;
        }
        else{
            CheckEditText = true;
        }
    }

    private class AsyncFid extends AsyncTask<String, String, String> {
        ProgressDialog dialog = new ProgressDialog(FindIdActivity.this);
        HttpURLConnection con;
        URL url = null;

        protected void onPreExecute() {
            super.onPreExecute();

            dialog.setMessage("아이디를 찾는중입니다");
            dialog.setCancelable(false);
            dialog.show();
        }

        protected String doInBackground(String... params) {

            try {
                url = new URL(DataParseUrl); //url 화
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "Exception Error";
            }

            try {
                con = (HttpURLConnection) url.openConnection(); //url을 연결한 객체 생성
                con.setReadTimeout(100000);
                con.setConnectTimeout(100000);
                con.setRequestMethod("POST"); //post 방식 통신
                con.setDoInput(true); //읽기모드지정
                con.setDoOutput(true); //쓰기모드지정

                //문자열을 담기 위한 객체
                Uri.Builder builder = new Uri.Builder().appendQueryParameter("U_name", params[0]).appendQueryParameter("U_birth", params[1]).appendQueryParameter("U_tel", params[2]);
                String query = builder.build().getEncodedQuery();

                OutputStream os = con.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                con.connect();
            } catch (IOException e) {
                e.printStackTrace();
                return "Exception Error";
            }

            try{
                int response = con.getResponseCode();

                //개행문자 \r\n
                if(response == HttpURLConnection.HTTP_OK){
                    InputStream input = con.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    return(result.toString());
                }
                else{
                    return("아이디 찾기 실패");
                }
            }
            catch(IOException e){
                e.printStackTrace();
                return "Exceiption Error";
            }
            finally{
                con.disconnect();
            }
        }

        protected void onPostExecute(String result){
            dialog.dismiss();
            try {
                JSONArray jArray = new JSONArray(result);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                }
            }
            catch(Exception e){

            }

            if(result != null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(FindIdActivity.this);
                builder.setTitle("아이디");
                builder.setMessage(result);
                builder.setCancelable(false);
                builder.setNegativeButton("닫기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }

        }

    }

}