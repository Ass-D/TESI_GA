package it.cnr.istc.gen.duericariche;

public class Individual {

	
	private int[] vettore;
	private double fitness = -1;
	
	

	
	public Individual(int[] vettore) {
	
		// Creiamo il cromosoma (vettore)
		
		this.vettore = vettore;
	}
	
/* Inizializziamo in modo randomico. 
* Creiamo un vettore che sia composto da numeri che vanno da 1 a 4
*/
	
	public Individual(int vettoreLength) {

	this.vettore = new int[vettoreLength];
		for (int i = 0; i < vettoreLength; i++) {
			vettore[i]=((int)((Math.random()*100)%4)+1);
			}
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
		return output;
	}
	

}
