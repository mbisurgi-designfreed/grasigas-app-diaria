package com.designfreed.grasigas_app_diaria.service;

import com.designfreed.grasigas_app_diaria.model.Movimiento;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovimientoService {
    @GET("api/movimientos")
    Call<List<Movimiento>> getMovimientos(@Query("desde") String desde, @Query("hasta") String hasta);

    @GET("api/movimientos/{choferId}")
    Call<List<Movimiento>> getMovimientosChofer(@Path("choferId") String choferId, @Query("desde") String desde, @Query("hasta") String hasta);

    @PUT("api/movimientos/{choferId}")
    Call<Movimiento> putMovimiento(@Path("choferId") String choferId, @Body Movimiento movimiento);
}
