package it.cnr.istc.gen.duericariche;

import it.cnr.istc.gen.duericariche.files.OrdiniFileReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    public static int maxGenerations = 300; // massimo num. di iterazioni per la terminazione

    public static void main(String[] args) {

        System.out.println("TEST  READ FILE: ");
        Ordine[] ordini = OrdiniFileReader.readFile("dati.csv");

        int id = 1;
        for (Ordine ordine : ordini) {
            System.out.println(id + ") " + ordine);
            id++;
        }

        double H = 127; //altezza del foglio
        double W = 203; //larghezza del foglio

        double areaFoglio = H * W; //area del foglio

        double alpha = 450; //costo spreco
        double beta = 50; //costo penalità

// Creiamo l'oggetto ga
        GeneticAlgorithm ga = new GeneticAlgorithm(300, 0.01, 0.95, 0);

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

        System.out.println("La schedulazione è: " + population.getFittest(0));

        double tempodicompletamento = -2;
        try {
            tempodicompletamento = ga.minimoCosto(population, ordini, areaFoglio, alpha, beta);
        } catch (SoluzioneImpossibileException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        //System.out.println("Cmax �: " + tempodicompletamento);


        /*  System.out.print("tp �: ");
        for (int i = 0; i < 8; i++) {
            System.out.print(tp[i] + " ");
        }
         */
    }

}
