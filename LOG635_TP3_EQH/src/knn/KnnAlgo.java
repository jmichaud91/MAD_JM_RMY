package knn;

import java.util.List;
import java.util.Map;

/**
 * Created by Rachid, Mohamed Yassine on 2017-04-01.
 */
public class KnnAlgo {

    private Map<String,List<Double>> trainData;
    private Map<String,List<Double>> predictionData;
    private int k ;

    public KnnAlgo(Map<String, List<Double>> trainData) {
        this.trainData = trainData;
    }


    private double distance(){
        double ageRandom = 23.0;

        List<Double> colunm = this.trainData.get("Age");


        for(double val : colunm){

        }

        return 2.0;
    }

}
