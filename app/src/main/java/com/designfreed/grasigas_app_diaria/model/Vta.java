package com.designfreed.grasigas_app_diaria.model;

public class Vta {
    private Float kilos;
    private Float pcio;

    public Vta(Float kilos, Float pcio) {
        this.kilos = kilos;
        this.pcio = pcio;
    }

    public Float getKilos() {
        return kilos;
    }

    public void setKilos(Float kilos) {
        this.kilos = kilos;
    }

    public Float getPcio() {
        return pcio;
    }

    public void setPcio(Float pcio) {
        this.pcio = pcio;
    }

    @Override
    public String toString() {
        return "Vta{" +
                "kilos=" + kilos +
                ", pcio=" + pcio +
                '}';
    }
}
