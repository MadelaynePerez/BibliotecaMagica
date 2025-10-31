/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bibliotecamagica.EstructurasBasicas;

/**
 *
 * @author Ana
 */
public class Pila {
    Nodo ValorInicial = null;
    Nodo Pivote = null;
    public int NumeroDeElementos = 0;
    
    public void Agregar(int valor){
        if(ValorInicial == null){
            //1. No hay datos
            Nodo tmp = new Nodo(valor);
            ValorInicial = tmp;
            Pivote = ValorInicial;
        }else{
            //2. Hay datos (siguiente)
            Nodo tmp2 = new Nodo(valor);
            Pivote.Siguiente = tmp2;
            Pivote = Pivote.Siguiente;
        }
        NumeroDeElementos += 1;
    }
    
    public Nodo Remover(){
        if(NumeroDeElementos ==  0) return null;
        Nodo retorno = Pivote;
        
        int inicio = 0;
        Nodo nuevoPivote = ValorInicial;
        while(inicio < NumeroDeElementos - 2){
            nuevoPivote = nuevoPivote.Siguiente;
            inicio +=1;
        }
        
        Pivote = nuevoPivote;
        Pivote.Siguiente = null;
        NumeroDeElementos -= 1;
        return retorno;
    }
}
