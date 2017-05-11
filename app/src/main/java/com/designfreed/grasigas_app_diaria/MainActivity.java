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

import com.designfreed.grasigas_app_diaria.model.Venta;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
            Integer visitas = Integer.valueOf(visitasField.getText().toString());
            Integer ventas = Integer.valueOf(ventasField.getText().toString());

            Venta venta = new Venta(uid, kilos, pesos, visitas, ventas);

            mDatabase.push().setValue(venta);

            kilosField.setText("");
            pesosField.setText("");
            visitasField.setText("");
            ventasField.setText("");

            kilosField.requestFocus();

            Toast.makeText(MainActivity.this, "Venta cargada exitosamente.", Toast.LENGTH_SHORT).show();

        } else {

            Toast.makeText(MainActivity.this, "Por  favor, complete todos los campos.", Toast.LENGTH_SHORT).show();

        }
    }
}
