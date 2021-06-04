package com.example.push_notifications_ass;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {


    RequestQueue requestQueue;
    EditText userNameSignUp, phoneNumberSignUp, emailSignUp, passwordSignUp;
    Button btnSignUp;
    TextView tvSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        requestQueue = Volley.newRequestQueue(getApplicationContext());


        userNameSignUp = (EditText) findViewById(R.id.ed_user_name_signup);
        emailSignUp = (EditText) findViewById(R.id.ed_email_signup);
        passwordSignUp = (EditText) findViewById(R.id.ed_password_signup);
        phoneNumberSignUp = (EditText) findViewById(R.id.ed_phone_number_signup);


        btnSignUp = (Button) findViewById(R.id.btn_sign_up);
        tvSignIn =findViewById(R.id.tv_sign_in);



        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewUser();

            }
        });

    }


    private void createNewUser() {
        String email = this.emailSignUp.getText().toString();
         String password = passwordSignUp.getText().toString();
         String userName = userNameSignUp.getText().toString();
         String phoneNumber = phoneNumberSignUp.getText().toString();
        Log.e("TAG", email);
        Log.e("TAG", userName);
        Log.e("TAG",password );

        if (email.isEmpty()) {
            this.emailSignUp.setError("Enter your email");

            return;
        }

        if (password.isEmpty()) {
            passwordSignUp.setError("Enter your password");

            return;
        }

        String URL="https://mcc-users-api.herokuapp.com/add_new_user";


        StringRequest stringRequest = new StringRequest(Request.Method.POST,URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.d("TAG", "onResponse: "+jsonObject.toString());
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
                params.put("userName", userName);
                params.put(" phoneNumber", phoneNumber);
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };

        requestQueue.add(stringRequest);

    }
}