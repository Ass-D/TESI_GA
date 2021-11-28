/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unina.tesi.bps;

import it.unina.tesi.bps.utils.ConsoleColors;
import java.util.LinkedList;
import java.util.List;
import org.fusesource.jansi.AnsiColors;

/**
 *
 * @author sommovir
 */
public class FoglioType {

    public final double H; //altezza del foglio
    public final double W; //larghezza del foglio
    public final double alpha; //costo spreco
    public final double beta; //costo penalità

    public final double spessore;
    public List<Ordine> ordini;

    public FoglioType(double H, double W, double alpha, double beta, double spessore) {
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
    
    public void printOrdini(){
        int i = 1;
        System.out.println(ConsoleColors.ANSI_YELLOW+" >> Spessore: "+ConsoleColors.ANSI_RED+spessore+ConsoleColors.ANSI_RESET);
        System.out.println(ConsoleColors.ANSI_YELLOW+" >> Totale ordini: "+ConsoleColors.ANSI_RED+ordini.size()+ConsoleColors.ANSI_RESET);
        for (Ordine ordine : ordini) {
            System.out.println(i+") "+ordine);
            i++;
        }
        System.out.println(ConsoleColors.ANSI_YELLOW+" ================================================ "+ConsoleColors.ANSI_RESET);
    }

    @Override
    public String toString() {
        return "spessore: "+spessore;
    }
    
    

}
