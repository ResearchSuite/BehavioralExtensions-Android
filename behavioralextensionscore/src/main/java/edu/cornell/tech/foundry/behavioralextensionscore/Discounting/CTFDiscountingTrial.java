package edu.cornell.tech.foundry.behavioralextensionscore.Discounting;

import java.io.Serializable;

/**
 * Created by jameskizer on 1/3/17.
 */
public class CTFDiscountingTrial implements Serializable {

    private double variableAmount;
    private double constantAmount;
    private int questionNum;

    public CTFDiscountingTrial(double variableAmount, double constantAmount, int questionNum) {
        this.variableAmount = variableAmount;
        this.constantAmount = constantAmount;
        this.questionNum = questionNum;
    }

    public int getQuestionNum() {
        return questionNum;
    }

    public double getVariableAmount() {
        return variableAmount;
    }

    public double getConstantAmount() {
        return constantAmount;
    }
}
