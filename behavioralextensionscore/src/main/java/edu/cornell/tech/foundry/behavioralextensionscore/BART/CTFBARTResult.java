package edu.cornell.tech.foundry.behavioralextensionscore.BART;

import org.researchstack.backbone.result.Result;

/**
 * Created by jameskizer on 12/14/16.
 */
public class CTFBARTResult extends Result{
    private CTFBARTTrialResult[] trialResults;

    public CTFBARTTrialResult[] getTrialResults() {
        return trialResults;
    }

    public void setTrialResults(CTFBARTTrialResult[] trialResults) {
        this.trialResults = trialResults.clone();
    }

    public CTFBARTResult(String identifier) {
        super(identifier);
    }
}
