package edu.cornell.tech.foundry.behavioralextensionsrstbsupport;

import com.google.gson.JsonObject;
import org.researchstack.backbone.step.Step;

import java.util.Arrays;
import edu.cornell.tech.foundry.researchsuitetaskbuilder.DefaultStepGenerators.RSTBBaseStepGenerator;
import edu.cornell.tech.foundry.researchsuitetaskbuilder.DefaultStepGenerators.descriptors.RSTBCustomStepDescriptor;
import edu.cornell.tech.foundry.researchsuitetaskbuilder.RSTBTaskBuilderHelper;

import edu.cornell.tech.foundry.behavioralextensionscore.GoNoGo.*;

/**
 * Created by jameskizer on 12/12/16.
 */
public class CTFGoNoGoStepGenerator extends RSTBBaseStepGenerator {
    public CTFGoNoGoStepGenerator()
    {
        super();
        this.supportedTypes = Arrays.asList(
                "CTFGoNoGoActiveStep"
        );
    }

    @Override
    public Step generateStep(RSTBTaskBuilderHelper helper, String type, JsonObject jsonObject) {

        RSTBCustomStepDescriptor stepDescriptor = helper.getCustomStepDescriptor(jsonObject);

        CTFGoNoGoDescriptor descriptor = helper.getGson().fromJson(stepDescriptor.parameters, CTFGoNoGoDescriptor.class);

        int[] cueTimes = new int[descriptor.cueTimes.length];

        for(int i=0; i<descriptor.cueTimes.length; i++) {
            cueTimes[i] = (int)(descriptor.cueTimes[i] * 1000.0);
        }

        CTFGoNoGoStepParameters parameters = new CTFGoNoGoStepParameters(
                (int)(descriptor.waitTime * 1000.0),
                (int)(descriptor.crossTime * 1000.0),
                (int)(descriptor.blankTime * 1000.0),
                cueTimes,
                (int)(descriptor.fillTime * 1000.0),
                descriptor.goCueTargetProbability,
                descriptor.noGoCueTargetProbability,
                descriptor.goCueProbability,
                descriptor.numberOfTrials
        );

        CTFGoNoGoStep step = new CTFGoNoGoStep(stepDescriptor.identifier, parameters);
        step.setOptional(stepDescriptor.optional);

        return step;
    }
}