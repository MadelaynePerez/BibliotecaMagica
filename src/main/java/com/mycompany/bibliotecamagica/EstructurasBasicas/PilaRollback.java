/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bibliotecamagica.EstructurasBasicas;

import com.mycompany.bibliotecamagica.Arboles.Biblioteca;
import com.mycompany.bibliotecamagica.Arboles.Libro;
import java.util.Stack;

/**
 *
 * @author Ana
 */
public class PilaRollback {

    private final Stack<Integer> pila;

    public PilaRollback() {
        this.pila = new Stack<>();
    }

    public void Agregar(int valor) {
        pila.push(valor);
    }

    public Integer Remover() {
        if (!pila.isEmpty()) {
            return pila.pop();
        }
        return null;
    }

    public int getNumeroDeElementos() {
        return pila.size();
    }

    public void limpiar() {
        pila.clear();
    }

    public void mostrar() {
        System.out.println("Contenido de la pila:");
        for (Integer valor : pila) {
            System.out.println(" - " + valor);
        }
    }
}
