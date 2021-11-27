/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unina.daura.bps;


/**
 *
 * @author sommovir
 */
public class Ordine {
    
    public double h; //altezza dell'ordine
    public double w; //larghezza dell'ordine
    public double pesoOrdine; //peso dell'ordine
    public double dueDate; //scadenza 
    public double retinatura; //retinatura
    

    public Ordine(double h, double w, double p, double d, double r) {
        this.h = h;
        this.w = w;
        this.pesoOrdine = p;
        this.dueDate = d;
        this.retinatura = r;
    }

    
    public final double area(){
        return this.h * this.w;
    }
    
    public final double tempoElaborazioneRetinatura(){
        return area() *retinatura;
    }
    
    public final double indiceDiPriotita(){
        return this.pesoOrdine / dueDate;
    }

    @Override
    public String toString() {
        return "Ordine: h = "+h+", w = "+w+", pesoOrdine= "+pesoOrdine+", dueDate = "+dueDate+", retinatura = "+retinatura;
    }
    
    
    
}
