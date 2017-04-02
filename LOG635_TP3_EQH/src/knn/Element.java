package knn;

/**
 * Created by Rachid, Mohamed Yassine on 2017-04-02.
 */
public class Element implements Comparable<Element>{
    private double distance ;
    private double leagueIndex;

    public Element(double distance, double leagueIndex) {
        this.distance = distance;
        this.leagueIndex = leagueIndex;
    }

    @Override
    public int compareTo(Element o) {
        if(this.distance <= o.getDistance() ){
            return -1;
        }else {
            return 1;
        }

    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getLeagueIndex() {
        return leagueIndex;
    }

    public void setLeagueIndex(double leagueIndex) {
        this.leagueIndex = leagueIndex;
    }
}
