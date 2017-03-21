package edu.cornell.tech.foundry.behavioralextensionsrsrpsupport;

import org.researchstack.backbone.result.Result;
import org.researchstack.backbone.result.StepResult;

import java.util.Map;
import java.util.UUID;

import edu.cornell.tech.foundry.behavioralextensionscore.DelayDiscounting.CTFDelayDiscountingResult;
import edu.cornell.tech.foundry.researchsuiteresultprocessor.RSRPFrontEndServiceProvider.spi.RSRPFrontEnd;
import edu.cornell.tech.foundry.researchsuiteresultprocessor.RSRPIntermediateResult;

/**
 * Created by jameskizer on 2/3/17.
 */
public class CTFDelayDiscountingRawResultFrontEnd implements RSRPFrontEnd {
    @Override
    public RSRPIntermediateResult transform(String taskIdentifier, UUID taskRunUUID, Map<String, Object> parameters) {

        Object param = parameters.get("DelayDiscountingResult");
        if (param == null || !(param instanceof StepResult)) {
            return null;
        }

        StepResult stepResult = (StepResult)param;
        Object result = stepResult.getResult();
        if(! (result instanceof CTFDelayDiscountingResult)) {
            return null;
        }

        CTFDelayDiscountingRaw raw = new CTFDelayDiscountingRaw(
                UUID.randomUUID(),
                taskIdentifier,
                taskRunUUID,
                (CTFDelayDiscountingResult)result
        );

        raw.setStartDate(((CTFDelayDiscountingResult) result).getStartDate());
        raw.setEndDate(((CTFDelayDiscountingResult) result).getEndDate());
        raw.setParameters(parameters);

        return raw;
    }

    @Override
    public boolean supportsType(String type) {
        if (type.equals("DelayDiscountingRaw")) return true;
        else return false;
    }
}
