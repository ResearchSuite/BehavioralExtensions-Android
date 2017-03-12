package edu.cornell.tech.foundry.behavioralextensionscore.DelayDiscounting;

import java.io.Serializable;

/**
 * Created by jameskizer on 1/3/17.
 */
public class CTFDelayDiscountingTrialResult implements Serializable {

    public enum Choice {
        NOW,
        LATER
    }

    private CTFDelayDiscountingTrial trial;
    private Choice choiceType;
    private double choiceValue;
    //time in seconds
    private double responseTime;

    public CTFDelayDiscountingTrialResult(CTFDelayDiscountingTrial trial, Choice choiceType, double choiceValue, double responseTime) {
        this.trial = trial;
        this.choiceType = choiceType;
        this.choiceValue = choiceValue;
        this.responseTime = responseTime;
    }

    public CTFDelayDiscountingTrial getTrial() {
        return trial;
    }

    public Choice getChoiceType() {
        return choiceType;
    }

    public double getChoiceValue() {
        return choiceValue;
    }

    public double getResponseTime() {
        return responseTime;
    }
}
