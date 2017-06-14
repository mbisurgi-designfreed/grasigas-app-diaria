package com.designfreed.grasigas_app_diaria.model;

public class Pto {
    private Float kilos;
    private Float pcio;

    public Pto(Float kilos, Float pcio) {
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
        return "Pto{" +
                "kilos=" + kilos +
                ", pcio=" + pcio +
                '}';
    }
}
