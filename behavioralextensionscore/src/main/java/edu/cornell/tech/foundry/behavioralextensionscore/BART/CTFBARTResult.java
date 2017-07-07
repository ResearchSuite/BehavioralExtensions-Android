package edu.cornell.tech.foundry.behavioralextensionscore.BART;

import org.researchstack.backbone.result.Result;

/**
 * Created by jameskizer on 12/14/16.
 */
public class CTFBARTResult extends Result{
    private CTFBARTTrialResult[] trialResults;

    private Integer pendingTrialIndex;
    private CTFBARTTrialResult[] inProgressTrialResults;

    public CTFBARTTrialResult[] getTrialResults() {
        return trialResults;
    }

    public void setTrialResults(CTFBARTTrialResult[] trialResults) {
        if (trialResults != null) {
            this.trialResults = trialResults.clone();
        }
    }

    public CTFBARTResult(String identifier) {
        super(identifier);
    }

    public CTFBARTTrialResult[] getInProgressTrialResults() {
        return inProgressTrialResults;
    }

    public void setInProgressTrialResults(CTFBARTTrialResult[] inProgressTrialResults) {
        this.inProgressTrialResults = inProgressTrialResults;
    }

    public Integer getPendingTrialIndex() {
        return pendingTrialIndex;
    }

    public void setPendingTrialIndex(Integer pendingTrialIndex) {
        this.pendingTrialIndex = pendingTrialIndex;
    }
}
