/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unina.tesi.bps.utils;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import it.unina.tesi.bps.FogliManager;
import it.unina.tesi.bps.Foglio;
import it.unina.tesi.bps.Main;
import it.unina.tesi.bps.Ordine;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.FileReader;

/**
 *
 * @author sommovir
 */
public class OrdiniFileReader {

    public static void readFile(String file) throws IOException, CsvException {

        int linesToAvoid = 7;
        int linesAvoided = 0;
        int lineParsed = 0;
        try ( CSVReader reader = new CSVReader(new FileReader(file))) {
            List<String[]> allLines = reader.readAll();

            //allLines.forEach(x -> System.out.println(Arrays.toString(x)));
            for (String[] lines : allLines) {
                //[1;44;82;1, 00;10;0, 14;;;;;]
//                System.out.println("LINE: "+linesAvoided);
                for (String line : lines) {
//                    System.out.println("LINEA: "+line);
                    if (line.contains("N.ordine")) {
                        linesAvoided++;
                        continue;
                    }
                    if (linesAvoided > 0 && linesAvoided < linesToAvoid) {
                        String[] split = line.split(";");
                        double foglioH = Double.parseDouble(split[1]);
                        double foglioW = Double.parseDouble(split[2]);
                        double alpha    = Double.parseDouble(split[3]);
                        double beta     = Double.parseDouble(split[4]);
                        double spessore     = Double.parseDouble(split[0]);
                        Foglio foglio = new Foglio(foglioH, foglioW, alpha, beta, spessore);
                        FogliManager.getInstance().mapFoglio(foglio);
                        linesAvoided++;
                        continue;
                    }
                    if (linesAvoided < linesToAvoid) {
                        linesAvoided++;
                        continue;
                    }
                    
                    //System.out.println("LINE : " + line);
                    String[] split = line.split(";");
                    try{
                    Ordine ordine = new Ordine(
                            Double.parseDouble(split[2]),
                            Double.parseDouble(split[1]),
                            Double.parseDouble(split[3]),
                            Double.parseDouble(split[4]),
                            Double.parseDouble(split[5])
                    );
                    double spessore = Double.parseDouble(split[6]);
//                    System.out.println("spessore: "+spessore);
                    FogliManager.getInstance().putOrdineInMap(ordine, FogliManager.getInstance().getFoglioBySpessore(spessore));
                    }catch(NumberFormatException ex){
                        ex.printStackTrace();
                        System.out.println("LINEA BUGGATISSIMA = "+line);
                    }
                }
            }

        } catch (IOException ex) {
            ex.printStackTrace();
           throw ex;
        } catch (CsvException ex) {
            ex.printStackTrace();
           throw ex;
        } catch (Exception ex){
            ex.printStackTrace();
        }

    }

}
