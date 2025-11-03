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
        Grafo grafo = new Grafo();
        HashTableISBN hash = new HashTableISBN();
        Map<String, Biblioteca> red = new java.util.HashMap<>();

        Biblioteca a = new Biblioteca("1", "Biblioteca Central", "Centro", 2, 1, 2);
        Biblioteca b = new Biblioteca("2", "Biblioteca Norte", "Zona 5", 1, 1, 1);
        Biblioteca c = new Biblioteca("3", "Biblioteca Sur", "Zona 8", 3, 2, 2);

        red.put(a.getNombre(), a);
        red.put(b.getNombre(), b);
        red.put(c.getNombre(), c);

        grafo.registrarBiblioteca(a);
        grafo.registrarBiblioteca(b);
        grafo.registrarBiblioteca(c);
        grafo.agregarConexion(a.getNombre(), b.getNombre(), 5, 10, true);
        grafo.agregarConexion(b.getNombre(), c.getNombre(), 4, 8, true);

        Libro libro = new Libro("Harry Potter", "J.K. Rowling", "HP-001", 1997, "Fantasía");

        SimuladorCompleto simulador = new SimuladorCompleto(grafo, hash, red);
        simulador.iniciarHilos();
        simulador.transferirLibro(a.getNombre(), c.getNombre(), libro, true);
    }

}
