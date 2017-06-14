package com.designfreed.grasigas_app_diaria.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Movimiento {
    private String _id;
    private String fecha;
    private Vta vta;
    private Pto pto;
    private Integer visitas;
    private Integer ventas;

    public Movimiento() {
    }

    public Movimiento(String _id, String fecha, Vta vta, Pto pto, Integer visitas, Integer ventas) {
        this._id = _id;
        this.fecha = fecha;
        this.vta = vta;
        this.pto = pto;
        this.visitas = visitas;
        this.ventas = ventas;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Vta getVta() {
        return vta;
    }

    public void setVta(Vta vta) {
        this.vta = vta;
    }

    public Pto getPto() {
        return pto;
    }

    public void setPto(Pto pto) {
        this.pto = pto;
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

    @Override
    public String toString() {
        return "Movimiento{" +
                "_id='" + _id + '\'' +
                ", fecha='" + fecha + '\'' +
                ", vta=" + vta +
                ", pto=" + pto +
                ", visitas=" + visitas +
                ", ventas=" + ventas +
                '}';
    }
}
