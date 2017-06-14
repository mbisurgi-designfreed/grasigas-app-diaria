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
import java.util.List;

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

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate()");

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

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.3:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MovimientoService service = retrofit.create(MovimientoService.class);

        Call<List<Movimiento>> call1 = service.getMovimientos("14/06/2017", "15/06/2017");

        Log.i("Endpoint URL: ", call1.request().url().toString());

        call1.enqueue(new Callback<List<Movimiento>>() {
            @Override
            public void onResponse(Call<List<Movimiento>> call, Response<List<Movimiento>> response) {
                Log.i("Status Code", String.valueOf(response.code()));
                Log.i("Body", String.valueOf(response.body()));
            }

            @Override
            public void onFailure(Call<List<Movimiento>> call, Throwable t) {
                Log.i("Error", t.toString());
            }
        });

        Movimiento mov = new Movimiento();
        mov.setFecha("15/06/2017");
        mov.setVta(new Vta(1550F, 120.55F));
        mov.setVisitas(15);
        mov.setVentas(10);

        Call<Movimiento> call2 = service.putMovimiento("59414611263e911bb7712efd", mov);

        Log.i("Endpoint URL: ", call2.request().url().toString());
        Log.i("Endpoint URL Body: ", call2.request().body().contentType().toString());

        call2.enqueue(new Callback<Movimiento>() {
            @Override
            public void onResponse(Call<Movimiento> call, Response<Movimiento> response) {
                Log.i("Status Code", String.valueOf(response.code()));
                Log.i("Body", String.valueOf(response.body()));
            }

            @Override
            public void onFailure(Call<Movimiento> call, Throwable t) {
                Log.i("Error", t.toString());
            }
        });
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

            String uid = mAuth.getCurrentUser().getUid();
            Float kilos = Float.valueOf(kilosField.getText().toString());
            Float pesos = Float.valueOf(pesosField.getText().toString());
            Float precioMe = pesos / kilos;
            Integer visitas = Integer.valueOf(visitasField.getText().toString());
            Integer ventas = Integer.valueOf(ventasField.getText().toString());

            Movimiento movimiento = new Movimiento();

            mDatabase.push().setValue(movimiento);

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
}
