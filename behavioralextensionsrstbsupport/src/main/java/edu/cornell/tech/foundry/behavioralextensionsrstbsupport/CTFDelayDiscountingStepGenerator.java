package edu.cornell.tech.foundry.behavioralextensionsrstbsupport;

import com.google.gson.JsonObject;

import org.researchstack.backbone.step.Step;

import java.util.Arrays;

import edu.cornell.tech.foundry.behavioralextensionscore.DelayDiscounting.CTFDelayDiscountingStep;
import edu.cornell.tech.foundry.behavioralextensionscore.DelayDiscounting.CTFDelayDiscountingStepParameters;
import edu.cornell.tech.foundry.researchsuitetaskbuilder.DefaultStepGenerators.RSTBBaseStepGenerator;
import edu.cornell.tech.foundry.researchsuitetaskbuilder.DefaultStepGenerators.descriptors.RSTBCustomStepDescriptor;
import edu.cornell.tech.foundry.researchsuitetaskbuilder.RSTBTaskBuilderHelper;

/**
 * Created by jameskizer on 1/3/17.
 */
public class CTFDelayDiscountingStepGenerator extends RSTBBaseStepGenerator {

    public CTFDelayDiscountingStepGenerator()
    {
        super();
        this.supportedTypes = Arrays.asList(
                "CTFDelayedDiscountingActiveStep"
        );
    }

    @Override
    public Step generateStep(RSTBTaskBuilderHelper helper, String type, JsonObject jsonObject) {

        RSTBCustomStepDescriptor stepDescriptor = helper.getCustomStepDescriptor(jsonObject);

        CTFDelayDiscountingStepParameters parameters = helper.getGson().fromJson(stepDescriptor.parameters, CTFDelayDiscountingStepParameters.class);

        CTFDelayDiscountingStep step = new CTFDelayDiscountingStep(stepDescriptor.identifier, parameters);
        step.setOptional(stepDescriptor.optional);

        return step;
    }
}
