package net.daum.android.map.openapi.sampleapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by seung on 2016-10-26.
 */
public class JoinActivity extends Activity {
    String DataParseUrl = "http://203.252.22.161:8088/register2.php";
    Boolean CheckEditText ;
    private String guserid, gusername, gpassword, gtel, gbirth, gemail;
    private EditText userid, username, password, tel, email;
    private EditText mDateDisplay;
    private View dateView;
    private ImageButton mDate;
    private int mYear,mMonth,mDay;
    static  final int DATE_DOALOG_ID = 0; //정적변수 선언


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join);

        userid = (EditText) findViewById(R.id.userid);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        tel = (EditText) findViewById(R.id.tel);
        email = (EditText) findViewById(R.id.email);


        //날짜 피커
        mDateDisplay = (EditText) findViewById(R.id.mb_date_display);
        mDate = (ImageButton) findViewById(R.id.mb_date);

        findViewById(R.id.btn_dia_join).setOnClickListener(clickListener);
        findViewById(R.id.btn_join_close).setOnClickListener(clickListener);

        mDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dateView = v;
                showDialog(DATE_DOALOG_ID);
            }
        });

        final Calendar objTime = Calendar.getInstance();
        mYear = objTime.get(Calendar.YEAR);
        mMonth = objTime.get(Calendar.MONTH);
        mDay = objTime.get(Calendar.DAY_OF_MONTH);

        updateDisplay();
    }


    Button.OnClickListener clickListener = new View.OnClickListener(){
        public void onClick(View v){
            switch (v.getId()) {
                case R.id.btn_dia_join:
                    GetCheckEditTextIsEmptyOrNot();

                    if (CheckEditText) {
                        SendDataToServer(guserid, gusername, gpassword, gemail, gbirth, gtel);
                        Toast.makeText(JoinActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(JoinActivity.this, "빈 항목을 입력해주세요", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.btn_join_close:
                    finish();
                    break;
            }
        }
    };

    public void GetCheckEditTextIsEmptyOrNot(){
        guserid = userid.getText().toString();
        gusername = username.getText().toString();
        gpassword = password.getText().toString();
        gbirth = mDateDisplay.getText().toString();
        gtel = tel.getText().toString();
        gemail = email.getText().toString();

        if(TextUtils.isEmpty(guserid) || TextUtils.isEmpty(gusername) || TextUtils.isEmpty(gpassword) || TextUtils.isEmpty(gbirth) || TextUtils.isEmpty(gtel) || TextUtils.isEmpty(gemail)){
            CheckEditText = false;
        }

        else{
            CheckEditText = true;
        }
    }

    public void SendDataToServer(final String userid, final String username, final String password, final String email, final String birth, final String tel){
        class SendPostRequestAsyncTask extends AsyncTask<String, Void, String> {
            protected String doInBackground(String... params){
                String puserid = userid;
                String pusername = username;
                String ppassword = password;
                String pemail = email;
                String pbirth = birth;
                String ptel = tel;

                List<NameValuePair> uservalues = new ArrayList<NameValuePair>();

                uservalues.add(new BasicNameValuePair("U_id", puserid));
                uservalues.add(new BasicNameValuePair("U_name", pusername));
                uservalues.add(new BasicNameValuePair("U_password", ppassword));
                uservalues.add(new BasicNameValuePair("U_email", pemail));
                uservalues.add(new BasicNameValuePair("U_birth", pbirth));
                uservalues.add(new BasicNameValuePair("U_tel", ptel));

                try{
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(DataParseUrl);
                    httpPost.setEntity(new UrlEncodedFormEntity(uservalues, "UTF-8"));
                    HttpResponse response = httpClient.execute(httpPost);
                    HttpEntity entity = response.getEntity();
                }catch (ClientProtocolException e) {
                }
                catch (IOException e) {
                }
                return "db insert";
            }

            protected void onPostExecute(String result){
                super.onPostExecute(result);
            }
        }

        SendPostRequestAsyncTask sendPostRequestAsyncTask = new SendPostRequestAsyncTask();
        sendPostRequestAsyncTask.execute(userid, username, password, email, birth, tel);
    }

    private void updateDisplay()
    {
        // Month is 0 based so add 1
        mDateDisplay.setText(new StringBuilder().append(mYear).append("-").append(mMonth + 1).append("-").append(mDay));

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

    @Override
    protected void onStart(){
        super.onStart();
    }

}
