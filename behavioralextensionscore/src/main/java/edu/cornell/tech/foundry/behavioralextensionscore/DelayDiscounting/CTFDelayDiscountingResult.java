package edu.cornell.tech.foundry.behavioralextensionscore.DelayDiscounting;

import org.researchstack.backbone.result.Result;

/**
 * Created by jameskizer on 1/3/17.
 */
public class CTFDelayDiscountingResult extends Result {

    private CTFDelayDiscountingTrialResult[] trialResults;
    private CTFDelayDiscountingTrial pendingTrial;
    private CTFDelayDiscountingTrialResult[] inProgressTrialResults;

    public CTFDelayDiscountingTrialResult[] getTrialResults() {
        return trialResults;
    }

    public void setTrialResults(CTFDelayDiscountingTrialResult[] trialResults) {
        if (trialResults != null){
            this.trialResults = trialResults.clone();
        }
    }

    public CTFDelayDiscountingResult(String identifier) {
        super(identifier);
    }

    public CTFDelayDiscountingTrialResult[] getInProgressTrialResults() {
        return inProgressTrialResults;
    }

    public void setInProgressTrialResults(CTFDelayDiscountingTrialResult[] inProgressTrialResults) {
        this.inProgressTrialResults = inProgressTrialResults;
    }

    public CTFDelayDiscountingTrial getPendingTrial() {
        return pendingTrial;
    }

    public void setPendingTrial(CTFDelayDiscountingTrial pendingTrial) {
        this.pendingTrial = pendingTrial;
    }
}
