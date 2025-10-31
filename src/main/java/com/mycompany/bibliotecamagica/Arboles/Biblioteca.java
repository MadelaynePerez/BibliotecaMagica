package com.mycompany.bibliotecamagica.Arboles;

import com.mycompany.bibliotecamagica.Arboles.Avl;
import com.mycompany.bibliotecamagica.Arboles.Libro;
import com.mycompany.bibliotecamagica.EstructurasBasicas.BusquedaOrdenamiento;
import com.mycompany.bibliotecamagica.EstructurasBasicas.Cola;
import com.mycompany.bibliotecamagica.EstructurasBasicas.Pila;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Biblioteca {

    private Avl arbolLibros;
    private ArbolB arbolB;
    private ArbolBMas arbolBMas;

    private Cola colaIngreso;
    private Cola colaTraspaso;
    private Cola colaSalida;
    private Pila pilaSalida;

    private String id;
    private String nombre;
    private String ubicacion;
    private int tiempoIngreso;
    private int tiempoTraspaso;
    private int intervaloDespacho;

    public Biblioteca(String id, String nombre, String ubicacion,
            int tiempoIngreso, int tiempoTraspaso, int intervaloDespacho) {
        this.id = id;
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.tiempoIngreso = tiempoIngreso;
        this.tiempoTraspaso = tiempoTraspaso;
        this.intervaloDespacho = intervaloDespacho;

        this.arbolLibros = new Avl();
        this.arbolB = new ArbolB();
        this.arbolBMas = new ArbolBMas();

        this.colaIngreso = new Cola();
        this.colaTraspaso = new Cola();
        this.colaSalida = new Cola();
        this.pilaSalida = new Pila();
    }

    public String getId() {
        return id;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public String getNombre() {
        return nombre;
    }

    public int getTiempoIngreso() {
        return tiempoIngreso;
    }

    public int getTiempoTraspaso() {
        return tiempoTraspaso;
    }

    public int getIntervaloDespacho() {
        return intervaloDespacho;
    }

    public Avl getArbolLibros() {
        return arbolLibros;
    }

    public ArbolB getArbolB() {
        return arbolB;
    }

    public ArbolBMas getArbolBMas() {
        return arbolBMas;
    }

    public Cola getColaIngreso() {
        return colaIngreso;
    }

    public Cola getColaTraspaso() {
        return colaTraspaso;
    }

    public Cola getColaSalida() {
        return colaSalida;
    }

    public Pila getPilaSalida() {
        return pilaSalida;
    }

    public void insertarLibro(Libro libro) {
        arbolLibros.insertar(libro);
        System.out.println("Libro agregado al AVL: " + libro.getTitulo());
    }

    public void agregarLibroACola(int valor) {
        colaIngreso.agregarCola(valor);
        System.out.println("Libro agregado a la cola con valor: " + valor);
    }

    public void despacharLibro(int valor) {
        pilaSalida.Agregar(valor);
        System.out.println("Libro despachado a la pila (valor): " + valor);
    }

    public void mostrarLibrosAVL() {
        System.out.println("Libros de la biblioteca " + nombre + ":");
        arbolLibros.mostrarInOrden();
    }

    public void mostrarColas() {
        System.out.println("Cola de ingreso: " + colaIngreso.NumeroDeElementos + " elementos.");
        System.out.println("Cola de traspaso: " + colaTraspaso.NumeroDeElementos + " elementos.");
        System.out.println("Cola de salida: " + colaSalida.NumeroDeElementos + " elementos.");
    }

    public void mostrarPila() {
        System.out.println("Pila de salida: " + pilaSalida.NumeroDeElementos + " elementos.");
    }

    public String resumen() {
        return "Biblioteca: " + nombre
                + "\n - Libros en AVL: (ver consola)"
                + "\n - Elementos en cola de ingreso: " + colaIngreso.NumeroDeElementos
                + "\n - En cola de traspaso: " + colaTraspaso.NumeroDeElementos
                + "\n - En cola de salida: " + colaSalida.NumeroDeElementos
                + "\n - En pila de salida: " + pilaSalida.NumeroDeElementos + "\n";
    }
}
