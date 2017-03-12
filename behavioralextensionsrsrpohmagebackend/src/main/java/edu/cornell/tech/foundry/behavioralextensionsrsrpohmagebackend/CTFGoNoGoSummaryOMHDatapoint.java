package edu.cornell.tech.foundry.behavioralextensionsrsrpohmagebackend;

import android.content.Context;
import android.support.annotation.Nullable;

import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import edu.cornell.tech.foundry.behavioralextensionsrsrpsupport.CTFGoNoGoSummary;
import edu.cornell.tech.foundry.omhclient.OMHAcquisitionProvenance;
import edu.cornell.tech.foundry.omhclient.OMHDataPointBuilder;
import edu.cornell.tech.foundry.omhclient.OMHSchema;


/**
 * Created by jameskizer on 2/6/17.
 */

public class CTFGoNoGoSummaryOMHDatapoint extends OMHDataPointBuilder {

    private CTFGoNoGoSummary summaryResult;
    private OMHAcquisitionProvenance acquisitionProvenance;

    public CTFGoNoGoSummaryOMHDatapoint(Context context, CTFGoNoGoSummary summaryResult) {
        this.summaryResult = summaryResult;
        this.acquisitionProvenance = new OMHAcquisitionProvenance(
                context.getPackageName(),
                summaryResult.getStartDate(),
                OMHAcquisitionProvenance.OMHAcquisitionProvenanceModality.SENSED
        );
    }

    @Override
    public String getDataPointID() {
        return this.summaryResult.getUuid().toString();
    }

    @Override
    public Date getCreationDateTime() {
        return this.summaryResult.getStartDate();
    }

    @Override
    public OMHSchema getSchema() {
        return new OMHSchema(
                "GoNoGoSummary",
                "Cornell",
                "1.0"
        );
    }

    @Nullable
    @Override
    public OMHAcquisitionProvenance getAcquisitionProvenance() {
        return this.acquisitionProvenance;
    }

    @Override
    public JSONObject getBody() {

        Map<String, Object> map = new HashMap<>();
        map.put("total", this.summaryResult.getTotalSummary().toJson());
        map.put("firstThird", this.summaryResult.getFirstThirdSummary().toJson());
        map.put("middleThird", this.summaryResult.getMiddleThirdSummary().toJson());
        map.put("lastThird", this.summaryResult.getLastThirdSummary().toJson());

        return new JSONObject(map);
    }

}
