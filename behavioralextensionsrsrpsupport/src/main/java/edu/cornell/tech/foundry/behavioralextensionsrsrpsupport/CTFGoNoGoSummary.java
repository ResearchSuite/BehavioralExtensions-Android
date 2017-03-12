package edu.cornell.tech.foundry.behavioralextensionsrsrpsupport;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import edu.cornell.tech.foundry.behavioralextensionscore.GoNoGo.CTFGoNoGoResult;
import edu.cornell.tech.foundry.behavioralextensionscore.GoNoGo.CTFGoNoGoTrial;
import edu.cornell.tech.foundry.behavioralextensionscore.GoNoGo.CTFGoNoGoTrialResult;
import edu.cornell.tech.foundry.researchsuiteresultprocessor.RSRPIntermediateResult;

/**
 * Created by jameskizer on 2/6/17.
 */

public class CTFGoNoGoSummary extends RSRPIntermediateResult {

    private CTFGoNoGoSummaryStruct totalSummary;
    private CTFGoNoGoSummaryStruct firstThirdSummary;
    private CTFGoNoGoSummaryStruct middleThirdSummary;
    private CTFGoNoGoSummaryStruct lastThirdSummary;


    public CTFGoNoGoSummaryStruct getTotalSummary() {
        return totalSummary;
    }

    public CTFGoNoGoSummaryStruct getFirstThirdSummary() {
        return firstThirdSummary;
    }

    public CTFGoNoGoSummaryStruct getMiddleThirdSummary() {
        return middleThirdSummary;
    }

    public CTFGoNoGoSummaryStruct getLastThirdSummary() {
        return lastThirdSummary;
    }

    public CTFGoNoGoSummary(CTFGoNoGoResult result) {

        super("GoNoGoSummary");

        CTFGoNoGoTrialResult[] trialResults = result.getTrialResults();
        this.totalSummary = generateSummary(trialResults);
        this.firstThirdSummary = generateSummary(Arrays.copyOfRange(trialResults, 0, trialResults.length / 3));
        this.middleThirdSummary = generateSummary(Arrays.copyOfRange(trialResults, trialResults.length / 3, (2 * trialResults.length) / 3));
        this.lastThirdSummary = generateSummary(Arrays.copyOfRange(trialResults, (2 * trialResults.length) / 3, trialResults.length));

    }

    @Nullable
    static private CTFGoNoGoSummaryStruct generateSummary(CTFGoNoGoTrialResult[] trialResults) {
        if (trialResults.length == 0) {
            return null;
        }

        List<CTFGoNoGoTrialResult> correctResponses = new ArrayList<>();
        List<CTFGoNoGoTrialResult> correctNonresponses = new ArrayList<>();
        List<CTFGoNoGoTrialResult> incorrectResponses = new ArrayList<>();
        List<CTFGoNoGoTrialResult> incorrectNonresponses = new ArrayList<>();

        for(int i=0; i<trialResults.length; i++) {
            CTFGoNoGoTrialResult trialResult = trialResults[i];

            if (trialResult.getTrial().getTarget() == CTFGoNoGoTrial.CTFGoNoGoTargetType.GO) {
                if (trialResult.isTapped()) {
                    correctResponses.add(trialResult);
                }
                else {
                    incorrectNonresponses.add(trialResult);
                }
            }
            else {
                if (trialResult.isTapped()) {
                    incorrectResponses.add(trialResult);
                }
                else {
                    correctNonresponses.add(trialResult);
                }
            }
        }

        int numberOfTrials = trialResults.length;
        int numberOfCorrectResponses = correctResponses.size();
        int numberOfCorrectNonresponses = correctNonresponses.size();
        int numberOfIncorrectResponses = incorrectResponses.size();
        int numberOfIncorrectNonresponses = incorrectNonresponses.size();

        double meanAccuracy = numberOfTrials > 0 ?
                (double) (numberOfCorrectResponses + numberOfCorrectNonresponses) / (double) numberOfTrials :
                0.0;

        List<Double> responseTimes = new ArrayList<>();
//        List<Double> correctResponseTimes = new ArrayList<>();
//        List<Double> incorrectResponseTimes = new ArrayList<>();

        double correctReponseTime = 0.0;
        double incorrectReponseTime = 0.0;


        for (CTFGoNoGoTrialResult result : correctResponses) {
            responseTimes.add(new Double(result.getResponseTime()));
//            correctResponseTimes.add(new Double(result.getResponseTime()));
            correctReponseTime += result.getResponseTime();
        }

        for (CTFGoNoGoTrialResult result : incorrectResponses) {
            responseTimes.add(new Double(result.getResponseTime()));
//            incorrectResponseTimes.add(new Double(result.getResponseTime()));
            incorrectReponseTime += result.getResponseTime();
        }

        double totalResponseTime = correctReponseTime + incorrectReponseTime;
        double meanResponseTime = responseTimes.size() > 0 ?
                (totalResponseTime / (double)responseTimes.size()) :
                0.0;

        double responseTimeMin = responseTimes.size() > 0 ?
                Collections.min(responseTimes).doubleValue() :
                0.0;

        double responseTimeMax = responseTimes.size() > 0 ?
                Collections.max(responseTimes).doubleValue() :
                0.0;

        double meanResponseTimeCorrect = correctResponses.size() > 0 ?
                (correctReponseTime / (double)correctResponses.size()) :
                0.0;

        double meanResponseTimeIncorrect = incorrectResponses.size() > 0 ?
                (incorrectReponseTime / (double)incorrectResponses.size()) :
                0.0;

        return new CTFGoNoGoSummaryStruct(
                numberOfTrials,
                numberOfCorrectResponses,
                numberOfCorrectNonresponses,
                numberOfIncorrectResponses,
                numberOfIncorrectNonresponses,
                meanAccuracy,
                meanResponseTime,
                responseTimeMin,
                responseTimeMax,
                meanResponseTimeCorrect,
                meanResponseTimeIncorrect
        );

    }
}
