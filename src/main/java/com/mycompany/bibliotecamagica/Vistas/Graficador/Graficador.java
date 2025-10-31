/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bibliotecamagica.Vistas.Graficador;

import com.mycompany.bibliotecamagica.Arboles.ArbolB;
import com.mycompany.bibliotecamagica.Arboles.ArbolBMas;
import com.mycompany.bibliotecamagica.Arboles.Avl;
import com.mycompany.bibliotecamagica.Arboles.Biblioteca;
import com.mycompany.bibliotecamagica.Arboles.Libro;
import com.mycompany.bibliotecamagica.EstructurasBasicas.Grafo;
import com.mycompany.bibliotecamagica.EstructurasBasicas.HashTableISBN;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Ana
 */
public class Graficador {

    private static final String RUTA_BASE
            = "C:\\Users\\Ana\\Documents\\NetBeansProjects\\BibliotecaMagica\\graficos\\";

    private static void guardarDot(String nombre, String contenido) {
        try {
            java.io.File carpeta = new java.io.File(RUTA_BASE);
            if (!carpeta.exists()) {
                carpeta.mkdirs();
            }

            String dot = RUTA_BASE + nombre + ".dot";
            String png = RUTA_BASE + nombre + ".png";

            try (FileWriter fw = new FileWriter(dot)) {
                fw.write(contenido);
            }

            Runtime.getRuntime().exec("dot -Tpng \"" + dot + "\" -o \"" + png + "\"");
            System.out.println("✅ Gráfico generado: " + png);
        } catch (IOException e) {
            System.err.println("Error al guardar gráfico: " + e.getMessage());
        }
    }

    // 1️⃣ AVL
    public static void graficarAVL(Avl avl) {
        guardarDot("AVL", avl.generarDot());
    }

    // 2️⃣ Árbol B
    public static void graficarArbolB(ArbolB arbol) {
        StringBuilder sb = new StringBuilder("digraph ArbolB {\nnode [shape=record, style=filled, color=lightblue];\n");
        generarDotBRec(arbol.getRaiz(), sb);
        sb.append("}\n");
        guardarDot("ArbolB", sb.toString());
    }

    private static void generarDotBRec(ArbolB.NodoB nodo, StringBuilder sb) {
        if (nodo == null) {
            return;
        }

        sb.append("\"").append(nodo.hashCode()).append("\" [label=\"");
        for (int i = 0; i < nodo.getLibros().size(); i++) {
            sb.append(nodo.getLibros().get(i).getAnio());
            if (i < nodo.getLibros().size() - 1) {
                sb.append("|");
            }
        }
        sb.append("\"];\n");

        if (!nodo.isHoja()) {
            for (ArbolB.NodoB hijo : nodo.getHijos()) {
                sb.append("\"").append(nodo.hashCode()).append("\" -> \"")
                        .append(hijo.hashCode()).append("\";\n");
                generarDotBRec(hijo, sb);
            }
        }
    }

    // 3️⃣ Árbol B+
    public static void graficarArbolBMas(ArbolBMas arbol) {
        arbol.graficar("ArbolBMas");
    }

    // 4️⃣ Tabla Hash (con colisiones y factor de carga)
    public static void graficarHash(HashTableISBN hash) {
        StringBuilder sb = new StringBuilder("digraph HashTable {\n");
        sb.append("node [shape=record, style=filled, color=lightgoldenrod];\n");

        for (int i = 0; i < hash.getTamaño(); i++) {
            List<Libro> lista = hash.getLista(i);
            sb.append("n").append(i).append(" [label=\"").append(i);

            if (lista != null && !lista.isEmpty()) {
                sb.append("|");
                for (int j = 0; j < lista.size(); j++) {
                    sb.append(lista.get(j).getIsbn());
                    if (j < lista.size() - 1) {
                        sb.append("|");
                    }
                }
            }
            sb.append("\"];\n");
        }

        sb.append("}\n");
        guardarDot("TablaHash", sb.toString());
    }

    // 5️⃣ Red de Bibliotecas (grafo)
    public static void graficarRed(Grafo grafo) {
        StringBuilder sb = new StringBuilder("graph RedBibliotecas {\n");
        sb.append("node [shape=box, style=filled, color=lightblue];\n");

        Map<String, List<Grafo.Arista>> ady = grafo.getAdyacencia();
        for (Map.Entry<String, List<Grafo.Arista>> entry : ady.entrySet()) {
            String origen = entry.getKey();
            for (Grafo.Arista a : entry.getValue()) {
                sb.append("\"").append(origen).append("\" -- \"").append(a.destino)
                        .append("\" [label=\"t=").append(a.tiempo)
                        .append(", c=").append(a.costo).append("\"];\n");
            }
        }

        sb.append("}\n");
        guardarDot("RedBibliotecas", sb.toString());
    }

    // 6️⃣ Colas (por biblioteca)
    public static void graficarColas(Biblioteca b) {
        StringBuilder sb = new StringBuilder("digraph Colas {\nrankdir=LR;\nnode [shape=box, style=filled, color=lightgrey];\n");
        sb.append(b.getColaIngreso().generarDot("Ingreso"));
        sb.append(b.getColaTraspaso().generarDot("Traspaso"));
        sb.append(b.getColaSalida().generarDot("Salida"));
        sb.append("}\n");
        guardarDot("Colas_" + b.getNombre(), sb.toString());
    }
}
