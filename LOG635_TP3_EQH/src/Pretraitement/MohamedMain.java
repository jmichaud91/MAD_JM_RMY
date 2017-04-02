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

        String filePathTrain ="Dataset.csv";
        String filePathPrediction = "DataPrediction.csv";

        LecteurExcel lecteurExcelTrain = new LecteurExcel(filePathTrain);
        LecteurExcel lecteurExcelPrediction = new LecteurExcel(filePathPrediction);

        // Map du excel brut sans filtre
        Map<String,List<Double>> mapDuExcelBruteTrain;
        mapDuExcelBruteTrain = lecteurExcelTrain.getHashMap();

        Map<String,List<Double>> mapDuExcelBrutePrediction;
        mapDuExcelBrutePrediction = lecteurExcelPrediction.getHashMap();

        //Pretraitement des donnes avec le filtre . (Celui-ci enleve les donnees anormal comme par exemple les null)
        Filtre filtreTrain = new Filtre(mapDuExcelBruteTrain);
        Map<String,List<Double>> mapFiltreTrain = filtreTrain.getDonnees();

        Filtre filtrePrediction = new Filtre(mapDuExcelBrutePrediction);
        Map<String,List<Double>> mapFiltrePrediction = filtrePrediction.getDonnees();


        //***************************************** Algos ***************************************/

        //Clone du Map pour l<envoyer dans le L<algo knn (Mohamed)
        Map<String,List<Double>> mapTrainKnn = new LinkedHashMap<String,List<Double>>(mapFiltreTrain);
        Map<String,List<Double>> mapPredictionKnn = new LinkedHashMap<String,List<Double>>(mapFiltrePrediction);

        KnnAlgo knn = new KnnAlgo(mapTrainKnn,mapPredictionKnn,25);
        knn.execute();

        //Clone du Map pour l<envoyer dans le L<algo arbre de decision (MAD)
        Map<String,List<Double>> mapPourLArbre = new HashMap<String,List<Double>>(mapFiltreTrain);

    }
}
