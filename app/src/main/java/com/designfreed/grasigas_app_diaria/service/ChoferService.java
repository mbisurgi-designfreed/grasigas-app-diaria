package com.designfreed.grasigas_app_diaria.service;

import com.designfreed.grasigas_app_diaria.model.Chofer;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ChoferService {
    @GET("api/choferes/{dni}")
    Call<Chofer> login(@Path("dni") String dni);
}
