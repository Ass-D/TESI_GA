package it.cnr.istc.gen.duericariche;

import it.cnr.istc.gen.duericariche.files.OrdiniFileReader;

public class Main {

    public static int maxGenerations = 100; // massimo num. di iterazioni per la terminazione

    public static void main(String[] args) {
        
        System.out.println("TEST  READ FILE: ");
        Ordine[] ordini = OrdiniFileReader.readFile("dati.csv");
        
        int id = 1;
        for (Ordine ordine : ordini) {
            System.out.println(id+") "+ordine);
            id++;
        }


     

        double H = 127; //altezza del foglio
        double W = 203; //larghezza del foglio

        double areaFoglio = H * W; //area del foglio

        double alpha = 450; //costo spreco
        double beta = 50; //costo penalità

//        Ordine[] ordini = new Ordine[30];

        //                      h   w   p   d   r
//        ordini[0] = new Ordine(2d, 2d, 3d, 3L, 3d);
//        ordini[1] = new Ordine(2d, 2d, 3d, 3L, 3d);
//        ordini[2] = new Ordine(2d, 2d, 3d, 3L, 3d);
//        ordini[3] = new Ordine(2d, 2d, 3d, 3L, 3d);
//        ordini[4] = new Ordine(2d, 2d, 3d, 3L, 3d);
//        ordini[5] = new Ordine(2d, 2d, 3d, 3L, 3d);
//        ordini[6] = new Ordine(2d, 2d, 3d, 3L, 3d);
//        ordini[7] = new Ordine(2d, 2d, 3d, 3L, 3d);
//        ordini[8] = new Ordine(2d, 2d, 3d, 3L, 3d);

        //TO BE CONTINUED..
      
// Creiamo l'oggetto ga
        GeneticAlgorithm ga = new GeneticAlgorithm(100, 0.01, 0.95, 0);

//Inizializziamo la popolazione specificando la lunghezza dei cromosomi
        Population population = ga.initPopulation(30);

//Valutiamo la popolazione

   

       ga.evalPopulation(population, areaFoglio, alpha, beta, ordini) ;


        int generation = 1;

        while (ga.isTerminationConditionMet(generation, maxGenerations) == false) {

//Stampiamo l'individuo pi� in forma 
            Individual fittest = population.getFittest(0);
            System.out.println(
                    "G" + generation + " Best solution (" + fittest.getFitness() + ")");

//Applichiamo il crossover
            population = ga.crossoverPopulation(population);

//Applichiamo la mutazione
            population = ga.mutatePopulation(population);

//Valutiamo la popolazione
//       OHNO     ga.evalPopulation(population, tp, w, alpha, MTBF, tg, tr);

            generation++;
        }

        System.out.println("Stopped after " + maxGenerations + " generations.");
        {
            Individual fittest = population.getFittest(0);
            System.out.println("La fitness è: " + fittest.getFitness());
        }

        System.out.println("La schedulazione è: " + population.getFittest(0));



      /*  System.out.print("tp �: ");
        for (int i = 0; i < 8; i++) {
            System.out.print(tp[i] + " ");
        }
*/
    }

}
