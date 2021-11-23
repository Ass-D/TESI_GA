/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.cnr.istc.gen.duericariche;

/**
 *
 * @author sommovir
 */
public class TesterClass {

    public static void main(String[] args) {

        System.out.println("Tester Main");
        for (int i = 0; i < 30; i++) {
            int x = ((int) ((Math.random() * 100) % 3)); //vettore[i] = ((int) ((Math.random() * 100) % 4) + 1);
            System.out.println("x = "+x);
        }
    }

}
