package it.unina.tesi.bps;

import it.unina.tesi.bps.exceptions.InvalidAttemptToExportException;
import it.unina.tesi.bps.exceptions.SoluzioneImpossibileException;
import it.unina.tesi.bps.utils.ConsoleColors;
import it.unina.tesi.bps.utils.Exporter;
import it.unina.tesi.bps.utils.OrdiniFileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.fusesource.jansi.AnsiConsole;

public class Main {

    public static int maxGenerations = 1000; // massimo num. di iterazioni per la terminazione
    public static final String version = "1.0";
    public static boolean verbose = true;
    public static boolean debug = false;
//    public static double foglio_W = 203; //larghezza del foglio
//    public static double alpha = 451.6; //costo spreco
//    public static double beta = 82.5; //costo penalità
//    public static double foglio_H = 127; //altezza del foglio

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
                    System.out.println(ConsoleColors.ANSI_RED + "Quitting.." + ConsoleColors.ANSI_RESET);
                    System.exit(0);
                } else if (command.equals("export")) {
                    Exporter.getInstance().openFile();

                } else if (command.equals("verbose on")) {
                    verbose = true;
                    System.out.println(ConsoleColors.ANSI_YELLOW + "verbose [ON] " + ConsoleColors.ANSI_RESET);

                } else if (command.equals("verbose off")) {
                    verbose = false;
                    System.out.println(ConsoleColors.ANSI_YELLOW + "verbose [OFF] " + ConsoleColors.ANSI_RESET);

                } else if (command.equals("debug on")) {
                    debug = true;
                    System.out.println(ConsoleColors.ANSI_YELLOW + "debug [ON] " + ConsoleColors.ANSI_RESET);

                } else if (command.equals("debug off")) {
                    debug = false;
                    System.out.println(ConsoleColors.ANSI_YELLOW + "debug [OFF] " + ConsoleColors.ANSI_RESET);

                }else if (command.equals("start")) {
                    System.out.println(ConsoleColors.ANSI_YELLOW + "Inserisci il nome del file contenente i dati (senza estensione)" + ConsoleColors.ANSI_GREEN);
                    String nomeFile = reader.readLine();
                    System.out.println(ConsoleColors.ANSI_YELLOW + "Inizio caricamento file.." + ConsoleColors.ANSI_RESET);
                    while (true) {
                        try {
                            OrdiniFileReader.readFile(nomeFile + ".csv");
                            Exporter.getInstance().init(nomeFile);
                            Exporter.getInstance().export("********************************************************");
                            Exporter.getInstance().export("                    B  P  S");
                            Exporter.getInstance().export("********************************************************");
                            Exporter.getInstance().export("loading file..");
                            break;
                        } catch (Exception ex) {
                            System.out.println(ConsoleColors.ANSI_RED + "Errore caricamento file, probabilmente hai sbagliato nome file" + ConsoleColors.ANSI_RESET);
                            System.out.println(ConsoleColors.ANSI_YELLOW + "Inserisci il nome del file contenente i dati (senza estensione)" + ConsoleColors.ANSI_GREEN);
                            nomeFile = reader.readLine();
                        }
                    }
                    System.out.println(ConsoleColors.ANSI_YELLOW + "File caricato con successo." + ConsoleColors.ANSI_RESET);
                    try {
                        Exporter.getInstance().export("File caricato con successo.");

                        List<FoglioType> fogli = FogliManager.getInstance().getAllFogli();
                        Exporter.getInstance().export("------------ RIEPILOGO SPESSORI FOGLI -------------");
                        for (FoglioType foglio : fogli) {
                            Exporter.getInstance().export("Tipologia di foglio - " + foglio + " con " + foglio.getOrdini().size() + " ordini");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "-----------------------------------" + ConsoleColors.ANSI_RESET);
                            System.out.println(ConsoleColors.ANSI_GREEN + " Foglio di Spessore: " + ConsoleColors.PURPLE_BRIGHT + foglio.getSpessore() + ConsoleColors.ANSI_RESET);
                            System.out.println(ConsoleColors.ANSI_GREEN + "      numero Ordini: " + ConsoleColors.PURPLE_BRIGHT + foglio.getOrdini().size() + ConsoleColors.ANSI_RESET);
                            System.out.println(ConsoleColors.ANSI_YELLOW + "-----------------------------------" + ConsoleColors.ANSI_RESET);
                            System.out.println(ConsoleColors.ANSI_GREEN + ">     H: " + ConsoleColors.ANSI_RED + foglio.getH() + ConsoleColors.ANSI_RESET);
                            System.out.println(ConsoleColors.ANSI_GREEN + ">     W: " + ConsoleColors.ANSI_RED + foglio.getW() + ConsoleColors.ANSI_RESET);
                            System.out.println(ConsoleColors.ANSI_GREEN + "> alpha: " + ConsoleColors.ANSI_RED + foglio.getAlpha() + ConsoleColors.ANSI_RESET);
                            System.out.println(ConsoleColors.ANSI_GREEN + ">  beta: " + ConsoleColors.ANSI_RED + foglio.getBeta() + ConsoleColors.ANSI_RESET);
                            System.out.println(ConsoleColors.ANSI_YELLOW + "-----------------------------------\n" + ConsoleColors.ANSI_RESET);
                            foglio.printOrdini();
                        }
                        Exporter.getInstance().export("-----------------------------------------");

                        System.out.println(ConsoleColors.ANSI_YELLOW + "************************************************************************" + ConsoleColors.ANSI_RESET);
                        System.out.println(ConsoleColors.ANSI_YELLOW + "************************************************************************" + ConsoleColors.ANSI_RESET);
                        System.out.println(ConsoleColors.ANSI_YELLOW + "************************************************************************" + ConsoleColors.ANSI_RESET);
                        System.out.println(ConsoleColors.ANSI_YELLOW + "************************************************************************" + ConsoleColors.ANSI_RESET);

                        for (FoglioType foglio : fogli) {
                            List<Ordine> ordini = foglio.getOrdini();
                            if (ordini.isEmpty()) {
                                System.out.println(ConsoleColors.ANSI_YELLOW + "----------------------------------------" + ConsoleColors.ANSI_RESET);
                                System.out.println(ConsoleColors.ANSI_CYAN + "Questo foglio (spessore = " + foglio.getSpessore() + " non contiene ordini. SKIP" + ConsoleColors.ANSI_RESET);
                                System.out.println(ConsoleColors.ANSI_YELLOW + "----------------------------------------\n" + ConsoleColors.ANSI_RESET);
                                continue;
                            }
                            System.out.println(ConsoleColors.ANSI_YELLOW + "----------------------------------------" + ConsoleColors.ANSI_RESET);
                            System.out.println(ConsoleColors.ANSI_RED + "Questo foglio (spessore = " + foglio.getSpessore() + " contiene " + foglio.getOrdini().size() + " ordini" + ConsoleColors.ANSI_RESET);
                            System.out.println(ConsoleColors.ANSI_YELLOW + "----------------------------------------\n" + ConsoleColors.ANSI_RESET);

                            double areaFoglio = foglio.getH() * foglio.getW(); //area del foglio

// Creiamo l'oggetto ga
                            GeneticAlgorithm ga = new GeneticAlgorithm(100, 0.01, 0.95, 0);

//Inizializziamo la popolazione specificando la lunghezza dei cromosomi
                            Population population = ga.initPopulation(ordini.size());

//Valutiamo la popolazione
                            ga.evalPopulation(population, areaFoglio, foglio.getAlpha(), foglio.getBeta(), ordini);

                            int generation = 1;

                            while (ga.isTerminationConditionMet(generation, maxGenerations) == false) {

//Stampiamo l'individuo più in forma 
                                Individual fittest = population.getFittest(0);
                                if (fittest == null) {
                                    continue;
                                }
                                System.out.println(
                                        "G" + generation + " Best solution (" + fittest.getFitness() + ")");

//Applichiamo il crossover
                                population = ga.crossoverPopulation(population);

//Applichiamo la mutazione
                                population = ga.mutatePopulation(population);

//Valutiamo la popolazione
                                ga.evalPopulation(population, areaFoglio, foglio.getAlpha(), foglio.getBeta(), ordini);
                                generation++;
                            }

                            System.out.println("Stopped after " + maxGenerations + " generations.");
                            {
                                Individual fittest = population.getFittest(0);
                                System.out.println("La fitness è: " + fittest.getFitness());
                            }

                            Individual fittest = population.getFittest(0);
                            System.out.println("La soluzione migliore è: " + fittest);
                            System.out.println("====================================================");

                            Exporter.getInstance().export("\n\n================ S O L U Z I O N E =================");
                            Exporter.getInstance().export("Del tipo di foglio - " + foglio + " con " + foglio.getOrdini().size() + " ordini");
                            Exporter.getInstance().export("La soluzione migliore è: " + fittest);
                            Exporter.getInstance().export("====================================================");
                            Exporter.getInstance().export("==================  O R D I N I  ===================");
                            List<Integer> sequenzaFogli = fittest.getSequenzaFogli();
                            for (Integer sequenza : sequenzaFogli) {
                                List<Ordine> ordiniByFoglio = fittest.getOrdiniByFoglio(foglio.getOrdini(), sequenza);
                                Exporter.getInstance().export("Foglio " + sequenza + ":");
                                for (Ordine ordine : ordiniByFoglio) {
                                    Exporter.getInstance().export("\tOrdine: " + ordine);
                                }
                                Exporter.getInstance().export("-------------------------------------------------------");
                            }

                            if (verbose) {
                                Exporter.getInstance().export("\n-------      SOLUZIONI ALTERNATIVE      --------------");
                            }
                            System.out.println("Le altre soluzioni: ");
                            int i = 1;
                            for (Individual individual : population.getIndividuals()) {
                                System.out.println(i + ") " + individual);
                                if (verbose) {
                                    Exporter.getInstance().export(i + ") " + individual);
                                    List<Integer> sequenzaAlternativa = individual.getSequenzaFogli();
                                    for (Integer sequenza : sequenzaAlternativa) {
                                        List<Ordine> ordiniByFoglio = individual.getOrdiniByFoglio(foglio.getOrdini(), sequenza);
                                        Exporter.getInstance().export("Foglio " + sequenza + ":");
                                        for (Ordine ordine : ordiniByFoglio) {
                                            Exporter.getInstance().export("\tOrdine: " + ordine);
                                        }
                                        Exporter.getInstance().export("-------------------------------------------------------");
                                    }

                                    i++;
                                    Exporter.getInstance().export("******************************************************\n");
                                }
                            }

                            Exporter.getInstance().export("\n====================================================");

                    
                            double minimoCosto = -2;
                            try {
                                minimoCosto = ga.minimoCosto(population, ordini.toArray(new Ordine[ordini.size()]), areaFoglio, foglio.getAlpha(), foglio.getBeta());
                            } catch (SoluzioneImpossibileException ex) {
                                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    } catch (InvalidAttemptToExportException ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    System.out.println(ConsoleColors.ANSI_YELLOW + "-----------------------------------" + ConsoleColors.ANSI_RESET);
                    System.out.println(ConsoleColors.ANSI_YELLOW + "                END\n" + ConsoleColors.ANSI_RESET);
                    System.out.println(ConsoleColors.ANSI_YELLOW + "-----------------------------------\n\n" + ConsoleColors.ANSI_RESET);
                    System.out.println(ConsoleColors.ANSI_YELLOW + "Inserisci comando (start per iniziare)." + ConsoleColors.ANSI_GREEN);
                    try {
                        Exporter.getInstance().stopExporting();
                    } catch (InvalidAttemptToExportException ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    }

                } else {
                    System.out.println(ConsoleColors.ANSI_RED + "Errore, comando sconosciuto." + ConsoleColors.ANSI_RESET);
                }
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                continue;
            }

        }
    }

}
