package com.mycompany.bibliotecamagica;

import com.mycompany.bibliotecamagica.Arboles.Biblioteca;
import com.mycompany.bibliotecamagica.Arboles.Libro;
import com.mycompany.bibliotecamagica.EstructurasBasicas.Grafo;
import com.mycompany.bibliotecamagica.EstructurasBasicas.HashTableISBN;
import com.mycompany.bibliotecamagica.EstructurasBasicas.TransferenciaLibro;
import java.util.List;
import java.util.Map;

public class SimuladorCompleto {

    private final Grafo grafo;
    private final HashTableISBN hashGlobal;
    private final Map<String, Biblioteca> bibliotecas;
    public static Map<String, Biblioteca> redGlobal;

    public SimuladorCompleto(Grafo grafo, HashTableISBN hashGlobal, Map<String, Biblioteca> bibliotecas) {
        this.grafo = grafo;
        this.hashGlobal = hashGlobal;
        this.bibliotecas = bibliotecas;
        SimuladorCompleto.redGlobal = bibliotecas;
    }

    public void iniciarHilos() {
        for (Biblioteca b : bibliotecas.values()) {
            Thread hilo = new Thread(b);
            hilo.start();
        }
    }

    public void detenerHilos() {
        for (Biblioteca b : bibliotecas.values()) {
            b.detener();
        }
    }

    public void transferirLibro(String origenNombre, String destinoNombre, Libro libro, boolean porTiempo) {
        Biblioteca origen = bibliotecas.get(origenNombre);
        Biblioteca destino = bibliotecas.get(destinoNombre);

        if (origen == null || destino == null) {
            System.out.println(" Biblioteca no encontrada");
            return;
        }

        if (hashGlobal.buscar(libro.getIsbn()) == null) {
            hashGlobal.insertar(libro.getIsbn(), libro);
        }

        var resultado = grafo.dijkstra(origenNombre, destinoNombre,
                porTiempo ? Grafo.Criterio.TIEMPO : Grafo.Criterio.COSTO);

        if (resultado.ruta.isEmpty()) {
            System.out.println("No hay ruta entre " + origenNombre + " y " + destinoNombre);
            return;
        }

        TransferenciaLibro transferencia = new TransferenciaLibro(
                libro, origenNombre, destinoNombre, porTiempo, resultado.ruta, resultado.pesoTotal);

        origen.getColaIngreso().encolar(transferencia);
        System.out.println(" Iniciando transferencia de \"" + libro.getTitulo() + "\" desde "
                + origenNombre + " hacia " + destinoNombre);
        System.out.println("Ruta: " + resultado.ruta);
    }
    public HashTableISBN getHash() {
    return this.hashGlobal;
}
}
