/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bibliotecamagica.Arboles;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ana
 */
public class Avl {

    class NodoAVL {

        Libro libro;
        NodoAVL izq, der;
        int altura;

        NodoAVL(Libro libro) {
            this.libro = libro;
            this.altura = 1;
        }
    }

    private NodoAVL raiz;

    public Avl() {
        raiz = null;
    }

    private int altura(NodoAVL n) {
        return (n == null) ? 0 : n.altura;
    }

    private int factorEquilibrio(NodoAVL n) {
        return (n == null) ? 0 : altura(n.izq) - altura(n.der);
    }

    private NodoAVL rotacionDerecha(NodoAVL y) {
        NodoAVL x = y.izq;
        NodoAVL T2 = x.der;

        x.der = y;
        y.izq = T2;

        y.altura = Math.max(altura(y.izq), altura(y.der)) + 1;
        x.altura = Math.max(altura(x.izq), altura(x.der)) + 1;

        return x;
    }

    private NodoAVL rotacionIzquierda(NodoAVL x) {
        NodoAVL y = x.der;
        NodoAVL T2 = y.izq;

        y.izq = x;
        x.der = T2;

        x.altura = Math.max(altura(x.izq), altura(x.der)) + 1;
        y.altura = Math.max(altura(y.izq), altura(y.der)) + 1;

        return y;
    }

    private String normalizar(String s) {
        if (s == null) {
            return "";
        }
        return s.trim().toLowerCase().replaceAll("\\s+", "");
    }

    private NodoAVL insertarNodo(NodoAVL nodo, Libro libro) {
        if (nodo == null) {
            return new NodoAVL(libro);
        }

        String nuevo = normalizar(libro.getTitulo());
        String actual = normalizar(nodo.libro.getTitulo());

        if (nuevo.compareTo(actual) < 0) {
            nodo.izq = insertarNodo(nodo.izq, libro);
        } else if (nuevo.compareTo(actual) > 0) {
            nodo.der = insertarNodo(nodo.der, libro);
        } else {
            return nodo;
        }

        nodo.altura = 1 + Math.max(altura(nodo.izq), altura(nodo.der));

        int balance = factorEquilibrio(nodo);

        if (balance > 1 && nuevo.compareTo(normalizar(nodo.izq.libro.getTitulo())) < 0) {
            return rotacionDerecha(nodo);
        }

        if (balance < -1 && nuevo.compareTo(normalizar(nodo.der.libro.getTitulo())) > 0) {
            return rotacionIzquierda(nodo);
        }

        if (balance > 1 && nuevo.compareTo(normalizar(nodo.izq.libro.getTitulo())) > 0) {
            nodo.izq = rotacionIzquierda(nodo.izq);
            return rotacionDerecha(nodo);
        }

        if (balance < -1 && nuevo.compareTo(normalizar(nodo.der.libro.getTitulo())) < 0) {
            nodo.der = rotacionDerecha(nodo.der);
            return rotacionIzquierda(nodo);
        }

        return nodo;
    }

    public void insertar(Libro libro) {
        raiz = insertarNodo(raiz, libro);
    }

    public void eliminar(String titulo) {
        raiz = eliminarRec(raiz, normalizar(titulo));
    }

    private NodoAVL eliminarRec(NodoAVL nodo, String titulo) {
        if (nodo == null) {
            return null;
        }

        String actual = normalizar(nodo.libro.getTitulo());

        if (titulo.compareTo(actual) < 0) {
            nodo.izq = eliminarRec(nodo.izq, titulo);
        } else if (titulo.compareTo(actual) > 0) {
            nodo.der = eliminarRec(nodo.der, titulo);
        } else {

            if (nodo.izq == null || nodo.der == null) {
                nodo = (nodo.izq != null) ? nodo.izq : nodo.der;
            } else {
                NodoAVL sucesor = obtenerMinimo(nodo.der);
                nodo.libro = sucesor.libro;
                nodo.der = eliminarRec(nodo.der, normalizar(sucesor.libro.getTitulo()));
            }
        }

        if (nodo == null) {
            return null;
        }

        // Actualizar altura
        nodo.altura = 1 + Math.max(altura(nodo.izq), altura(nodo.der));

        // Rebalancear
        int balance = factorEquilibrio(nodo);
        if (balance > 1 && factorEquilibrio(nodo.izq) >= 0) {
            return rotacionDerecha(nodo);
        }
        if (balance > 1 && factorEquilibrio(nodo.izq) < 0) {
            nodo.izq = rotacionIzquierda(nodo.izq);
            return rotacionDerecha(nodo);
        }
        if (balance < -1 && factorEquilibrio(nodo.der) <= 0) {
            return rotacionIzquierda(nodo);
        }
        if (balance < -1 && factorEquilibrio(nodo.der) > 0) {
            nodo.der = rotacionDerecha(nodo.der);
            return rotacionIzquierda(nodo);
        }

        return nodo;
    }

    private NodoAVL obtenerMinimo(NodoAVL nodo) {
        while (nodo.izq != null) {
            nodo = nodo.izq;
        }
        return nodo;
    }

    public java.util.List<Libro> aLista() {
        java.util.List<Libro> res = new java.util.ArrayList<>();
        aListaRec(raiz, res);
        return res;
    }

    private void aListaRec(NodoAVL nodo, java.util.List<Libro> res) {
        if (nodo == null) {
            return;
        }
        aListaRec(nodo.izq, res);
        res.add(nodo.libro);
        aListaRec(nodo.der, res);
    }

    public Libro buscar(String titulo) {
        return buscarNodo(raiz, normalizar(titulo));
    }

    private Libro buscarNodo(NodoAVL nodo, String titulo) {
        if (nodo == null) {
            return null;
        }
        String actual = normalizar(nodo.libro.getTitulo());

        if (actual.equals(titulo)) {
            return nodo.libro;
        } else if (titulo.compareTo(actual) < 0) {
            return buscarNodo(nodo.izq, titulo);
        } else {
            return buscarNodo(nodo.der, titulo);
        }
    }

    public void mostrarInOrden() {
        mostrarInOrden(raiz);
    }

    private void mostrarInOrden(NodoAVL nodo) {
        if (nodo == null) {
            return;
        }
        mostrarInOrden(nodo.izq);
        System.out.println(nodo.libro.getTitulo() + " (" + nodo.libro.getAnio() + ")");
        mostrarInOrden(nodo.der);
    }

    public String generarDot() {
        StringBuilder sb = new StringBuilder();
        sb.append("digraph AVL {\n");
        sb.append("  node [shape=box, style=filled, color=lightblue];\n");
        generarDotRec(raiz, sb);
        sb.append("}\n");
        return sb.toString();
    }

    public void mostrarCoincidencias(String texto, javax.swing.JTextArea area) {
        mostrarCoincidenciasRec(raiz, texto.toLowerCase(), area);
    }

    private void mostrarCoincidenciasRec(NodoAVL nodo, String texto, javax.swing.JTextArea area) {
        if (nodo == null) {
            return;
        }

        mostrarCoincidenciasRec(nodo.izq, texto, area);

        Libro l = nodo.libro;
        String t = l.getTitulo().toLowerCase();
        String a = l.getAutor().toLowerCase();
        String g = l.getGenero().toLowerCase();
        String i = l.getIsbn().toLowerCase();
        String anio = String.valueOf(l.getAnio());

        if (t.contains(texto) || a.contains(texto) || g.contains(texto) || i.contains(texto) || anio.equals(texto)) {
            area.append(" " + l.getTitulo() + " | " + l.getAutor() + " | " + l.getAnio() + " | " + l.getGenero() + "\n");
        }

        mostrarCoincidenciasRec(nodo.der, texto, area);
    }

    private void generarDotRec(NodoAVL nodo, StringBuilder sb) {
        if (nodo == null) {
            return;
        }
        sb.append("  \"").append(nodo.libro.getTitulo()).append("\";\n");
        if (nodo.izq != null) {
            sb.append("  \"").append(nodo.libro.getTitulo()).append("\" -> \"")
                    .append(nodo.izq.libro.getTitulo()).append("\";\n");
            generarDotRec(nodo.izq, sb);
        }
        if (nodo.der != null) {
            sb.append("  \"").append(nodo.libro.getTitulo()).append("\" -> \"")
                    .append(nodo.der.libro.getTitulo()).append("\";\n");
            generarDotRec(nodo.der, sb);
        }
    }

    public List<Libro> obtenerTodosLosLibros() {
        List<Libro> resultado = new ArrayList<>();
        obtenerLibrosRec(raiz, resultado);
        return resultado;
    }

    private void obtenerLibrosRec(NodoAVL nodo, List<Libro> lista) {
        if (nodo != null) {
            obtenerLibrosRec(nodo.izq, lista);
            lista.add(nodo.libro);
            obtenerLibrosRec(nodo.der, lista);
        }
    }
}
