package net.daum.android.map.openapi.sampleapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by seung on 2016-10-26.
 */
public class SignupFragment extends Fragment {
    private Context context;
    public static final String ARG_PLANET_NUMBER = "planet_number";

    public SignupFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.join, container, false);
        context = getActivity();

        Button signup_btn = (Button) rootView.findViewById(R.id.btn_dia_join);
        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("가입", "완료");
            }

        });
        return rootView;
    }
}
