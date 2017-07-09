package edu.cornell.tech.foundry.behavioralextensionscore.Discounting;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.researchstack.backbone.result.StepResult;
import org.researchstack.backbone.step.Step;
import org.researchstack.backbone.ui.callbacks.StepCallbacks;
import org.researchstack.backbone.ui.step.layout.StepLayout;
import org.researchstack.backbone.ui.views.SubmitBar;

import edu.cornell.tech.foundry.behavioralextensionscore.R;


/**
 * Created by jameskizer on 1/3/17.
 */
public class CTFDiscountingStepLayout extends FrameLayout implements StepLayout {


    private interface DoTrialCompletion {
        void completion(CTFDiscountingTrialResult result);
    }

    private interface PerformTrialsCompletion {
        void completion(CTFDiscountingTrialResult[] results);
    }

    public static final String TAG = CTFDiscountingStepLayout.class.getSimpleName();

    private StepResult   stepResult;
    private CTFDiscountingStep step;

    private StepCallbacks callbacks;
    private CTFDiscountingTrialResult[] trialResults;

    private CTFDiscountingTrial pendingTrial;
    private CTFDiscountingTrialResult[] inProgressTrialResults;


    // UI
    private TextView promptTextView;
    private Button variableButton;
    private Button constantButton;

    //Getters and Setters
    public Step getStep()
    {
        return this.step;
    }

    //Constructors
    public CTFDiscountingStepLayout(Context context)
    {
        super(context);
    }

    public CTFDiscountingStepLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public CTFDiscountingStepLayout(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void initialize(Step step, StepResult result)
    {
        if(! (step instanceof CTFDiscountingStep))
        {
            throw new RuntimeException("Step being used in CTFGoNoGoStep is not a CTFGoNoGoStep");
        }

        this.step = (CTFDiscountingStep) step;
        this.stepResult = result == null ? new StepResult<>(step) : result;

        this.initializeStep((CTFDiscountingStep) step, result);
    }

    //Init Methods
    public void initializeStep(CTFDiscountingStep step, StepResult result)
    {
//        this.trials = this.generateTrials(step.getStepParams());

        this.initStepLayout(step);

        final CTFDiscountingStepLayout self = this;

        CTFDiscountingTrialResult[] trialResults;
        CTFDiscountingTrial firstTrial;

        if (result != null
                && result.getResult() != null
                && ((CTFDiscountingResult)result.getResult()).getInProgressTrialResults() != null
                && ((CTFDiscountingResult)result.getResult()).getPendingTrial() != null) {
            trialResults = ((CTFDiscountingResult)result.getResult()).getInProgressTrialResults();
            firstTrial = ((CTFDiscountingResult)result.getResult()).getPendingTrial();
        }

        else {
            trialResults = new CTFDiscountingTrialResult[step.getNumQuestions()];
            firstTrial = new CTFDiscountingTrial(
                    step.getInitialVariableAmount(),
                    step.getConstantAmount(),
                    0
            );
        }

        this.inProgressTrialResults = trialResults;

        this.trialResults = null;
        this.performTrials(firstTrial, trialResults, new PerformTrialsCompletion() {
            @Override
            public void completion(CTFDiscountingTrialResult[] results) {
                self.trialResults = results;
//                self.inProgressTrialResults = null;
//                self.pendingTrial = null;
                self.onNextClicked();
            }
        });
    }

    private void performTrials(CTFDiscountingTrial trial, final CTFDiscountingTrialResult[] results, final PerformTrialsCompletion completion) {
        //update ui
        //do trial

        final int index = trial.getQuestionNum();
        final CTFDiscountingStepLayout self = this;
        this.pendingTrial = trial;

        this.doTrial(trial, new DoTrialCompletion() {
            @Override
            public void completion(CTFDiscountingTrialResult result) {
                inProgressTrialResults[index] = result;
                results[index] = result;

                CTFDiscountingTrial nextTrial = self.nextTrial(index + 1, results);

                //check to see if we are done
                if (nextTrial.getQuestionNum() >= results.length) {
                    completion.completion(results);
                    return;
                }
                else {

                    self.performTrials(nextTrial, results, completion);
                }

            }
        });
    }

    private void completeTrial(CTFDiscountingTrialResult.Choice choice, CTFDiscountingTrial trial, long startTime, DoTrialCompletion completion) {

        long endTime = SystemClock.elapsedRealtime();
        long responseTimeMS = (endTime - startTime);
        double responseTimeSec = (double)responseTimeMS / 1000.0;

        double choiceValue = (choice == CTFDiscountingTrialResult.Choice.VARIABLE) ? trial.getVariableAmount() : trial.getConstantAmount();

        CTFDiscountingTrialResult trialResult = new CTFDiscountingTrialResult(
                trial,
                choice,
                choiceValue,
                responseTimeSec
        );

        completion.completion(trialResult);
    }

    private void doTrial(final CTFDiscountingTrial trial, final DoTrialCompletion completion) {

        final long startTime = SystemClock.elapsedRealtime();

        //set button ui and handlers
        String variableButtonText = String.format(step.getVariableFormatString(), trial.getVariableAmount());
        this.variableButton.setText(variableButtonText);
        this.variableButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                completeTrial(CTFDiscountingTrialResult.Choice.VARIABLE, trial, startTime, completion);
            }
        });

        String constantButtonText = String.format(step.getConstantFormatString(), trial.getConstantAmount());
        this.constantButton.setText(constantButtonText);
        this.constantButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                completeTrial(CTFDiscountingTrialResult.Choice.CONSTANT, trial, startTime, completion);
            }
        });

    }

    private CTFDiscountingTrial nextTrial(Integer id, CTFDiscountingTrialResult[] trialResults) {

        CTFDiscountingTrialResult lastTrialResult = trialResults[id-1];
        CTFDiscountingTrial lastTrial = lastTrialResult.getTrial();

        Double nextAmount = step.getComputeNextVariableAmount().computeNextVariableAmount(id, trialResults);

        return new CTFDiscountingTrial(
                nextAmount,
                lastTrial.getConstantAmount(),
                id
        );
    }


    private void initStepLayout(CTFDiscountingStep step) {

        // Init root
        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.ctf_discounting, this, true);

        SubmitBar submitBar = (SubmitBar) findViewById(R.id.rsb_submit_bar);
        submitBar.getPositiveActionView().setVisibility(View.GONE);
        if (step.isOptional()) {
            submitBar.setNegativeTitle(R.string.rsb_step_skip);
            submitBar.setNegativeAction(v -> onSkipClicked());
        }
        else {
            submitBar.getNegativeActionView().setVisibility(View.GONE);
        }

        this.promptTextView = (TextView) findViewById(R.id.prompt_textview);
        this.promptTextView.setText(step.getText());

        this.variableButton = (Button) findViewById(R.id.variable_button);
        this.constantButton = (Button) findViewById(R.id.constant_button);

    }

    @Override
    public View getLayout()
    {
        return this;
    }

    @Override
    public boolean isBackEventConsumed()
    {
        //clear progress on back touch
        callbacks.onSaveStep(StepCallbacks.ACTION_PREV, this.getStep(), this.getStepResult(true));
        return false;
    }

    @Override
    public void setCallbacks(StepCallbacks callbacks)
    {
        this.callbacks = callbacks;
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {

    }

    @Override
    public Parcelable onSaveInstanceState()
    {
        //don't clear on rotation
        callbacks.onSaveStep(StepCallbacks.ACTION_NONE, getStep(), this.getStepResult(false));
        return super.onSaveInstanceState();
    }

    protected void onNextClicked()
    {
        callbacks.onSaveStep(StepCallbacks.ACTION_NEXT,
                this.getStep(),
                this.getStepResult(false));

    }

    public void onSkipClicked()
    {
        if(callbacks != null)
        {
            // empty step result when skipped
            callbacks.onSaveStep(StepCallbacks.ACTION_NEXT,
                    this.getStep(),
                    this.getStepResult(true));
        }
    }


    public StepResult getStepResult(boolean shouldClear)
    {

        if(shouldClear)
        {
            stepResult.setResult(null);
        }
        else
        {
            CTFDiscountingResult result = new CTFDiscountingResult(step.getIdentifier());
            result.setStartDate(stepResult.getStartDate());
            result.setEndDate(stepResult.getEndDate());
            if (this.trialResults != null) {
                result.setTrialResults(this.trialResults);
                result.setInProgressTrialResults(null);
                result.setPendingTrial(null);
            }
            else {
                result.setTrialResults(null);
                result.setInProgressTrialResults(this.inProgressTrialResults);
                result.setPendingTrial(this.pendingTrial);
            }
            stepResult.setResult(result);
        }

        return stepResult;
    }
}
