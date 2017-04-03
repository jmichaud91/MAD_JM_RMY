package Pretraitement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
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
        this.data = this.adaptationValeurManquante(data);
        this.data = this.eleminationElementAnormaux(data);
        return this.data;
    }

    private Map<String,List<Double>> eleminationElementAnormaux(Map<String,List<Double>> crudData){
    	
    	
    	
        for(String key : crudData.keySet()){
        	
        	
            List<Double> column = new ArrayList<Double>(crudData.get(key));
            List<Integer> indexesToRemove = new ArrayList<Integer>();


            for (int i=0; i<column.size();i++){
                if(column.get(i) < 0){
                    indexesToRemove.add(i);
                }
            }

            Collections.sort(indexesToRemove, Collections.reverseOrder());
            for (int i : indexesToRemove){
                ManipulationMap.removeByIndex(crudData,i);
            }
        }
        
        if(!crudData.containsKey("LeagueIndex"))
        {
        	Map<String,List<Double>> dataWIthEmptyValues = new LinkedHashMap<String,List<Double>>();
        	int i = 0;
        	for (Map.Entry<String, List<Double>> entry : crudData.entrySet())
        	{
        		if (i == 1)
        		{
        			List<Double> emptyValues = new ArrayList<>();
        			for (int j = 0; j < entry.getValue().size(); j++)
        			{
        				emptyValues.add(0d);
        			}
        			dataWIthEmptyValues.put("LeagueIndex", emptyValues);
        			dataWIthEmptyValues.put(entry.getKey(), entry.getValue());
        		}
        		else
        		dataWIthEmptyValues.put(entry.getKey(), entry.getValue());
        		
        		i++;
        	}
        	return dataWIthEmptyValues;
        }

        return crudData;
    }

    private Map<String,List<Double>> adaptationValeurManquante(Map<String,List<Double>> crudData){


        for(String key : crudData.keySet()){

            List<Double> column = new ArrayList<Double>(crudData.get(key));
            List<Integer> indexesToRemove = new ArrayList<Integer>();


            for (int i=0; i<column.size();i++){
                if(column.get(i) == null){
                    indexesToRemove.add(i);
                }
            }

            Collections.sort(indexesToRemove, Collections.reverseOrder());
            for (int i : indexesToRemove){
                for (String cle : crudData.keySet()){
                    if(crudData.get(cle).get(i)==null){
                        crudData.get(cle).set(i,0.0);
                    }
                }
//                ManipulationMap.removeByIndex(crudData,i);
            }
        }
        return crudData;
    }






}
