package edu.cornell.tech.foundry.behavioralextensionscore.GoNoGo;

import java.io.Serializable;

/**
 * Created by jameskizer on 12/14/16.
 */
public class CTFGoNoGoTrialResult implements Serializable {

    private CTFGoNoGoTrial trial;
    private double responseTime;
    private boolean tapped;

    public CTFGoNoGoTrialResult(CTFGoNoGoTrial trial, double responseTime, boolean tapped) {
        this.trial = trial;
        this.responseTime = responseTime;
        this.tapped = tapped;
    }

    public CTFGoNoGoTrial getTrial() {
        return trial;
    }

    public double getResponseTime() {
        return responseTime;
    }

    public boolean isTapped() {
        return tapped;
    }
}
