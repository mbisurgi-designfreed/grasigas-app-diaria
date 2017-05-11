package com.designfreed.grasigas_app_diaria.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Venta {
    private String uid;
    private String fecha;
    private Float kilos;
    private Float pesos;
    private Integer visitas;
    private Integer ventas;

    public Venta(String uid, Float kilos, Float pesos, Integer visitas, Integer ventas) {
        this.uid = uid;
        this.fecha = formatDate(new Date());
        this.kilos = kilos;
        this.pesos = pesos;
        this.visitas = visitas;
        this.ventas = ventas;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Float getKilos() {
        return kilos;
    }

    public void setKilos(Float kilos) {
        this.kilos = kilos;
    }

    public Float getPesos() {
        return pesos;
    }

    public void setPesos(Float pesos) {
        this.pesos = pesos;
    }

    public Integer getVisitas() {
        return visitas;
    }

    public void setVisitas(Integer visitas) {
        this.visitas = visitas;
    }

    public Integer getVentas() {
        return ventas;
    }

    public void setVentas(Integer ventas) {
        this.ventas = ventas;
    }

    private String formatDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        return formatter.format(date);
    }
}
