package Pretraitement;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Created by Rachid, Mohamed Yassine on 2017-03-31.
 */
public class LecteurExcel {

    private Scanner fichierExcel;

    public LecteurExcel(String path) {
        File file = new File(path);

        try {
            this.fichierExcel = new Scanner(file);
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }

    public Map<String,List<Double>> getHashMap(){

       String[] keys = fichierExcel.next().split(",");

       for(int i=0; i<keys.length;i++){
           keys[i]= keys[i].replace("\"","");
       }

        List<Double>[] values = new List[keys.length];

        for( int i=0; i< values.length;i++ ){
            values[i] = new ArrayList<Double>();
        }



        while (fichierExcel.hasNext()) {
            String ligne = fichierExcel.next();
            String[] tabLigne = ligne.split(",");


            for(int i =0 ; i < tabLigne.length;i++) {

                if (tabLigne[i].equals("NULL")  ) {
                    values[i].add(null);
                }else if (tabLigne[i].contains("e-")){
                    values[i].add(0.0);
                } else {
                    values[i].add(Double.parseDouble(tabLigne[i]));
                }

            }
        }

       Map<String,List<Double>> map = new LinkedHashMap<String,List<Double>>();

        for(int i=0; i< keys.length; i++){
            map.put(keys[i],values[i]);
        }



        return map;
    }


}
