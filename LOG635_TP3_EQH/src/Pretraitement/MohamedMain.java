package Pretraitement;

import java.util.HashMap;
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

        Map<String,List<Double>> mapDuExcelBrute;
        // Map du excel brut sans filtre
        mapDuExcelBrute = lecteurExcel.getHashMap();

        Filtre filtre = new Filtre(mapDuExcelBrute);

        //Donne filtrer ...( C<est ce que Mohamed va achever demain matin)
        Map<String,List<Double>> mapFiltre = filtre.getDonnees();

        //On peut supprimmer l<element a l<index 8 de la Map
        ManipulationMap.removeByIndex(mapDuExcelBrute,8);
        // On peut afficher une colone
        ManipulationMap.printColumn(mapDuExcelBrute,"Age");
        int o = 9;
    }
}
