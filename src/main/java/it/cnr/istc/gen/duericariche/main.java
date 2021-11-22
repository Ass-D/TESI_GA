package it.cnr.istc.gen.duericariche;

public class main {
	
	
	public static int maxGenerations = 100; // massimo num. di iterazioni per la terminazione


	public static void main(String[] args) {
		
		//int alpha = 100000; //alpha per la prob di guasto
		int MTBF = 6; //tempo medio tra due guasti
		double tg = 5; //tempo medio di recupero a guasto
		double tr = 2; //tempo di ricarica
                
                //
                int H = 3; //altezza del foglio
                int W = 4; //larghezza del foglio
                int Area = H*W; //area del foglio
                
                double alpha = 3.2d; //costo spreco
                double beta = 2.1d; //costo penalità

 
                
                Ordine [] ordini = new Ordine[30];
                
                //                      h   w   p   d   r
                ordini[0] = new Ordine(2d, 2d, 3d, 3L, 3d);
                ordini[1] = new Ordine(2d, 2d, 3d, 3L, 3d);
                ordini[2] = new Ordine(2d, 2d, 3d, 3L, 3d);
                ordini[3] = new Ordine(2d, 2d, 3d, 3L, 3d);
                ordini[4] = new Ordine(2d, 2d, 3d, 3L, 3d);
                ordini[5] = new Ordine(2d, 2d, 3d, 3L, 3d);
                ordini[6] = new Ordine(2d, 2d, 3d, 3L, 3d);
                ordini[7] = new Ordine(2d, 2d, 3d, 3L, 3d);
                ordini[8] = new Ordine(2d, 2d, 3d, 3L, 3d);
                //TO BE CONTINUED..
                
                
       
                
                

		 /* Calcoliamo il vettore dei tempi di processamento in modo casuale
		 
		int [ ] tp = new int [10]; //vettore tempi di processamento
		for (int i=0; i<10; i++) {
			tp[i] = ((int) ((Math.random()*100)%2)+1); } //genero un vettore di lunghezza 10 con valori tra 1 e 2
		
		*/
	    
		double [] tp = {0.5,0.7,1,1.5,2,2.5,3.1,3.5}; 
		double [] w = {1,1.5,1,2,1.5,1.5,1.5,1.5};

		
		/*Definiamo un vettore per peso di ogni job
		double [] w = new double [10];
		int w1=0;
		for (int i=0; i<10; i++) {
			w1=(int) ((Math.random()*100%3));
			switch (w1) {
			case 0: w[i]=1;
					break;
			case 1: w[i]=1.5;
					break;
			case 2: w[i]=2;
					break;
			}
		}*/
		
		
		 
//Creiamo l'oggetto ga
		    GeneticAlgorithm ga = new GeneticAlgorithm(100, 0.01, 0.95, 0);
		    
		   
//Inizializziamo la popolazione specificando la lunghezza dei cromosomi
			Population population = ga.initPopulation(8);

//Valutiamo la popolazione
			ga.evalPopulation(population, tp, w, alpha, MTBF, tg, tr);

			int generation = 1;

			while (ga.isTerminationConditionMet(generation, maxGenerations) == false) {

//Stampiamo l'individuo pi� in forma 
				Individual fittest = population.getFittest(0);
				System.out.println(
						"G" + generation + " Best solution (" + fittest.getFitness() + ")" );

//Applichiamo il crossover
			population = ga.crossoverPopulation(population);
					
//Applichiamo la mutazione
			population = ga.mutatePopulation(population);


//Valutiamo la popolazione
			ga.evalPopulation(population, tp, w, alpha, MTBF, tg, tr);

					generation++;
				}

			System.out.println("Stopped after " + maxGenerations + " generations.");{
			Individual fittest = population.getFittest(0);
			System.out.println("La fitness � " + fittest.getFitness());
			}
	
			System.out.println("La schedulazione �: " + population.getFittest(0));
			
			
			
		
			double tempodicompletamento = ga.tempodicompletamento (population,tp,tr);
			System.out.println("Cmax �: " + tempodicompletamento );
			
			System.out.print("tp �: ");
			for (int i = 0; i < 8; i++)  {
			   System.out.print(tp[i] + " ");
			 } 
			
}


}
