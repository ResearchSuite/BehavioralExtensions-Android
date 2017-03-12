package edu.cornell.tech.foundry.behavioralextensionsrsrpsupport;

import android.support.annotation.Nullable;

import org.researchstack.backbone.result.StepResult;

import java.util.Map;

import edu.cornell.tech.foundry.behavioralextensionscore.GoNoGo.CTFGoNoGoResult;
import edu.cornell.tech.foundry.researchsuiteresultprocessor.RSRPFrontEndServiceProvider.spi.RSRPFrontEnd;
import edu.cornell.tech.foundry.researchsuiteresultprocessor.RSRPIntermediateResult;

/**
 * Created by jameskizer on 2/6/17.
 */

public class CTFGoNoGoSummaryResultFrontEnd implements RSRPFrontEnd {
    @Nullable
    @Override
    public RSRPIntermediateResult transform(Map<String, StepResult> parameters) {
        StepResult stepResult = parameters.get("GoNoGoResult");
        if (stepResult == null) {
            return null;
        }

        Object result = stepResult.getResult();
        if(! (result instanceof CTFGoNoGoResult)) {
            return null;
        }

        CTFGoNoGoSummary summary = new CTFGoNoGoSummary((CTFGoNoGoResult)result);

        summary.setStartDate(((CTFGoNoGoResult) result).getStartDate());
        summary.setEndDate(((CTFGoNoGoResult) result).getEndDate());

        return summary;
    }

    @Override
    public boolean supportsType(String type) {
        if (type.equals("GoNoGoSummary")) return true;
        else return false;
    }
}
