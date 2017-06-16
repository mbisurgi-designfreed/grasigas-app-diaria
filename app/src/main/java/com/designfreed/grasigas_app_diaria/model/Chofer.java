package com.designfreed.grasigas_app_diaria.model;

import java.io.Serializable;

public class Chofer implements Serializable {
    private String _id;
    private String nombre;
    private String apellido;
    private String dni;
    private String password;

    public Chofer() {
    }

    public Chofer(String _id, String nombre, String apellido, String dni, String password) {
        this._id = _id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.password = password;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
