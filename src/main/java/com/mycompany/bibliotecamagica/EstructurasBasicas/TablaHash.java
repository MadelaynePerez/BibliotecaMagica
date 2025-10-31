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
public class TablaHash {

    private LinkedList<Libro>[] tabla;
    private int tamaño;

    public TablaHash(int tamaño) {
        this.tamaño = tamaño;
        tabla = new LinkedList[tamaño];
        for (int i = 0; i < tamaño; i++) {
            tabla[i] = new LinkedList<>();
        }
    }

    private int hash(String isbn) {
        int suma = 0;
        for (char c : isbn.toCharArray()) {
            suma += c;
        }
        return suma % tamaño;
    }

    public void insertar(Libro libro) {
        int indice = hash(libro.getIsbn());
        for (Libro l : tabla[indice]) {
            if (l.getIsbn().equals(libro.getIsbn())) {
                System.out.println("ISBN duplicado: " + libro.getIsbn());
                return;
            }
        }
        tabla[indice].add(libro);
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

    public void eliminar(String isbn) {
        int indice = hash(isbn);
        tabla[indice].removeIf(l -> l.getIsbn().equals(isbn));
    }

    public void mostrar() {
        for (int i = 0; i < tamaño; i++) {
            if (!tabla[i].isEmpty()) {
                System.out.print("[" + i + "] ");
                for (Libro l : tabla[i]) {
                    System.out.print(l.getTitulo() + " | ");
                }
                System.out.println();
            }
        }
    }
}
