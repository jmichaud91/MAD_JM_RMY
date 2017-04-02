package knn;

import Pretraitement.ManipulationMap;

import java.util.List;
import java.util.Map;

/**
 * Created by Rachid, Mohamed Yassine on 2017-04-01.
 */
public class KnnAlgo {

    private double[][] trainData;
    private double[][] predictionData;
    private int k ;

    public KnnAlgo(Map<String, List<Double>> trainData, Map<String, List<Double>> predictionData, int k) {
        this.trainData= ManipulationMap.generateMatrice(trainData);
        this.predictionData = ManipulationMap.generateMatrice(predictionData);
        this.k = k;
    }

    public void execute(){
        System.out.println("Execution du knn !");
    }
}
