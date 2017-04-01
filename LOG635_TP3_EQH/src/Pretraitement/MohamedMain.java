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

        LecteurExcel excel = new LecteurExcel(filepath);
        Map<String,List<Double>> map;
        map = excel.getHashMap();

    }
}
