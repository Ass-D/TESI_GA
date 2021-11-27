/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unina.daura.bps.utils;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import it.unina.daura.bps.Main;
import it.unina.daura.bps.Ordine;
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

    public static Ordine[] readFile(String file) throws IOException, CsvException {

        Ordine[] ordini = new Ordine[30];
        int linesToAvoid = 3;
        int linesAvoided = 0;
        int lineParsed = 0;
        try ( CSVReader reader = new CSVReader(new FileReader(file))) {
            List<String[]> allLines = reader.readAll();
            ordini = new Ordine[30];

            //allLines.forEach(x -> System.out.println(Arrays.toString(x)));
            for (String[] lines : allLines) {
                //[1;44;82;1, 00;10;0, 14;;;;;]

                for (String line : lines) {
                    if (linesAvoided == 1) {
                        String[] split = line.split(";");
                        Main.foglio_H = Double.parseDouble(split[0]);
                        Main.foglio_W = Double.parseDouble(split[1]);
                        Main.alpha    = Double.parseDouble(split[2]);
                        Main.beta     = Double.parseDouble(split[3]);
                    }
                    if (linesAvoided < linesToAvoid) {
                        linesAvoided++;
                        continue;
                    }
                    //System.out.println("LINE : " + line);
                    String[] split = line.split(";");
                    Ordine ordine = new Ordine(
                            Double.parseDouble(split[2]),
                            Double.parseDouble(split[1]),
                            Double.parseDouble(split[3]),
                            Double.parseDouble(split[4]),
                            Double.parseDouble(split[5])
                    );
                    ordini[lineParsed] = ordine;
                    lineParsed++;
                    if (lineParsed == 30) {
                        return ordini;
                    }
                }
//                System.out.println("----------------------------");

//                Ordine ordine = new Ordine(
                //Double.parseDouble(line[2], line[1], line[3], line[4], line[5]);
            }

        } catch (IOException ex) {
           throw ex;
        } catch (CsvException ex) {
           throw ex;
        }

        return ordini;

    }

}
