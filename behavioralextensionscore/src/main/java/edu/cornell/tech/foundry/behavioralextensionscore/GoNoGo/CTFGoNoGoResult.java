package edu.cornell.tech.foundry.behavioralextensionscore.GoNoGo;

import org.researchstack.backbone.result.Result;

/**
 * Created by jameskizer on 12/12/16.
 */
public class CTFGoNoGoResult extends Result {

    private CTFGoNoGoTrialResult[] trialResults;
    private Integer pendingTrialIndex;
    private CTFGoNoGoTrialResult[] inProgressTrialResults;

    public CTFGoNoGoTrialResult[] getTrialResults() {
        return trialResults;
    }

    public void setTrialResults(CTFGoNoGoTrialResult[] trialResults) {
        if (trialResults != null) {
            this.trialResults = trialResults.clone();
        }
    }

    public CTFGoNoGoResult(String identifier) {
        super(identifier);
    }

    public Integer getPendingTrialIndex() {
        return pendingTrialIndex;
    }

    public CTFGoNoGoTrialResult[] getInProgressTrialResults() {
        return inProgressTrialResults;
    }

    public void setPendingTrialIndex(Integer pendingTrialIndex) {
        this.pendingTrialIndex = pendingTrialIndex;
    }

    public void setInProgressTrialResults(CTFGoNoGoTrialResult[] inProgressTrialResults) {
        this.inProgressTrialResults = inProgressTrialResults;
    }
}
