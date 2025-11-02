/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bibliotecamagica.Arboles;

/**
 *
 * @author Ana
 */
public class Libro {

    private String titulo;
    private String isbn;
    private String genero;
    private int anio;
    private String autor;
    private String estado; // disponible, prestado, en tránsito, agotado

    public Libro(String titulo, String autor, String isbn, int anio, String genero) {
        this.titulo = titulo;
        this.autor = autor;
        this.isbn = isbn;
        this.anio = anio;
        this.genero = genero;
        this.estado = "Disponible";
    }

   
    public Libro(String titulo, String autor, String isbn, int anio, String genero, String estado) {
        this.titulo = titulo;
        this.autor = autor;
        this.isbn = isbn;
        this.anio = anio;
        this.genero = genero;
        this.estado = estado;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public String getIsbn() {
        return isbn;
    }

    public int getAnio() {
        return anio;
    }

    public String getGenero() {
        return genero;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return titulo + " - " + autor + " (" + anio + ") [" + genero + "] - Estado: " + estado;
    }
}
