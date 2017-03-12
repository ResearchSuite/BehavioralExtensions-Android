package edu.cornell.tech.foundry.behavioralextensionscore.BART;

import org.researchstack.backbone.step.Step;

/**
 * Created by jameskizer on 12/14/16.
 */
public class CTFBARTStep extends Step {

    @Override
    public Class getStepLayoutClass()
    {
        return CTFBARTStepLayout.class;
    }

    private CTFBARTParameters stepParams;

    public CTFBARTStep(
            String identifier,
            CTFBARTParameters stepParameters
    )
    {
        super(identifier);
        this.stepParams = stepParameters;
    }

    public CTFBARTParameters getStepParams() {
        return stepParams;
    }
}
