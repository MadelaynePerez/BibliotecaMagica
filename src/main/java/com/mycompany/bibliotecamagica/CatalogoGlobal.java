package com.mycompany.bibliotecamagica;

import com.mycompany.bibliotecamagica.Arboles.Biblioteca;
import com.mycompany.bibliotecamagica.Arboles.Libro;
import com.mycompany.bibliotecamagica.EstructurasBasicas.HashTableISBN;
import java.util.Map;

public class CatalogoGlobal {

    private final Map<String, Biblioteca> bibliotecasPorId;
    private final HashTableISBN hash;

    public CatalogoGlobal(Map<String, Biblioteca> bibliotecasPorId, HashTableISBN hash) {
        this.bibliotecasPorId = bibliotecasPorId;
        this.hash = hash;
    }

    public Map<String, Biblioteca> getBibliotecas() {
        return bibliotecasPorId;
    }

    public HashTableISBN getHash() {
        return hash;
    }

    public void insertarLibroEn(String idBiblioteca, Libro libro) {
        Biblioteca b = bibliotecasPorId.get(idBiblioteca);

        if (b == null) {
            if (!bibliotecasPorId.isEmpty()) {
                b = bibliotecasPorId.values().iterator().next();
                System.out.println("Biblioteca no encontrada, usando: " + b.getNombre());
            } else {
                System.out.println("No hay bibliotecas registradas.");
                return;
            }
        }

        b.getArbolAvl().insertar(libro);
        b.getArbolB().insertar(libro);
        b.getArbolBMas().insertar(libro);

        hash.insertar(libro.getIsbn(), libro);
        System.out.println("Libro agregado a " + b.getNombre() + ": " + libro.getTitulo());
    }

    public void eliminarLibro(String isbn) {
        Object obj = hash.buscar(isbn);
        if (obj == null) {
            System.out.println("No existe el ISBN " + isbn + " en el hash.");
            return;
        }

        if (!(obj instanceof Libro libro)) {
            System.out.println("La clave " + isbn + " no corresponde a un libro.");
            return;
        }

        for (Biblioteca b : bibliotecasPorId.values()) {
            b.getArbolAvl().eliminar(libro.getTitulo());
            b.getArbolB().eliminarPorAnio(libro.getAnio(), libro.getTitulo());

        }

        hash.eliminar(isbn);
        System.out.println("Libro eliminado globalmente: " + libro.getTitulo());
    }

    public void marcarAgotado(String isbn) {
        Object obj = hash.buscar(isbn);
        if (obj instanceof Libro l) {
            l.setEstado("Agotado");
            System.out.println("Libro marcado como agotado: " + l.getTitulo());
        }
    }

    public void devolverLibro(String idBiblioteca) {
        Biblioteca b = bibliotecasPorId.get(idBiblioteca);
        if (b == null) {
            System.out.println("Biblioteca no encontrada para rollback.");
            return;
        }

        Integer valor = b.getPilaSalida().Remover();
        if (valor == null) {
            System.out.println("No hay libros para devolver en " + b.getNombre());
            return;
        }

        System.out.println("Libro devuelto desde pila de " + b.getNombre() + " (hash=" + valor + ")");
    }
}
