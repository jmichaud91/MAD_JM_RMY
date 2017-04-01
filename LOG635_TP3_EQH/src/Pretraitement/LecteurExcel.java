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

       List<Double> colum = new ArrayList<Double>();

       String[] keys = fichierExcel.next().split(",");



        while (fichierExcel.hasNext()) {
            String ligne = fichierExcel.next();
            String[] tabLigne = ligne.split(",");

            if(tabLigne[2].equals("NULL")){
                colum.add(null);
            }else {
                colum.add(Double.parseDouble(tabLigne[2]));
            }

        }

       Map<String,List<Double>> map = new HashMap<String,List<Double>>();
       map.put("Age",colum);


        return null;
    }


}
