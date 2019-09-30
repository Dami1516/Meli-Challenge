package com.mercadolibre.android.sdk.example.model;

import android.graphics.Bitmap;

public class User {
    private Bitmap imagen;
    private String nombre="Anonimo";
    private String ciudad="";
    private String provincia="";
    private String pais="";
    private String puntos="";

    public User() {
    }

    public User(Bitmap imagen, String nombre, String ciudad, String provincia, String pais, String puntos) {
        this.imagen = imagen;
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.provincia = provincia;
        this.pais = pais;
        this.puntos = puntos;
    }

    public Bitmap getImagen() {
        return imagen;
    }

    public void setImagen(Bitmap img) { this.imagen = img; }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getPuntos() {
        return puntos;
    }

    public void setPuntos(String puntos) {
        this.puntos = puntos;
    }
}
