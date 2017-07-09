package edu.cornell.tech.foundry.behavioralextensionsrsrpsupport;

import org.researchstack.backbone.result.Result;
import org.researchstack.backbone.result.StepResult;

import java.util.Map;
import java.util.UUID;

import edu.cornell.tech.foundry.behavioralextensionscore.Discounting.CTFDiscountingResult;
import edu.cornell.tech.foundry.researchsuiteresultprocessor.RSRPFrontEndServiceProvider.spi.RSRPFrontEnd;
import edu.cornell.tech.foundry.researchsuiteresultprocessor.RSRPIntermediateResult;

/**
 * Created by jameskizer on 2/3/17.
 */
public class CTFDiscountingRawResultFrontEnd implements RSRPFrontEnd {
    @Override
    public RSRPIntermediateResult transform(String taskIdentifier, UUID taskRunUUID, Map<String, Object> parameters) {

        Object param = parameters.get("result");
        if (param == null || !(param instanceof StepResult)) {
            return null;
        }

        StepResult stepResult = (StepResult)param;
        Object result = stepResult.getResult();
        if(! (result instanceof CTFDiscountingResult)) {
            return null;
        }

        CTFDiscountingRaw raw = new CTFDiscountingRaw(
                UUID.randomUUID(),
                taskIdentifier,
                taskRunUUID,
                (CTFDiscountingResult)result
        );

        raw.setStartDate(((CTFDiscountingResult) result).getStartDate());
        raw.setEndDate(((CTFDiscountingResult) result).getEndDate());
        raw.setParameters(parameters);

        return raw;
    }

    @Override
    public boolean supportsType(String type) {
        if (type.equals("DiscountingRaw")) return true;
        else return false;
    }
}
