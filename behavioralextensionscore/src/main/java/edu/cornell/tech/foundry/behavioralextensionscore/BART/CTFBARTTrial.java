package edu.cornell.tech.foundry.behavioralextensionscore.BART;

import java.io.Serializable;

/**
 * Created by jameskizer on 12/14/16.
 */
public class CTFBARTTrial implements Serializable {
    private int trialIndex;
    private int maxPayingPumps;
    private double earningPerPump;

    public CTFBARTTrial(int trialIndex, int maxPayingPumps, double earningPerPump) {
        this.trialIndex = trialIndex;
        this.maxPayingPumps = maxPayingPumps;
        this.earningPerPump = earningPerPump;
    }

    public int getTrialIndex() {
        return trialIndex;
    }

    public int getMaxPayingPumps() {
        return maxPayingPumps;
    }

    public double getEarningPerPump() {
        return earningPerPump;
    }
}
