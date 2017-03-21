package edu.cornell.tech.foundry.behavioralextensionsrsrpsupport;

import android.support.annotation.Nullable;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import edu.cornell.tech.foundry.behavioralextensionscore.GoNoGo.CTFGoNoGoResult;
import edu.cornell.tech.foundry.behavioralextensionscore.GoNoGo.CTFGoNoGoTrial;
import edu.cornell.tech.foundry.behavioralextensionscore.GoNoGo.CTFGoNoGoTrialResult;
import edu.cornell.tech.foundry.researchsuiteresultprocessor.RSRPIntermediateResult;

/**
 * Created by jameskizer on 2/6/17.
 */

public class CTFGoNoGoSummary extends RSRPIntermediateResult {

    private class StreakInfo {
        public StreakInfo(int count, boolean correct) {
            this.count = count;
            this.correct = correct;
        }

        int count;
        boolean correct;
    }

    private String variableLabel;
    private int numberOfTrials;
    private CTFGoNoGoSummaryStruct totalSummary;
    private CTFGoNoGoSummaryStruct firstThirdSummary;
    private CTFGoNoGoSummaryStruct middleThirdSummary;
    private CTFGoNoGoSummaryStruct lastThirdSummary;

    public String getVariableLabel() {
        return variableLabel;
    }

    public int getNumberOfTrials() {
        return numberOfTrials;
    }

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

    private boolean correct(CTFGoNoGoTrialResult trialResult) {
        return trialResult.isTapped() == (trialResult.getTrial().getTarget() == CTFGoNoGoTrial.CTFGoNoGoTargetType.GO);
    }

    public CTFGoNoGoSummary(
            UUID uuid,
            String taskIdentifier,
            UUID taskRunUUID,
            CTFGoNoGoResult result
    ) {

        super("GoNoGoSummary", uuid, taskIdentifier, taskRunUUID);

        this.variableLabel = result.getIdentifier();
        this.numberOfTrials = result.getTrialResults().length;

        CTFGoNoGoTrialResult[] trialResults = result.getTrialResults();
        StreakInfo[] streaks = new StreakInfo[trialResults.length];

        StreakInfo last = null;
        //streak info for a result is the streak you had coming into the trial
        for(int i=0; i<trialResults.length; i++) {

            CTFGoNoGoTrialResult trialResult = trialResults[i];
            if (last == null) {
                streaks[i] = new StreakInfo(0, correct(trialResult));
            }
            else {

                if (correct(trialResult) == last.correct) {
                    streaks[i] = new StreakInfo(last.count+1, last.correct);
                }
                else {
                    streaks[i] = new StreakInfo(1, !last.correct);
                }
            }
        }


        this.totalSummary = generateSummary(trialResults, streaks);
        this.firstThirdSummary = generateSummary(Arrays.copyOfRange(trialResults, 0, trialResults.length / 3), Arrays.copyOfRange(streaks, 0, trialResults.length / 3));
        this.middleThirdSummary = generateSummary(Arrays.copyOfRange(trialResults, trialResults.length / 3, (2 * trialResults.length) / 3), Arrays.copyOfRange(streaks, trialResults.length / 3, (2 * trialResults.length) / 3));
        this.lastThirdSummary = generateSummary(Arrays.copyOfRange(trialResults, (2 * trialResults.length) / 3, trialResults.length), Arrays.copyOfRange(streaks, (2 * trialResults.length) / 3, trialResults.length));

    }

    @Nullable
    static private CTFGoNoGoSummaryStruct generateSummary(CTFGoNoGoTrialResult[] trialResults, StreakInfo[] streaks) {
        if (trialResults.length == 0) {
            return null;
        }

        List<CTFGoNoGoTrialResult> correctResponses = new ArrayList<>();
        List<CTFGoNoGoTrialResult> correctNonresponses = new ArrayList<>();
        List<CTFGoNoGoTrialResult> incorrectResponses = new ArrayList<>();
        List<CTFGoNoGoTrialResult> incorrectNonresponses = new ArrayList<>();
        List<CTFGoNoGoTrialResult> resultsAfterOneIncorrect = new ArrayList<>();
        List<CTFGoNoGoTrialResult> resultsAfterTenCorrect = new ArrayList<>();

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

            StreakInfo streak = streaks[i];
            if ((streak.count == 1) && !streak.correct) {
                resultsAfterOneIncorrect.add(trialResult);
            }

            if ((streak.count == 10) && streak.correct) {
                resultsAfterTenCorrect.add(trialResult);
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


        SummaryStatistics responseTimes = new SummaryStatistics();
        SummaryStatistics correctReponseTimes = new SummaryStatistics();
        SummaryStatistics incorrectReponseTimes = new SummaryStatistics();
        SummaryStatistics responseTimeAfterOneIncorrect = new SummaryStatistics();
        SummaryStatistics reponseTimeAfterTenCorrect = new SummaryStatistics();


        for (CTFGoNoGoTrialResult result : correctResponses) {
            responseTimes.addValue(result.getResponseTime());
            correctReponseTimes.addValue(result.getResponseTime());
        }

        for (CTFGoNoGoTrialResult result : incorrectResponses) {
            responseTimes.addValue(result.getResponseTime());
            incorrectReponseTimes.addValue(result.getResponseTime());
        }

        for (CTFGoNoGoTrialResult result : resultsAfterOneIncorrect) {
            responseTimeAfterOneIncorrect.addValue(result.getResponseTime());
        }

        for (CTFGoNoGoTrialResult result : resultsAfterTenCorrect) {
            reponseTimeAfterTenCorrect.addValue(result.getResponseTime());
        }

        double meanResponseTime = responseTimes.getN() > 0 ? responseTimes.getMean() : 0.0;

        double responseTimeRange = responseTimes.getN() > 0 ? responseTimes.getMax() - responseTimes.getMin(): 0.0;

        double responseTimeStdDev = responseTimes.getN() > 0 ? responseTimes.getStandardDeviation() : 0.0;

        double meanResponseTimeAfterOneIncorrect = responseTimeAfterOneIncorrect.getN() > 0 ? responseTimeAfterOneIncorrect.getMean() : 0.0;

        double meanResponseTimeAfterTenCorrect = reponseTimeAfterTenCorrect.getN() > 0 ? reponseTimeAfterTenCorrect.getMean() : 0.0;

        double meanResponseTimeCorrect = correctReponseTimes.getN() > 0 ? correctReponseTimes.getMean() : 0.0;

        double meanResponseTimeIncorrect = incorrectReponseTimes.getN() > 0 ? incorrectReponseTimes.getMean() : 0.0;

        return new CTFGoNoGoSummaryStruct(
                numberOfTrials,
                numberOfCorrectResponses,
                numberOfCorrectNonresponses,
                numberOfIncorrectResponses,
                numberOfIncorrectNonresponses,
                meanAccuracy,
                meanResponseTime,
                responseTimeRange,
                responseTimeStdDev,
                meanResponseTimeAfterOneIncorrect,
                meanResponseTimeAfterTenCorrect,
                meanResponseTimeCorrect,
                meanResponseTimeIncorrect
        );

    }
}
