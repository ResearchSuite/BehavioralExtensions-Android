package edu.cornell.tech.foundry.behavioralextensionscore.BART;

import java.io.Serializable;

/**
 * Created by jameskizer on 12/14/16.
 */
public class CTFBARTParameters implements Serializable {

    public int getNumberOfTrials() {
        return numberOfTrials;
    }

    public double getEarningsPerPump() {
        return earningsPerPump;
    }

    public int getMaxPayingPumpsPerTrial() {
        return maxPayingPumpsPerTrial;
    }

    public boolean canExplodeOnFirstPump() {
        return canExplodeOnFirstPump;
    }

    private int numberOfTrials;
    private double earningsPerPump;
    private int maxPayingPumpsPerTrial;
    private boolean canExplodeOnFirstPump;

    public CTFBARTParameters() {

    }
}
