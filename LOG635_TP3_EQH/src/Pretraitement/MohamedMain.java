package Pretraitement;

import java.io.Console;
import knn.KnnAlgo;

import java.util.*;

/**
 * Created by Rachid, Mohamed Yassine on 2017-03-31.
 */
public class MohamedMain {
    private static AutoResetEvent algo1IsDoneEvent = new AutoResetEvent(false);
    private static AutoResetEvent algo2IsDoneEvent = new AutoResetEvent(false);

    public static void main(String[] args) throws InterruptedException {
        System.out.println("****************** Main de Mohamed **************************");


        String filePathTrain = "Dataset.csv";
        String filePathPrediction = "DataPrediction.csv";

        LecteurExcel lecteurExcelTrain = new LecteurExcel(filePathTrain);
        LecteurExcel lecteurExcelPrediction = new LecteurExcel(filePathPrediction);
        if (lecteurExcelTrain.GetFichierExcel() != null && lecteurExcelPrediction.GetFichierExcel() != null) {
            
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

        KnnAlgo knnWithkEqual15 = new KnnAlgo(mapTrainKnn,mapPredictionKnn,15);
        KnnAlgo knnWithkEqual5 = new KnnAlgo(mapTrainKnn,mapPredictionKnn,5);
        
        StartExecution(knnWithkEqual5, knnWithkEqual15);
        

        //Clone du Map pour l<envoyer dans le L<algo arbre de decision (MAD)
        Map<String,List<Double>> mapPourLArbre = new HashMap<String,List<Double>>(mapFiltreTrain);

        }
        else
        {
            System.out.println("Could not read files properly.  Execution stopped.");
        }
    }
    
    private static void StartExecution(KnnAlgo algo1, KnnAlgo algo2) throws InterruptedException
    {
        Thread knnWith15 = new Thread(new Runnable() {
            @Override
            public void run() {
                algo1.execute();
                algo1IsDoneEvent.set();
            }
        });
        
        Thread knnWith5 = new Thread(new Runnable() {
            @Override
            public void run() {
                algo2.execute();
                algo2IsDoneEvent.set();
            }
        });
        
        knnWith15.start();
        knnWith5.start();
        
        algo1IsDoneEvent.waitOne();
        algo2IsDoneEvent.waitOne();
        
        System.out.println("Knn with k=15 voted for : " + algo1.getPrediction(0));
        System.out.println("Knn with k=5 voted for : " + algo1.getPrediction(0));
    }
}
