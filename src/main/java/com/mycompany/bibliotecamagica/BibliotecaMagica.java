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
import com.mycompany.bibliotecamagica.EstructurasBasicas.PilaRollback;
import com.mycompany.bibliotecamagica.EstructurasBasicas.HashTableISBN;
import com.mycompany.bibliotecamagica.Vistas.Graficador.Graficador;
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

        System.out.println("🧩 INICIANDO PRUEBAS DE VISUALIZACION...\n");

        // === 1️⃣ AVL ===
        Avl avl = new Avl();
        avl.insertar(new Libro("978-01", "Cien Años", "Realismo", 1985, "Gabo"));
        avl.insertar(new Libro("978-02", "Rayuela", "Surrealismo", 1963, "Cortázar"));
        avl.insertar(new Libro("978-03", "El Quijote", "Clásico", 1605, "Cervantes"));
        avl.insertar(new Libro("978-04", "La Odisea", "Épico", -800, "Homero"));
        avl.insertar(new Libro("978-05", "Ficciones", "Fantasía", 1944, "Borges"));
        System.out.println("✅ AVL generado.");
        Graficador.graficarAVL(avl);

        // === 2️⃣ Árbol B ===
        ArbolB arbolB = new ArbolB();
        arbolB.insertar(new Libro("978-06", "Alicia", "Infantil", 1865, "Carroll"));
        arbolB.insertar(new Libro("978-07", "Ulises", "Moderno", 1922, "Joyce"));
        arbolB.insertar(new Libro("978-08", "Hamlet", "Drama", 1603, "Shakespeare"));
        arbolB.insertar(new Libro("978-09", "Crimen", "Drama", 1866, "Dostoievski"));
        arbolB.insertar(new Libro("978-10", "Divina Comedia", "Épico", 1320, "Dante"));
        System.out.println("✅ Árbol B generado.");
        Graficador.graficarArbolB(arbolB);

        // === 3️⃣ Árbol B+ ===
        ArbolBMas arbolBMas = new ArbolBMas();
        arbolBMas.insertar(new Libro("It", "Stephen King", "978-01", 1986, "Terror"));
        arbolBMas.insertar(new Libro("Sandman", "Neil Gaiman", "978-02", 1989, "Fantasía"));
        arbolBMas.insertar(new Libro("Cementerio de animales", "Stephen King", "978-03", 1983, "Terror"));
        arbolBMas.insertar(new Libro("Coraline", "Neil Gaiman", "978-04", 2002, "Infantil"));
        arbolBMas.insertar(new Libro("Good Omens", "Gaiman y Pratchett", "978-05", 1990, "Comedia"));
        arbolBMas.insertar(new Libro("Carrie", "Stephen King", "978-06", 1974, "Horror"));
        arbolBMas.insertar(new Libro("American Gods", "Neil Gaiman", "978-07", 2001, "Mitología"));
        arbolBMas.insertar(new Libro("El Resplandor", "Stephen King", "978-08", 1977, "Psicológico"));
        arbolBMas.graficar("ArbolBMas");

        // === 4️⃣ Tabla Hash ===
        HashTableISBN hash = new HashTableISBN(10);
        hash.insertar(new Libro("111-A", "Matematicas", "Educativo", 2010, "Smith"));
        hash.insertar(new Libro("222-B", "Programacion", "Tecnología", 2018, "Ana Perez"));
        hash.insertar(new Libro("333-C", "Historia", "Social", 2005, "Carlos R."));
        hash.insertar(new Libro("444-D", "Quimica", "Ciencia", 2015, "Lopez"));
        System.out.println("✅ Tabla Hash generada.");
        Graficador.graficarHash(hash);

        // === 5️⃣ Grafo (Red de Bibliotecas) ===
        Grafo grafo = new Grafo();
        grafo.agregarConexion("Biblioteca Central", "Biblioteca Zona 1", 10, 5, true);
        grafo.agregarConexion("Biblioteca Central", "Biblioteca Norte", 20, 10, true);
        grafo.agregarConexion("Biblioteca Zona 1", "Biblioteca Sur", 15, 8, true);
        grafo.agregarConexion("Biblioteca Norte", "Biblioteca Sur", 25, 12, true);
        System.out.println("✅ Red de bibliotecas generada.");
        Graficador.graficarRed(grafo);

        // === 6️⃣ Colas ===
        Biblioteca biblio = new Biblioteca(
                "Biblioteca Central",
                "Zona 1, Quetzaltenango",
                "Ana Pérez",
                1,
                200,
                1
        );
        biblio.getColaIngreso().agregarCola(101);
        biblio.getColaIngreso().agregarCola(102);
        biblio.getColaTraspaso().agregarCola(201);
        biblio.getColaSalida().agregarCola(301);
        biblio.getColaSalida().agregarCola(302);
        System.out.println("✅ Colas generadas.");
        Graficador.graficarColas(biblio);

        System.out.println("\n🎉 TODAS LAS VISUALIZACIONES GENERADAS EXITOSAMENTE 🎨");
    }

}
