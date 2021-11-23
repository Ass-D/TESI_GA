/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.cnr.istc.gen.duericariche.files;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import it.cnr.istc.gen.duericariche.Ordine;
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

    public static Ordine[] readFile(String file) {

        Ordine[] ordini = new Ordine[30];
        int lineParsed = 0;
        try ( CSVReader reader = new CSVReader(new FileReader(file))) {
            List<String[]> allLines = reader.readAll();
            ordini = new Ordine[30];

            //allLines.forEach(x -> System.out.println(Arrays.toString(x)));
            for (String[] lines : allLines) {
                //[1;44;82;1, 00;10;0, 14;;;;;]

                
                for (String line : lines) {
                    if (line.contains("N.ordine")) {
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
                    if(lineParsed == 30){
                        return ordini;
                    }
                }
//                System.out.println("----------------------------");

//                Ordine ordine = new Ordine(
                //Double.parseDouble(line[2], line[1], line[3], line[4], line[5]);
            }

        } catch (IOException ex) {
            Logger.getLogger(FileReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CsvException ex) {
            Logger.getLogger(FileReader.class.getName()).log(Level.SEVERE, null, ex);
        }

        return ordini;

    }

}
