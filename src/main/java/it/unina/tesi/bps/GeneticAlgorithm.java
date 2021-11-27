package it.unina.tesi.bps;

import it.unina.tesi.bps.exceptions.SoluzioneImpossibileException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    private double getSommaTempoRetinatura(int[] indexes, Ordine[] ordini) {
        double somma = 0;
        for (int index : indexes) {
            somma += ordini[index].tempoElaborazioneRetinatura();
        }
        return somma;
    }

    private double getSommaIndicePriorita(int[] indexes, Ordine[] ordini) {
        double somma = 0;
        for (int index : indexes) {
            somma += ordini[index].indiceDiPriotita();
        }
        return somma;
    }

//Calcoliamo la fitness per un individuo
    public double calcFitness(Individual individual, double areaFoglio, double alpha, double beta, List<Ordine> ordini) throws SoluzioneImpossibileException {

        //  int[] vettore = individual.getvettore();
        //int[] nessunFoglio = individual.getOrdiniByIndex(0);
        int[] foglio1 = individual.getOrdiniByIndex(1);
        int[] foglio2 = individual.getOrdiniByIndex(2);
        int[] foglio3 = individual.getOrdiniByIndex(3);
        int[] foglio4 = individual.getOrdiniByIndex(4);

        double areaOrdiniFoglio1 = calculateAreaOrdiniByIndex(foglio1, ordini.toArray(new Ordine[ordini.size()]));
        double areaOrdiniFoglio2 = calculateAreaOrdiniByIndex(foglio2, ordini.toArray(new Ordine[ordini.size()]));
        double areaOrdiniFoglio3 = calculateAreaOrdiniByIndex(foglio3, ordini.toArray(new Ordine[ordini.size()]));
        double areaOrdiniFoglio4 = calculateAreaOrdiniByIndex(foglio4, ordini.toArray(new Ordine[ordini.size()]));

        if (areaOrdiniFoglio1 > areaFoglio || areaOrdiniFoglio2 > areaFoglio || areaOrdiniFoglio3 > areaFoglio || areaOrdiniFoglio4 > areaFoglio) {
//            System.out.println("areaOrdiniFoglio1  =  "+areaOrdiniFoglio1);
//            System.out.println("areaFoglio  =  "+areaFoglio);
//            System.out.println("============================================");
//            System.out.println("areaOrdiniFoglio2  =  "+areaOrdiniFoglio2);
//            System.out.println("areaFoglio  =  "+areaFoglio);
//            System.out.println("============================================");
            throw new SoluzioneImpossibileException("La somma delle aree degli ordini non può eccedere l'area del foglio");
        } else {
//            System.out.println("********** VINCOLI OK ****************");
        }

        double sprecoFoglio1 = (areaFoglio - areaOrdiniFoglio1) / areaFoglio;
        double sprecoFoglio2 = (areaFoglio - areaOrdiniFoglio2) / areaFoglio;
        double sprecoFoglio3 = (areaFoglio - areaOrdiniFoglio3) / areaFoglio;
        double sprecoFoglio4 = (areaFoglio - areaOrdiniFoglio4) / areaFoglio;
        
        
////ASSUNTA:  VORREI INSERIRE ANCHE QUESTO VINCOLO come quello sopra
/*
        if (sprecoFoglio1 >= 0.25 || sprecoFoglio2 >= 0.25 || sprecoFoglio3 >= 0.25 ){
            throw new SoluzioneImpossibileException2('L indice di utilizzo del foglio è troppo basso');
        }
*/
        double costoSpreco = alpha * (sprecoFoglio1 + sprecoFoglio2 + sprecoFoglio3 + sprecoFoglio4);
        
        double minDueDateFoglio1 = calculateMinExpirationDate(foglio1, ordini.toArray(new Ordine[ordini.size()]));
        double minDueDateFoglio2 = calculateMinExpirationDate(foglio2, ordini.toArray(new Ordine[ordini.size()]));
        double minDueDateFoglio3 = calculateMinExpirationDate(foglio3, ordini.toArray(new Ordine[ordini.size()]));
        double minDueDateFoglio4 = calculateMinExpirationDate(foglio4, ordini.toArray(new Ordine[ordini.size()]));

        double tempoElaborazioneFoglio1 = getSommaTempoRetinatura(foglio1, ordini.toArray(new Ordine[ordini.size()]));
        double tempoElaborazioneFoglio2 = getSommaTempoRetinatura(foglio2, ordini.toArray(new Ordine[ordini.size()]));
        double tempoElaborazioneFoglio3 = getSommaTempoRetinatura(foglio3, ordini.toArray(new Ordine[ordini.size()]));
        double tempoElaborazioneFoglio4 = getSommaTempoRetinatura(foglio4, ordini.toArray(new Ordine[ordini.size()]));

        //qva
//        if(individual.getvettore()[0] == 1){
//            double c1 = tempoElaborazioneFoglio1; asdoòhashd
//        }
   // TEMPI DI COMPLETAMENTO DEI FOGLI
   
       double tempoCompletamentoF1 = tempoElaborazioneFoglio1;
       double tempoCompletamentoF2 = tempoElaborazioneFoglio1 + tempoElaborazioneFoglio2;
       double tempoCompletamentoF3 = tempoElaborazioneFoglio1 + tempoElaborazioneFoglio2 + tempoElaborazioneFoglio3;
       double tempoCompletamentoF4 = tempoElaborazioneFoglio1 + tempoElaborazioneFoglio2 + tempoElaborazioneFoglio3 + tempoElaborazioneFoglio4;

// ASSUNTA - IL CONCETTO DELLA MAPPA METTO A COMMENTO PERCHè DEVO VALUTARE QUALE CONCETTO MI AIUTA DI PIù. 
// SE DECIDERE IO LA SEQUENZA SEMPRE di 1-2-3-4 COME FATTO ALL'INIZIO O FARLA VALUTARE ALL' ALGORITMO
  /*     Map<Integer, Double> tempiElabMap = individual.getTempiDiElaborazione(ordini);

        //TODO RENDERE GENERICA QUESTA PARTE 
        double ritardoFoglio1 = 0;
        double ritardoFoglio2 = 0;
        double ritardoFoglio3 = 0;
        double ritardoFoglio4 = 0;
        if (tempiElabMap.containsKey(1)) {
            ritardoFoglio1 = tempiElabMap.get(1) - minDueDateFoglio1 >= 0 ? tempiElabMap.get(1) - minDueDateFoglio1 : 0;
        }
        if (tempiElabMap.containsKey(2)) {
            ritardoFoglio2 = tempiElabMap.get(2) - minDueDateFoglio1 >= 0 ? tempiElabMap.get(2) - minDueDateFoglio1 : 0;
        }
        if (tempiElabMap.containsKey(3)) {
            ritardoFoglio3 = tempiElabMap.get(3) - minDueDateFoglio1 >= 0 ? tempiElabMap.get(3) - minDueDateFoglio1 : 0;
        }
        if (tempiElabMap.containsKey(4)) {
            ritardoFoglio4 = tempiElabMap.get(4) - minDueDateFoglio1 >= 0 ? tempiElabMap.get(4) - minDueDateFoglio1 : 0;
        }
*/
  
  // RITARDO FOGLI
  
        double ritardoFoglio1 = tempoCompletamentoF1 - minDueDateFoglio1 >= 0 ? tempoCompletamentoF1 - minDueDateFoglio1 : 0;
        double ritardoFoglio2 = tempoCompletamentoF2 - minDueDateFoglio2 >= 0 ? tempoCompletamentoF2 - minDueDateFoglio2 : 0;
        double ritardoFoglio3 = tempoCompletamentoF3 - minDueDateFoglio3 >= 0 ? tempoCompletamentoF3 - minDueDateFoglio3 : 0;
        double ritardoFoglio4 = tempoCompletamentoF4 - minDueDateFoglio4 >= 0 ? tempoCompletamentoF4 - minDueDateFoglio4 : 0;
  
        double sommatoriaIndicePriority1 = getSommaIndicePriorita(foglio1, ordini.toArray(new Ordine[ordini.size()]));
        double sommatoriaIndicePriority2 = getSommaIndicePriorita(foglio2, ordini.toArray(new Ordine[ordini.size()]));
        double sommatoriaIndicePriority3 = getSommaIndicePriorita(foglio3, ordini.toArray(new Ordine[ordini.size()]));
        double sommatoriaIndicePriority4 = getSommaIndicePriorita(foglio4, ordini.toArray(new Ordine[ordini.size()]));

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

        //<editor-fold defaultstate="collapsed" desc=" Codice della tua amica ">    
//
//        double tcagv2 = max3 + max4 + tr; //tempo di completamento AGV2
//
////Calcoliamo la fitness
//        double fitness = 0;
//        if (tcagv1 > tcagv2) {
//            fitness = tcagv1;
//        } else {
//            fitness = tcagv2;
//        }
//
////Invertiamo la fitness (in modo da averla decrescente)
//        fitness = 1 / fitness;
//
////Memorizziamo la fitness
//        individual.setFitness(fitness);
//
//        return fitness;
//</editor-fold>
    }

    /*Valutiamo la popolazione attraverso un loop che calcola la fitness per ogni individuo
* e dopo calcola la fitness dell'intera popolazione
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
//                System.out.println("Vincolo non rispettato");

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
* la ruota della roulette.
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
        Population newPopulation = new Population(population.size());

//Facciamo un loop per capire se applicare il crossover all'individuo 1
        for (int populationIndex = 0; populationIndex < population.size(); populationIndex++) {
            Individual parent1 = population.getFittest(populationIndex);

            if (this.crossoverRate > Math.random() && populationIndex >= this.elitismCount) {

                Individual offspring = new Individual(parent1.getVettoreLength());

//Cerchiamo il secondo genitore
                Individual parent2 = selectParent(population);

//Facciamo un loop sul genoma
                for (int iIndex = 0; iIndex < parent1.getVettoreLength(); iIndex++) {

//Usiamo met� dei geni del genitore 1 e metà di quelli del genitore 2 
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

//Facciamo un loop a partire dalle persone pi� in forma
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

        //int[] nessunFoglio = fittest.getOrdiniByIndex(0);
        int[] foglio1 = fittest.getOrdiniByIndex(1);
        int[] foglio2 = fittest.getOrdiniByIndex(2);
        int[] foglio3 = fittest.getOrdiniByIndex(3);
        int[] foglio4 = fittest.getOrdiniByIndex(4);

        double areaOrdiniFoglio1 = calculateAreaOrdiniByIndex(foglio1, ordini);
        double areaOrdiniFoglio2 = calculateAreaOrdiniByIndex(foglio2, ordini);
        double areaOrdiniFoglio3 = calculateAreaOrdiniByIndex(foglio3, ordini);
        double areaOrdiniFoglio4 = calculateAreaOrdiniByIndex(foglio4, ordini);

        if (areaOrdiniFoglio1 > areaFoglio || areaOrdiniFoglio2 > areaFoglio || areaOrdiniFoglio3 > areaFoglio|| areaOrdiniFoglio4 > areaFoglio) {
            throw new SoluzioneImpossibileException("La somma delle aree degli ordini non può eccedere l'area del foglio");
        }

        double sprecoFoglio1 = (areaFoglio - areaOrdiniFoglio1) / areaFoglio;
        double sprecoFoglio2 = (areaFoglio - areaOrdiniFoglio2) / areaFoglio;
        double sprecoFoglio3 = (areaFoglio - areaOrdiniFoglio3) / areaFoglio;
        double sprecoFoglio4 = (areaFoglio - areaOrdiniFoglio4) / areaFoglio;

        double costoSpreco = alpha * (sprecoFoglio1 + sprecoFoglio2+ sprecoFoglio3+ sprecoFoglio4);

        double minDueDateFoglio1 = calculateMinExpirationDate(foglio1, ordini);
        double minDueDateFoglio2 = calculateMinExpirationDate(foglio2, ordini);
        double minDueDateFoglio3 = calculateMinExpirationDate(foglio3, ordini);
        double minDueDateFoglio4 = calculateMinExpirationDate(foglio4, ordini);

        double tempoElaborazioneFoglio1 = getSommaTempoRetinatura(foglio1, ordini);
        double tempoElaborazioneFoglio2 = getSommaTempoRetinatura(foglio2, ordini);
        double tempoElaborazioneFoglio3 = getSommaTempoRetinatura(foglio3, ordini);
        double tempoElaborazioneFoglio4 = getSommaTempoRetinatura(foglio4, ordini);

       double tempoCompletamentoF1 = tempoElaborazioneFoglio1;
       double tempoCompletamentoF2 = tempoElaborazioneFoglio1 + tempoElaborazioneFoglio2;
       double tempoCompletamentoF3 = tempoElaborazioneFoglio1 + tempoElaborazioneFoglio2 + tempoElaborazioneFoglio3;
       double tempoCompletamentoF4 = tempoElaborazioneFoglio1 + tempoElaborazioneFoglio2 + tempoElaborazioneFoglio3 + tempoElaborazioneFoglio4;

     
        double ritardoFoglio1 = tempoCompletamentoF1 - minDueDateFoglio1 >= 0 ? tempoCompletamentoF1 - minDueDateFoglio1 : 0;
        double ritardoFoglio2 = tempoCompletamentoF2 - minDueDateFoglio2 >= 0 ? tempoCompletamentoF2 - minDueDateFoglio2 : 0;
        double ritardoFoglio3 = tempoCompletamentoF3 - minDueDateFoglio3 >= 0 ? tempoCompletamentoF3 - minDueDateFoglio3 : 0;
        double ritardoFoglio4 = tempoCompletamentoF4 - minDueDateFoglio4 >= 0 ? tempoCompletamentoF4 - minDueDateFoglio4 : 0;
  
        double sommatoriaIndicePriority1 = getSommaIndicePriorita(foglio1, ordini);
        double sommatoriaIndicePriority2 = getSommaIndicePriorita(foglio2, ordini);
        double sommatoriaIndicePriority3 = getSommaIndicePriorita(foglio3, ordini);
        double sommatoriaIndicePriority4 = getSommaIndicePriorita(foglio4, ordini); 
        
        double costoPenalty = beta * (ritardoFoglio1 * sommatoriaIndicePriority1 + 
                ritardoFoglio2 * sommatoriaIndicePriority2 + 
                ritardoFoglio3 * sommatoriaIndicePriority3 + 
                ritardoFoglio4 * sommatoriaIndicePriority4);
        

        return costoSpreco + costoPenalty;

//        int[] Fittest = population.getFittest(0).getvettore();
//
//        int[] primoFoglio = new int[Fittest.length];
//        for (int i = 0; i < Fittest.length; i++) {
//            if (Fittest[i] != 0 && Fittest[i] != 2) {
//                primoFoglio[i] = Fittest[i];
//            }
//        }
//
//        int[] secondoFoglio = new int[Fittest.length];
//        for (int i = 0; i < Fittest.length; i++) {
//            if (Fittest[i] != 0 && Fittest[i] != 1) {
//                secondoFoglio[i] = Fittest[i];
//            }
//        }
        // FOGLIO 1
//        double[] tf1 = new double[vettore.length]; //tempo figurato1 AGV1
//        double cumulata1 = 0;
//        for (int i = 0; i < vettore.length; i++) {
//            if (agv1[i] == 1) {
//                tf1[i] = tp[i] * w[i] + cumulata1;
//                cumulata1 = tf1[i];
//            }
//        }
//
//        double[] tf2 = new double[vettore.length]; //tempo figurato2 AGV1
//        double cumulata2 = 0;
//        for (int i = 0; i < vettore.length; i++) {
//            if (agv1[i] == 2) {
//                tf2[i] = tp[i] * w[i] + cumulata2;
//                cumulata2 = tf2[i];
//            }
//        }
//        double[] t1 = new double[Fittest.length]; //tempo di complet1 AGV1
//        double cumulata1 = 0;
//        for (int i = 0; i < Fittest.length; i++) {
//            if (primoFoglio[i] == 1) {
//                t1[i] = tp[i] + cumulata1;
//                cumulata1 = t1[i];
//            }
//        }
//
//        double massimo1 = t1[0]; //massimo dei tempi di complet1 AGV1
//        for (int i = 0; i < Fittest.length; i++) {
//            if (t1[i] > massimo1) {
//                massimo1 = t1[i];
//            }
//        }
//
//        double[] t2 = new double[Fittest.length]; //tempo di complet2 AGV1
//        double cumulata2 = 0;
//        for (int i = 0; i < Fittest.length; i++) {
//            if (primoFoglio[i] == 2) {
//                t2[i] = tp[i] + cumulata2;
//                cumulata2 = t2[i];
//            }
//        }
//
//        double massimo2 = t2[0]; //massimo dei tempi di complet2 AGV1
//        for (int i = 0; i < Fittest.length; i++) {
//            if (t2[i] > massimo2) {
//                massimo2 = t2[i];
//            }
//        }
//
//        double tcagv1 = massimo1 + massimo2 + tr; //tempo di completamento agv1 
//
//        //AGV2
//        double[] t3 = new double[Fittest.length]; //tempo di complet3 AGV2
//        double cumulata3 = 0;
//        for (int i = 0; i < Fittest.length; i++) {
//            if (secondoFoglio[i] == 3) {
//                t3[i] = tp[i] + cumulata3;
//                cumulata3 = t3[i];
//            }
//        }
//
//        double massimo3 = t3[0]; //massimo dei tempi di complet3 AGV2
//        for (int i = 0; i < Fittest.length; i++) {
//            if (t3[i] > massimo3) {
//                massimo3 = t3[i];
//            }
//        }
//
//        double[] t4 = new double[Fittest.length]; //tempo di complet4 AGV2
//        double cumulata4 = 0;
//        for (int i = 0; i < Fittest.length; i++) {
//            if (secondoFoglio[i] == 4) {
//                t4[i] = tp[i] + cumulata4;
//                cumulata4 = t4[i];
//            }
//        }
//
//        double massimo4 = t4[0]; //massimo dei tempi di complet4 AGV2
//        for (int i = 0; i < Fittest.length; i++) {
//            if (t4[i] > massimo4) {
//                massimo4 = t4[i];
//            }
//        }
//
//        double tcagv2 = massimo3 + massimo4 + tr; //tempo di completamento agv2
//
//        double fitness1 = 0;
//        if (tcagv1 > tcagv2) {
//            fitness1 = tcagv1;
//        } else {
//            fitness1 = tcagv2;
//        }
//
//        return fitness1;
    }

}
