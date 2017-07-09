package edu.cornell.tech.foundry.behavioralextensionscore.Discounting;

import java.io.Serializable;

/**
 * Created by jameskizer on 1/3/17.
 */
public class CTFDiscountingTrialResult implements Serializable {

    public enum Choice {
        VARIABLE,
        CONSTANT
    }

    private CTFDiscountingTrial trial;
    private Choice choiceType;
    private double choiceValue;
    //time in seconds
    private double responseTime;

    public CTFDiscountingTrialResult(CTFDiscountingTrial trial, Choice choiceType, double choiceValue, double responseTime) {
        this.trial = trial;
        this.choiceType = choiceType;
        this.choiceValue = choiceValue;
        this.responseTime = responseTime;
    }

    public CTFDiscountingTrial getTrial() {
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
