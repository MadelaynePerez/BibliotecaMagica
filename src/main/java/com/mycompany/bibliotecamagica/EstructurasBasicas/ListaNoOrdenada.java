/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bibliotecamagica.EstructurasBasicas;

import com.mycompany.bibliotecamagica.Arboles.Libro;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author Ana
 */
public class ListaNoOrdenada {

    private List<Libro> libros;
    private List<Libro> librosPorISBN;

    public ListaNoOrdenada() {
        libros = new ArrayList<>();
        librosPorISBN = new ArrayList<>();
    }

   
    public void agregar(Libro libro) {
        libros.add(libro);
        librosPorISBN.add(libro);
        librosPorISBN.sort(Comparator.comparing(Libro::getIsbn, String.CASE_INSENSITIVE_ORDER));
    }

   
    public Libro buscarPorTituloSecuencial(String titulo) {
        for (Libro l : libros) {
            if (l.getTitulo().equalsIgnoreCase(titulo)) {
                return l;
            }
        }
        return null;
    }

   
    public Libro buscarPorISBNSecuencial(String isbn) {
        for (Libro l : libros) {
            if (l.getIsbn().equalsIgnoreCase(isbn)) {
                return l;
            }
        }
        return null;
    }

   
    public Libro buscarPorISBNBinaria(String isbn) {
        int inicio = 0, fin = librosPorISBN.size() - 1;
        while (inicio <= fin) {
            int mid = (inicio + fin) / 2;
            int cmp = librosPorISBN.get(mid).getIsbn().compareToIgnoreCase(isbn);
            if (cmp == 0) {
                return librosPorISBN.get(mid);
            }
            if (cmp > 0) {
                fin = mid - 1;
            } else {
                inicio = mid + 1;
            }
        }
        return null;
    }

    public void mostrarLibros() {
        System.out.println("\n=== Catálogo General de Libros ===");
        for (Libro l : libros) {
            System.out.println("Título: " + l.getTitulo()
                    + " | Autor: " + l.getAutor()
                    + " | ISBN: " + l.getIsbn()
                    + " | Año: " + l.getAnio()
                    + " | Género: " + l.getGenero()
                    + " | Estado: " + l.getEstado());
        }
    }

    // ----------------- MÉTODOS AUXILIARES -----------------
    public List<Libro> getLibros() {
        return libros;
    }

    public List<Libro> getLibrosOrdenadosPorISBN() {
        return librosPorISBN;
    }

    public boolean estaVacia() {
        return libros.isEmpty();
    }

    public int getTamaño() {
        return libros.size();
    }

    public void limpiar() {
        libros.clear();
        librosPorISBN.clear();
    }
}
