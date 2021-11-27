package it.unina.daura.bps;

import it.unina.daura.bps.exceptions.SoluzioneImpossibileException;
import it.unina.daura.bps.utils.ConsoleColors;
import it.unina.daura.bps.utils.OrdiniFileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.fusesource.jansi.AnsiConsole;

public class Main {

    public static int maxGenerations = 100; // massimo num. di iterazioni per la terminazione
    public static final String version = "1.0";
    public static double foglio_W = 203; //larghezza del foglio
    public static double alpha = 451.6; //costo spreco
    public static double beta = 82.5; //costo penalità
    public static double foglio_H = 127; //altezza del foglio

    public static void main(String[] args) {

        AnsiConsole.systemInstall();
        System.out.println(ConsoleColors.ANSI_YELLOW + "================================================================" + ConsoleColors.ANSI_RESET);
        System.out.println(ConsoleColors.ANSI_GREEN + "\t\t<<" + ConsoleColors.CYAN_BRIGHT + " BIN PACKING SHEDULING " + ConsoleColors.ANSI_GREEN + ">> " + ConsoleColors.ANSI_RESET);
        System.out.println(ConsoleColors.ANSI_GREEN + "\t\t      Version: " + ConsoleColors.PURPLE_BRIGHT + version + ConsoleColors.ANSI_RESET);
        System.out.println(ConsoleColors.ANSI_YELLOW + "================================================================\n\n\n" + ConsoleColors.ANSI_RESET);
        
        System.out.println(ConsoleColors.ANSI_YELLOW + "Inserisci comando (start per iniziare)." + ConsoleColors.ANSI_GREEN);

        while (true) {
            BufferedReader reader
                    = new BufferedReader(new InputStreamReader(System.in));
            try {
                String command = reader.readLine();
                if (command.equals("quit")) {
                    System.out.println(ConsoleColors.ANSI_RED + "Quitting.."+ConsoleColors.ANSI_RESET);
                    System.exit(0);
                }
                if (command.equals("start")) {
                    System.out.println(ConsoleColors.ANSI_YELLOW + "Inserisci il nome del file contenente i dati (senza estensione)"+ConsoleColors.ANSI_GREEN);
                    String nomeFile = reader.readLine();
                    System.out.println(ConsoleColors.ANSI_YELLOW+ "Inizio caricamento file.."+ConsoleColors.ANSI_RESET);
                    Ordine[] ordini = null;
                    while(true){
                        try{
                            ordini = OrdiniFileReader.readFile(nomeFile+".csv");
                            break;
                        }catch(Exception ex){
                            System.out.println(ConsoleColors.ANSI_RED+ "Errore caricamento file, probabilmente hai sbagliato nome file"+ConsoleColors.ANSI_RESET);
                            //FIX
                        }
                    }
                    System.out.println(ConsoleColors.ANSI_YELLOW+ "File caricato con successo."+ConsoleColors.ANSI_RESET);
                    System.out.println(ConsoleColors.ANSI_YELLOW + "-----------------------------------\n\n\n" + ConsoleColors.ANSI_RESET);
                    System.out.println(ConsoleColors.ANSI_GREEN+ "> H:\t\t"+ConsoleColors.ANSI_RED+foglio_H+ConsoleColors.ANSI_RESET);
                    System.out.println(ConsoleColors.ANSI_GREEN+ "> W:\t\t"+ConsoleColors.ANSI_RED+foglio_W+ConsoleColors.ANSI_RESET);
                    System.out.println(ConsoleColors.ANSI_GREEN+ "> alpha:\t"+ConsoleColors.ANSI_RED+alpha+ConsoleColors.ANSI_RESET);
                    System.out.println(ConsoleColors.ANSI_GREEN+ "> beta:\t\t"+ConsoleColors.ANSI_RED+beta+ConsoleColors.ANSI_RESET);
                    System.out.println(ConsoleColors.ANSI_YELLOW + "-----------------------------------\n\n\n" + ConsoleColors.ANSI_RESET);

                    int id = 1;
                    for (Ordine ordine : ordini) {
                        System.out.println(id + ") " + ordine);
                        id++;
                    }

                    double areaFoglio = foglio_H * foglio_W; //area del foglio


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
                        System.out.println("Soluzione alternativa " + i + ") " + individual);
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
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                continue;
            }

        }
    }

}
