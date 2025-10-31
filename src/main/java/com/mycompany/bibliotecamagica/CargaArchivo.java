package com.mycompany.bibliotecamagica;

import com.mycompany.bibliotecamagica.Arboles.Biblioteca;
import com.mycompany.bibliotecamagica.Arboles.Libro;

import com.mycompany.bibliotecamagica.EstructurasBasicas.Grafo;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

public class CargaArchivo {

    private final Map<String, Set<String>> isbnPorColeccion = new HashMap<>();

    private static String claveColeccion(Libro l) {
        return (l.getTitulo() + "|" + l.getAutor()).toLowerCase();
    }

    public List<Biblioteca> cargarBibliotecas(String ruta, Grafo grafo, List<String> errores) {
        List<Biblioteca> bibliotecas = new ArrayList<>();
        File archivo = new File(ruta);

        if (!archivo.exists()) {
            errores.add("No existe el archivo de bibliotecas: " + ruta);
            return bibliotecas;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            boolean primera = true;
            int n = 0;

            while ((linea = br.readLine()) != null) {
                n++;
                if (primera) {
                    primera = false; // Ignorar encabezado
                    continue;
                }
                linea = linea.replace("\"", "").trim();
                if (linea.isBlank()) {
                    continue;
                }

                String[] p = linea.split(",");
                if (p.length < 6) {
                    errores.add("Línea " + n + " inválida (bibliotecas): " + linea);
                    continue;
                }

                try {
                    String id = p[0].trim();
                    String nombre = p[1].trim();
                    String ubicacion = p[2].trim();
                    int tiempoIngreso = Integer.parseInt(p[3].trim());
                    int tiempoTraspaso = Integer.parseInt(p[4].trim());
                    int intervalo = Integer.parseInt(p[5].trim());

                    Biblioteca bibliotecaa = new Biblioteca(id, nombre, ubicacion, tiempoIngreso, tiempoTraspaso, intervalo);
                    bibliotecas.add(bibliotecaa);
                    grafo.agregarBiblioteca(id);
                } catch (Exception ex) {
                    errores.add("Línea " + n + " inválida (valores): " + linea);
                }
            }
        } catch (IOException e) {
            errores.add("Error leyendo bibliotecas: " + e.getMessage());
        }

        return bibliotecas;
    }

    public int cargarLibros(String ruta, Map<String, Biblioteca> porId, List<String> errores) {
        File archivo = new File(ruta);
        if (!archivo.exists()) {
            errores.add("No existe el archivo de libros: " + ruta);
            return 0;
        }

        int ok = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            boolean primera = true;
            int n = 0;

            while ((linea = br.readLine()) != null) {
                n++;
                if (primera) {
                    primera = false;
                    continue;
                }

                linea = linea.replace("\"", "").trim();
                if (linea.isBlank()) {
                    continue;
                }

                String[] p = linea.split(",");
                if (p.length < 6) {
                    errores.add("Línea " + n + " inválida (libros): " + linea);
                    continue;
                }

                try {
                    String titulo = p[0].trim();
                    String autor = p[1].trim();
                    String isbn = p[2].trim();
                    int anio = Integer.parseInt(p[3].trim());
                    String genero = p[4].trim();
                    String idBiblioteca = p[5].trim();

                    if (!Pattern.matches(".*\\S.*", titulo) || !Pattern.matches(".*\\S.*", autor)) {
                        errores.add("Línea " + n + " inválida (titulo/autor vacíos)");
                        continue;
                    }

                    Libro libro = new Libro(titulo, autor, isbn, anio, genero);

                    String clave = claveColeccion(libro);
                    isbnPorColeccion.putIfAbsent(isbn, new HashSet<>());
                    Set<String> colecciones = isbnPorColeccion.get(isbn);
                    if (!colecciones.isEmpty() && !colecciones.contains(clave)) {
                        errores.add("ISBN en colección distinta (bloqueado) en línea " + n + ": " + isbn);
                        continue;
                    }
                    colecciones.add(clave);

                    Biblioteca b = porId.get(idBiblioteca);
                    if (b == null) {
                        errores.add("Biblioteca destino no existe (línea " + n + "): " + idBiblioteca);
                        continue;
                    }

                    b.insertarLibro(libro);
                    ok++;
                } catch (Exception ex) {
                    errores.add("Línea " + n + " inválida (valores): " + linea);
                }
            }
        } catch (IOException e) {
            errores.add("Error leyendo libros: " + e.getMessage());
        }

        return ok;
    }

    public int cargarConexiones(String ruta, Grafo grafo, List<String> errores) {
        File archivo = new File(ruta);
        if (!archivo.exists()) {
            errores.add("No existe el archivo de conexiones: " + ruta);
            return 0;
        }

        int ok = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            boolean primera = true;
            int n = 0;

            while ((linea = br.readLine()) != null) {
                n++;
                if (primera) {
                    primera = false;
                    continue;
                }

                linea = linea.replace("\"", "").trim();
                if (linea.isBlank()) {
                    continue;
                }

                String[] p = linea.split(",");
                if (p.length < 5) {
                    errores.add("Línea " + n + " inválida (conexiones): " + linea);
                    continue;
                }

                try {
                    String origen = p[0].trim();
                    String destino = p[1].trim();
                    int tiempo = Integer.parseInt(p[2].trim());
                    int costo = Integer.parseInt(p[3].trim());
                    boolean bidireccional = Boolean.parseBoolean(p[4].trim());

                    grafo.agregarConexion(origen, destino, tiempo, costo, bidireccional);
                    ok++;
                } catch (Exception ex) {
                    errores.add("Línea " + n + " inválida (valores): " + linea);
                }
            }
        } catch (IOException e) {
            errores.add("Error leyendo conexiones: " + e.getMessage());
        }

        return ok;
    }
}
