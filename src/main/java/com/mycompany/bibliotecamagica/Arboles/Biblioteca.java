package com.mycompany.bibliotecamagica.Arboles;


import com.mycompany.bibliotecamagica.Arboles.Avl;
import com.mycompany.bibliotecamagica.Arboles.Libro;
import com.mycompany.bibliotecamagica.EstructurasBasicas.Cola;
import com.mycompany.bibliotecamagica.EstructurasBasicas.Pila;

public class Biblioteca {

    private String id;
    private String nombre;
    private String ubicacion;
    private int tiempoIngreso;
    private int tiempoTraspaso;
    private int intervaloDespacho;

    private Avl arbolLibros;
    private Cola colaIngreso;
    private Pila pilaSalida;

    public Biblioteca(String id, String nombre, String ubicacion, int tiempoIngreso, int tiempoTraspaso, int intervaloDespacho) {
        this.id = id;
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.tiempoIngreso = tiempoIngreso;
        this.tiempoTraspaso = tiempoTraspaso;
        this.intervaloDespacho = intervaloDespacho;

        this.arbolLibros = new Avl();
        this.colaIngreso = new Cola();
        this.pilaSalida = new Pila();
    }

    public String getId() {
        return id;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void insertarLibro(Libro libro) {
        arbolLibros.insertar(libro);
        System.out.println(" Libro agregado al AVL: " + libro.getTitulo());
    }

    public void agregarLibroACola(int valor) {
        colaIngreso.agregarCola(valor);
        System.out.println(" Libro agregado a la cola con valor: " + valor);
    }

    public void despacharLibro(int valor) {
        pilaSalida.Agregar(valor);
        System.out.println(" Libro despachado a la pila (valor): " + valor);
    }

    public void mostrarLibrosAVL() {
        System.out.println(" Libros de la biblioteca " + nombre + ":");
        arbolLibros.mostrarInOrden();
    }

    public void mostrarCola() {
        System.out.println(" Cola actual: " + colaIngreso.NumeroDeElementos + " elementos.");
    }

    public void mostrarPila() {
        System.out.println("🧱 Pila actual: " + pilaSalida.NumeroDeElementos + " elementos.");
    }

    public String getNombre() {
        return nombre;
    }

    public Avl getArbolLibros() {
        return arbolLibros;
    }

    public Cola getColaIngreso() {
        return colaIngreso;
    }

    public Pila getPilaSalida() {
        return pilaSalida;
    }

    public String resumen() {
        return "️ Biblioteca: " + nombre
                + "\n - Libros en AVL: (ver consola)"
                + "\n - Elementos en cola: " + colaIngreso.NumeroDeElementos
                + "\n - Libros despachados en pila: " + pilaSalida.NumeroDeElementos + "\n";
    }
}
