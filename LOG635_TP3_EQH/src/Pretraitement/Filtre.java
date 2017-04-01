package Pretraitement;

import java.util.List;
import java.util.Map;

/**
 * Created by Yassine on 2017-03-27.
 */
public class Filtre{

   private Map<String,List<Double>> data ;

    public Filtre(Map<String,List<Double>> data) {
        this.data = data;
    }

    public Map<String,List<Double>> getDonnees(){
        this.data = this.eleminationElementAnormaux(data);
        this.data = this.AdaptationValeurManquante(data);
        return this.data;
    }

    private Map<String,List<Double>> eleminationElementAnormaux(Map<String,List<Double>> crudData){
        //        for(String key : crudData.keySet()){
//            List<Double> column = crudData.get(key);
//            int i = 0;
//            for (Double val : column){
//                if(val == null){
//                    ManipulationMap.removeByIndex(crudData,i);
//                }
//
//            }
//        }

        return crudData;
    }

    private Map<String,List<Double>> AdaptationValeurManquante( Map<String,List<Double>> crudData){

//        for(String key : crudData.keySet()){
//            List<Double> column = crudData.get(key);
//            int i = 0;
//            for (Double val : column){
//                if(val == null){
//                    ManipulationMap.removeByIndex(crudData,i);
//                }
//
//            }
//        }
//        List<Double> column = crudData.get("Age");
//        int i = 0;
//        for (Double val : column){
//            if(val == null){
//                ManipulationMap.removeByIndex(crudData,i);
//            }
//            i++;
//        }


        return crudData;
    }






}
