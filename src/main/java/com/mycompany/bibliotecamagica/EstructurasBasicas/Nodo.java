/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bibliotecamagica.EstructurasBasicas;

/**
 *
 * @author Ana
 */
public class Nodo {
    public int Valor; //Valor
    public Nodo Siguiente = null; //Valor siguiente -->1,2,3  1 -> 2 -> 3
    public Nodo Anterior = null; //Valor anterior -> 1,2,3 3->2
    
    public Nodo(int valor){
        this.Valor = valor;
    }
    
    public Nodo(){
        
    }
}
