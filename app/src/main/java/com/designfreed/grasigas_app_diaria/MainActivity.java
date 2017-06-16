package com.designfreed.grasigas_app_diaria;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.designfreed.grasigas_app_diaria.model.Movimiento;
import com.designfreed.grasigas_app_diaria.model.Vta;
import com.designfreed.grasigas_app_diaria.service.MovimientoService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private EditText kilosField;
    private EditText pesosField;
    private EditText visitasField;
    private EditText ventasField;
    private Button cargarBtn;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private MovimientoService service;

    private static final String TAG = "MainActivity";
    private static final String SERVER_URL = "http://192.168.0.3:3000/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate()");

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                String credentials = Credentials.basic("33899255", "maximati");

                Request request = original.newBuilder()
                        .header("Authorization", credentials)
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        });

        OkHttpClient client = httpClient.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {

                    Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);

                } else {



                }
            }
        };

        mDatabase = FirebaseDatabase.getInstance().getReference().child("ventas");
        mDatabase.keepSynced(true);

        kilosField = (EditText) findViewById(R.id.kilos_field);
        pesosField = (EditText) findViewById(R.id.pesos_field);
        visitasField = (EditText) findViewById(R.id.visitas_field);
        ventasField = (EditText) findViewById(R.id.ventas_field);

        cargarBtn = (Button) findViewById(R.id.cargar_btn);
        cargarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarVentas();
            }
        });

        service = retrofit.create(MovimientoService.class);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.d(TAG, "onStart()");

        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.d(TAG, "onStop()");

        mAuth.removeAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.d(TAG, "onDestroy()");
    }

    private void cargarVentas() {

        if (!TextUtils.isEmpty(kilosField.getText()) && !TextUtils.isEmpty(pesosField.getText()) && !TextUtils.isEmpty(visitasField.getText()) && !TextUtils.isEmpty(ventasField.getText())) {

            Float kilos = Float.valueOf(kilosField.getText().toString());
            Float pesos = Float.valueOf(pesosField.getText().toString());
            Float precioMe = pesos / kilos;
            Integer visitas = Integer.valueOf(visitasField.getText().toString());
            Integer ventas = Integer.valueOf(ventasField.getText().toString());

            Movimiento mov = new Movimiento();
            mov.setFecha(formatDate(new Date()));
            mov.setVta(new Vta(kilos, precioMe));
            mov.setVisitas(visitas);
            mov.setVentas(ventas);

            String choferId = "";

            Call<Movimiento> call = service.putMovimiento(choferId, mov);

            call.enqueue(new Callback<Movimiento>() {
                @Override
                public void onResponse(Call<Movimiento> call, Response<Movimiento> response) {
                    if (response.code() == 200) {
                        Log.d("Http Response", "Code: " + String.valueOf(response.code()) + " Message: " + response.message());
                    }

                    if (response.code() == 401) {
                        Log.d("Http Response", "Code: " + String.valueOf(response.code()) + " Message: " + response.message());
                    }

                    if (response.code() == 500) {
                        Log.d("Http Response", "Code: " + String.valueOf(response.code()) + " Message: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<Movimiento> call, Throwable t) {
                    Log.i("Http Response", t.toString());
                }
            });

            kilosField.setText("");
            pesosField.setText("");
            visitasField.setText("");
            ventasField.setText("");

            kilosField.requestFocus();

            Toast.makeText(MainActivity.this, "Movimiento cargada exitosamente.", Toast.LENGTH_SHORT).show();

        } else {

            Toast.makeText(MainActivity.this, "Por  favor, complete todos los campos.", Toast.LENGTH_SHORT).show();

        }
    }

    private String formatDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        return formatter.format(date);
    }

}
