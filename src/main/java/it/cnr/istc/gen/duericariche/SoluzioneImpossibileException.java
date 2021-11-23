/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.cnr.istc.gen.duericariche;

/**
 *
 * @author sommovir
 */
public class SoluzioneImpossibileException extends Exception {

    public SoluzioneImpossibileException(String detail) {
        super("Soluzione inammissibile: "+detail);
    }
    
    
    
}
