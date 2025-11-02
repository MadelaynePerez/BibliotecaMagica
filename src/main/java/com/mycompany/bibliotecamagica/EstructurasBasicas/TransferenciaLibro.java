/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bibliotecamagica.EstructurasBasicas;

import com.mycompany.bibliotecamagica.Arboles.Libro;
import java.util.List;

/**
 *
 * @author Ana
 */
public class TransferenciaLibro {

    private Libro libro;
    private String origen;
    private String destino;
    private boolean porTiempo;
    private List<String> ruta;
    private long eta;

    public TransferenciaLibro(Libro libro, String origen, String destino, boolean porTiempo, List<String> ruta, long eta) {
        this.libro = libro;
        this.origen = origen;
        this.destino = destino;
        this.porTiempo = porTiempo;
        this.ruta = ruta;
        this.eta = eta;
    }

    public Libro getLibro() {
        return libro;
    }

    public String getOrigen() {
        return origen;
    }

    public String getDestino() {
        return destino;
    }

    public boolean isPorTiempo() {
        return porTiempo;
    }

    public List<String> getRuta() {
        return ruta;
    }

    public long getEta() {
        return eta;
    }

    @Override
    public String toString() {
        return "Transferencia de \"" + libro.getTitulo() + "\" (" + origen + " → " + destino + ")";
    }
}
