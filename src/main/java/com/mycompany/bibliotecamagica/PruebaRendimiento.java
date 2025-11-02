/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bibliotecamagica;

import com.mycompany.bibliotecamagica.Arboles.Avl;
import com.mycompany.bibliotecamagica.Arboles.Libro;
import com.mycompany.bibliotecamagica.EstructurasBasicas.HashTableISBN;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ana
 */
public class PruebaRendimiento {

    public static void probar() {
        List<Libro> lista = new ArrayList<>();
        Avl avl = new Avl();
        HashTableISBN hash = new HashTableISBN();

      
        for (int i = 0; i < 5000; i++) {
            Libro l = new Libro("Titulo " + i, "Autor " + i,
                    "ISBN-" + i, 2000 + (i % 20), "Fantasia");
            lista.add(l);
            avl.insertar(l);
            hash.insertar(l.getIsbn(), l);
        }

        String buscar = "ISBN-3500";

        long t1 = System.nanoTime();
        Libro sec = null;
        for (Libro l : lista) {
            if (l.getIsbn().equals(buscar)) {
                sec = l;
                break;
            }
        }
        long t2 = System.nanoTime();

        long t3 = System.nanoTime();
        Libro avlRes = avl.buscar("Titulo 3500");
        long t4 = System.nanoTime();

        long t5 = System.nanoTime();
        Libro hres = (Libro) hash.buscar(buscar);
        long t6 = System.nanoTime();

        System.out.println("Secuencial: " + (t2 - t1) + " ns " + (sec != null));
        System.out.println("AVL: " + (t4 - t3) + " ns " + (avlRes != null));
        System.out.println("Hash: " + (t6 - t5) + " ns " + (hres != null));
    }
}
