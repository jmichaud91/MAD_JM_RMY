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
        this.data = this.eleminationElementAnormaux(data);
        this.data = this.reductionAttributs(data);
        return this.data;
    }

    private Map<String,List<Double>> eleminationElementAnormaux(Map<String,List<Double>> crudData){
        List<Double> ages = crudData.get("Age");

        for(double age :ages){
            System.out.println(age);
        }

        return crudData;
    }

    private Map<String,List<Double>> AdaptationValeurManquante( Map<String,List<Double>> crudData){

        return crudData;
    }

    private Map<String,List<Double>> reductionAttributs( Map<String,List<Double>> crudData){

        return crudData;
    }

}
