package it.unina.tesi.bps;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import javax.swing.JOptionPane;

/* La popolazione � un insieme di individui e la classe popolazione viene utilizzata per 
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

    //Otteniamo gli individui dalla popolazione 	/**
    public Individual[] getIndividuals() {
        return this.population;
    }

    private void eliminateNulls(){
        List<Individual> list = new LinkedList<>();
        for (Individual individual : this.population) {
            if(individual != null){
                list.add(individual);
            }
        }
        this.population = list.toArray(new Individual[list.size()]);
    }
    
    
    /*Cerchiamo un individuo nella popolazione attraverso la sua fitness, ordinando la popolazione 
	*  attraverso la fitness e facciamoci restituire l'individuo pi� adatto (fittest)
     */
    public Individual getFittest(int offset) {
        
        eliminateNulls();

        if(this.population.length == 0){
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
     * Calcola il quantitativo minimo di fogli usato dai migliori individui.
     * Elimina tutti gli individui che usano più fogli del quantitativo minimo.
     */
    public void purge() {
        int minFogli = 100;
        for (Individual individual : population) {
            int fogliUsati = individual.getFogliUsati();
            if( fogliUsati < minFogli){
                minFogli = fogliUsati;
            }
        }
        List<Individual> survivors = new LinkedList<>();
        for (Individual individual : population) {
            if(individual.getFogliUsati() <= minFogli && !individual.isZombie()){
                survivors.add(individual);
            }
        }
        this.population = survivors.toArray(new Individual[survivors.size()]);
        
//        if(population.length == 0){
//            JOptionPane.showMessageDialog(null, "ZERO POPULATION");
//        }
        
        
    }

}
