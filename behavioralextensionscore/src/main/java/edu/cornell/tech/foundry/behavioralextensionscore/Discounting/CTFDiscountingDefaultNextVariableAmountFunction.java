package edu.cornell.tech.foundry.behavioralextensionscore.Discounting;

import android.support.annotation.Nullable;

import java.io.Serializable;

/**
 * Created by jameskizer on 7/9/17.
 */

public class CTFDiscountingDefaultNextVariableAmountFunction implements CTFDiscountingStep.NextVariableAmountFunction, Serializable {

    @Nullable
    @Override
    public Double computeNextVariableAmount(int numberOfTrialResults, CTFDiscountingTrialResult[] trialResults) {

        assert(numberOfTrialResults > 0);

        CTFDiscountingTrialResult lastResult = trialResults[numberOfTrialResults-1];

        double absoluteDifference = trialResults[0].getTrial().getVariableAmount();

        for(int i=0; i<numberOfTrialResults; i++) {

            absoluteDifference = absoluteDifference / 2.0;

        }

        //if choice is variable, next variable amount should be lower
        //if choice is constant, next variable amount should be higher
        double differenceAmount = lastResult.getChoiceType() == CTFDiscountingTrialResult.Choice.VARIABLE ? -absoluteDifference : absoluteDifference;
        return trialResults[numberOfTrialResults-1].getTrial().getVariableAmount() + differenceAmount;
    }
}
