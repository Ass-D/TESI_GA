package it.unina.tesi.bps;

import it.unina.tesi.bps.exceptions.SoluzioneImpossibileException;
import it.unina.tesi.bps.utils.Exporter;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class GeneticAlgorithm {

    private int populationSize; // dimensione della popolazione 
    private double mutationRate; // tasso di mutazione 
    private double crossoverRate; // tasso di crossover
    private int elitismCount; // elitismo

    public GeneticAlgorithm(int populationSize, double mutationRate, double crossoverRate, int elitismCount) {
        this.populationSize = populationSize;
        this.mutationRate = mutationRate;
        this.crossoverRate = crossoverRate;
        this.elitismCount = elitismCount;
    }

//Inizializziamo la popolazione 
    public Population initPopulation(int vettoreLength) {

        Population population = new Population(this.populationSize, vettoreLength);
        return population;
    }

    private double calculateAreaOrdiniByIndex(int[] indexes, Ordine[] ordini) {
        double area = 0;
        for (int index : indexes) {
            area += ordini[index].area();
        }
        return area;
    }

    /**
     * Calcola il minimo tra le scadenze degli ordini dati a partire dall'array
     * degli indici del medesimo foglio
     *
     * @param indexes
     * @param ordini
     * @return
     */
    private double calculateMinExpirationDate(int[] indexes, Ordine[] ordini) {
        double min = ordini[0].dueDate;
        for (int index : indexes) {
            if (ordini[index].dueDate < min) {
                min = ordini[index].dueDate;
            }
        }
        return min;
    }
 //Calcoliamo per ciascun ordine il tempo di lavorazione di retinatura necessario
    private double getSommaTempoRetinatura(int[] indexes, Ordine[] ordini) {
        double somma = 0;
        for (int index : indexes) {
            somma += ordini[index].tempoLavorazioneRetinatura();
        }
        return somma;
    }
 // Calcoliamo per ciascun ordine l'indice di priorità
    private double getSommaIndicePriorita(int[] indexes, Ordine[] ordini) {
        double somma = 0;
        for (int index : indexes) {
            somma += ordini[index].indiceDiPriorita();
        }
        return somma;
    }

//Calcoliamo la fitness per un individuo
    public double calcFitness(Individual individual, double areaFoglio, double alpha, double beta, List<Ordine> ordini) throws SoluzioneImpossibileException {

        //  int[] vettore = individual.getvettore();
        
        int[] foglio1 = null;
        int[] foglio2 = null;
        int[] foglio3 = null;
        int[] foglio4 = null;

        if(individual == null){
            return 0;
        }
//        System.out.println(" <<<<individual : "+individual);
        List<Integer> sequenzaFogli = individual.getSequenzaFogli();

        int numFogli = sequenzaFogli.size();

        foglio1 = individual.getOrdiniByIndex(sequenzaFogli.get(0));
        foglio2 = numFogli < 2 ? null : individual.getOrdiniByIndex(sequenzaFogli.get(1));
        foglio3 = numFogli < 3 ? null : individual.getOrdiniByIndex(sequenzaFogli.get(2));
        foglio4 = numFogli < 4 ? null : individual.getOrdiniByIndex(sequenzaFogli.get(3));

        double areaOrdiniFoglio1 = calculateAreaOrdiniByIndex(foglio1, ordini.toArray(new Ordine[ordini.size()]));
        double areaOrdiniFoglio2 = numFogli < 2 ? 0 : calculateAreaOrdiniByIndex(foglio2, ordini.toArray(new Ordine[ordini.size()]));
        double areaOrdiniFoglio3 = numFogli < 3 ? 0 : calculateAreaOrdiniByIndex(foglio3, ordini.toArray(new Ordine[ordini.size()]));
        double areaOrdiniFoglio4 = numFogli < 4 ? 0 : calculateAreaOrdiniByIndex(foglio4, ordini.toArray(new Ordine[ordini.size()]));

        if (areaOrdiniFoglio1 > areaFoglio || areaOrdiniFoglio2 > areaFoglio || areaOrdiniFoglio3 > areaFoglio || areaOrdiniFoglio4 > areaFoglio) {
            
            individual.setNospace(true);
            throw new SoluzioneImpossibileException("La somma delle aree degli ordini non può eccedere l'area del foglio");
        } else {

        }
    // Calcoliamo per ciascun foglio l'indice di spreco
        double sprecoFoglio1 = (areaFoglio - areaOrdiniFoglio1) / areaFoglio;
        double sprecoFoglio2 = (areaFoglio - areaOrdiniFoglio2) / areaFoglio;
        double sprecoFoglio3 = (areaFoglio - areaOrdiniFoglio3) / areaFoglio;
        double sprecoFoglio4 = (areaFoglio - areaOrdiniFoglio4) / areaFoglio;

        if (sprecoFoglio2 == 1) {
            sprecoFoglio2 = 0;
        }
        if (sprecoFoglio3 == 1) {
            sprecoFoglio3 = 0;
        }
        if (sprecoFoglio4 == 1) {
            sprecoFoglio4 = 0;
        }

    // Calcoliamo il costo di spreco complessivo
        double costoSpreco = alpha * (sprecoFoglio1 + sprecoFoglio2 + sprecoFoglio3 + sprecoFoglio4);
    
    // Calcoliamo la data di scadenza di ciascun foglio
    
        double minDueDateFoglio1 = calculateMinExpirationDate(foglio1, ordini.toArray(new Ordine[ordini.size()]));
        double minDueDateFoglio2 = numFogli < 2 ? 0 : calculateMinExpirationDate(foglio2, ordini.toArray(new Ordine[ordini.size()]));
        double minDueDateFoglio3 = numFogli < 3 ? 0 : calculateMinExpirationDate(foglio3, ordini.toArray(new Ordine[ordini.size()]));
        double minDueDateFoglio4 = numFogli < 4 ? 0 : calculateMinExpirationDate(foglio4, ordini.toArray(new Ordine[ordini.size()]));
    
// Calcoliamo il tempo di processamento di ciascun foglio
        double tempoProcessamentoFoglio1 = getSommaTempoRetinatura(foglio1, ordini.toArray(new Ordine[ordini.size()]));
        double tempoProcessamentoFoglio2 = numFogli < 2 ? 0 : getSommaTempoRetinatura(foglio2, ordini.toArray(new Ordine[ordini.size()]));
        double tempoProcessamentoFoglio3 = numFogli < 3 ? 0 : getSommaTempoRetinatura(foglio3, ordini.toArray(new Ordine[ordini.size()]));
        double tempoProcessamentoFoglio4 = numFogli < 4 ? 0 : getSommaTempoRetinatura(foglio4, ordini.toArray(new Ordine[ordini.size()]));

   
 // Calcoliamo il tempo di completamento di ciascun foglio
        
       double tempoCompletamentoF1 = tempoProcessamentoFoglio1;
       double tempoCompletamentoF2 = tempoProcessamentoFoglio1 + tempoProcessamentoFoglio2;
       double tempoCompletamentoF3 = tempoProcessamentoFoglio1 + tempoProcessamentoFoglio2 + tempoProcessamentoFoglio3;
       double tempoCompletamentoF4 = tempoProcessamentoFoglio1 + tempoProcessamentoFoglio2 + tempoProcessamentoFoglio3 + tempoProcessamentoFoglio4;

        // Calcoliamo il ritardo dei fogli
        double ritardoFoglio1 = tempoCompletamentoF1 - minDueDateFoglio1 >= 0 ? tempoCompletamentoF1 - minDueDateFoglio1 : 0;
        double ritardoFoglio2 = tempoCompletamentoF2 - minDueDateFoglio2 >= 0 ? tempoCompletamentoF2 - minDueDateFoglio2 : 0;
        double ritardoFoglio3 = tempoCompletamentoF3 - minDueDateFoglio3 >= 0 ? tempoCompletamentoF3 - minDueDateFoglio3 : 0;
        double ritardoFoglio4 = tempoCompletamentoF4 - minDueDateFoglio4 >= 0 ? tempoCompletamentoF4 - minDueDateFoglio4 : 0;
  
        // Calcoliamo l'indice di priorità di ciscun foglio
        double sommatoriaIndicePriority1 = getSommaIndicePriorita(foglio1, ordini.toArray(new Ordine[ordini.size()]));
        double sommatoriaIndicePriority2 = numFogli < 2 ? 0 : getSommaIndicePriorita(foglio2, ordini.toArray(new Ordine[ordini.size()]));
        double sommatoriaIndicePriority3 = numFogli < 3 ? 0 : getSommaIndicePriorita(foglio3, ordini.toArray(new Ordine[ordini.size()]));
        double sommatoriaIndicePriority4 = numFogli < 4 ? 0 : getSommaIndicePriorita(foglio4, ordini.toArray(new Ordine[ordini.size()]));
        
        // Calcoliamo il costo di penalità complessivo
        double costoPenalty = beta * (ritardoFoglio1 * sommatoriaIndicePriority1
                + ritardoFoglio2 * sommatoriaIndicePriority2
                + ritardoFoglio3 * sommatoriaIndicePriority3
                + ritardoFoglio4 * sommatoriaIndicePriority4);

////Calcoliamo la fitness
        double fitness = costoSpreco + costoPenalty;

////Invertiamo la fitness (in modo da averla decrescente)
        fitness = 1 / fitness;
//        System.out.println("\n\n FITNESS ==== >>  "+fitness);

////Memorizziamo la fitness
        individual.setFitness(fitness);
        return fitness;

    }

    /*Valutiamo la popolazione attraverso un loop che calcola la fitness per ogni individuo
     e dopo calcola la fitness dell'intera popolazione
     */
    public void evalPopulation(Population population, double areaFoglio, double alpha, double beta, List<Ordine> ordini) {

        double populationFitness = 0;

        for (Individual individual : population.getIndividuals()) {
            try {

//                System.out.println("INDIVIDUAL FITNESS BEFORE = "+individual.getFitness());
                double fitness = calcFitness(individual, areaFoglio, alpha, beta, ordini);
//                System.out.println("INDIVIDUAL FITNESS AFTER = "+individual.getFitness());
                populationFitness += fitness;
            } catch (SoluzioneImpossibileException ex) {
            }
        }
        population.setPopulationFitness(populationFitness);
    }

//Verifichiamo le condizioni di terminazione
    public boolean isTerminationConditionMet(int generationsCount, int maxGenerations) {
        return (generationsCount > maxGenerations);

    }

    /*Facciamo la selezione per il crossover in modo randomico e dopo applichiamo il crossover. 
* Ogni individuo viene considerato per il crossover e confrontando il tasso di crossover con 
* un numero casuale decidiamo se all'individuo va applicato il crossover. 
* Dopo aver selezionato un primo individuo bisogna trovarne un secondo, per la selezione utilizziamo
* il metodo della ruota della roulette.
     */
    public Individual selectParent(Population population) {

//Otteniamo gli individui 
        Individual individuals[] = population.getIndividuals();

//Giriamo la ruota della roulette
        double populationFitness = population.getPopulationFitness();
        double rouletteWheelPosition = Math.random() * populationFitness;

//Troviamo il genitore 1 	
        double spinWheel = 0;
        for (Individual individual : individuals) {
            spinWheel += individual.getFitness();
            if (spinWheel >= rouletteWheelPosition) {
                return individual;
            }
        }
        return individuals[population.size() - 1];
    }

    public Population crossoverPopulation(Population population) {

//Creiamo una nuova popolazione 
        population.select();
        Population newPopulation = new Population(population.size());

//Facciamo un loop per capire se applicare il crossover all'individuo 1
        for (int populationIndex = 0; populationIndex < population.size(); populationIndex++) {
            Individual parent1 = population.getFittest(populationIndex);

            if (this.crossoverRate > Math.random() && populationIndex >= this.elitismCount) {

                Individual offspring = new Individual(parent1.getVettoreLength());

//Cerchiamo il genitore 2
                Individual parent2 = selectParent(population);

//Facciamo un loop sul genoma
                for (int iIndex = 0; iIndex < parent1.getVettoreLength(); iIndex++) {

//Usiamo metà dei geni del genitore 1 e metà di quelli del genitore 2 
                    if (0.5 > Math.random()) {
                        offspring.setI(iIndex, parent1.getI(iIndex));
                    } else {
                        offspring.setI(iIndex, parent2.getI(iIndex));
                    }
                }

//Aggiungiamo la prole alla nuova popolazione
                newPopulation.setIndividual(populationIndex, offspring);
            } else {

                newPopulation.setIndividual(populationIndex, parent1);
            }
        }

        return newPopulation;
    }

    /* Applichiamo la mutazione, tenendo conto del tasso di mutazione. 
* Per la mutazione invertiamo la posizione di due geni usando quella che viene chiamata mutazione
* di scambio (swap mutation).
* 
     */
    public Population mutatePopulation(Population population) {

//Creiamo una nuova popolazione che contenga gli individui mutati
        Population newPopulation = new Population(this.populationSize);

//Facciamo un loop a partire dalle persone più in forma
        for (int populationIndex = 0; populationIndex < population.size(); populationIndex++) {
            Individual individual = population.getFittest(populationIndex);

//Saltiamo la mutazione se incontriamo individui di elite
            if (populationIndex >= this.elitismCount) {

//Facciamo un loop sui geni di ogni individuo per vedere quali geni devono essere mutati
                for (int iIndex = 0; iIndex < individual.getVettoreLength(); iIndex++) {

                    if (this.mutationRate > Math.random()) {
//Se un gene deve essere mutato ne viene scelto un altro casuale per lo swap 

                        int newIPos = (int) (Math.random() * individual.getVettoreLength());

                        int i1 = individual.getI(newIPos);
                        int i2 = individual.getI(iIndex);

                        individual.setI(iIndex, i1);
                        individual.setI(newIPos, i2);
                    }
                }
            }

//Aggiungiamo l'individuo mutato alla popolazione
            newPopulation.setIndividual(populationIndex, individual);
        }

        return newPopulation;
    }

    public double minimoCosto(Population population, Ordine[] ordini, double areaFoglio, double alpha, double beta) throws SoluzioneImpossibileException {

        Individual fittest = population.getFittest(0);

            //  int[] vettore = individual.getvettore();
      
        int[] foglio1 = null;
        int[] foglio2 = null;
        int[] foglio3 = null;
        int[] foglio4 = null;

        if(fittest == null){
            return 0;
        }
//        System.out.println(" <<<<individual : "+individual);
        List<Integer> sequenzaFogli = fittest.getSequenzaFogli();

        int numFogli = sequenzaFogli.size();

        foglio1 = fittest.getOrdiniByIndex(sequenzaFogli.get(0));
        foglio2 = numFogli < 2 ? null : fittest.getOrdiniByIndex(sequenzaFogli.get(1));
        foglio3 = numFogli < 3 ? null : fittest.getOrdiniByIndex(sequenzaFogli.get(2));
        foglio4 = numFogli < 4 ? null : fittest.getOrdiniByIndex(sequenzaFogli.get(3));

        double areaOrdiniFoglio1 = calculateAreaOrdiniByIndex(foglio1, ordini);
        double areaOrdiniFoglio2 = numFogli < 2 ? 0 : calculateAreaOrdiniByIndex(foglio2, ordini);
        double areaOrdiniFoglio3 = numFogli < 3 ? 0 : calculateAreaOrdiniByIndex(foglio3, ordini);
        double areaOrdiniFoglio4 = numFogli < 4 ? 0 : calculateAreaOrdiniByIndex(foglio4, ordini);

        if (areaOrdiniFoglio1 > areaFoglio || areaOrdiniFoglio2 > areaFoglio || areaOrdiniFoglio3 > areaFoglio || areaOrdiniFoglio4 > areaFoglio) {

            fittest.setNospace(true);
            throw new SoluzioneImpossibileException("La somma delle aree degli ordini non può eccedere l'area del foglio");
        } else {

        }

        double sprecoFoglio1 = (areaFoglio - areaOrdiniFoglio1) / areaFoglio;
        double sprecoFoglio2 = (areaFoglio - areaOrdiniFoglio2) / areaFoglio;
        double sprecoFoglio3 = (areaFoglio - areaOrdiniFoglio3) / areaFoglio;
        double sprecoFoglio4 = (areaFoglio - areaOrdiniFoglio4) / areaFoglio;

        if (sprecoFoglio2 == 1) {
            sprecoFoglio2 = 0;
        }
        if (sprecoFoglio3 == 1) {
            sprecoFoglio3 = 0;
        }
        if (sprecoFoglio4 == 1) {
            sprecoFoglio4 = 0;
        }

        double costoSpreco = alpha * (sprecoFoglio1 + sprecoFoglio2 + sprecoFoglio3 + sprecoFoglio4);

        double minDueDateFoglio1 = calculateMinExpirationDate(foglio1,ordini);
        double minDueDateFoglio2 = numFogli < 2 ? 0 : calculateMinExpirationDate(foglio2, ordini);
        double minDueDateFoglio3 = numFogli < 3 ? 0 : calculateMinExpirationDate(foglio3, ordini);
        double minDueDateFoglio4 = numFogli < 4 ? 0 : calculateMinExpirationDate(foglio4, ordini);

        double tempoProcessamentoFoglio1 = getSommaTempoRetinatura(foglio1, ordini);
        double tempoProcessamentoFoglio2 = numFogli < 2 ? 0 : getSommaTempoRetinatura(foglio2, ordini);
        double tempoProcessamentoFoglio3 = numFogli < 3 ? 0 : getSommaTempoRetinatura(foglio3, ordini);
        double tempoProcessamentoFoglio4 = numFogli < 4 ? 0 : getSommaTempoRetinatura(foglio4, ordini);


             Map<Integer, Double> tempiProcessMap = fittest.getTempiDiProcessamento(ordini);

        double ritardoFoglio1 = 0;
        double ritardoFoglio2 = 0;
        double ritardoFoglio3 = 0;
        double ritardoFoglio4 = 0;
        if (tempiProcessMap.containsKey(1)) {
            ritardoFoglio1 = tempiProcessMap.get(1) - minDueDateFoglio1 >= 0 ? tempiProcessMap.get(1) - minDueDateFoglio1 : 0;
        }
        if (tempiProcessMap.containsKey(2)) {
            ritardoFoglio2 = tempiProcessMap.get(2) - minDueDateFoglio1 >= 0 ? tempiProcessMap.get(2) - minDueDateFoglio1 : 0;
        }
        if (tempiProcessMap.containsKey(3)) {
            ritardoFoglio3 = tempiProcessMap.get(3) - minDueDateFoglio1 >= 0 ? tempiProcessMap.get(3) - minDueDateFoglio1 : 0;
        }
        if (tempiProcessMap.containsKey(4)) {
            ritardoFoglio4 = tempiProcessMap.get(4) - minDueDateFoglio1 >= 0 ? tempiProcessMap.get(4) - minDueDateFoglio1 : 0;
        }
      
        double sommatoriaIndicePriority1 = getSommaIndicePriorita(foglio1, ordini);
        double sommatoriaIndicePriority2 = numFogli < 2 ? 0 : getSommaIndicePriorita(foglio2, ordini);
        double sommatoriaIndicePriority3 = numFogli < 3 ? 0 : getSommaIndicePriorita(foglio3, ordini);
        double sommatoriaIndicePriority4 = numFogli < 4 ? 0 : getSommaIndicePriorita(foglio4, ordini);

        double costoPenalty = beta * (ritardoFoglio1 * sommatoriaIndicePriority1
                + ritardoFoglio2 * sommatoriaIndicePriority2
                + ritardoFoglio3 * sommatoriaIndicePriority3
                + ritardoFoglio4 * sommatoriaIndicePriority4);


////Calcoliamo la fitness
        double fitness = costoSpreco + costoPenalty;

////Invertiamo la fitness (in modo da averla decrescente)
        fitness = 1 / fitness;
//        System.out.println("\n\n FITNESS ==== >>  "+fitness);

////Memorizziamo la fitness
       // fittest.setFitness(fitness);
        
        return fitness;

    }

}
