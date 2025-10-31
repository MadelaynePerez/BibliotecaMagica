/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.bibliotecamagica;

import com.mycompany.bibliotecamagica.Arboles.Biblioteca;
import com.mycompany.bibliotecamagica.Arboles.ArbolB;
import com.mycompany.bibliotecamagica.Arboles.ArbolBMas;
import com.mycompany.bibliotecamagica.Arboles.Avl;
import com.mycompany.bibliotecamagica.Arboles.Libro;
import com.mycompany.bibliotecamagica.EstructurasBasicas.Cola;
import com.mycompany.bibliotecamagica.EstructurasBasicas.Grafo;
import com.mycompany.bibliotecamagica.EstructurasBasicas.Nodo;
import com.mycompany.bibliotecamagica.EstructurasBasicas.Pila;
import com.mycompany.bibliotecamagica.EstructurasBasicas.TablaHash;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Ana
 */
public class BibliotecaMagica {

    public static void main(String[] args) {
        System.out.println("Hello World!");

        Grafo grafo = new Grafo();
        CargaArchivo carga = new CargaArchivo();
        List<String> errores = new ArrayList<>();

        // === RUTAS DE ARCHIVOS ===
        String rutaBibliotecas = "C:\\Users\\Ana\\Desktop\\Bibliotecas.csv";
        String rutaLibros = "C:\\Users\\Ana\\Desktop\\Libro.csv";
        String rutaConexiones = "C:\\Users\\Ana\\Desktop\\coneciones.csv";

        // === CARGA DE DATOS ===
        List<Biblioteca> bibliotecas = carga.cargarBibliotecas(rutaBibliotecas, grafo, errores);
        Map<String, Biblioteca> mapaPorId = new HashMap<>();
        for (Biblioteca b : bibliotecas) {
            mapaPorId.put(b.getId(), b);
        }

        carga.cargarLibros(rutaLibros, mapaPorId, errores);
        carga.cargarConexiones(rutaConexiones, grafo, errores);

        if (!errores.isEmpty()) {
            System.out.println("=== ERRORES DETECTADOS ===");
            errores.forEach(System.out::println);
        }

        System.out.println("\n=== BIBLIOTECAS CARGADAS ===");
        for (Biblioteca b : bibliotecas) {
            System.out.println(b.resumen());
        }

        // === PRUEBA MANUAL ===
        Biblioteca b1 = bibliotecas.get(0);
        System.out.println("\nInsertando libro manualmente en " + b1.getNombre());
        Libro nuevo = new Libro("Harry Potter", "J.K. Rowling", "978-84-376-0494-7", 1997, "Fantasía");
        b1.insertarLibro(nuevo);

        System.out.println("\n=== AVL de " + b1.getNombre() + " ===");
        b1.mostrarLibrosAVL();

        // === BUSCAR POR TÍTULO ===
        System.out.println("\nBuscando 'Harry Potter' en AVL...");
        Libro encontrado = b1.getArbolLibros().buscar("Harry Potter");
        if (encontrado != null) {
            System.out.println("Libro encontrado: " + encontrado);
        } else {
            System.out.println("Libro no encontrado.");
        }

        // === ELIMINAR Y BUSCAR NUEVAMENTE ===
        System.out.println("\nEliminando 'Harry Potter' del árbol...");
        // Si tienes implementado el eliminar, llámalo aquí.
         b1.getArbolLibros().eliminar("Harry Potter");

        System.out.println("\nBuscando nuevamente 'Harry Potter'...");
        encontrado = b1.getArbolLibros().buscar("Harry Potter");
        if (encontrado != null) {
            System.out.println("Libro encontrado: " + encontrado);
        } else {
            System.out.println("Libro no encontrado (correcto).");
        }

       
        //System.out.println("\n=== GRAFO DE BIBLIOTECAS ===");
        //grafo.mostrarGrafo();

        // === PRUEBA DE COLAS Y PILAS ===
        b1.agregarLibroACola(1);
        b1.agregarLibroACola(2);
        b1.mostrarCola();

        b1.despacharLibro(100);
        b1.despacharLibro(200);
        b1.mostrarPila();

        System.out.println("\n===== PRUEBA FINALIZADA =====");
    }
}
