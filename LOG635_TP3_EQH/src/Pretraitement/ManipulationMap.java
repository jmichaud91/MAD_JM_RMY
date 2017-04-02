package Pretraitement;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Rachid, Mohamed Yassine on 2017-04-01.
 */

//Classe qui contient 2 methodes utiles , une qui permet de supprimer un element par son index
// et un permet d<afficher columns
public class ManipulationMap {


    public static void removeByIndex(Map<String,List<Double>> map, int index){

        for(String key: map.keySet()){
            List<Double> toModify = map.get(key);
            toModify.remove(index);
            map.put(key,toModify);
        }

    }

    public static void printColumn(Map<String,List<Double>> map, String key){
        List<Double> column = map.get(key);
        System.out.println(key);
        for (Double value: column){
            System.out.println(value);
        }
    }
    
    
    public static void generatePredictionMap(Map<String,List<Double>> trainMap){
        
        Map<String,List<Double>> tMap = new LinkedHashMap<>(trainMap);

        List<Double>[] valuesPred = new List[tMap.keySet().size()];

        for( int i=0; i< valuesPred.length;i++ ){
            valuesPred[i] = new ArrayList<Double>();
        }

        for(int i = 0; i <30 ; i++){
            valuesPred[1] = tMap.get("GameID");
        }

    }

    public static double[][] generateMatrice(Map<String,List<Double>> trainMap){
        Map<String,List<Double>> tMap = new LinkedHashMap<String,List<Double>>(trainMap);

        int nbElement = tMap.get("TotalHours").size();

        double[][] matrice = new double[tMap.keySet().size()][nbElement];

        int o = 0;
        for (String key : tMap.keySet()) {
            Double[] array = tMap.get(key).toArray(new Double[nbElement]);
            for (int i = 0; i < array.length; i++) {
                matrice[o][i] = array[i];
            }
            o++;
        }
        o=0;

        return matrice;
    }

}
