package com.shubzz.wireparent;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.roger.catloadinglibrary.CatLoadingView;

import org.json.JSONException;
import org.json.JSONObject;


public class Login extends AppCompatActivity {

    private ImageView logo, ivSignIn, btnTwitter;
    private AutoCompleteTextView email, password;
    private TextView forgotPass, signUp;
    private Button btnSignIn;
    private static final String KEY_STATUS = "status";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_FULL_NAME = "full_name";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_EMPTY = "";
    private String login_url = "http://192.168.43.98/wire/login.php";
    private SessionHandler session;
    CatLoadingView vi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initializeGUI();


        session = new SessionHandler(getApplicationContext());
        if (session.isLoggedIn()) {
            loadMainactivity();
        }
        //user = firebaseAuth.getCurrentUser();

//        if(user != null) {
//            finish();
//            startActivity(new Intent(Login.this,MainActivity.class));
//        }

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String inEmail = email.getText().toString().trim();
                String inPassword = password.getText().toString().trim();

                //disp();
                if (validateInput(inEmail, inPassword)) {
                    signUser(inEmail, inPassword);
                }

            }
        });


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, SignUp.class));
            }
        });

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, PwReset.class));
            }
        });

    }

    private void loadMainactivity() {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }

    private void displayLoader() {
        vi = new CatLoadingView();
        vi.show(getSupportFragmentManager(), "");
        vi.setCanceledOnTouchOutside(false);
    }


    public void signUser(final String email, String password) {
        displayLoader();
        JSONObject request = new JSONObject();
        try {
            //Populate the request parameters
            request.put(KEY_USERNAME, email);
            request.put(KEY_PASSWORD, password);
            Log.d("Request", request.toString());

        } catch (JSONException e) {
            e.printStackTrace();  //parent app
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest(Request.Method.POST, login_url, request, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                vi.dismiss();
                try {
                    //Check if user got logged in successfully

                    if (response.getInt(KEY_STATUS) == 0) {
                        session.loginUser(email, response.getString(KEY_FULL_NAME), response.getString("uq_key1"), response.getString("uq_key2"), response.getString("uq_key3"), response.getString("uq_key4"));
                        Log.d("UQKEY", response.getString("uq_key1"));
                        //Toast.makeText(getApplicationContext(),"VOLLAAA IT WORKED",Toast.LENGTH_LONG).show();
                        loadMainactivity();

                    } else {
                        Toast.makeText(getApplicationContext(),
                                response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                vi.dismiss();

                //Display error message whenever an error occurs

                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
        MySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
//        progressDialog.setMessage("Verificating...");
//        progressDialog.show();
//
//        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if(task.isSuccessful()){
//                    progressDialog.dismiss();
//                    Toast.makeText(LoginActivity.this,"Login Successful",Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
//                }
//                else{
//                    progressDialog.dismiss();
//                    Toast.makeText(LoginActivity.this,"Invalid email or password",Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

    }


    private void initializeGUI() {

        logo = findViewById(R.id.ivLogLogo);
        ivSignIn = findViewById(R.id.ivSignIn);
        btnTwitter = findViewById(R.id.ivFacebook);
        email = findViewById(R.id.atvEmailLog);
        password = findViewById(R.id.atvPasswordLog);
        forgotPass = findViewById(R.id.tvForgotPass);
        signUp = findViewById(R.id.tvSignIn);
        btnSignIn = findViewById(R.id.btnSignIn);
//        progressDialog = new ProgressDialog(this);
//
//        firebaseAuth = FirebaseAuth.getInstance();

    }


    public boolean validateInput(String inemail, String inpassword) {
        if (inemail.isEmpty()) {
            email.setError("Email field is empty.");
            return false;
        }
        if (inpassword.isEmpty()) {
            password.setError("Password is empty.");
            return false;
        }

        return true;
    }

}