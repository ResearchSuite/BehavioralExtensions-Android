package edu.cornell.tech.foundry.behavioralextensionsrsrpsupport;

import org.researchstack.backbone.result.Result;
import org.researchstack.backbone.result.StepResult;

import java.util.Map;

import edu.cornell.tech.foundry.behavioralextensionscore.DelayDiscounting.CTFDelayDiscountingResult;
import edu.cornell.tech.foundry.researchsuiteresultprocessor.RSRPFrontEndServiceProvider.spi.RSRPFrontEnd;
import edu.cornell.tech.foundry.researchsuiteresultprocessor.RSRPIntermediateResult;

/**
 * Created by jameskizer on 2/3/17.
 */
public class CTFDelayDiscountingRawResultFrontEnd implements RSRPFrontEnd {
    @Override
    public RSRPIntermediateResult transform(Map<String, StepResult> parameters) {

        StepResult stepResult = parameters.get("DelayDiscountingResult");
        if (stepResult == null) {
            return null;
        }

        Object result = stepResult.getResult();
        if(! (result instanceof CTFDelayDiscountingResult)) {
            return null;
        }

        CTFDelayDiscountingRaw raw = new CTFDelayDiscountingRaw((CTFDelayDiscountingResult)result);

        raw.setStartDate(((CTFDelayDiscountingResult) result).getStartDate());
        raw.setEndDate(((CTFDelayDiscountingResult) result).getEndDate());


        return raw;
    }

    @Override
    public boolean supportsType(String type) {
        if (type.equals("DelayDiscountingRaw")) return true;
        else return false;
    }
}
