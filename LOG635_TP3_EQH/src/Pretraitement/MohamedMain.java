package Pretraitement;

import knn.KnnAlgo;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Rachid, Mohamed Yassine on 2017-03-31.
 */
public class MohamedMain {


    public static void main(String[] args) {
        System.out.println("****************** Main de Mohamed **************************");

        String filepath ="Dataset.csv";

        LecteurExcel lecteurExcel = new LecteurExcel(filepath);


        // Map du excel brut sans filtre
        Map<String,List<Double>> mapDuExcelBrute;
        mapDuExcelBrute = lecteurExcel.getHashMap();


        //Pretraitement des donnes avec le filtre . (Celui-ci enleve les donnees anormal comme par exemple les null)
        Filtre filtre = new Filtre(mapDuExcelBrute);
        Map<String,List<Double>> mapFiltre = filtre.getDonnees();


        //***************************************** knn ***************************************/

        //Clone du Map pour l<envoyer dans le L<algo knn (Mohamed)
        Map<String,List<Double>> mapPourKnn = new LinkedHashMap<String,List<Double>>(mapFiltre);


        //Clone du Map pour l<envoyer dans le L<algo arbre de decision (MAD)
        Map<String,List<Double>> mapPourLArbre = new LinkedHashMap<String,List<Double>>(mapFiltre);



    }
}
