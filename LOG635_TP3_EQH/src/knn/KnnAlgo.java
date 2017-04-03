package knn;

import Pretraitement.ManipulationMap;

import java.util.*;

/**
 * Created by Rachid, Mohamed Yassine on 2017-04-01.
 */
public class KnnAlgo {

    private double[][] trainData;
    private double[][] predictionData;
    private List<Double> niveauConfiance;
    private Double[] nosEvaludation;
    
    private final int K;
    private final String ATTRIBUTS= "LeagueIndex,Age,HoursPerWeek,TotalHours,APM,ActionLatency,TotalMapExplored";
    

    public KnnAlgo(Map<String, List<Double>> trainData, Map<String, List<Double>> predictionData, int k) {
        this.selectAttributs(trainData);
        this.selectAttributs(predictionData);
        this.trainData= ManipulationMap.generateMatrice(trainData);
        this.predictionData = ManipulationMap.generateMatrice(predictionData);
        this.K = k;
        this.niveauConfiance = new ArrayList<Double>();
    }

    public double getPrediction(int indexPreduiction){

        List<Element> prochesVoisins = new ArrayList<Element>();

        for (int i=0; i < trainData[0].length ; i++) {
            if( prochesVoisins.size() < K  ){
                  prochesVoisins.add(new Element(obtenirDistance(i, indexPreduiction),trainData[0][i]));
            } else if(obtenirDistance(i, indexPreduiction)< prochesVoisins.get(prochesVoisins.size() - 1).getDistance()){
                  prochesVoisins.remove(prochesVoisins.size() - 1);
                  prochesVoisins.add(new Element(obtenirDistance(i, indexPreduiction),trainData[0][i]));
                  Collections.sort(prochesVoisins);
            }
        }

        return obtenirGagnantVote(prochesVoisins);

    }

    public void execute(){
        Double[] predictions = new Double[predictionData[0].length];

        for(int i=0; i<predictionData[0].length;i++) {
            predictions[i] = getPrediction(i); //23 => 1.0
        }

        double bonnePredictions = 0 ;
        for(int i=0; i<predictionData[0].length;i++){
            if(predictionData[0][i] == predictions[i]){
                bonnePredictions+=1;
            }
        }
        
        this.nosEvaludation = predictions;
        
        bonnePredictions = (bonnePredictions/predictionData[0].length)*100;



        System.out.println("Taux de precision avec Knn k="+this.K+" : "+bonnePredictions+"%.");
    }


    public double obtenirDistance(int indexTrain,int indexPrediction){

        double distance=0;

        for(int i=1; i<7;i++){
            distance +=(trainData[i][indexTrain] - predictionData[i][indexPrediction])*(trainData[i][indexTrain] - predictionData[i][indexPrediction]) ;
        }

        distance = Math.sqrt(distance);

        return distance;
    }

    private double obtenirGagnantVote(List<Element> prochesVoisins){
        // probablement 8 a cause des null
        double[] classement = new double[10];

        for (Element voisin: prochesVoisins){
            classement[(int)voisin.getLeagueIndex()]+=1;
        }

        int maxIndex = 0;
        for (int i = 1; i < classement.length; i++) {
            double newnumber = classement[i];
            if ((newnumber > classement[maxIndex])) {
                maxIndex = i;
            }
        }

//        double moyenne= 0;
//        for(Element val : prochesVoisins){
//            moyenne+=val.getLeagueIndex();
//        }
//        moyenne = moyenne/prochesVoisins.size();

        niveauConfiance.add(classement[maxIndex]/this.K);

        return (double)maxIndex;
    }






    private void selectAttributs(Map<String, List<Double>> map){

        Set<String> keys = new LinkedHashSet<>(map.keySet());

       for(String key : keys){
           if(!ATTRIBUTS.contains(key)){
               map.remove(key);
           }
       }
    }

    public List<Double> getNiveauConfiance() {
        return niveauConfiance;
    }

    public double getNiveauConfianceGlobal(){

        double moyenne= 0;
        for(double val : this.niveauConfiance){
            moyenne+=val;
        }
        moyenne = (moyenne/this.niveauConfiance.size())*100;

        return moyenne;
    }

    public int getK() {
        return K;
    }

	public Double[] getNosEvaludation() {
		return nosEvaludation;
	}
    
}
