/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bibliotecamagica.EstructurasBasicas;

import com.mycompany.bibliotecamagica.Arboles.Libro;

/**
 *
 * @author Ana
 */
public class ListaDoble {

    private class Nodo {

        Libro libro;
        Nodo anterior, siguiente;

        Nodo(Libro libro) {
            this.libro = libro;
        }
    }

    private Nodo cabeza;
    private Nodo cola;
    private int tamaño;

    public ListaDoble() {
        cabeza = null;
        cola = null;
        tamaño = 0;
    }

    public void insertarInicio(Libro libro) {
        Nodo nuevo = new Nodo(libro);
        if (cabeza == null) {
            cabeza = cola = nuevo;
        } else {
            nuevo.siguiente = cabeza;
            cabeza.anterior = nuevo;
            cabeza = nuevo;
        }
        tamaño++;
    }

    public void insertarFinal(Libro libro) {
        Nodo nuevo = new Nodo(libro);
        if (cola == null) {
            cabeza = cola = nuevo;
        } else {
            cola.siguiente = nuevo;
            nuevo.anterior = cola;
            cola = nuevo;
        }
        tamaño++;
    }

    public Libro buscarPorTitulo(String titulo) {
        Nodo actual = cabeza;
        while (actual != null) {
            if (actual.libro.getTitulo().equalsIgnoreCase(titulo)) {
                return actual.libro;
            }
            actual = actual.siguiente;
        }
        return null;
    }

    public Libro buscarPorISBN(String isbn) {
        Nodo actual = cabeza;
        while (actual != null) {
            if (actual.libro.getIsbn().equalsIgnoreCase(isbn)) {
                return actual.libro;
            }
            actual = actual.siguiente;
        }
        return null;
    }

    public boolean eliminarPorISBN(String isbn) {
        Nodo actual = cabeza;
        while (actual != null) {
            if (actual.libro.getIsbn().equalsIgnoreCase(isbn)) {
                if (actual == cabeza && actual == cola) {
                    cabeza = cola = null;
                } else if (actual == cabeza) {
                    cabeza = cabeza.siguiente;
                    cabeza.anterior = null;
                } else if (actual == cola) {
                    cola = cola.anterior;
                    cola.siguiente = null;
                } else {
                    actual.anterior.siguiente = actual.siguiente;
                    actual.siguiente.anterior = actual.anterior;
                }
                tamaño--;
                return true;
            }
            actual = actual.siguiente;
        }
        return false;
    }

    public void mostrar() {
        Nodo actual = cabeza;
        System.out.println(" Lista Doble (colección de libros):");
        while (actual != null) {
            Libro l = actual.libro;
            System.out.println("- " + l.getTitulo() + " | " + l.getAutor() + " | " + l.getIsbn() + " | " + l.getAnio() + " | " + l.getGenero());
            actual = actual.siguiente;
        }
    }

    public int getTamaño() {
        return tamaño;
    }

    public boolean estaVacia() {
        return tamaño == 0;
    }

    public void mostrarInvertido() {
        Nodo actual = cola;
        System.out.println(" Lista Doble (recorrido inverso):");
        while (actual != null) {
            System.out.println("- " + actual.libro.getTitulo() + " (" + actual.libro.getIsbn() + ")");
            actual = actual.anterior;
        }
    }
}
