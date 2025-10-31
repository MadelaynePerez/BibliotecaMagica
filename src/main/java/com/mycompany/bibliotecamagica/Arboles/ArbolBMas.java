/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bibliotecamagica.Arboles;

/**
 *
 * @author Ana
 */
import java.util.*;

public class ArbolBMas {

    private static final int ORDEN = 4;
    private NodoBMas raiz;

    static class NodoBMas {

        boolean hoja;
        ArrayList<String> claves;
        ArrayList<ArrayList<Libro>> libros;
        ArrayList<NodoBMas> hijos;
        NodoBMas siguiente;

        NodoBMas(boolean hoja) {
            this.hoja = hoja;
            claves = new ArrayList<>();
            libros = new ArrayList<>();
            hijos = new ArrayList<>();
            siguiente = null;
        }
    }

    public ArbolBMas() {
        raiz = new NodoBMas(true);
    }

    public void insertar(Libro libro) {
        if (raiz == null) {
            raiz = new NodoBMas(true);
        }

        Par resultado = insertarRec(raiz, libro);

        if (resultado.nodoDerecho != null) {
            NodoBMas nuevaRaiz = new NodoBMas(false);
            nuevaRaiz.claves.add(resultado.clave);
            nuevaRaiz.hijos.add(raiz);
            nuevaRaiz.hijos.add(resultado.nodoDerecho);
            raiz = nuevaRaiz;
        }
    }

    private Par insertarRec(NodoBMas nodo, Libro libro) {
        if (nodo.hoja) {
            int idx = Collections.binarySearch(nodo.claves, libro.getGenero());
            if (idx >= 0) {
                nodo.libros.get(idx).add(libro);
            } else {
                idx = -idx - 1;
                nodo.claves.add(idx, libro.getGenero());
                ArrayList<Libro> lista = new ArrayList<>();
                lista.add(libro);
                nodo.libros.add(idx, lista);
            }

            if (nodo.claves.size() >= ORDEN) {
                return dividirHoja(nodo);
            }
            return new Par("", null);
        } else {
            int i = 0;
            while (i < nodo.claves.size() && libro.getGenero().compareTo(nodo.claves.get(i)) > 0) {
                i++;
            }
            Par resultado = insertarRec(nodo.hijos.get(i), libro);

            if (resultado.nodoDerecho != null) {
                nodo.claves.add(i, resultado.clave);
                nodo.hijos.add(i + 1, resultado.nodoDerecho);
                if (nodo.claves.size() >= ORDEN) {
                    return dividirInterno(nodo);
                }
            }
            return new Par("", null);
        }
    }

    private Par dividirHoja(NodoBMas nodo) {
        int med = nodo.claves.size() / 2;
        NodoBMas derecha = new NodoBMas(true);

        derecha.claves.addAll(nodo.claves.subList(med, nodo.claves.size()));
        derecha.libros.addAll(nodo.libros.subList(med, nodo.libros.size()));

        nodo.claves = new ArrayList<>(nodo.claves.subList(0, med));
        nodo.libros = new ArrayList<>(nodo.libros.subList(0, med));

        derecha.siguiente = nodo.siguiente;
        nodo.siguiente = derecha;

        return new Par(derecha.claves.get(0), derecha);
    }

    private Par dividirInterno(NodoBMas nodo) {
        int med = nodo.claves.size() / 2;
        NodoBMas derecha = new NodoBMas(false);

        derecha.claves.addAll(nodo.claves.subList(med + 1, nodo.claves.size()));
        derecha.hijos.addAll(nodo.hijos.subList(med + 1, nodo.hijos.size()));

        String mediana = nodo.claves.get(med);

        nodo.claves = new ArrayList<>(nodo.claves.subList(0, med));
        nodo.hijos = new ArrayList<>(nodo.hijos.subList(0, med + 1));

        return new Par(mediana, derecha);
    }

    private static class Par {

        String clave;
        NodoBMas nodoDerecho;

        Par(String clave, NodoBMas nodoDerecho) {
            this.clave = clave;
            this.nodoDerecho = nodoDerecho;
        }
    }

    public ArrayList<Libro> buscarPorGenero(String genero) {
        NodoBMas nodo = raiz;
        while (!nodo.hoja) {
            int i = 0;
            while (i < nodo.claves.size() && genero.compareTo(nodo.claves.get(i)) > 0) {
                i++;
            }
            nodo = nodo.hijos.get(i);
        }

        for (int i = 0; i < nodo.claves.size(); i++) {
            if (nodo.claves.get(i).equals(genero)) {
                return nodo.libros.get(i);
            }
        }
        return new ArrayList<>();
    }

    public void mostrar() {
        NodoBMas nodo = raiz;
        while (!nodo.hoja) {
            nodo = nodo.hijos.get(0);
        }

        System.out.println("\nLibros por genero:");
        while (nodo != null) {
            for (int i = 0; i < nodo.claves.size(); i++) {
                System.out.println("- " + nodo.claves.get(i) + " (" + nodo.libros.get(i).size() + " libros)");
            }
            nodo = nodo.siguiente;
        }
    }

    public void graficar(String nombreArchivo) {
        try {
            String ruta = "C:\\Users\\Ana\\Documents\\NetBeansProjects\\BibliotecaMagica\\graficos\\" + nombreArchivo + ".dot";
            String png = "C:\\Users\\Ana\\Documents\\NetBeansProjects\\BibliotecaMagica\\graficos\\" + nombreArchivo + ".png";

            java.io.File carpeta = new java.io.File("C:\\Users\\Ana\\Documents\\NetBeansProjects\\BibliotecaMagica\\graficos\\");
            if (!carpeta.exists()) {
                carpeta.mkdirs();
            }

            java.io.FileWriter fw = new java.io.FileWriter(ruta);
            fw.write("digraph BMas {\nnode [shape=record, style=filled, color=lightyellow];\n");
            generarDotRec(raiz, fw);
            fw.write("}\n");
            fw.close();

            Runtime.getRuntime().exec("dot -Tpng \"" + ruta + "\" -o \"" + png + "\"");
            System.out.println("Archivo " + png + " generado.");
        } catch (Exception e) {
            System.err.println("Error al graficar: " + e.getMessage());
        }
    }

    private void generarDotRec(NodoBMas nodo, java.io.FileWriter fw) throws Exception {
        if (nodo == null) {
            return;
        }

        fw.write("\"" + nodo.hashCode() + "\" [label=\"");
        for (int i = 0; i < nodo.claves.size(); i++) {
            fw.write(nodo.claves.get(i));
            if (i != nodo.claves.size() - 1) {
                fw.write("|");
            }
        }
        fw.write("\"];\n");

        if (!nodo.hoja) {
            for (NodoBMas hijo : nodo.hijos) {
                fw.write("\"" + nodo.hashCode() + "\" -> \"" + hijo.hashCode() + "\";\n");
                generarDotRec(hijo, fw);
            }
        }
    }
}
