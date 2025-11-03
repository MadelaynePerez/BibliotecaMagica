package com.mycompany.bibliotecamagica.EstructurasBasicas;

import com.mycompany.bibliotecamagica.Arboles.Biblioteca;
import java.util.*;

public class Grafo {

    public enum Criterio {
        TIEMPO, COSTO
    }

    public static class Arista {

        public final String destino;
        public final int tiempo;
        public final int costo;

        public Arista(String destino, int tiempo, int costo) {
            this.destino = destino.trim();
            this.tiempo = tiempo;
            this.costo = costo;
        }

        @Override
        public String toString() {
            return destino + "(t=" + tiempo + ", c=" + costo + ")";
        }
    }

    private final Map<String, List<Arista>> ady = new HashMap<>();
    private final Map<String, Biblioteca> bibliotecas = new HashMap<>();

    public void registrarBiblioteca(Biblioteca biblio) {
        if (biblio != null) {
            bibliotecas.put(biblio.getNombre().trim(), biblio);
            agregarBiblioteca(biblio.getNombre().trim());
        }
    }

    public void agregarBiblioteca(String nombre) {
        if (nombre != null && !nombre.trim().isEmpty()) {
            ady.putIfAbsent(nombre.trim(), new ArrayList<>());
        }
    }

    public Map<String, List<Arista>> getAdyacencia() {
        return ady;
    }

    public void agregarConexion(String origen, String destino, int tiempo, int costo, boolean bidireccional) {
        origen = origen.trim();
        destino = destino.trim();
        agregarBiblioteca(origen);
        agregarBiblioteca(destino);
        ady.get(origen).add(new Arista(destino, tiempo, costo));
        if (bidireccional) {
            ady.get(destino).add(new Arista(origen, tiempo, costo));
        }
    }

    public List<Arista> vecinos(String origen) {
        return ady.getOrDefault(origen.trim(), Collections.emptyList());
    }

    public ResultadoRuta dijkstra(String origen, String destino, Criterio criterio) {
        origen = origen.trim();
        destino = destino.trim();

        if (!ady.containsKey(origen) || !ady.containsKey(destino)) {
            System.out.println("No existe alguno de los nodos: " + origen + " o " + destino);
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
                int peso = (criterio == Criterio.TIEMPO) ? e.tiempo : e.costo;
                if (peso < 0) {
                    continue;
                }

                int alt = dist.get(u) + peso;
                if (alt < dist.getOrDefault(e.destino, Integer.MAX_VALUE)) {
                    dist.put(e.destino, alt);
                    prev.put(e.destino, u);
                    pq.remove(e.destino);
                    pq.add(e.destino);
                }
            }
        }

        if (dist.get(destino) == Integer.MAX_VALUE) {
            System.out.println("No hay ruta disponible entre " + origen + " y " + destino);
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
            return ruta.isEmpty() ? "Sin ruta"
                    : "Ruta: " + String.join(" -> ", ruta) + " | " + criterio + "=" + pesoTotal;
        }
    }

    public List<String> encontrarRuta(String origen, String destino, boolean porTiempo) {
        Criterio c = porTiempo ? Criterio.TIEMPO : Criterio.COSTO;
        ResultadoRuta r = dijkstra(origen, destino, c);
        System.out.println(r.toString());
        return r.ruta;
    }

    public String mostrarTexto() {
        StringBuilder sb = new StringBuilder("Conexiones:\n");
        for (var e : ady.entrySet()) {
            sb.append("  ").append(e.getKey()).append(" -> ");
            for (var a : e.getValue()) {
                sb.append(a).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
