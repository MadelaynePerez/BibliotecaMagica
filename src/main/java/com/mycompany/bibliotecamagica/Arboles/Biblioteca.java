package com.mycompany.bibliotecamagica.Arboles;

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

    // Contadores
    private int ingresos;
    private int traspasos;
    private int salidas;

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

        this.activo = true;
    }

    @Override
    public void run() {
        try {
            while (activo) {

                if (!colaIngreso.estaVacia()) {
                    TransferenciaLibro t = colaIngreso.desencolar();
                    ingresos++;
                    System.out.println(nombre + " Ingreso: " + t.getLibro().getTitulo());
                    Thread.sleep(tiempoIngreso * 1000);

                    if (t.getDestino().equals(nombre)) {
                        insertarLibro(t.getLibro());
                        pilaSalida.Agregar(t.getLibro().hashCode());
                        System.out.println(nombre + " recibió libro final: " + t.getLibro().getTitulo());
                    } else {
                        colaTraspaso.encolar(t);
                        System.out.println(nombre + " pasa a TRASPASO: " + t.getLibro().getTitulo());
                    }
                }

                if (!colaTraspaso.estaVacia()) {
                    TransferenciaLibro t = colaTraspaso.desencolar();
                    traspasos++;
                    System.out.println(nombre + " Traspasando: " + t.getLibro().getTitulo());
                    Thread.sleep(tiempoTraspaso * 1000);

                    colaSalida.encolar(t);
                    System.out.println(nombre + " pasa a SALIDA: " + t.getLibro().getTitulo());
                }

                if (!colaSalida.estaVacia()) {
                    TransferenciaLibro t = colaSalida.desencolar();
                    salidas++;
                    System.out.println(nombre + " Despachando: " + t.getLibro().getTitulo());
                    Thread.sleep(intervaloDespacho * 1000);

                    int idx = t.getRuta().indexOf(nombre);
                    if (idx != -1 && idx + 1 < t.getRuta().size()) {
                        String siguienteNombre = t.getRuta().get(idx + 1);
                        Biblioteca siguiente = SimuladorCompleto.redGlobal.get(siguienteNombre);
                        if (siguiente != null) {
                            siguiente.getColaIngreso().encolar(t);
                            System.out.println(nombre + " envió " + t.getLibro().getTitulo() + " a " + siguienteNombre);
                        }
                    } else {
                        insertarLibro(t.getLibro());
                        pilaSalida.Agregar(t.getLibro().hashCode());
                        System.out.println(nombre + " entrega finalizada: " + t.getLibro().getTitulo());
                    }
                }

                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void detener() {
        this.activo = false;
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

    public String getId() {
        return id;
    }

    public String getUbicacion() {
        return ubicacion;
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

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public void setTiempos(int tiempoIngreso, int tiempoTraspaso, int intervaloDespacho) {
        this.tiempoIngreso = tiempoIngreso;
        this.tiempoTraspaso = tiempoTraspaso;
        this.intervaloDespacho = intervaloDespacho;
    }

    public int getIngresos() {
        return ingresos;
    }

    public int getTraspasos() {
        return traspasos;
    }

    public int getSalidas() {
        return salidas;
    }

    public int getPila() {
        return pilaSalida.getNumeroDeElementos();
    }
}
