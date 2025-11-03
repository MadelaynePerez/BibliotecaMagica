package com.mycompany.bibliotecamagica;

import com.mycompany.bibliotecamagica.Arboles.Biblioteca;
import com.mycompany.bibliotecamagica.Arboles.Libro;
import com.mycompany.bibliotecamagica.EstructurasBasicas.Grafo;
import com.mycompany.bibliotecamagica.EstructurasBasicas.HashTableISBN;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
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
                    primera = false;
                    continue;
                }

                linea = linea.trim();
                if (linea.isBlank()) {
                    continue;
                }

                String[] p = parseCSVLine(linea);
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

                    Biblioteca biblio = new Biblioteca(id, nombre, ubicacion, tiempoIngreso, tiempoTraspaso, intervalo);
                    bibliotecas.add(biblio);
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

    public int cargarLibros(String ruta, Map<String, Biblioteca> bibliotecas,
            Grafo grafo, SimuladorCompleto simulador, List<String> errores) {

        File archivo = new File(ruta);
        if (!archivo.exists()) {
            errores.add("No existe el archivo de libros: " + ruta);
            return 0;
        }

        int ok = 0;
        boolean hilosIniciados = false; 

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(archivo), java.nio.charset.StandardCharsets.UTF_8))) {

            String linea;
            boolean primera = true;
            int n = 0;

            while ((linea = br.readLine()) != null) {
                n++;
                if (primera) {
                    primera = false;
                    continue;
                }
                if (linea.trim().isEmpty()) {
                    continue;
                }

                String[] p = linea.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                for (int i = 0; i < p.length; i++) {
                    p[i] = p[i].replace("\"", "").trim();
                }

                if (p.length < 9) {
                    errores.add("Línea " + n + " inválida (se esperaban 9 campos, encontrados " + p.length + "): " + linea);
                    continue;
                }

                try {
                    String titulo = p[0];
                    String isbn = p[1];
                    String genero = p[2];
                    String anioStr = p[3];
                    String autor = p[4];
                    String estado = p[5];
                    String idBiblioteca = p[6];
                    String destino = p[7];
                    String prioridad = p[8];

                    int anio;
                    try {
                        anio = Integer.parseInt(anioStr.replaceAll("[^0-9]", ""));
                    } catch (Exception e) {
                        errores.add("Línea " + n + ": año inválido (" + anioStr + ")");
                        continue;
                    }

                    Biblioteca b = bibliotecas.get(idBiblioteca);
                    if (b == null) {
                        errores.add("Línea " + n + ": biblioteca no encontrada (" + idBiblioteca + ")");
                        continue;
                    }

                    Libro libro = new Libro(titulo, autor, isbn, anio, genero);
                    libro.setEstado(estado);

                    String clave = (titulo + "|" + autor).toLowerCase();
                    Set<String> set = isbnPorColeccion.computeIfAbsent(clave, k -> new HashSet<>());

                    boolean isbnEnOtraColeccion = isbnPorColeccion.entrySet().stream()
                            .anyMatch(e -> !e.getKey().equals(clave) && e.getValue().contains(isbn));

                    if (isbnEnOtraColeccion) {
                        errores.add("Línea " + n + ": ISBN " + isbn + " ya existe en otra colección");
                        continue;
                    }

                    set.add(isbn);
                    b.insertarLibro(libro);

                    if (simulador != null) {
                        simulador.getHash().insertar(isbn, libro);
                    }

                    if (estado.equalsIgnoreCase("En tránsito") && grafo != null && simulador != null) {
                        simulador.transferirLibro(idBiblioteca, destino, libro,
                                prioridad.equalsIgnoreCase("tiempo"));

                       
                        if (!hilosIniciados) {
                            simulador.iniciarHilos();
                            hilosIniciados = true;
                            System.out.println(">>> Simulación iniciada automáticamente por libros en tránsito <<<");
                        }
                    }

                    ok++;

                } catch (Exception ex) {
                    errores.add("Línea " + n + " inválida: " + linea + " (" + ex.getMessage() + ")");
                }
            }
        } catch (IOException e) {
            errores.add("Error leyendo archivo: " + e.getMessage());
        }

        return ok;
    }

    public int cargarLibros(String ruta, Map<String, Biblioteca> bibliotecas, List<String> errores) {
        return cargarLibros(ruta, bibliotecas, null, null, errores);
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

                linea = linea.trim();
                if (linea.isBlank()) {
                    continue;
                }

                String[] p = parseCSVLine(linea);
                if (p.length < 4) {
                    errores.add("Línea " + n + " inválida (conexiones): " + linea);
                    continue;
                }

                try {
                    String origen = p[0].trim();
                    String destino = p[1].trim();
                    double tiempoD = Double.parseDouble(p[2].trim());
                    double costoD = Double.parseDouble(p[3].trim());
                    int tiempo = (int) tiempoD;
                    int costo = (int) costoD;
                    boolean bidireccional = (p.length >= 5)
                            ? Boolean.parseBoolean(p[4].trim())
                            : true;

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

    private static String[] parseCSVLine(String linea) {
        List<String> partes = new ArrayList<>();
        StringBuilder campo = new StringBuilder();
        boolean dentroComillas = false;

        for (int i = 0; i < linea.length(); i++) {
            char c = linea.charAt(i);

            if (c == '"') {
                dentroComillas = !dentroComillas;
            } else if (c == ',' && !dentroComillas) {
                partes.add(campo.toString());
                campo.setLength(0);
            } else {
                campo.append(c);
            }
        }
        partes.add(campo.toString());
        return partes.toArray(new String[0]);
    }
}
