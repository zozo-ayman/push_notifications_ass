package com.example.push_notifications_ass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignInActivity extends AppCompatActivity {


    RequestQueue requestQueue;
    EditText emailSignIn, passwordSignIn;
    Button btnSignIn;
    String token;
    TextView tvSignUp;
    String email;
    String passwordSign;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        emailSignIn = (EditText) findViewById(R.id.ed_email_signin);
        passwordSignIn = (EditText) findViewById(R.id.ed_password_signin);
        btnSignIn = (Button) findViewById(R.id.btn_sign_in);

        tvSignUp =findViewById(R.id.tv_sign_up);

        //
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        //
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getReqToken();
                CheckUser();
                UpdateRegestration();

            }
        });
    }


    //
    private void getReqToken() {

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if(!task.isSuccessful()){
                    Log.e("msg","Failed to get token"+task.getException());
                    return;
                }

                token = task.getResult();
                Log.d("msg","token : "+token);
            }
        });
    }

    //
    private void CheckUser() {
        email = emailSignIn.getText().toString();
        passwordSign = passwordSignIn.getText().toString();

        if (TextUtils.isEmpty(email)) {
            emailSignIn.setError("Enter your email");
            emailSignIn.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(passwordSign)) {
            passwordSignIn.setError("Enter password");
            passwordSignIn.requestFocus();
            return;
        }
        String URL="https://mcc-users-api.herokuapp.com/login";


        StringRequest stringRequest = new StringRequest(Request.Method.POST,URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject objres = new JSONObject(response);
                            Log.d("TAG", "onResponse: "+objres.toString());
                        } catch (JSONException e) {
                            Log.d("TAG", "Server Error ");
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();

            }
        }) {

            @Override
            protected Map<String, String> getParams(){
                HashMap<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", passwordSign);
                return params;
            }
        };
        emailSignIn.setText("");
        passwordSignIn.setText("");
        requestQueue.add(stringRequest);
    }



    private void UpdateRegestration() {
        String URL="https://mcc-users-api.herokuapp.com/add_reg_token";

        StringRequest stringRequest = new StringRequest(Request.Method.PUT,URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject objres = new JSONObject(response);
                            Log.d("TAG", "SuccessOnResponse: "+objres.toString());
                        } catch (JSONException e) {
                            Log.d("TAG", "Server Error ");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
            }
        }) {

            //
            @Override
            protected Map<String, String> getParams(){
                HashMap<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", passwordSign);
                params.put("token",token);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}