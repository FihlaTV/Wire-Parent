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

public class SignUp extends AppCompatActivity {
    private static final String KEY_STATUS = "status";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_FULL_NAME = "full_name";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_EMPTY = "";
    private String register_url = "http://34.93.78.17/project/register.php";
    private SessionHandler session;
    private ImageView logo, joinus;
    private AutoCompleteTextView username, email, password;
    private Button signup;
    private TextView signin;
    private CatLoadingView vi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new SessionHandler(getApplicationContext());
        setContentView(R.layout.activity_signup);

        initializeGUI();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String inputName = username.getText().toString().trim();
                final String inputPw = password.getText().toString().trim();
                final String inputEmail = email.getText().toString().trim();

                if (validateInput(inputName, inputPw, inputEmail))
                    registerUser(inputName, inputPw, inputEmail);

            }
        });


        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUp.this, Login.class));
            }
        });

    }


    private void initializeGUI() {

        logo = findViewById(R.id.ivRegLogo);
        joinus = findViewById(R.id.ivJoinUs);
        username = findViewById(R.id.atvUsernameReg);
        email = findViewById(R.id.atvEmailReg);
        password = findViewById(R.id.atvPasswordReg);
        signin = findViewById(R.id.tvSignIn);
        signup = findViewById(R.id.btnSignUp);

    }

    private void displayLoader() {
        vi = new CatLoadingView();
        vi.show(getSupportFragmentManager(), "");
        vi.setCanceledOnTouchOutside(false);

    }

    private void registerUser(final String inputName, final String inputPw, final String inputEmail) {
        displayLoader();
        JSONObject request = new JSONObject();
        try {
            //Populate the request parameters
            request.put(KEY_USERNAME, inputEmail); //request.put(KEY_USERNAME, username);
            request.put(KEY_PASSWORD, inputPw); //request.put(KEY_PASSWORD, password);
            request.put(KEY_FULL_NAME, inputName); //request.put(KEY_FULL_NAME, fullName);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest(Request.Method.POST, register_url, request, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                vi.dismiss();
                try {
                    //Check if user got registered successfully
                    if (response.getInt(KEY_STATUS) == 0) {
                        //Set the user session
                       // email.setText(response.toString());
                        //Log.d("Response",response.toString());
                        session.loginUser(inputEmail, inputName, response.getString("uq_key1"), response.getString("uq_key2"), response.getString("uq_key3"), response.getString("uq_key4"));
                        //session.loginUser(inputEmail,inputName);
                        //Toast.makeText(getApplicationContext(), "VOLAAAAAAA WORKEDDDD", Toast.LENGTH_LONG).show();
                        loadDashboard();

                    } else if (response.getInt(KEY_STATUS) == 1) {
                        //Display error message if username is already existsing
                        email.setError("Username already taken!");
                        email.requestFocus();

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
                        error.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("JSONError", error.getMessage());

            }
        });
            MySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
//
//        progressDialog.setMessage("Verificating...");
//        progressDialog.show();
//
//
//        firebaseAuth.createUserWithEmailAndPassword(inputEmail,inputPw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if(task.isSuccessful()){
//                    progressDialog.dismiss();
//                    sendUserData(inputName, inputPw);
//                    Toast.makeText(RegistrationActivity.this,"You've been registered successfully.",Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(RegistrationActivity.this,MainActivity.class));
//                }
//                else{
//                    progressDialog.dismiss();
//                    Toast.makeText(RegistrationActivity.this,"Email already exists.",Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

    }

    private void loadDashboard() {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }

    private void sendUserData(String username, String password) {

//        firebaseDatabase = FirebaseDatabase.getInstance();
//        DatabaseReference users = firebaseDatabase.getReference("users");
//        UserProfile user = new UserProfile(username, password);
//        users.push().setValue(user);

    }

    private boolean validateInput(String inName, String inPw, String inEmail) {

        if (inName.isEmpty()) {
            username.setError("Username is empty.");
            return false;
        }
        if (inPw.isEmpty()) {
            password.setError("Password is empty.");
            return false;
        }
        if (inEmail.isEmpty()) {
            email.setError("Email is empty.");
            return false;
        }

        return true;
    }


}
