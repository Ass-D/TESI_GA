/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unina.tesi.bps;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import static java.util.stream.Collectors.toList;

/**
 *
 * @author sommovir
 */
public class FogliManager {
    
    private static FogliManager _instance = null;
//    private Map<Double,List<Ordine>> ordiniPerSpessoreMap = new HashMap<>();
    private Map<Double, FoglioType> fogliPerSpessoreMap = new HashMap<>();
    
    public static FogliManager getInstance() {
        if (_instance == null) {
            _instance = new FogliManager();
            return _instance;
        } else {
            return _instance;
        }
    }
    
    private FogliManager() {
        super();
    }
    
    public void clear(){
        fogliPerSpessoreMap.clear();
    }
    
    public List<FoglioType> getAllFogli(){
        return this.fogliPerSpessoreMap.values().stream().collect(toList());
    }
    
    public FoglioType getFoglioBySpessore(double spessore){
        return this.fogliPerSpessoreMap.get(spessore);
    }
    
    public List<Ordine> getOrdiniBySpessore(double spessore){
        return this.fogliPerSpessoreMap.get(spessore).getOrdini();
    }
    
    public void mapFoglio(FoglioType foglio){
        System.out.println("FOGLIO is null ? "+foglio == null);
        System.out.println("SPESSORE: "+foglio.getSpessore());
        this.fogliPerSpessoreMap.put(foglio.getSpessore(), foglio);
    }
    
    public void putOrdineInMap(Ordine ordine, FoglioType foglio){
        if(!this.fogliPerSpessoreMap.containsKey(foglio.getSpessore())){
            this.fogliPerSpessoreMap.put(foglio.getSpessore(),foglio);
        }
        this.fogliPerSpessoreMap.get(foglio.getSpessore()).addOrdine(ordine);
    }
    
}
