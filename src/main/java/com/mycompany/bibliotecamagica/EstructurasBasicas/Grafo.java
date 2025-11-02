/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bibliotecamagica.EstructurasBasicas;

import com.mycompany.bibliotecamagica.Arboles.Biblioteca;
import java.util.*;

/**
 *
 * @author Ana
 */
public class Grafo {

    public enum Criterio {
        TIEMPO, COSTO
    }

    public static class Arista {

        public final String destino;
        public final int tiempo;
        public final int costo;

        public Arista(String destino, int tiempo, int costo) {
            this.destino = destino;
            this.tiempo = tiempo;
            this.costo = costo;
        }
    }

    private final Map<String, List<Arista>> ady = new HashMap<>();
    private final Map<String, Biblioteca> bibliotecas = new HashMap<>();

    public void registrarBiblioteca(Biblioteca biblio) {
        bibliotecas.put(biblio.getNombre(), biblio);
        agregarBiblioteca(biblio.getNombre());
    }

    public Biblioteca obtenerBiblioteca(String nombre) {
        return bibliotecas.get(nombre);
    }

    public void agregarBiblioteca(String nombre) {
        ady.putIfAbsent(nombre, new ArrayList<>());
    }

    public Map<String, List<Arista>> getAdyacencia() {
        return ady;
    }

    public void agregarConexion(String origen, String destino, int tiempo, int costo, boolean bidireccional) {
        agregarBiblioteca(origen);
        agregarBiblioteca(destino);
        ady.get(origen).add(new Arista(destino, tiempo, costo));
        if (bidireccional) {
            ady.get(destino).add(new Arista(origen, tiempo, costo));
        }
    }

    public List<Arista> vecinos(String origen) {
        return ady.getOrDefault(origen, Collections.emptyList());
    }

    public ResultadoRuta dijkstra(String origen, String destino, Criterio criterio) {
        if (!ady.containsKey(origen) || !ady.containsKey(destino)) {
            return ResultadoRuta.vacio();
        }

        Map<String, Integer> dist = new HashMap<>();
        Map<String, String> prev = new HashMap<>();
        for (String v : ady.keySet()) {
            dist.put(v, Integer.MAX_VALUE);
        }
        dist.put(origen, 0);

        PriorityQueue<String> pq = new PriorityQueue<>(Comparator.comparingInt(dist::get));
        pq.add(origen);

        while (!pq.isEmpty()) {
            String u = pq.poll();
            if (u.equals(destino)) {
                break;
            }
            for (Arista e : vecinos(u)) {
                int w = (criterio == Criterio.TIEMPO ? e.tiempo : e.costo);
                if (dist.get(u) == Integer.MAX_VALUE) {
                    continue;
                }
                int alt = dist.get(u) + w;
                if (alt < dist.getOrDefault(e.destino, Integer.MAX_VALUE)) {
                    dist.put(e.destino, alt);
                    prev.put(e.destino, u);
                    pq.remove(e.destino);
                    pq.add(e.destino);
                }
            }
        }

        if (dist.get(destino) == Integer.MAX_VALUE) {
            return ResultadoRuta.vacio();
        }

        LinkedList<String> ruta = new LinkedList<>();
        for (String at = destino; at != null; at = prev.get(at)) {
            ruta.addFirst(at);
        }

        return new ResultadoRuta(ruta, dist.get(destino), criterio);
    }

    public static class ResultadoRuta {

        public final List<String> ruta;
        public final int pesoTotal;
        public final Criterio criterio;

        public ResultadoRuta(List<String> ruta, int peso, Criterio c) {
            this.ruta = ruta;
            this.pesoTotal = peso;
            this.criterio = c;
        }

        public static ResultadoRuta vacio() {
            return new ResultadoRuta(Collections.emptyList(), -1, Criterio.TIEMPO);
        }

        @Override
        public String toString() {
            return ruta.isEmpty()
                    ? "Sin ruta"
                    : "Ruta: " + String.join(" -> ", ruta) + " | " + criterio + "=" + pesoTotal;
        }
    }

    public List<String> encontrarRuta(String origen, String destino, boolean porTiempo) {
        Criterio c = porTiempo ? Criterio.TIEMPO : Criterio.COSTO;
        return dijkstra(origen, destino, c).ruta;
    }

    public String mostrarTexto() {
        StringBuilder sb = new StringBuilder("Conexiones:\n");
        for (var e : ady.entrySet()) {
            sb.append("  ").append(e.getKey()).append(" -> ");
            for (var a : e.getValue()) {
                sb.append(a.destino).append("(t=").append(a.tiempo).append(",c=").append(a.costo).append(") ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
