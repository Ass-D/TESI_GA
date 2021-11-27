/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unina.tesi.bps.utils;

import it.unina.tesi.bps.exceptions.InvalidAttemptToExportException;
import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 *
 * @author sommovir
 */
public class Exporter {

    private static Exporter _instance = null;
    public static final String EXPORT_FOLDER = "./exports";
    private String currentLogPath;
    private long startingLoggingTime = -1;

    public static Exporter getInstance() {
        if (_instance == null) {
            _instance = new Exporter();
            return _instance;
        } else {
            return _instance;
        }
    }

    private Exporter() {
        super();
    }

    public void init(String fileName) throws IOException, InvalidAttemptToExportException {
        this.startingLoggingTime = new Date().getTime();
        this.currentLogPath = EXPORT_FOLDER + "/" + fileName + "-" + startingLoggingTime + ".log";
        File storedFile = new File(currentLogPath);
        storedFile.getParentFile().mkdirs();
        System.out.println("path: " + storedFile.getAbsolutePath());
        storedFile.createNewFile(); // if file already exists will do nothing
        FileOutputStream currentLoggingFile = new FileOutputStream(storedFile, false);
        String timestamp = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        export("TIMESTAMP: " + timestamp);
    }

    public void openFile() {
        File fileLog = new File(this.currentLogPath);

        Desktop desktop = Desktop.getDesktop();

        //first check if Desktop is supported by Platform or not
        if (!Desktop.isDesktopSupported()) {
            System.out.println("Desktop is not supported");
            return;
        }

        if (fileLog.exists()) {
            try {
                desktop.open(fileLog);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void stopExporting() throws InvalidAttemptToExportException {
        long elapsedTime = new Date().getTime() - this.startingLoggingTime;
        Duration elaps = Duration.of(elapsedTime, ChronoUnit.MILLIS);
        long seconds = elaps.get(ChronoUnit.SECONDS);
        long h = elaps.toHoursPart();
        long m = elaps.toMinutesPart();
        long s = elaps.toSecondsPart();
        export("----------------------------------------------------------------");
        export("Elapsed Time: " + " " + h + "h "
                + m + "m "
                + s + "s ");
        export("----------------------------------------------------------------");
        currentLogPath = null;
        this.startingLoggingTime = -1;
    }

    public void export(String textToExport) throws InvalidAttemptToExportException {

        String timestamp = new SimpleDateFormat("HH:mm:ss").format(new Date());

        if (currentLogPath == null) {
            throw new InvalidAttemptToExportException();
        }
        try ( FileWriter fw = new FileWriter(currentLogPath, StandardCharsets.UTF_8, true);  BufferedWriter bw = new BufferedWriter(fw);  PrintWriter out = new PrintWriter(bw)) {

            out.println(textToExport);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
