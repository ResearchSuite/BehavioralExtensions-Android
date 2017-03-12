package edu.cornell.tech.foundry.behavioralextensionsrsrpohmagebackend;

import android.content.Context;
import android.support.annotation.Nullable;

import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import edu.cornell.tech.foundry.behavioralextensionsrsrpsupport.CTFBARTSummary;
import edu.cornell.tech.foundry.omhclient.OMHAcquisitionProvenance;
import edu.cornell.tech.foundry.omhclient.OMHDataPointBuilder;
import edu.cornell.tech.foundry.omhclient.OMHSchema;

/**
 * Created by jameskizer on 2/7/17.
 */

public class CTFBARTSummaryOMHDatapoint extends OMHDataPointBuilder {
    private CTFBARTSummary summaryResult;
    private OMHAcquisitionProvenance acquisitionProvenance;

    public CTFBARTSummaryOMHDatapoint(Context context, CTFBARTSummary summaryResult) {
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
                "BARTSummary",
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
        map.put("variable_label", this.summaryResult.getVariableLabel());
        map.put("max_pumps_per_balloon", this.summaryResult.getMaxPumpsPerBalloon());

        map.put("mean_pumps_after_explode", this.summaryResult.getMeanPumpsAfterExplode());
        map.put("mean_pumps_after_no_explode", this.summaryResult.getMeanPumpsAfterNoExplode());

        map.put("number_of_balloons", this.summaryResult.getNumberOfBalloons());
        map.put("number_of_explosions", this.summaryResult.getNumberOfExplosions());

        map.put("pumps_mean", this.summaryResult.getPumpsMean());
        map.put("pumps_mean_first_third", this.summaryResult.getFirstThirdPumpsMean());
        map.put("pumps_mean_second_third", this.summaryResult.getMiddleThirdPumpsMean());
        map.put("pumps_mean_last_third", this.summaryResult.getLastThirdPumpsMean());

        map.put("pumps_range", this.summaryResult.getPumpsRange());
        map.put("pumps_range_first_third", this.summaryResult.getFirstThirdPumpsRange());
        map.put("pumps_range_second_third", this.summaryResult.getMiddleThirdPumpsRange());
        map.put("pumps_range_last_third", this.summaryResult.getLastThirdPumpsRange());

        map.put("pumps_standard_deviation", this.summaryResult.getPumpsStdDev());
        map.put("pumps_standard_deviation_first_third", this.summaryResult.getFirstThirdPumpsStdDev());
        map.put("pumps_standard_deviation_second_third", this.summaryResult.getMiddleThirdPumpsStdDev());
        map.put("pumps_standard_deviation_last_third", this.summaryResult.getLastThirdPumpsStdDev());

        map.put("researcher_code", this.summaryResult.getResearcherCode());
        map.put("total_gains", this.summaryResult.getTotalGains());

        return new JSONObject(map);
    }
}
