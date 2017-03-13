package net.daum.android.map.openapi.sampleapp;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.apache.http.HttpResponse;
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
import java.util.List;

public class LoginFragment extends Fragment {

    Boolean CheckEditText ;
    private String guserid, gpassword;
    Activity root = getActivity();
    public static final String ARG_PLANET_NUMBER = "planet_number";
    private EditText userid, password;
    private String DataParseUrl = "http://203.252.22.161:8088/login2.php";

    public LoginFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        userid = (EditText) rootView.findViewById(R.id.login_id);
        password = (EditText) rootView.findViewById(R.id.login_pw);

        //회원가입 클릭
        Button btn_join = (Button) rootView.findViewById(R.id.btn_join);
        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent joinDiaAct = new Intent(getActivity(), JoinActivity.class);
                startActivity(joinDiaAct);
            }
        });

        //로그인 클릭
        Button btn_login = (Button) rootView.findViewById(R.id.login);
        btn_login.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                GetCheckEditTextIsEmptyOrNot();

                if (CheckEditText) {
                    new AsyncLogin().execute(guserid, gpassword);
                } else {
                    Toast.makeText(getActivity(), "빈 항목을 입력해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //아이디찾기 클릭
        Button btn_find_id = (Button) rootView.findViewById(R.id.btn_find_id);
        btn_find_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent findIdDiaAct = new Intent(getActivity(), FindIdActivity.class);
                startActivity(findIdDiaAct);
               /* final Dialog diaFindId = new Dialog(getActivity());
                diaFindId.setContentView(R.layout.find_id);
                diaFindId.setTitle("아이디 찾기");
                diaFindId.findViewById(R.id.btn_id_close).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

                diaFindId.findViewById(R.id.btn_id_close).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        final Dialog idView = new Dialog(getActivity());
                        idView.setContentView(R.layout.find_id);
                        idView.setTitle("비밀번호");
                    }
                });
                diaFindId.show();*/

            }
        });

        //비밀번호찾기 클릭
        Button btn_find_pw = (Button) rootView.findViewById(R.id.btn_find_pw);
        btn_find_pw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent findPwDiaAct = new Intent(getActivity(), FindPwActivity.class);
                startActivity(findPwDiaAct);
               /* final Dialog diaFindPw = new Dialog(getActivity());
                diaFindPw.setContentView(R.layout.find_pw);
                diaFindPw.setTitle("비밀번호 찾기");
                diaFindPw.findViewById(R.id.btn_pw_close).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        diaFindPw.cancel();
                    }
                });

                diaFindPw.findViewById(R.id.btn_pw_find).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        diaFindPw.dismiss();
                    }
                });
                diaFindPw.show();*/
            }

        });
        return rootView;
    }

//    public void checkLogin(View arg0) {
//        final String id = userid.getText().toString();
//        final String pw = password.getText().toString();
//
//        new AsyncLogin().execute(id, pw);
//    }

    public void GetCheckEditTextIsEmptyOrNot(){
        guserid = userid.getText().toString();
        gpassword = password.getText().toString();

        if(TextUtils.isEmpty(guserid) || TextUtils.isEmpty(gpassword)){
            CheckEditText = false;
        }
        else{
            CheckEditText = true;
        }
    }

    private class AsyncLogin extends AsyncTask<String, String, String> {
        ProgressDialog dialog = new ProgressDialog(getActivity());
        HttpURLConnection con;
        URL url = null;

        protected void onPreExecute() {
            super.onPreExecute();

            dialog.setMessage("로그인중입니다");
            dialog.setCancelable(false);
            dialog.show();
        }

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

                Uri.Builder builder = new Uri.Builder().appendQueryParameter("U_id", params[0]).appendQueryParameter("U_password", params[1]);
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

                if(response == HttpURLConnection.HTTP_OK){
                    InputStream input = con.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    return(result.toString());
                }
                else{
                    return("로그인 실패");
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

            if(result.equalsIgnoreCase("true")){
                Toast.makeText(getActivity(), "로그인 성공", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
            else if(result.equalsIgnoreCase("false")){
                Toast.makeText(getActivity(), "입력한 정보를 다시 확인하세요", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
