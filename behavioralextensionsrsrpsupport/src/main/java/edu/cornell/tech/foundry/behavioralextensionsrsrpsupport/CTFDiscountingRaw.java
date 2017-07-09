package edu.cornell.tech.foundry.behavioralextensionsrsrpsupport;

import java.util.UUID;

import edu.cornell.tech.foundry.behavioralextensionscore.Discounting.CTFDiscountingResult;
import edu.cornell.tech.foundry.behavioralextensionscore.Discounting.CTFDiscountingTrial;
import edu.cornell.tech.foundry.behavioralextensionscore.Discounting.CTFDiscountingTrialResult;
import edu.cornell.tech.foundry.researchsuiteresultprocessor.RSRPIntermediateResult;

/**
 * Created by jameskizer on 2/3/17.
 */
public class CTFDiscountingRaw extends RSRPIntermediateResult {

    private String variableLabel;
    private Double[] variableArray;
    private Double[] constantArray;
    private Double[] choiceArray;
    private Double[] times;

    public static String type = "DelayDiscountingRaw";

    public CTFDiscountingRaw(
            UUID uuid,
            String taskIdentifier,
            UUID taskRunUUID,
            CTFDiscountingResult result) {

        super(type, uuid, taskIdentifier, taskRunUUID);

        this.variableLabel = result.getIdentifier();

        CTFDiscountingTrialResult[] trialResults = result.getTrialResults();
        if (trialResults == null) {
            return;
        }

        this.variableArray = new Double[trialResults.length];
        this.constantArray = new Double[trialResults.length];
        this.choiceArray = new Double[trialResults.length];
        this.times = new Double[trialResults.length];

        for(int i=0; i<trialResults.length; i++) {
            CTFDiscountingTrialResult trialResult = trialResults[i];
            CTFDiscountingTrial trial = trialResult.getTrial();
            this.variableArray[i] = trial.getVariableAmount();
            this.constantArray[i] = trial.getConstantAmount();
            this.choiceArray[i] = trialResult.getChoiceValue();
            this.times[i] = trialResult.getResponseTime();
         }

    }

    public String getVariableLabel() {
        return variableLabel;
    }

    public Double[] getVariableArray() {
        return variableArray;
    }

    public Double[] getConstantArray() {
        return constantArray;
    }

    public Double[] getChoiceArray() {
        return choiceArray;
    }

    public Double[] getTimes() {
        return times;
    }
}
