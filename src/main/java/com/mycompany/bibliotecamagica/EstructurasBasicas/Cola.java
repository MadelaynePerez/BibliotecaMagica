/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bibliotecamagica.EstructurasBasicas;

/**
 *
 * @author Ana
 */
public class Cola {

    Nodo ValorInicial = null;
    Nodo Pivote = null;
    public int NumeroDeElementos = 0;

    public void agregarCola(int valor) {
        if (ValorInicial == null) {
            Nodo temporal = new Nodo(valor);
            ValorInicial = temporal;
            Pivote = ValorInicial;
        } else {
            Nodo tmp2 = new Nodo(valor);
            Pivote.Anterior = tmp2;
            Pivote = Pivote.Anterior;
        }
        NumeroDeElementos += 1;
    }

    public Nodo RemoverCola() {
        if (NumeroDeElementos == 0) {
            return null;
        }
        Nodo cola = ValorInicial;
        ValorInicial = cola.Anterior;
        NumeroDeElementos -= 1;

        return cola;
    }

    public String generarDot(String nombre) {
        StringBuilder sb = new StringBuilder();
        sb.append("subgraph cluster_").append(nombre).append(" { label=\"")
                .append(nombre).append("\";\n");

        Nodo actual = ValorInicial;
        while (actual != null) {
            sb.append("\"").append(nombre).append("_").append(actual.hashCode())
                    .append("\" [label=\"").append(actual.Valor).append("\"];\n");

            if (actual.Siguiente != null) {
                sb.append("\"").append(nombre).append("_").append(actual.hashCode())
                        .append("\" -> \"").append(nombre).append("_")
                        .append(actual.Siguiente.hashCode()).append("\";\n");
            }
            actual = actual.Siguiente;
        }

        sb.append("}\n");
        return sb.toString();
    }
}
