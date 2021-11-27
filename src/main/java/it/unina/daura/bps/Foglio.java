/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unina.daura.bps;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author sommovir
 */
public class Foglio {

    public final double H; //altezza del foglio
    public final double W; //larghezza del foglio
    public final double alpha; //costo spreco
    public final double beta; //costo penalità

    public final double spessore;
    public List<Ordine> ordini;

    public Foglio(double H, double W, double alpha, double beta, double spessore) {
        this.H = H;
        this.W = W;
        this.alpha = alpha;
        this.beta = beta;
        this.spessore = spessore;
        this.ordini = new LinkedList<Ordine>();
    }

    public List<Ordine> getOrdini() {
        return ordini;
    }

    public void addOrdine(Ordine ordine) {
        this.ordini.add(ordine);
    }

    public double getW() {
        return W;
    }

    public double getAlpha() {
        return alpha;
    }

    public double getBeta() {
        return beta;
    }

    public double getH() {
        return H;
    }

    public double getSpessore() {
        return spessore;
    }

}
