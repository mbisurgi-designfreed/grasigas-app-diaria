package com.designfreed.grasigas_app_diaria;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.designfreed.grasigas_app_diaria.model.Movimiento;
import com.designfreed.grasigas_app_diaria.service.AuthService;
import com.designfreed.grasigas_app_diaria.service.MovimientoService;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReportActivity extends AppCompatActivity {
    private TextView kilosDiarioVtaText;
    private TextView kilosDiarioPtoText;
    private TextView kilosDiarioDifText;
    private TextView kilosDiarioDifPorText;
    private TextView kilosMensualVtaText;
    private TextView kilosMensualPtoText;
    private TextView kilosMensualDifText;
    private TextView kilosMensualDifPorText;

    private MovimientoService service;

    private static final String TAG = "ReportActivity";
    private static final String SERVER_URL = "https://intense-tor-11265.herokuapp.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        Log.d(TAG, "onCreate()");

        kilosDiarioVtaText = (TextView) findViewById(R.id.diario_vta_text);
        kilosDiarioPtoText = (TextView) findViewById(R.id.diario_pto_text);
        kilosDiarioDifText = (TextView) findViewById(R.id.diario_dif_text);
        kilosDiarioDifPorText = (TextView) findViewById(R.id.diario_dif_por_text);
        kilosMensualVtaText = (TextView) findViewById(R.id.mensual_vta_text);
        kilosMensualPtoText = (TextView) findViewById(R.id.mensual_pto_text);
        kilosMensualDifText = (TextView) findViewById(R.id.mensual_dif_text);
        kilosMensualDifPorText = (TextView) findViewById(R.id.mensual_dif_por_text);

        if (AuthService.getInstance().getCurrentUser() == null) {

            Intent loginIntent = new Intent(ReportActivity.this, LoginActivity.class);
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(loginIntent);

        } else {

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();

                    String username = AuthService.getInstance().getCurrentUser().getDni();
                    String password = AuthService.getInstance().getCurrentUser().getPassword();

                    String credentials = Credentials.basic(username, password);

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

            service = retrofit.create(MovimientoService.class);

            cargarResumen();
        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logout) {

            AuthService.getInstance().logout();

            Intent loginIntent = new Intent(ReportActivity.this, LoginActivity.class);
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(loginIntent);

            finish();
        }

        if (item.getItemId() == R.id.add) {

            Intent addIntent = new Intent(ReportActivity.this, MainActivity.class);
            startActivity(addIntent);

        }

        return true;
    }

    private void cargarResumen() {
        String hasta = formatDateString(new Date());
        String desde = formatDateString(primerDiaMes(formatStringDate(hasta)));

        String choferId = AuthService.getInstance().getCurrentUser().get_id();

        Call<List<Movimiento>> call = service.getMovimientosChofer(choferId, desde, hasta);

        call.enqueue(new Callback<List<Movimiento>>() {
            @Override
            public void onResponse(Call<List<Movimiento>> call, Response<List<Movimiento>> response) {
                if (response.code() == 200) {

                    Log.d("Http Response", "Code: " + String.valueOf(response.code()) + " Message: " + response.message());

                    Movimiento diario = response.body().get(response.body().size() - 1);

                    Float diarioVta = diario.getVta().getKilos();
                    Float diarioPto = diario.getPto().getKilos();
                    Float diarioDif = diarioVta - diarioPto;
                    Float diarioDifPor = diarioDif / diarioPto;

                    Float mensualVta = 0F;
                    Float mensualPto = 0F;


                    for (Movimiento mov: response.body()) {
                        if (mov.getVta() != null) {

                            mensualVta = mensualVta + mov.getVta().getKilos();

                        } else {

                            mensualVta = mensualVta + 0;

                        }

                        mensualPto = mensualPto + mov.getPto().getKilos();
                    }

                    Float mensualDif = mensualVta - mensualPto;
                    Float mensualDifPor = mensualDif / mensualPto;

                    NumberFormat formatNumber = NumberFormat.getNumberInstance();
                    formatNumber.setMinimumFractionDigits(2);

                    NumberFormat formatPercentaje = NumberFormat.getPercentInstance();
                    formatPercentaje.setMinimumFractionDigits(0);

                    kilosDiarioVtaText.setText(formatNumber.format(diarioVta));
                    kilosDiarioPtoText.setText(formatNumber.format(diarioPto));
                    kilosDiarioDifText.setText(formatNumber.format(diarioDif));
                    kilosDiarioDifPorText.setText(formatPercentaje.format(diarioDifPor));
                    kilosDiarioDifPorText.setTextColor(getDifColor(diarioDif));

                    kilosMensualVtaText.setText(formatNumber.format(mensualVta));
                    kilosMensualPtoText.setText(formatNumber.format(mensualPto));
                    kilosMensualDifText.setText(formatNumber.format(mensualDif));
                    kilosMensualDifPorText.setText(formatPercentaje.format(mensualDifPor));
                    kilosMensualDifPorText.setTextColor(getDifColor(mensualDif));

                }
            }

            @Override
            public void onFailure(Call<List<Movimiento>> call, Throwable t) {
                Log.i("Http Response", t.toString());
            }
        });
    }

    private Date primerDiaMes(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));

        return cal.getTime();
    }

    private String formatDateString(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        return formatter.format(date);
    }

    private Date formatStringDate(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        try {
            return formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    private int getDifColor(Float dif) {
        int estadoResourceColorId = R.color.green;

        if (dif < 0) {
            estadoResourceColorId = R.color.red;
        }

        return ContextCompat.getColor(getApplicationContext(), estadoResourceColorId);
    }

}
