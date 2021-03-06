package it.unina.tesi.bps;

import it.unina.tesi.bps.exceptions.InvalidAttemptToExportException;
import it.unina.tesi.bps.utils.Exporter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class Individual {

    private int[] vettore;
    private double fitness = -1;
    private List<Integer> sequenzaFogli = new LinkedList<>();
    private boolean SprecoNonAccettabile = false;
    private boolean nospace = false;

    public Individual(int[] vettore) {

        // Creiamo il cromosoma (vettore)
        this.vettore = vettore;
        for (int foglio : vettore) {
            if (sequenzaFogli.isEmpty()) {
                sequenzaFogli.add(foglio);
            } else {
                if (!sequenzaFogli.contains(foglio)) {
                    sequenzaFogli.add(foglio);
                }
            }
        }
    }

    public void setSprecoNonAccettabile(boolean SprecoNonAccettabile) {
        this.SprecoNonAccettabile = SprecoNonAccettabile;
    }

    public boolean isSprecoNonAccettabile() {
        return SprecoNonAccettabile;
    }

    public boolean isNospace() {
        return nospace;
    }

    public void setNospace(boolean nospace) {
        this.nospace = nospace;
    }
    

    public int getFogliUsati() {
        Set<Integer> fogli = new HashSet<>();
        for (Integer foglio : vettore) {
            fogli.add(foglio);
        }
        return fogli.size();
    }

    public List<Integer> getSequenzaFogli() {
        if (this.sequenzaFogli.isEmpty()) {
            for (int foglio : vettore) {
                if (sequenzaFogli.isEmpty()) {
                    sequenzaFogli.add(foglio);
                } else {
                    if (!sequenzaFogli.contains(foglio)) {
                        sequenzaFogli.add(foglio);
                    }
                }
            }
        }
        return sequenzaFogli;
    }

    public int[] getOrdiniByIndex(int index) {
        if (index < 1 || index > 4) {
            throw new IllegalArgumentException("l'indice deve essere compreso tra 1 e 4 [" + index + "]");
        }
        List<Integer> result = new ArrayList<>();

        for (int i = 0; i < vettore.length; i++) {
            if (vettore[i] == index) {
                result.add(i);
            }
        }
        return result.stream().mapToInt(i -> i).toArray();

    }
    
    public List<Ordine> getOrdiniByFoglio(List<Ordine> ordini, int foglio) {
        List<Ordine> result = new LinkedList<>();

        int i = 0;
        for (int index : vettore) {
            if (index == foglio) {
                result.add(ordini.get(i));
            }
            i++;
        }
        
        return result;
    }

    public List<Ordine> getOrdiniByFoglio(Ordine[] ordini, int foglio) {
        List<Ordine> result = new LinkedList<>();

        int i = 0;
        for (int index : vettore) {
            if (index == foglio) {
                result.add(ordini[i]);
            }
            i++;
        }
        
        return result;
    }

    private double sumTempiLavorazione(List<Ordine> ordini) {
        double d = 0;
        for (Ordine ordine : ordini) {
            d += ordine.tempoLavorazioneRetinatura();
        }
        return d;
    }

    public Map<Integer, Double> getTempiDiProcessamento(Ordine[] ordini) {

        //double [] ritardiResult = new double[sequenzaFogli.size()];
        Map<Integer, Double> mapResult = new HashMap<>();

        List<Ordine> ordiniPrimoFoglio = getOrdiniByFoglio(ordini, sequenzaFogli.get(0));

        mapResult.put(sequenzaFogli.get(0), sumTempiLavorazione(ordiniPrimoFoglio));

        for (int i = 1; i < sequenzaFogli.size(); i++) {
            List<Ordine> ordiniPerFoglio = getOrdiniByFoglio(ordini, sequenzaFogli.get(i));
            //ritardiResult[i] = ritardiResult[i-1] + sumTempiElaborazione(ordiniPerFoglio);
            double tempoProcessamentoPrecedente = mapResult.get(sequenzaFogli.get(i - 1));

            mapResult.put(sequenzaFogli.get(i), tempoProcessamentoPrecedente + sumTempiLavorazione(ordiniPerFoglio));
        }
        return mapResult;

    }

    // Inizializziamo in modo randomico. Creiamo un vettore che sia composto da numeri che vanno da 1 a 4
    public Individual(int vettoreLength) {

        this.vettore = new int[vettoreLength];
        for (int i = 0; i < vettoreLength; i++) {
            vettore[i] = ((int) ((Math.random() * 100) % 4) + 1); //riempe il vettore con numeri casuali tra {1,2,3,4}
        }
        // System.out.println("INDIVIDUAL: "+this);
    }

//Otteniamo il cromosoma dell'individuo e la sua lunghezza 
    public int[] getvettore() {
        return this.vettore;
    }

    public int getVettoreLength() {
        return this.vettore.length;
    }

//Impostiamo e otteniamo il gene all'offset  
    public void setI(int offset, int i) {
        this.vettore[offset] = i;
    }

    public int getI(int offset) {
        return this.vettore[offset];
    }

//Memorizziamo e otteniamo la fitness di un individuo
    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public double getFitness() {
        return this.fitness;
    }

//Visualizziamo il cromosoma come una stringa 
    public String toString() {
        String output = "";
        for (int i = 0; i < vettore.length; i++) {
            output += this.vettore[i];
        }
        return output + "  - fogli: "+getFogliUsati()+ "  - "+(this.SprecoNonAccettabile ? "SprecoNonAccettabile" : "SprecoAccettabile") +" - "+(this.nospace ? "no space" : "space ok");
    }

    Map<Integer, Double> getTempiDiElaborazione(List<Ordine> ordini) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

}
