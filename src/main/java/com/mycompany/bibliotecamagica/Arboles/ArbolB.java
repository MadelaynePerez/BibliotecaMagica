/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bibliotecamagica.Arboles;

/**
 *
 * @author Ana
 */

import java.util.ArrayList;

public class ArbolB {

    private static final int T = 3; 
    private NodoB raiz;

    static class NodoB {
        ArrayList<Libro> libros;
        ArrayList<NodoB> hijos;
        boolean hoja;

        NodoB(boolean hoja) {
            this.hoja = hoja;
            this.libros = new ArrayList<>();
            this.hijos = new ArrayList<>();
        }
    }

    public ArbolB() {
        raiz = null;
    }

    // insrrtarr
    public void insertar(Libro libro) {
        if (raiz == null) {
            raiz = new NodoB(true);
            raiz.libros.add(libro);
        } else {
            if (raiz.libros.size() == 2 * T - 1) {
                NodoB nuevaRaiz = new NodoB(false);
                nuevaRaiz.hijos.add(raiz);
                dividirHijo(nuevaRaiz, 0, raiz);

                int i = 0;
                if (libro.getAnio() > nuevaRaiz.libros.get(0).getAnio()) i++;
                insertarNoLleno(nuevaRaiz.hijos.get(i), libro);

                raiz = nuevaRaiz;
            } else {
                insertarNoLleno(raiz, libro);
            }
        }
    }

    private void insertarNoLleno(NodoB nodo, Libro libro) {
        int i = nodo.libros.size() - 1;

        if (nodo.hoja) {
            nodo.libros.add(libro);
            while (i >= 0 && libro.getAnio() < nodo.libros.get(i).getAnio()) {
                nodo.libros.set(i + 1, nodo.libros.get(i));
                i--;
            }
            nodo.libros.set(i + 1, libro);
        } else {
            while (i >= 0 && libro.getAnio() < nodo.libros.get(i).getAnio())
                i--;

            i++;
            if (nodo.hijos.get(i).libros.size() == 2 * T - 1) {
                dividirHijo(nodo, i, nodo.hijos.get(i));
                if (libro.getAnio() > nodo.libros.get(i).getAnio()) i++;
            }
            insertarNoLleno(nodo.hijos.get(i), libro);
        }
    }

    private void dividirHijo(NodoB padre, int i, NodoB hijo) {
        NodoB nuevo = new NodoB(hijo.hoja);

        for (int j = 0; j < T - 1; j++)
            nuevo.libros.add(hijo.libros.get(j + T));

        if (!hijo.hoja)
            for (int j = 0; j < T; j++)
                nuevo.hijos.add(hijo.hijos.get(j + T));

        for (int j = hijo.libros.size() - 1; j >= T - 1; j--)
            hijo.libros.remove(j);

        if (!hijo.hoja)
            for (int j = hijo.hijos.size() - 1; j >= T; j--)
                hijo.hijos.remove(j);

        padre.hijos.add(i + 1, nuevo);
        padre.libros.add(i, hijo.libros.get(T - 1));
    }

    // - Busqeda

    public Libro buscarPorAnio(int anio) {
        return buscarPorAnio(raiz, anio);
    }

    private Libro buscarPorAnio(NodoB nodo, int anio) {
        if (nodo == null) return null;

        int i = 0;
        while (i < nodo.libros.size() && anio > nodo.libros.get(i).getAnio())
            i++;

        if (i < nodo.libros.size() && nodo.libros.get(i).getAnio() == anio)
            return nodo.libros.get(i);

        if (nodo.hoja) return null;

        return buscarPorAnio(nodo.hijos.get(i), anio);
    }

    public void buscarPorRango(int desde, int hasta) {
        buscarPorRango(raiz, desde, hasta);
    }

    private void buscarPorRango(NodoB nodo, int desde, int hasta) {
        if (nodo == null) return;

        for (int i = 0; i < nodo.libros.size(); i++) {
            if (!nodo.hoja)
                buscarPorRango(nodo.hijos.get(i), desde, hasta);

            int anio = nodo.libros.get(i).getAnio();
            if (anio >= desde && anio <= hasta)
                System.out.println(nodo.libros.get(i).getTitulo() + " (" + anio + ")");
        }

        if (!nodo.hoja)
            buscarPorRango(nodo.hijos.get(nodo.hijos.size() - 1), desde, hasta);
    }

    // msosrar
    public void mostrar() {
        mostrar(raiz, 0);
    }

    private void mostrar(NodoB nodo, int nivel) {
        if (nodo == null) return;
        for (int i = 0; i < nodo.libros.size(); i++) {
            if (!nodo.hoja) mostrar(nodo.hijos.get(i), nivel + 1);
            System.out.println("   ".repeat(nivel) +
                    nodo.libros.get(i).getTitulo() +
                    " (" + nodo.libros.get(i).getAnio() + ")");
        }
        if (!nodo.hoja)
            mostrar(nodo.hijos.get(nodo.hijos.size() - 1), nivel + 1);
    }
}
