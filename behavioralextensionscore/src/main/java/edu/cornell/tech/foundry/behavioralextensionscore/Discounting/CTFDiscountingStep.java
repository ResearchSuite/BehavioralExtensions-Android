package edu.cornell.tech.foundry.behavioralextensionscore.Discounting;

import android.support.annotation.Nullable;

import org.researchstack.backbone.step.Step;

/**
 * Created by jameskizer on 1/3/17.
 */
public class CTFDiscountingStep extends Step {

    public interface NextVariableAmountFunction {
        @Nullable
        Double computeNextVariableAmount(int numberOfTrialResults, CTFDiscountingTrialResult[] trialResults);
    }

    @Override
    public Class getStepLayoutClass()
    {
        return CTFDiscountingStepLayout.class;
    }

    private Double constantAmount;
    private Double initialVariableAmount;
    private NextVariableAmountFunction computeNextVariableAmount;
    private Integer numQuestions;
    private String constantFormatString;
    private String variableFormatString;


    public CTFDiscountingStep(
            String identifier,
            String text,
            Double constantAmount,
            Double initialVariableAmount,
            Integer numQuestions,
            String constantFormatString,
            String variableFormatString,
            NextVariableAmountFunction computeNextVariableAmount
    )
    {
        super(identifier);
        this.setText(text);
        this.constantAmount = constantAmount;
        this.initialVariableAmount = initialVariableAmount;
        this.numQuestions = numQuestions;
        this.constantFormatString = constantFormatString;
        this.variableFormatString = variableFormatString;
        this.computeNextVariableAmount = computeNextVariableAmount;
    }

    public CTFDiscountingStep(
            String identifier,
            String text,
            Double constantAmount,
            Double initialVariableAmount,
            Integer numQuestions,
            String constantFormatString,
            String variableFormatString
    )
    {
        super(identifier);
        this.setText(text);
        this.constantAmount = constantAmount;
        this.initialVariableAmount = initialVariableAmount;
        this.numQuestions = numQuestions;
        this.constantFormatString = constantFormatString;
        this.variableFormatString = variableFormatString;
        this.computeNextVariableAmount = new CTFDiscountingDefaultNextVariableAmountFunction();
    }

    public Double getConstantAmount() {
        return constantAmount;
    }

    public Double getInitialVariableAmount() {
        return initialVariableAmount;
    }

    public NextVariableAmountFunction getComputeNextVariableAmount() {
        return computeNextVariableAmount;
    }

    public Integer getNumQuestions() {
        return numQuestions;
    }

    public String getConstantFormatString() {
        return constantFormatString;
    }

    public String getVariableFormatString() {
        return variableFormatString;
    }
}
