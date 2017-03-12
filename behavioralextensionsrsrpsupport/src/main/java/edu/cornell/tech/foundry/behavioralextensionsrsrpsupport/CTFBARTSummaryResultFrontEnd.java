package edu.cornell.tech.foundry.behavioralextensionsrsrpsupport;

import android.support.annotation.Nullable;

import org.researchstack.backbone.result.StepResult;

import java.util.Map;

import edu.cornell.tech.foundry.behavioralextensionscore.BART.CTFBARTResult;
import edu.cornell.tech.foundry.researchsuiteresultprocessor.RSRPFrontEndServiceProvider.spi.RSRPFrontEnd;
import edu.cornell.tech.foundry.researchsuiteresultprocessor.RSRPIntermediateResult;

/**
 * Created by jameskizer on 2/7/17.
 */

public class CTFBARTSummaryResultFrontEnd implements RSRPFrontEnd {
    @Nullable
    @Override
    public RSRPIntermediateResult transform(Map<String, StepResult> parameters) {
        StepResult stepResult = parameters.get("BARTResult");
        if (stepResult == null) {
            return null;
        }

        Object result = stepResult.getResult();
        if(! (result instanceof CTFBARTResult)) {
            return null;
        }

        CTFBARTSummary summary = new CTFBARTSummary((CTFBARTResult)result);

        summary.setStartDate(((CTFBARTResult) result).getStartDate());
        summary.setEndDate(((CTFBARTResult) result).getEndDate());

        return summary;
    }

    @Override
    public boolean supportsType(String type) {
        if (type.equals("BARTSummary")) return true;
        else return false;
    }
}
