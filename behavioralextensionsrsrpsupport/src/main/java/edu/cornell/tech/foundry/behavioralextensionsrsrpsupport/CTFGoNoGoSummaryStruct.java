package edu.cornell.tech.foundry.behavioralextensionsrsrpsupport;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jameskizer on 2/6/17.
 */

public class CTFGoNoGoSummaryStruct {

    public int numberOfTrials;
    public int numberOfCorrectResponses;
    public int numberOfCorrectNonresponses;
    public int numberOfIncorrectResponses;
    public int numberOfIncorrectNonresponses;

    public double meanAccuracy;
    public double responseTimeMean;
    public double responseTimeMin;
    public double responseTimeMax;

//    public double meanResponseTimeAfterOneError;
//    public double meanResponseTimeAfterTenStreak;

    public double meanResponseTimeCorrect;
    public double meanResponseTimeIncorrect;

    public CTFGoNoGoSummaryStruct(int numberOfTrials, int numberOfCorrectResponses, int numberOfCorrectNonresponses, int numberOfIncorrectResponses, int numberOfIncorrectNonresponses, double meanAccuracy, double responseTimeMean, double responseTimeMin, double responseTimeMax, double meanResponseTimeCorrect, double meanResponseTimeIncorrect) {
        this.numberOfTrials = numberOfTrials;
        this.numberOfCorrectResponses = numberOfCorrectResponses;
        this.numberOfCorrectNonresponses = numberOfCorrectNonresponses;
        this.numberOfIncorrectResponses = numberOfIncorrectResponses;
        this.numberOfIncorrectNonresponses = numberOfIncorrectNonresponses;
        this.meanAccuracy = meanAccuracy;
        this.responseTimeMean = responseTimeMean;
        this.responseTimeMin = responseTimeMin;
        this.responseTimeMax = responseTimeMax;
        this.meanResponseTimeCorrect = meanResponseTimeCorrect;
        this.meanResponseTimeIncorrect = meanResponseTimeIncorrect;
    }

    public JSONObject toJson() {

        Map<String, Object> map = new HashMap<>();
        map.put("numberOfTrials", this.numberOfTrials);
        map.put("numberOfCorrectResponses", this.numberOfCorrectResponses);
        map.put("numberOfCorrectNonresponses", this.numberOfCorrectNonresponses);
        map.put("numberOfIncorrectResponses", this.numberOfIncorrectResponses);
        map.put("numberOfIncorrectNonresponses", this.numberOfIncorrectNonresponses);
        map.put("meanAccuracy", this.meanAccuracy);
        map.put("responseTimeMean", this.responseTimeMean);
        map.put("responseTimeMin", this.responseTimeMin);
        map.put("responseTimeMax", this.responseTimeMax);
        map.put("meanResponseTimeCorrect", this.meanResponseTimeCorrect);
        map.put("meanResponseTimeIncorrect", this.meanResponseTimeIncorrect);

        return new JSONObject(map);
    }
}
