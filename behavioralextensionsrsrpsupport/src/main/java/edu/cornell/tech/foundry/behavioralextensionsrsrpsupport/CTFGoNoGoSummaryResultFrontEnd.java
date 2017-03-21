package edu.cornell.tech.foundry.behavioralextensionsrsrpsupport;

import android.support.annotation.Nullable;

import org.researchstack.backbone.result.StepResult;

import java.util.Map;
import java.util.UUID;

import edu.cornell.tech.foundry.behavioralextensionscore.GoNoGo.CTFGoNoGoResult;
import edu.cornell.tech.foundry.researchsuiteresultprocessor.RSRPFrontEndServiceProvider.spi.RSRPFrontEnd;
import edu.cornell.tech.foundry.researchsuiteresultprocessor.RSRPIntermediateResult;

/**
 * Created by jameskizer on 2/6/17.
 */

public class CTFGoNoGoSummaryResultFrontEnd implements RSRPFrontEnd {
    @Nullable
    @Override
    public RSRPIntermediateResult transform(String taskIdentifier, UUID taskRunUUID, Map<String, Object> parameters) {
        Object param = parameters.get("GoNoGoResult");
        if (param == null || !(param instanceof StepResult)) {
            return null;
        }

        StepResult stepResult = (StepResult) param;
        Object result = stepResult.getResult();
        if(! (result instanceof CTFGoNoGoResult)) {
            return null;
        }

        CTFGoNoGoSummary summary = new CTFGoNoGoSummary(
                UUID.randomUUID(),
                taskIdentifier,
                taskRunUUID,
                (CTFGoNoGoResult)result
        );

        summary.setStartDate(((CTFGoNoGoResult) result).getStartDate());
        summary.setEndDate(((CTFGoNoGoResult) result).getEndDate());
        summary.setParameters(parameters);

        return summary;
    }

    @Override
    public boolean supportsType(String type) {
        if (type.equals("GoNoGoSummary")) return true;
        else return false;
    }
}
