/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bibliotecamagica.EstructurasBasicas;

import com.mycompany.bibliotecamagica.Arboles.Libro;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author Ana
 */


// Módulo con búsquedas y 5 ordenamientos
public class BusquedaOrdenamiento {

   

  
    public static Libro busquedaSecuencialPorAutor(List<Libro> lista, String autor) {
        String t = autor.toLowerCase().trim();
        for (Libro l: lista) if (l.getAutor().toLowerCase().contains(t)) return l;
        return null;
    }

  
    public static Libro busquedaBinariaPorTitulo(List<Libro> listaOrdenadaPorTitulo, String titulo) {
        int lo=0, hi=listaOrdenadaPorTitulo.size()-1;
        String key = titulo.toLowerCase().trim().replaceAll("\\s+","");
        while (lo<=hi) {
            int mid=(lo+hi)/2;
            String mt = listaOrdenadaPorTitulo.get(mid).getTitulo().toLowerCase().trim().replaceAll("\\s+","");
            int cmp = mt.compareTo(key);
            if (cmp==0) return listaOrdenadaPorTitulo.get(mid);
            if (cmp<0) lo=mid+1; else hi=mid-1;
        }
        return null;
    }

    

    public static void intercambio(List<Libro> a, Comparator<Libro> cmp) {
        for (int i=0;i<a.size();i++)
            for (int j=i+1;j<a.size();j++)
                if (cmp.compare(a.get(j), a.get(i))<0) Collections.swap(a,i,j);
    }

    public static void seleccion(List<Libro> a, Comparator<Libro> cmp) {
        for (int i=0;i<a.size();i++) {
            int min=i;
            for (int j=i+1;j<a.size();j++) if (cmp.compare(a.get(j),a.get(min))<0) min=j;
            Collections.swap(a,i,min);
        }
    }

    public static void insercion(List<Libro> a, Comparator<Libro> cmp) {
        for (int i=1;i<a.size();i++) {
            Libro key=a.get(i); int j=i-1;
            while (j>=0 && cmp.compare(key,a.get(j))<0) { a.set(j+1,a.get(j)); j--; }
            a.set(j+1,key);
        }
    }

    public static void shell(List<Libro> a, Comparator<Libro> cmp) {
        for (int gap=a.size()/2; gap>0; gap/=2) {
            for (int i=gap;i<a.size();i++) {
                Libro temp=a.get(i); int j=i;
                while (j>=gap && cmp.compare(temp,a.get(j-gap))<0) { a.set(j,a.get(j-gap)); j-=gap; }
                a.set(j,temp);
            }
        }
    }

    public static void quicksort(List<Libro> a, Comparator<Libro> cmp) {
        qs(a,0,a.size()-1,cmp);
    }
    private static void qs(List<Libro> a, int lo, int hi, Comparator<Libro> cmp) {
        if (lo>=hi) return;
        Libro p=a.get((lo+hi)/2);
        int i=lo, j=hi;
        while (i<=j) {
            while (cmp.compare(a.get(i),p)<0) i++;
            while (cmp.compare(a.get(j),p)>0) j--;
            if (i<=j) { Collections.swap(a,i,j); i++; j--; }
        }
        qs(a,lo,j,cmp); qs(a,i,hi,cmp);
    }

    
    public static Comparator<Libro> porTitulo() { return Comparator.comparing(Libro::getTitulo, String.CASE_INSENSITIVE_ORDER); }
    public static Comparator<Libro> porAutor()  { return Comparator.comparing(Libro::getAutor,  String.CASE_INSENSITIVE_ORDER); }
    public static Comparator<Libro> porISBN()   { return Comparator.comparing(Libro::getIsbn,   String.CASE_INSENSITIVE_ORDER); }
    public static Comparator<Libro> porAnio()   { return Comparator.comparingInt(Libro::getAnio); }
}

