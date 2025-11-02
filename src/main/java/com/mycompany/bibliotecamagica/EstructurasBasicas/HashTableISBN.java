/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bibliotecamagica.EstructurasBasicas;

import com.mycompany.bibliotecamagica.Arboles.Biblioteca;
import com.mycompany.bibliotecamagica.Arboles.Libro;
import java.util.LinkedList;

/**
 *
 * @author Ana
 */
public class HashTableISBN {

    public static class Nodo {

        public String clave;
        public Object valor;
        public Nodo siguiente;

        public Nodo(String clave, Object valor) {
            this.clave = clave;
            this.valor = valor;
            this.siguiente = null;
        }
    }

    private Nodo[] tabla;
    private int tamaño;
    private int elementos;
    private final double FACTOR_CARGA = 0.75;

    public HashTableISBN() {
        this(53);
    }

    public HashTableISBN(int tamaño) {
        this.tamaño = siguientePrimo(tamaño);
        this.tabla = new Nodo[this.tamaño];
        this.elementos = 0;
    }

    private int hash(String clave) {
        int h = 0;
        for (char c : clave.toCharArray()) {
            h = 31 * h + c;
        }
        return Math.abs(h) % tamaño;
    }

    private void redimensionar() {
        int nuevoTamaño = siguientePrimo(tamaño * 2);
        Nodo[] nueva = new Nodo[nuevoTamaño];

        for (Nodo n : tabla) {
            while (n != null) {
                Nodo siguiente = n.siguiente;
                int indice = Math.abs(n.clave.hashCode()) % nuevoTamaño;
                n.siguiente = nueva[indice];
                nueva[indice] = n;
                n = siguiente;
            }
        }

        tabla = nueva;
        tamaño = nuevoTamaño;
    }

    public void insertar(String clave, Object valor) {
        if ((double) elementos / tamaño >= FACTOR_CARGA) {
            redimensionar();
        }

        int indice = hash(clave);
        Nodo actual = tabla[indice];

        while (actual != null) {
            if (actual.clave.equals(clave)) {
                System.out.println("Clave duplicada bloqueada: " + clave);
                return;
            }
            actual = actual.siguiente;
        }

        Nodo nuevo = new Nodo(clave, valor);
        nuevo.siguiente = tabla[indice];
        tabla[indice] = nuevo;
        elementos++;
    }

    public Object buscar(String clave) {
        int indice = hash(clave);
        Nodo actual = tabla[indice];

        while (actual != null) {
            if (actual.clave.equals(clave)) {
                return actual.valor;
            }
            actual = actual.siguiente;
        }
        return null;
    }

    public boolean eliminar(String clave) {
        int indice = hash(clave);
        Nodo actual = tabla[indice];
        Nodo previo = null;

        while (actual != null) {
            if (actual.clave.equals(clave)) {
                if (previo == null) {
                    tabla[indice] = actual.siguiente;
                } else {
                    previo.siguiente = actual.siguiente;
                }
                elementos--;
                return true;
            }
            previo = actual;
            actual = actual.siguiente;
        }
        return false;
    }

    public void mostrar() {
        System.out.println("\nTabla Hash: tamaño=" + tamaño + ", elementos=" + elementos);
        for (int i = 0; i < tamaño; i++) {
            Nodo n = tabla[i];
            if (n != null) {
                System.out.print("[" + i + "] -> ");
                while (n != null) {
                    if (n.valor instanceof Libro l) {
                        System.out.print("Libro: " + l.getTitulo() + " (" + l.getIsbn() + ") | ");
                    } else if (n.valor instanceof Biblioteca b) {
                        System.out.print("Biblioteca: " + b.getNombre() + " | ");
                    } else {
                        System.out.print("Otro(" + n.clave + ") | ");
                    }
                    n = n.siguiente;
                }
                System.out.println();
            }
        }
    }

    private static boolean esPrimo(int n) {
        if (n < 2) {
            return false;
        }
        if (n % 2 == 0 && n != 2) {
            return false;
        }
        for (int i = 3; i * i <= n; i += 2) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }

    private static int siguientePrimo(int n) {
        while (!esPrimo(n)) {
            n++;
        }
        return n;
    }

    public int getTamaño() {
        return tamaño;
    }

    public Nodo[] getTabla() {
        return tabla;
    }
}
