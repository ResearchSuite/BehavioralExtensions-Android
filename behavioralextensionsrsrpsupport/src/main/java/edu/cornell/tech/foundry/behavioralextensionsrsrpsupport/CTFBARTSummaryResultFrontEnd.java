package edu.cornell.tech.foundry.behavioralextensionsrsrpsupport;

import android.support.annotation.Nullable;

import org.researchstack.backbone.result.StepResult;

import java.util.Map;
import java.util.UUID;

import edu.cornell.tech.foundry.behavioralextensionscore.BART.CTFBARTResult;
import edu.cornell.tech.foundry.researchsuiteresultprocessor.RSRPFrontEndServiceProvider.spi.RSRPFrontEnd;
import edu.cornell.tech.foundry.researchsuiteresultprocessor.RSRPIntermediateResult;

/**
 * Created by jameskizer on 2/7/17.
 */

public class CTFBARTSummaryResultFrontEnd implements RSRPFrontEnd {

    @Nullable
    @Override
    public RSRPIntermediateResult transform(
            String taskIdentifier,
            UUID taskRunUUID,
            Map<String,Object> parameters) {

        Object param = parameters.get("BARTResult");
        if (param == null || !(param instanceof StepResult)) {
            return null;
        }
        StepResult stepResult = (StepResult)param;

        Object result = stepResult.getResult();
        if(! (result instanceof CTFBARTResult)) {
            return null;
        }

        CTFBARTSummary summary = new CTFBARTSummary(
                UUID.randomUUID(),
                taskIdentifier,
                taskRunUUID,
                (CTFBARTResult)result
        );

        summary.setStartDate(((CTFBARTResult) result).getStartDate());
        summary.setEndDate(((CTFBARTResult) result).getEndDate());
        summary.setParameters(parameters);

        return summary;
    }

    @Override
    public boolean supportsType(String type) {
        if (type.equals("BARTSummary")) return true;
        else return false;
    }
}
