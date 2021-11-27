package it.unina.daura.bps;

import it.unina.daura.bps.exceptions.SoluzioneImpossibileException;
import it.unina.daura.bps.files.OrdiniFileReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    public static int maxGenerations = 100; // massimo num. di iterazioni per la terminazione

    public static void main(String[] args) {

        System.out.println("TEST  READ FILE: ");
        Ordine[] ordini = OrdiniFileReader.readFile("dati3.csv");

        int id = 1;
        for (Ordine ordine : ordini) {
            System.out.println(id + ") " + ordine);
            id++;
        }

        double H = 127; //altezza del foglio
        double W = 203; //larghezza del foglio

        double areaFoglio = H * W; //area del foglio

        double alpha = 451.6; //costo spreco
        double beta = 82.5; //costo penalità

// Creiamo l'oggetto ga
        GeneticAlgorithm ga = new GeneticAlgorithm(100, 0.01, 0.95, 0);

//Inizializziamo la popolazione specificando la lunghezza dei cromosomi
        Population population = ga.initPopulation(30);

//Valutiamo la popolazione
        ga.evalPopulation(population, areaFoglio, alpha, beta, ordini);

        int generation = 1;

        while (ga.isTerminationConditionMet(generation, maxGenerations) == false) {

//Stampiamo l'individuo più in forma 
            Individual fittest = population.getFittest(0); 
            System.out.println(
                    "G" + generation + " Best solution (" + fittest.getFitness() + ")");

//Applichiamo il crossover
            population = ga.crossoverPopulation(population);

//Applichiamo la mutazione
            population = ga.mutatePopulation(population);

//Valutiamo la popolazione
            ga.evalPopulation(population, areaFoglio, alpha, beta, ordini);
            generation++;
        }

        System.out.println("Stopped after " + maxGenerations + " generations.");
        {
            Individual fittest = population.getFittest(0);
            System.out.println("La fitness è: " + fittest.getFitness());
        }
        
        
        System.out.println("La soluzione migliore è: " + population.getFittest(0));
        System.out.println("====================================================");
        System.out.println("Le altre soluzioni: ");
        int i = 0;
        for (Individual individual : population.getIndividuals()) {
            System.out.println("Soluzione alternativa "+i+") "+individual);
            i++;
        }
     
    ////ASSUNTA: MOSTRARE come output anche lo spreco dei fogli 1 e 2 della soluzione migliore    
    //    System.out.println("spreco foglio 1:" +sprecoFoglio1 );
    //    System.out.println("spreco foglio 2:" +sprecoFoglio2 );  
   //ASSUNTA: DOMANDA : perchè abbiamo messo uguale a -2 ??     
        double minimoCosto = -2;
        try {
            minimoCosto = ga.minimoCosto(population, ordini, areaFoglio, alpha, beta);
        } catch (SoluzioneImpossibileException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
