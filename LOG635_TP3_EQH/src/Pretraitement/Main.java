package Pretraitement;

import knn.KnnAlgo;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import arbreDecision.DatasetContainer;
import arbreDecision.TreeBuilder;
import arbreDecision.TreeRoot;
import form.mainForm;

/**
 * Created by Rachid, Mohamed Yassine on 2017-03-31.
 */
public class Main {
    private static AutoResetEvent algo1IsDoneEvent = new AutoResetEvent(false);
    private static AutoResetEvent algo2IsDoneEvent = new AutoResetEvent(false);
    private static AutoResetEvent algo3IsDoneEvent = new AutoResetEvent(false);

    public static void main(String[] args) throws InterruptedException, IOException {
        System.out.println("****************** Main de Mohamed **************************");
        
        mainForm mainform = new mainForm();

        String filePathTrain = "Dataset.csv";
        String filePathPrediction = "DataPrediction.csv";

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


        //***************************************** Les 2 map avec validation croisé ***************************************/

//        (NbElementPredition /NbElement) = RATIO
        final int RATIO = 4;

        Map<String, List<Double>> MapCroiseTrain = ManipulationMap.generateCroiseMapTraining(mapFiltreTrain,RATIO);

        Map<String, List<Double>> MapCroisePrediction = ManipulationMap.generateCroiseMapPrediction(mapFiltreTrain,RATIO);
        
      //  for (Map.Entry<String, List<Double>> entry : MapCroisePrediction.entrySet())
       // {
        //	MapCroiseTrain.get(entry.getKey()).addAll(entry.getValue());
        //}


        //***************************************** Algos ****************************************************************/

        //Clone du Map pour l<envoyer dans le L<algo knn (Mohamed)
        Map<String, List<Double>> mapTrainKnn = new LinkedHashMap<String, List<Double>>(MapCroiseTrain);
        Map<String, List<Double>> mapPredictionKnn = new LinkedHashMap<String, List<Double>>(MapCroisePrediction);

        KnnAlgo knnWithkEqual15 = new KnnAlgo(mapTrainKnn, mapPredictionKnn, 50);
        KnnAlgo knnWithkEqual5 = new KnnAlgo(mapTrainKnn, mapPredictionKnn, 20);

        //Clone du Map pour l<envoyer dans le L<algo arbre de decision (MAD)

        Map<String, List<Double>> mapPourLArbre = new LinkedHashMap<String, List<Double>>(MapCroiseTrain);
        DatasetContainer container = new DatasetContainer(mapPourLArbre);
        TreeBuilder builder = new TreeBuilder();
        builder.setPrunePercent(80);
      //  TreeRoot tree = builder.buildTree(container, 80);

        //***** To test the classification of the tree ***
      //  List<Map<String,Double>> instancesToTest =  ManipulationMap.getLinesMap(MapCroisePrediction);

       /* int countCorrectInstances = 0;
        int countIncorrectInstances = 0;
        for (Map<String,Double> lineToClassify : instancesToTest)
        {
            String prediction = tree.classifyInstance(lineToClassify);
            if (Double.parseDouble(prediction) == lineToClassify.get("LeagueIndex"))
            {
                countCorrectInstances++;
            }
            else
            {
                countIncorrectInstances++;
            }

        }
        System.out.println("classification: " + (countCorrectInstances/(double) (countCorrectInstances + countIncorrectInstances))*100);
        */

         StartParalleleExecution(knnWithkEqual5, knnWithkEqual15, builder, container, MapCroisePrediction);

    }

    //Execution en paralléle des 2 algos Knn
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


    //Execution en paralléle des 3 algos knn+arbre
    private static void StartParalleleExecution(KnnAlgo algo1, KnnAlgo algo2, TreeBuilder algo3, DatasetContainer container, Map<String, List<Double>> mapPrediction) throws InterruptedException {
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

        Thread treeWith80PercentPrune = new Thread(new Runnable() {
            @Override
            public void run() {
              TreeRoot tree = algo3.buildTree(container);
              tree.classifyInstances(mapPrediction);
                algo3IsDoneEvent.set();
            }
        });


        knnWith15.start();
        knnWith5.start();
        treeWith80PercentPrune.start();

        algo1IsDoneEvent.waitOne();
        algo2IsDoneEvent.waitOne();
        algo3IsDoneEvent.waitOne();

        System.out.println("Niveau de confiance pour k=" + algo1.getK() + " : " + algo1.getNiveauConfianceGlobal() + " %");
        System.out.println("Niveau de confiance pour k=" + algo2.getK() + " : " + algo2.getNiveauConfianceGlobal() + " %");
    }


}
