/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unina.tesi.bps;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author sommovir
 */
public class TesterClass {

    public static void main(String[] args) {

//        System.out.println("Tester Main");
//        for (int i = 0; i < 30; i++) {
//            int x = ((int) ((Math.random() * 100) % 3)); //vettore[i] = ((int) ((Math.random() * 100) % 4) + 1);
//            System.out.println("x = "+x);
//        }
        int [] vettore = new int[]{4,4,4,1,4,2,1,3,3,4,2,1,3}; //4 1 2 3
        List<Integer> sequenzaFogli = new LinkedList<>();
        for (int foglio : vettore) {
            if (sequenzaFogli.isEmpty()) {
                sequenzaFogli.add(foglio);
            } else {
                if (!sequenzaFogli.contains(foglio)) {
                    sequenzaFogli.add(foglio);
    }
            }
        }
        for (Integer integer : sequenzaFogli) {
            System.out.println("FOGLIO: "+integer);
        }
    }

}
