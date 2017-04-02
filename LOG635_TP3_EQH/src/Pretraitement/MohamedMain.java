package Pretraitement;

import knn.KnnAlgo;
import mainPackage.DatasetContainer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import arbreDecision.TreeBuilder;
import arbreDecision.TreeRoot;

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
        String filepathTest = "ForTest_ArbreDecisionAttrNum.txt";
        String filePathTestClassification = "testClassificationTree.txt";

        LecteurExcel lecteurExcelTrain = new LecteurExcel(filePathTrain);
        LecteurExcel lecteurExcelPrediction = new LecteurExcel(filePathPrediction);


        // Map du excel brut sans filtre
        Map<String, List<Double>> mapDuExcelBruteTrain;
        mapDuExcelBruteTrain = lecteurExcelTrain.getHashMap();
        //mapDuExcelBrute = lecteurExcel.getHashMap();

        Map<String, List<Double>> mapDuExcelBrutePrediction;
        mapDuExcelBrutePrediction = lecteurExcelPrediction.getHashMap();

        //Pretraitement des donnes avec le filtre . (Celui-ci enleve les donnees anormal comme par exemple les null)
        Filtre filtreTrain = new Filtre(mapDuExcelBruteTrain);
        Map<String, List<Double>> mapFiltreTrain = filtreTrain.getDonnees();

        Filtre filtrePrediction = new Filtre(mapDuExcelBrutePrediction);
        Map<String, List<Double>> mapFiltrePrediction = filtrePrediction.getDonnees();


        //***************************************** Algos ***************************************/

        //Clone du Map pour l<envoyer dans le L<algo knn (Mohamed)
        Map<String, List<Double>> mapTrainKnn = new LinkedHashMap<String, List<Double>>(mapFiltreTrain);
        Map<String, List<Double>> mapPredictionKnn = new LinkedHashMap<String, List<Double>>(mapFiltrePrediction);

        KnnAlgo knnWithkEqual15 = new KnnAlgo(mapTrainKnn, mapPredictionKnn, 15);
        KnnAlgo knnWithkEqual5 = new KnnAlgo(mapTrainKnn, mapPredictionKnn, 5);




        //Clone du Map pour l<envoyer dans le L<algo arbre de decision (MAD)
        Map<String, List<Double>> mapPourLArbre = new LinkedHashMap<String, List<Double>>(mapFiltreTrain);
        DatasetContainer container = new DatasetContainer(mapPourLArbre);
        TreeBuilder builder = new TreeBuilder();
        TreeRoot tree = builder.buildTree(container, 80);

        //***** To test the classification of the tree ***
     /* LecteurExcel lecteurExcelClassification = new LecteurExcel(filePathTestClassification);
        List<Map<String,Double>> instancesToTest = lecteurExcelClassification.getLinesMap();
        for (Map<String,Double> lineToClassify : instancesToTest)
        {
        	System.out.println("classification: " + tree.classifyInstance(lineToClassify));
        }*/

        StartParalleleExecution(knnWithkEqual5, knnWithkEqual15);

    }

    //Execution en paralléle des algos
    private static void StartParalleleExecution(KnnAlgo algo1, KnnAlgo algo2) throws InterruptedException {
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

        System.out.println("Niveau de confiance pour k=" + algo1.getK() + " : " + algo1.getNiveauConfianceGlobal() + " %");
        System.out.println("Niveau de confiance pour k=" + algo2.getK() + " : " + algo2.getNiveauConfianceGlobal() + " %");
    }


}
