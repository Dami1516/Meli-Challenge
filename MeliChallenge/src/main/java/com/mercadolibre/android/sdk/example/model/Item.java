package com.mercadolibre.android.sdk.example.model;

import android.graphics.Bitmap;

import java.io.Serializable;

public class Item  implements Serializable {
    private Bitmap imagen;
    private String nombre;
    private String precioAnterior;
    private String precio;
    private int envioGratis;
    private String[] fotos;
    private String estado;
    private String vendidos;
    private int mercadoPago;
    private String permaLink;
    private String id;

    public Item(){

    }

    public Item(Bitmap imagen, String nombre, String precio, int envioGratis) {
        this.imagen = imagen;
        this.nombre = nombre;
        this.precio = precio;
        this.envioGratis = envioGratis;
    }

    public String getPermaLink() {
        return permaLink;
    }

    public void setPermaLink(String permaLink) {
        this.permaLink = permaLink;
    }

    public String getPrecioAnterior() {
        return precioAnterior;
    }

    public void setPrecioAnterior(String precioAnterior) {
        this.precioAnterior = precioAnterior;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String[] getFotos() {
        return fotos;
    }

    public void setFotos(String[] fotos) {
        this.fotos = fotos;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getVendidos() {
        return vendidos;
    }

    public void setVendidos(String vendidos) {
        this.vendidos = vendidos;
    }

    public int getMercadoPago() {
        return mercadoPago;
    }

    public void setMercadoPago(int mercadoPago) {
        this.mercadoPago = mercadoPago;
    }

    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public void setEnvioGratis(int envioGratis){
        this.envioGratis = envioGratis;
    }

    public Bitmap getImagen() { return imagen; }

    public String getNombre() {
        return nombre;
    }

    public String getPrecio() {
        return precio;
    }

    public int getEnvioGratis() { return  envioGratis; }

}