package edu.cornell.tech.foundry.behavioralextensionsrstbsupport;

import com.google.gson.JsonObject;

import org.researchstack.backbone.step.Step;

import java.util.Arrays;

import edu.cornell.tech.foundry.behavioralextensionscore.BART.CTFBARTParameters;
import edu.cornell.tech.foundry.behavioralextensionscore.BART.CTFBARTStep;
import edu.cornell.tech.foundry.researchsuitetaskbuilder.DefaultStepGenerators.RSTBBaseStepGenerator;
import edu.cornell.tech.foundry.researchsuitetaskbuilder.DefaultStepGenerators.descriptors.RSTBCustomStepDescriptor;
import edu.cornell.tech.foundry.researchsuitetaskbuilder.RSTBTaskBuilderHelper;


/**
 * Created by jameskizer on 12/14/16.
 */
public class CTFBARTStepGenerator  extends RSTBBaseStepGenerator {
    public CTFBARTStepGenerator()
    {
        super();
        this.supportedTypes = Arrays.asList(
                "CTFBARTActiveStep"
        );
    }

    @Override
    public Step generateStep(RSTBTaskBuilderHelper helper, String type, JsonObject jsonObject) {

        RSTBCustomStepDescriptor stepDescriptor = helper.getCustomStepDescriptor(jsonObject);

        CTFBARTParameters parameters = helper.getGson().fromJson(stepDescriptor.parameters, CTFBARTParameters.class);

        CTFBARTStep step = new CTFBARTStep(stepDescriptor.identifier, parameters);
        step.setOptional(stepDescriptor.optional);

        return step;
    }
}
