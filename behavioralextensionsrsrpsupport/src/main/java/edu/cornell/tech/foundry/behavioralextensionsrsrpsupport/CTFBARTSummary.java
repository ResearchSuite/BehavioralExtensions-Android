package edu.cornell.tech.foundry.behavioralextensionsrsrpsupport;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.apache.commons.math3.util.MathUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Formatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import edu.cornell.tech.foundry.behavioralextensionscore.BART.CTFBARTResult;
import edu.cornell.tech.foundry.behavioralextensionscore.BART.CTFBARTTrialResult;
import edu.cornell.tech.foundry.researchsuiteresultprocessor.RSRPIntermediateResult;

/**
 * Created by jameskizer on 2/7/17.
 */

public class CTFBARTSummary extends RSRPIntermediateResult {

    private class SummaryStats {
        public double mean;
        public int range;
        public double stdDev;

        public SummaryStats(double mean, int range, double stdDev) {
            this.mean = mean;
            this.range = range;
            this.stdDev = stdDev;
        }
    }

    String variableLabel;
    int maxPumpsPerBalloon;

    int[] pumpsPerBalloon;

    int numberOfBalloons;
    int numberOfExplosions;

    double meanPumpsAfterExplode;
    double meanPumpsAfterNoExplode;

    double pumpsMean;
    double pumpsRange;
    double pumpsStdDev;

    double firstThirdPumpsMean;
    double firstThirdPumpsRange;
    double firstThirdPumpsStdDev;

    double middleThirdPumpsMean;
    double middleThirdPumpsRange;
    double middleThirdPumpsStdDev;

    double lastThirdPumpsMean;
    double lastThirdPumpsRange;
    double lastThirdPumpsStdDev;

    String researcherCode = "";
    double totalGains;

    public CTFBARTSummary(
            UUID uuid,
            String taskIdentifier,
            UUID taskRunUUID,
            CTFBARTResult result
    ) {

        super("BARTSummary", uuid, taskIdentifier, taskRunUUID);

        CTFBARTTrialResult[] trialResults = result.getTrialResults();

        if (trialResults.length > 0) {
            this.variableLabel = String.format("BART%.02f", trialResults[0].getTrial().getEarningPerPump());

            ArrayList<Integer> pumpsPerBalloonObject = new ArrayList<>();
            this.pumpsPerBalloon = new int[trialResults.length];
            ArrayList<Double> payoutPerBalloon = new ArrayList<>();
            double totalGains = 0.0;
            for(int i=0; i<trialResults.length; i++) {
                this.pumpsPerBalloon[i] = trialResults[i].getNumPumps();
                pumpsPerBalloonObject.add(trialResults[i].getNumPumps());
                payoutPerBalloon.add(trialResults[i].getPayout());
                totalGains += trialResults[i].getPayout();
            }

            this.maxPumpsPerBalloon = Collections.max(pumpsPerBalloonObject);

            this.totalGains = totalGains;

            this.numberOfBalloons = trialResults.length;

            Set<Integer> explodedIndices = new HashSet<>();
            for(int i=0; i<trialResults.length; i++) {
                CTFBARTTrialResult trialResult = trialResults[i];

                if(trialResult.isExploded()) {
                    explodedIndices.add(trialResult.getTrial().getTrialIndex());
                }
            }

            this.numberOfExplosions = explodedIndices.size();

            SummaryStatistics afterExplosion = new SummaryStatistics();
            SummaryStatistics afterNoExplosion = new SummaryStatistics();
            for(int i=0; i<trialResults.length; i++) {
                CTFBARTTrialResult trialResult = trialResults[i];

                int index = trialResult.getTrial().getTrialIndex();
                if (index != 0 && explodedIndices.contains(index)) {
                    afterExplosion.addValue((double)trialResult.getNumPumps());
                }
                else {
                    afterNoExplosion.addValue((double)trialResult.getNumPumps());
                }
            }

            this.meanPumpsAfterExplode = Double.isNaN(afterExplosion.getMean()) ? 0.0 : afterExplosion.getMean();
            this.meanPumpsAfterNoExplode = Double.isNaN(afterNoExplosion.getMean()) ? 0.0 : afterNoExplosion.getMean();

            SummaryStats total = this.generateSummaryStatistics(trialResults);
            if (total != null) {
                this.pumpsMean = total.mean;
                this.pumpsRange = total.range;
                this.pumpsStdDev = total.stdDev;
            }

            SummaryStats firstThird = this.generateSummaryStatistics(Arrays.copyOfRange(trialResults, 0, trialResults.length / 3));
            if (firstThird != null) {
                this.firstThirdPumpsMean = firstThird.mean;
                this.firstThirdPumpsRange = firstThird.range;
                this.firstThirdPumpsStdDev = firstThird.stdDev;
            }

            SummaryStats middleThird = this.generateSummaryStatistics(Arrays.copyOfRange(trialResults, trialResults.length / 3, (2*trialResults.length) / 3));
            if (middleThird != null) {
                this.middleThirdPumpsMean = middleThird.mean;
                this.middleThirdPumpsRange = middleThird.range;
                this.middleThirdPumpsStdDev = middleThird.stdDev;
            }

            SummaryStats lastThird = this.generateSummaryStatistics(Arrays.copyOfRange(trialResults, (2*trialResults.length) / 3, trialResults.length));
            if (lastThird != null) {
                this.lastThirdPumpsMean = lastThird.mean;
                this.lastThirdPumpsRange = lastThird.range;
                this.lastThirdPumpsStdDev = lastThird.stdDev;
            }
        }

    }

    private SummaryStats generateSummaryStatistics(CTFBARTTrialResult[] trialResults) {

        double pumpMean = 0.0;
        int pumpRange = 0;
        double pumpStdDev = 0.0;

        if (trialResults.length > 0) {

//            int totalPumps = 0;
            List<Integer> pumpCounts = new ArrayList<>();
            SummaryStatistics summary = new SummaryStatistics();

            for (int i=0; i<trialResults.length; i++) {

                CTFBARTTrialResult trialResult = trialResults[i];

//                totalPumps += trialResult.getNumPumps();
                pumpCounts.add(new Integer(trialResult.getNumPumps()));

                summary.addValue((double)trialResult.getNumPumps());
            }

            pumpMean = Double.isNaN(summary.getMean()) ? 0.0 : summary.getMean() ;

            int pumpMin = Collections.min(pumpCounts).intValue();
            int pumpMax = Collections.max(pumpCounts).intValue();
            pumpRange = pumpMax - pumpMin;

            pumpStdDev = summary.getStandardDeviation();

        }

        return new SummaryStats(pumpMean, pumpRange, pumpStdDev);

    }

    public String getVariableLabel() {
        return variableLabel;
    }

    public int getMaxPumpsPerBalloon() {
        return maxPumpsPerBalloon;
    }

    public int[] getPumpsPerBalloon() {
        return pumpsPerBalloon;
    }

    public int getNumberOfBalloons() {
        return numberOfBalloons;
    }

    public int getNumberOfExplosions() {
        return numberOfExplosions;
    }

    public double getMeanPumpsAfterExplode() {
        return meanPumpsAfterExplode;
    }

    public double getMeanPumpsAfterNoExplode() {
        return meanPumpsAfterNoExplode;
    }

    public double getPumpsMean() {
        return pumpsMean;
    }

    public double getPumpsRange() {
        return pumpsRange;
    }

    public double getPumpsStdDev() {
        return pumpsStdDev;
    }

    public double getFirstThirdPumpsMean() {
        return firstThirdPumpsMean;
    }

    public double getFirstThirdPumpsRange() {
        return firstThirdPumpsRange;
    }

    public double getFirstThirdPumpsStdDev() {
        return firstThirdPumpsStdDev;
    }

    public double getMiddleThirdPumpsMean() {
        return middleThirdPumpsMean;
    }

    public double getMiddleThirdPumpsRange() {
        return middleThirdPumpsRange;
    }

    public double getMiddleThirdPumpsStdDev() {
        return middleThirdPumpsStdDev;
    }

    public double getLastThirdPumpsMean() {
        return lastThirdPumpsMean;
    }

    public double getLastThirdPumpsRange() {
        return lastThirdPumpsRange;
    }

    public double getLastThirdPumpsStdDev() {
        return lastThirdPumpsStdDev;
    }

    public String getResearcherCode() {
        return researcherCode;
    }

    public double getTotalGains() {
        return totalGains;
    }
}
