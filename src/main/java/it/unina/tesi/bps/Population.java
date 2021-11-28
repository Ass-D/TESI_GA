package it.unina.tesi.bps;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import javax.swing.JOptionPane;

/* La popolazione è un insieme di individui e la classe popolazione viene utilizzata per 
	* operazioni a livello di gruppo da eseguire sugli individui (come la selezione di
	* individui a cui applicare il crossover o la mutazione). 
 */

//Creiamo la classe Population e inizializzaimo una popolazione vuota definendo la dimensione
public class Population {

    private Individual population[];
    private double populationFitness = -1;

    public Population(int populationSize) {
        // Initial population
        this.population = new Individual[populationSize];
    }

    //Inizializziamo una popolazione di individui
    public Population(int populationSize, int chromosomeLength) {

        this.population = new Individual[populationSize];

        //Creiamo ogni individuo inizializzando il suo cromosoma alla lunghezza data 
        for (int individualCount = 0; individualCount < populationSize; individualCount++) {

            Individual individual = new Individual(chromosomeLength);

            //Aggiungiamo ogni individuo alla popolazione 
            this.population[individualCount] = individual;
        }
    }

    //Otteniamo gli individui dalla popolazione 
    public Individual[] getIndividuals() {
        return this.population;
    }

    private void eliminateNulls() {
        List<Individual> list = new LinkedList<>();
        for (Individual individual : this.population) {
            if (individual != null) {
                list.add(individual);
            }
        }
        this.population = list.toArray(new Individual[list.size()]);
    }

    /*Cerchiamo un individuo nella popolazione attraverso la sua fitness, ordinando la popolazione 
      attraverso la fitness e facciamoci restituire l'individuo più adatto (fittest)
     */
    public Individual getFittest(int offset) {

        eliminateNulls();

        if (this.population.length == 0) {
            return null;
        }
        Arrays.sort(this.population, new Comparator<Individual>() {
            @Override
            public int compare(Individual o1, Individual o2) {

                if (o1.getFitness() > o2.getFitness()) {
                    return -1;
                } else if (o1.getFitness() < o2.getFitness()) {
                    return 1;
                }
                return 0;
            }
        });

        return this.population[offset];

    }

    //Impostiamo e otteniamo la fitness della popolazione 
    public void setPopulationFitness(double fitness) {
        this.populationFitness = fitness;
    }

    public double getPopulationFitness() {
        return this.populationFitness;
    }

    //Otteniamo la dimensione della popolazione 
    public int size() {
        return this.population.length;
    }

    //Impostiamo e otteniamo un individuo all'offset
    public Individual setIndividual(int offset, Individual individual) {
        return population[offset] = individual;
    }

    public Individual getIndividual(int offset) {
        return population[offset];
    }

    //Mescoliamo la popolazione 
    public void shuffle() {
        Random rnd = new Random();
        for (int i = population.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            Individual a = population[index];
            population[index] = population[i];
            population[i] = a;
        }
    }

    /**
     * Calcoliamo il quantitativo minimo di fogli usato dai migliori individui.
     * Eliminiamo tutti gli individui che usano più fogli del quantitativo minimo.
     */
    public void select() {
        int minFogli = 1000;
        int cromoLenght = population[0].getVettoreLength();
        int originalSize = population.length;
        
//troviamo gli individui che utilizzano il numero minimo di fogli dalla popolazione e che rispettano il vincolo degli spazi
        for (Individual individual : population) {
            if (individual.isNospace()) {
                continue;
            }
            int fogliUsati = individual.getFogliUsati();
            if (fogliUsati < minFogli) {
                minFogli = fogliUsati;
            }
        }
        if (Main.debug) {
            System.out.println("BEFORE SELECT, min = " + minFogli);
        }
        //Settiamo a SprecoNonAccettabile=true tutti gli individui che usano più fogli del minimo trovato
        for (Individual individual : population) {
            if (individual.getFogliUsati() > minFogli) {
                individual.setSprecoNonAccettabile(true);
            }
            if (Main.debug) {
                System.out.println(individual);
            }
        }

        //Creiamo una lista vuota dei sopravvissuti e ci mettiamo tutti gli 
        //individui che non sono SprecoNonAccettabile e che rispettano il vincolo sulle aree
        List<Individual> survivors = new LinkedList<>();
        for (Individual individual : population) {
            if (!individual.isSprecoNonAccettabile() && !individual.isNospace()) {
                survivors.add(individual);
            }
        }
        if (Main.debug) {
            System.out.println("SURVIROS SIZE: " + survivors.size());
            System.out.println("AFTER SELECT");
            for (Individual individual : survivors) {
                System.out.println(individual);
            }
        }

        //Iniziamo la fase di ripopolamento per tornare a 100 unità di individui
        if (Main.debug) {
            System.out.println("REFILLING..");
        }
        //Calcoliamo il totale degli individui mancanti = missing. 
        int missing = originalSize - survivors.size();
        //cloning i sopravvissuti e calcoliamo la percentuale di elementi da clonare
        int n_clones = (int) (missing / 1.5); //75%
        if (Main.debug) {
            System.out.println("Cloning " + n_clones + " individui");
        }
        //prepariamo una lista dove inserire tutti gli individui da clonare
        List<Individual> clones = new LinkedList<>();
        for (int i = 0; i < n_clones; i++) {
            int random = survivors.size() == 1 ? 0 : nexRandomInRange(0, survivors.size() - 1);
            //aggiungiamo ai cloni un invididuo preso a caso tra i sopravvissuti. 
            clones.add(new Individual(survivors.get(random).getvettore()));
        }
        //aggiungiamo i cloni a tutti i sopravvisuti
        survivors.addAll(clones);
     
        //Raggiungiamo la quota finale di individui generando a caso i rimanenti
        if (Main.debug) {
            System.out.println("Creating " + (missing - n_clones) + " new Individual !");
        }
        for (int i = 0; i < missing - n_clones; i++) {
            survivors.add(new Individual(cromoLenght));
        }

        this.population = survivors.toArray(new Individual[survivors.size()]);
        if (Main.debug) {
            System.out.println("AFTER CLONING");

            for (Individual individual : survivors) {

                System.out.println(individual);
            }

            System.out.println("---------------------------------------------------");
        }

    }

    public int nexRandomInRange(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }

}
