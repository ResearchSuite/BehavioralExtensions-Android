package edu.cornell.tech.foundry.behavioralextensionscore.Discounting;

import org.researchstack.backbone.result.Result;

/**
 * Created by jameskizer on 1/3/17.
 */
public class CTFDiscountingResult extends Result {

    private CTFDiscountingTrialResult[] trialResults;
    private CTFDiscountingTrial pendingTrial;
    private CTFDiscountingTrialResult[] inProgressTrialResults;

    public CTFDiscountingTrialResult[] getTrialResults() {
        return trialResults;
    }

    public void setTrialResults(CTFDiscountingTrialResult[] trialResults) {
        if (trialResults != null){
            this.trialResults = trialResults.clone();
        }
    }

    public CTFDiscountingResult(String identifier) {
        super(identifier);
    }

    public CTFDiscountingTrialResult[] getInProgressTrialResults() {
        return inProgressTrialResults;
    }

    public void setInProgressTrialResults(CTFDiscountingTrialResult[] inProgressTrialResults) {
        this.inProgressTrialResults = inProgressTrialResults;
    }

    public CTFDiscountingTrial getPendingTrial() {
        return pendingTrial;
    }

    public void setPendingTrial(CTFDiscountingTrial pendingTrial) {
        this.pendingTrial = pendingTrial;
    }
}
