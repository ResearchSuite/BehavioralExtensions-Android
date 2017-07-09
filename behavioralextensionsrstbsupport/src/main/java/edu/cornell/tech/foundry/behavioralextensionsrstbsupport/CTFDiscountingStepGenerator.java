package edu.cornell.tech.foundry.behavioralextensionsrstbsupport;

import com.google.gson.JsonObject;

import org.researchstack.backbone.step.Step;

import java.util.Arrays;

import edu.cornell.tech.foundry.behavioralextensionscore.Discounting.CTFDiscountingStep;
import edu.cornell.tech.foundry.researchsuitetaskbuilder.DefaultStepGenerators.RSTBBaseStepGenerator;
import edu.cornell.tech.foundry.researchsuitetaskbuilder.DefaultStepGenerators.descriptors.RSTBCustomStepDescriptor;
import edu.cornell.tech.foundry.researchsuitetaskbuilder.RSTBTaskBuilderHelper;

/**
 * Created by jameskizer on 7/9/17.
 */

public class CTFDiscountingStepGenerator extends RSTBBaseStepGenerator {

    public CTFDiscountingStepGenerator()
    {
        super();
        this.supportedTypes = Arrays.asList(
                "CTFDiscountingActiveStep"
        );
    }

    @Override
    public Step generateStep(RSTBTaskBuilderHelper helper, String type, JsonObject jsonObject) {

        CTFDiscountingDescriptor descriptor = helper.getGson().fromJson(jsonObject, CTFDiscountingDescriptor.class);

        CTFDiscountingStep step = new CTFDiscountingStep(
                descriptor.identifier,
                descriptor.text,
                descriptor.constantAmount,
                descriptor.initialVariableAmount,
                descriptor.numQuestions,
                descriptor.constantFormatString,
                descriptor.variableFormatString
        );

        step.setOptional(descriptor.optional);

        return step;
    }

}
