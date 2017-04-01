package Pretraitement;

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

}
