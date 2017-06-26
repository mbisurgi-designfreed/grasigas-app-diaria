package com.designfreed.grasigas_app_diaria;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.designfreed.grasigas_app_diaria.model.Chofer;
import com.designfreed.grasigas_app_diaria.service.AuthService;
import com.designfreed.grasigas_app_diaria.service.ChoferService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private EditText dniField;
    private EditText passwordField;
    private Button loginBtn;

    private ProgressDialog mProgress;

    private ChoferService service;

    private static final String TAG = "LoginActivity";
    private static final String SERVER_URL = "https://intense-tor-11265.herokuapp.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        dniField = (EditText) findViewById(R.id.dni_field);
        passwordField = (EditText) findViewById(R.id.password_field);

        loginBtn = (Button) findViewById(R.id.login_btn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dniSignIn();
            }
        });

        mProgress = new ProgressDialog(this);

        service = retrofit.create(ChoferService.class);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.d(TAG, "onStart()");
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.d(TAG, "onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.d(TAG, "onDestroy()");
    }

    private void dniSignIn() {
        String dni = dniField.getText().toString();
        final String password = passwordField.getText().toString();

        if (!TextUtils.isEmpty(dni) && !TextUtils.isEmpty(password)) {

            mProgress.setMessage("Iniciando sesion ...");
            mProgress.show();

            Call<Chofer> call = service.login(dni);

            call.enqueue(new Callback<Chofer>() {
                @Override
                public void onResponse(Call<Chofer> call, Response<Chofer> response) {
                    if (response.code() == 200) {

                        Chofer chofer = response.body();

                        if (chofer.getPassword().equals(password)) {

                            mProgress.dismiss();

                            AuthService.getInstance().setCurrentUser(chofer);

                            Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(mainIntent);

                        } else {

                            mProgress.dismiss();

                            Toast.makeText(LoginActivity.this, "Datos de ingreso incorrectos.", Toast.LENGTH_SHORT).show();

                        }

                    }

                    if (response.code() == 401) {

                        Log.d("Http Response", "Code: " + String.valueOf(response.code()) + " Message: " + response.message());

                    }

                    if (response.code() == 500) {

                        Log.d("Http Response", "Code: " + String.valueOf(response.code()) + " Message: " + response.message());

                    }
                }

                @Override
                public void onFailure(Call<Chofer> call, Throwable t) {
                    Log.i("Http Response", t.toString());
                }
            });

        } else {

            Toast.makeText(LoginActivity.this, "Por  favor, ingrese sus datos de acceso.", Toast.LENGTH_SHORT).show();

        }
    }
}
