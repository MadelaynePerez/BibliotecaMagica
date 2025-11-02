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
    private final Map<String, Biblioteca> bibliotecasPorNombre;

    public SimuladorCompleto(Grafo grafo, HashTableISBN hashGlobal,
            Map<String, Biblioteca> bibliotecasPorNombre) {
        this.grafo = grafo;
        this.hashGlobal = hashGlobal;
        this.bibliotecasPorNombre = bibliotecasPorNombre;
    }

    public void transferirLibro(Biblioteca origen, Biblioteca destino, Libro libro, boolean porTiempo) {
        if (hashGlobal.buscar(libro.getIsbn()) == null) {
            hashGlobal.insertar(libro.getIsbn(), libro);
        }

        List<String> ruta = grafo.encontrarRuta(origen.getNombre(), destino.getNombre(), porTiempo);
        if (ruta == null || ruta.isEmpty()) {
            System.out.println("No hay ruta entre " + origen.getNombre() + " y " + destino.getNombre());
            return;
        }

        long eta = estimarTiempo(ruta);
        TransferenciaLibro t = new TransferenciaLibro(libro, origen.getNombre(), destino.getNombre(), porTiempo, ruta, eta);

        origen.getColaIngreso().encolar(t);
        System.out.println("Transferencia creada de " + libro.getTitulo() + " desde "
                + origen.getNombre() + " hacia " + destino.getNombre()
                + " (ETA: " + eta + " ms)");
    }

    private long estimarTiempo(List<String> ruta) {
        return ruta.size() * 1000L;
    }
}
