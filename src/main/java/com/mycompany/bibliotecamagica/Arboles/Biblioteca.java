package com.mycompany.bibliotecamagica.Arboles;

import com.mycompany.bibliotecamagica.EstructurasBasicas.Cola;
import com.mycompany.bibliotecamagica.EstructurasBasicas.ColaObj;
import com.mycompany.bibliotecamagica.EstructurasBasicas.PilaRollback;
import com.mycompany.bibliotecamagica.EstructurasBasicas.TransferenciaLibro;
import com.mycompany.bibliotecamagica.SimuladorCompleto;

public class Biblioteca implements Runnable {

    private Avl arbolAvl;
    private ArbolB arbolB;
    private ArbolBMas arbolBMas;

    private ColaObj<TransferenciaLibro> colaIngreso;
    private ColaObj<TransferenciaLibro> colaTraspaso;
    private ColaObj<TransferenciaLibro> colaSalida;
    private PilaRollback pilaSalida;

    private String id;
    private String nombre;
    private String ubicacion;
    private int tiempoIngreso;
    private int tiempoTraspaso;
    private int intervaloDespacho;

    private boolean activo = true;

    public Biblioteca(String id, String nombre, String ubicacion,
            int tiempoIngreso, int tiempoTraspaso, int intervaloDespacho) {
        this.id = id;
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.tiempoIngreso = tiempoIngreso;
        this.tiempoTraspaso = tiempoTraspaso;
        this.intervaloDespacho = intervaloDespacho;

        this.arbolAvl = new Avl();
        this.arbolB = new ArbolB();
        this.arbolBMas = new ArbolBMas();

        this.colaIngreso = new ColaObj<>();
        this.colaTraspaso = new ColaObj<>();
        this.colaSalida = new ColaObj<>();
        this.pilaSalida = new PilaRollback();
    }

    @Override
    public void run() {
        try {
            while (activo) {

                if (!colaIngreso.estaVacia()) {
                    TransferenciaLibro t = colaIngreso.desencolar();
                    Thread.sleep(tiempoIngreso * 1000);

                    if (t.getDestino().equals(nombre)) {
                        insertarLibro(t.getLibro());
                        System.out.println(nombre + " recibió el libro final: " + t.getLibro().getTitulo());
                        System.out.println(" Transferencia completada: " + t.getLibro().getTitulo() + " llegó a " + nombre);

                        detener();
                    } else {
                        colaTraspaso.encolar(t);
                    }
                }

                if (!colaTraspaso.estaVacia()) {
                    TransferenciaLibro t = colaTraspaso.desencolar();
                    Thread.sleep(tiempoTraspaso * 1000);
                    colaSalida.encolar(t);
                }

                if (!colaSalida.estaVacia()) {
                    TransferenciaLibro t = colaSalida.desencolar();
                    Thread.sleep(intervaloDespacho * 1000);
                    pilaSalida.Agregar(t.getLibro().hashCode());
                    System.out.println(nombre + " despachó el libro: " + t.getLibro().getTitulo());

                    int idx = t.getRuta().indexOf(nombre);
                    if (idx != -1 && idx + 1 < t.getRuta().size()) {
                        String siguienteNombre = t.getRuta().get(idx + 1);
                        Biblioteca siguiente = SimuladorCompleto.redGlobal.get(siguienteNombre);
                        if (siguiente != null) {
                            siguiente.getColaIngreso().encolar(t);
                            System.out.println(" Enviado a " + siguienteNombre);
                        }
                    } else {
                        System.out.println(" Libro llegó a su destino final: " + t.getDestino());
                    }
                }

                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void detener() {
        activo = false;
    }

    public void insertarLibro(Libro libro) {
        arbolAvl.insertar(libro);
        arbolB.insertar(libro);
        arbolBMas.insertar(libro);
    }

    public String getNombre() {
        return nombre;
    }

    public ColaObj<TransferenciaLibro> getColaIngreso() {
        return colaIngreso;
    }

    public ColaObj<TransferenciaLibro> getColaTraspaso() {
        return colaTraspaso;
    }

    public ColaObj<TransferenciaLibro> getColaSalida() {
        return colaSalida;
    }

    public Avl getArbolAvl() {
        return arbolAvl;
    }

    public ArbolB getArbolB() {
        return arbolB;
    }

    public ArbolBMas getArbolBMas() {
        return arbolBMas;
    }

    public PilaRollback getPilaSalida() {
        return pilaSalida;
    }

}
