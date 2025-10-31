/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bibliotecamagica;

import com.mycompany.bibliotecamagica.Arboles.Biblioteca;
import com.mycompany.bibliotecamagica.Arboles.Libro;
import com.mycompany.bibliotecamagica.EstructurasBasicas.Grafo;
import com.mycompany.bibliotecamagica.EstructurasBasicas.HashTableISBN;
import java.util.List;

/**
 * @author Ana
 */
public class SimuladorCompleto {

    private Grafo grafo;
    private HashTableISBN hashGlobal;

    public SimuladorCompleto(Grafo grafo, HashTableISBN hashGlobal) {
        this.grafo = grafo;
        this.hashGlobal = hashGlobal;
    }

    public void transferirLibro(Biblioteca origen, Biblioteca destino, Libro libro, boolean porTiempo) {
        System.out.println("\n=== SIMULACIÓN COMPLETA DE TRANSFERENCIA ===");
        System.out.println("Libro: " + libro.getTitulo());
        System.out.println("Origen: " + origen.getNombre());
        System.out.println("Destino: " + destino.getNombre());
        System.out.println("Criterio: " + (porTiempo ? "Tiempo" : "Costo") + "\n");

        if (hashGlobal.buscar(libro.getIsbn()) != null) {
            System.out.println("El ISBN ya existe en el sistema, se omite duplicado: " + libro.getIsbn());
            return;
        }

        long inicio = System.currentTimeMillis();
        List<String> ruta = grafo.encontrarRuta(origen.getNombre(), destino.getNombre(), porTiempo);
        if (ruta == null || ruta.isEmpty()) {
            System.out.println("No hay ruta entre las bibliotecas seleccionadas.");
            return;
        }

        System.out.println("Ruta elegida: " + String.join(" -> ", ruta));

        libro.setEstado("En tránsito");
        origen.getColaIngreso().agregarCola(libro.hashCode());
        hashGlobal.insertar(libro);
        System.out.println("\nLibro colocado en la cola de ingreso de " + origen.getNombre());

        for (int i = 0; i < ruta.size(); i++) {
            String nombreB = ruta.get(i);
            System.out.println("\nBiblioteca actual: " + nombreB);

            try {
                Thread.sleep(700);
            } catch (InterruptedException ignored) {
            }

            if (i < ruta.size() - 1) {
                System.out.println("Preparando traspaso...");
                origen.getColaIngreso().RemoverCola();
                origen.getColaTraspaso().agregarCola(libro.hashCode());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ignored) {
                }
                origen.getColaTraspaso().RemoverCola();
                origen.getColaSalida().agregarCola(libro.hashCode());
                System.out.println("Libro enviado hacia el siguiente nodo...");
            } else {
                System.out.println("Llegó al destino final: " + destino.getNombre());
                libro.setEstado("Disponible");
                destino.insertarLibro(libro);
                destino.getArbolB().insertar(libro);
                destino.getArbolBMas().insertar(libro);
            }
        }

        long fin = System.currentTimeMillis();
        System.out.println("\nTiempo total de transferencia: " + (fin - inicio) + " ms");
        System.out.println("Estado final: " + libro.getEstado());
        System.out.println("=== SIMULACIÓN FINALIZADA ===\n");
    }
}
