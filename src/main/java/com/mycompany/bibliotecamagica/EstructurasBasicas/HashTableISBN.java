/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bibliotecamagica.EstructurasBasicas;

import com.mycompany.bibliotecamagica.Arboles.Libro;
import java.util.LinkedList;

/**
 *
 * @author Ana
 */
public class HashTableISBN {

    private LinkedList<Libro>[] tabla;
    private int tamaño;
    private int elementos;
    private final double FACTOR_CARGA = 0.75;

    public HashTableISBN() {
        this(50);
    }

    public int getTamaño() {
        return tamaño;
    }

    public java.util.LinkedList<Libro> getLista(int i) {
        return tabla[i];
    }

    public HashTableISBN(int tamaño) {
        this.tamaño = tamaño;
        this.elementos = 0;
        this.tabla = new LinkedList[tamaño];
        for (int i = 0; i < tamaño; i++) {
            tabla[i] = new LinkedList<>();
        }
    }

    private int hash(String isbn) {
        int suma = 0;
        for (char c : isbn.toCharArray()) {
            suma += c;
        }
        return Math.abs(suma) % tamaño;
    }

    private void redimensionar() {
        int nuevoTamaño = tamaño * 2;
        System.out.println("Redimensionando tabla hash a tamaño: " + nuevoTamaño);
        LinkedList<Libro>[] nuevaTabla = new LinkedList[nuevoTamaño];
        for (int i = 0; i < nuevoTamaño; i++) {
            nuevaTabla[i] = new LinkedList<>();
        }

        for (LinkedList<Libro> lista : tabla) {
            for (Libro libro : lista) {
                int nuevoIndice = Math.abs(libro.getIsbn().hashCode()) % nuevoTamaño;
                nuevaTabla[nuevoIndice].add(libro);
            }
        }

        this.tamaño = nuevoTamaño;
        this.tabla = nuevaTabla;
    }

    public void insertar(Libro libro) {
        if ((double) elementos / tamaño >= FACTOR_CARGA) {
            redimensionar();
        }

        int indice = hash(libro.getIsbn());
        for (Libro l : tabla[indice]) {
            if (l.getIsbn().equals(libro.getIsbn())) {
                System.out.println("Advertencia: ISBN duplicado bloqueado: " + libro.getIsbn());
                return;
            }
        }

        tabla[indice].add(libro);
        elementos++;
    }

    public Libro buscar(String isbn) {
        int indice = hash(isbn);
        for (Libro l : tabla[indice]) {
            if (l.getIsbn().equals(isbn)) {
                return l;
            }
        }
        return null;
    }

    public boolean eliminar(String isbn) {
        int indice = hash(isbn);
        boolean eliminado = tabla[indice].removeIf(l -> l.getIsbn().equals(isbn));
        if (eliminado) {
            elementos--;
        }
        return eliminado;
    }

    public void mostrar() {
        System.out.println("Tabla Hash (por ISBN): tamaño=" + tamaño + ", elementos=" + elementos);
        for (int i = 0; i < tamaño; i++) {
            if (!tabla[i].isEmpty()) {
                System.out.print("[" + i + "] -> ");
                for (Libro l : tabla[i]) {
                    System.out.print(l.getTitulo() + " (" + l.getIsbn() + ") | ");
                }
                System.out.println();
            }
        }
    }
}
