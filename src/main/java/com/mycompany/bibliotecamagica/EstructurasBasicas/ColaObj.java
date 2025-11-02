/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bibliotecamagica.EstructurasBasicas;

import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author Ana
 */
public class ColaObj<T> {

    private final Queue<T> elementos;

    public ColaObj() {
        this.elementos = new LinkedList<>();
    }

    public void encolar(T valor) {
        elementos.add(valor);
    }

    public T desencolar() {
        return elementos.poll();
    }

    public T frente() {
        return elementos.peek();
    }

    public boolean estaVacia() {
        return elementos.isEmpty();
    }

    public int size() {
        return elementos.size();
    }

    public void limpiar() {
        elementos.clear();
    }

    public void mostrar() {
        for (T elem : elementos) {
            System.out.println(elem);
        }
    }

    public String generarDot(String nombre) {
        StringBuilder sb = new StringBuilder();
        sb.append("subgraph cluster_").append(nombre).append(" { label=\"")
                .append(nombre).append("\";\n");

        int contador = 0;
        for (T elemento : elementos) {
            sb.append("\"").append(nombre).append("_").append(contador)
                    .append("\" [label=\"").append(elemento.toString()).append("\"];\n");
            contador++;
        }

        sb.append("}\n");
        return sb.toString();
    }
}
