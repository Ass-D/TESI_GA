/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.cnr.istc.gen.duericariche;


/**
 *
 * @author sommovir
 */
public class Ordine {
    
    public double h; //altezza dell'ordine
    public double w; //larghezza dell'ordine
    public double p; //peso dell'ordine
    public long d; //scadenza 
    public double r; //retinatura
    

    public Ordine(double h, double w, double p, long d, double r) {
        this.h = h;
        this.w = w;
        this.p = p;
        this.d = d;
        this.r = r;
    }

    
    
    
    public final double area(){
        return this.h * this.w;
    }
    
    public final double tempoElaborazioneRetinatura(){
        return area() *r;
    }
    
    public final double indiceDiPriotita(){
        return this.p / d;
    }
    
    
}
