package edu.cornell.tech.foundry.behavioralextensionscore.GoNoGo;

import android.content.Context;
import android.os.Handler;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.researchstack.backbone.result.StepResult;
import org.researchstack.backbone.step.Step;
import org.researchstack.backbone.ui.callbacks.StepCallbacks;
import org.researchstack.backbone.ui.step.layout.StepLayout;
import org.researchstack.backbone.ui.views.SubmitBar;
//import org.smalldatala


import java.util.Random;

import android.support.v4.content.ContextCompat;

import edu.cornell.tech.foundry.behavioralextensionscore.BXHelpers;
import edu.cornell.tech.foundry.behavioralextensionscore.R;

/**
 * Created by jameskizer on 12/12/16.
 */

public class CTFGoNoGoStepLayout extends FrameLayout implements StepLayout {
    public static final String TAG = CTFGoNoGoStepLayout.class.getSimpleName();

    private enum CTFGoNoGoViewState {
        BLANK,
        CROSS,
        GO_CUE,
        NO_GO_CUE,
        GO_CUE_NO_GO_TARGET,
        NO_GO_CUE_NO_GO_TARGET,
        GO_CUE_GO_TARGET,
        NO_GO_CUE_GO_TARGET
    };

    private StepResult   stepResult;
    private CTFGoNoGoStep step;

    private StepCallbacks callbacks;
    private CTFGoNoGoTrial[] trials;
    private CTFGoNoGoTrialResult[] trialResults;

    private Integer pendingTrialIndex;
    private CTFGoNoGoTrialResult[] inProgressTrialResults;
    private Boolean hasStarted = false;

    // Layout
    private View horizontalView;
    private View verticalView;
    private View plusSign;
    private RelativeLayout mainLayout;
    private TextView feedbackLabel;

    private long tapTime;

    private Boolean stopped;
    private Boolean getStopped() {
        return stopped;
    }

    private interface DoTrialCompletion {
        void completion(CTFGoNoGoTrialResult result);
    }

    private interface PerformTrialsCompletion {
        void completion(CTFGoNoGoTrialResult[] results);
    }

    //Getters and Setters
    public Step getStep()
    {
        return this.step;
    }

    //Constructors
    public CTFGoNoGoStepLayout(Context context)
    {
        super(context);
    }

    public CTFGoNoGoStepLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public CTFGoNoGoStepLayout(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void initialize(Step step, StepResult result)
    {
        if(! (step instanceof CTFGoNoGoStep))
        {
            throw new RuntimeException("Step being used in CTFGoNoGoStep is not a CTFGoNoGoStep");
        }

        this.step = (CTFGoNoGoStep) step;
        this.stepResult = result == null ? new StepResult<>(step) : result;

        this.initializeStep((CTFGoNoGoStep) step, result);
    }

    //Init Methods
    public void initializeStep(CTFGoNoGoStep step, final StepResult result)
    {
        this.trials = this.generateTrials(step.getStepParams());

        this.initStepLayout(step);
    }

    private void initStepLayout(CTFGoNoGoStep step) {

        // Init root
        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.ctf_go_no_go, this, true);

        SubmitBar submitBar = (SubmitBar) findViewById(R.id.rsb_submit_bar);
        submitBar.getPositiveActionView().setVisibility(View.GONE);
        if (step.isOptional()) {
            submitBar.setNegativeTitle(R.string.rsb_step_skip);
            submitBar.setNegativeAction(v -> onSkipClicked());
        }
        else {
            submitBar.getNegativeActionView().setVisibility(View.GONE);
        }

        this.horizontalView = findViewById(R.id.horizontal_game_view);
        this.verticalView = findViewById(R.id.vertical_game_view);

        this.plusSign = findViewById(R.id.plus_image);
        this.mainLayout = (RelativeLayout) findViewById(R.id.square_task_main_layout);
        this.feedbackLabel = (TextView) findViewById(R.id.feedback_label);


        this.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tappedSquare();
            }
        });

    }

    private void startTrials(StepResult result) {
        this.hasStarted = true;
        this.stopped = false;

        Integer trialIndex;
        CTFGoNoGoTrialResult[] trialResults;

        if (result != null
                && result.getResult() != null
                && ((CTFGoNoGoResult)result.getResult()).getInProgressTrialResults() != null
                && ((CTFGoNoGoResult)result.getResult()).getPendingTrialIndex() != null) {
            trialResults = ((CTFGoNoGoResult)result.getResult()).getInProgressTrialResults();
            trialIndex = ((CTFGoNoGoResult)result.getResult()).getPendingTrialIndex();
        }

        else {
            trialResults = new CTFGoNoGoTrialResult[this.trials.length];
            trialIndex = 0;
        }

        this.inProgressTrialResults = trialResults;
        this.trialResults = null;

        this.performTrials(trialIndex, CTFGoNoGoStepLayout.this.trials, trialResults, new PerformTrialsCompletion() {
            public void completion(CTFGoNoGoTrialResult[] results) {
                if (!getStopped()) {
                    CTFGoNoGoStepLayout.this.inProgressTrialResults = null;
                    CTFGoNoGoStepLayout.this.pendingTrialIndex = null;
                    CTFGoNoGoStepLayout.this.trialResults = results;
                    CTFGoNoGoStepLayout.this.onNextClicked();
                }
            }
        });

    }

    private void performTrials(int index, CTFGoNoGoTrial[] trials, CTFGoNoGoTrialResult[] results, PerformTrialsCompletion completion) {

        this.pendingTrialIndex = index;
        if (getStopped()) {
            completion.completion(null);
            return;
        }

        if (index == trials.length) {
            completion.completion(results);
            return;
        }

        else {

            CTFGoNoGoTrial trial = trials[index];

            this.doTrial(trial, new DoTrialCompletion() {
                @Override
                public void completion(CTFGoNoGoTrialResult result) {
                    if (result == null) {
                        return;
                    }
                    inProgressTrialResults[index] = result;
                    results[index] = result;
                    CTFGoNoGoStepLayout.this.performTrials(index + 1, trials, results, completion);
                }
            });

        }
    }

    private void doTrial(CTFGoNoGoTrial trial, DoTrialCompletion completion) {

        //1) set view state to blank
        CTFGoNoGoStepLayout.this.setViewState(CTFGoNoGoViewState.BLANK);
        new Handler().postDelayed( new Runnable() {
            @Override
            public void run() {

                if (getStopped()) {
                    completion.completion(null);
                    return;
                }

                //2) set view state to cross
                CTFGoNoGoStepLayout.this.setViewState(CTFGoNoGoViewState.CROSS);

                new Handler().postDelayed( new Runnable() {
                    @Override
                    public void run() {

                        if (getStopped()) {
                            completion.completion(null);
                            return;
                        }

                        //1) set view state to blank
                        CTFGoNoGoStepLayout.this.setViewState(CTFGoNoGoViewState.BLANK);

                        new Handler().postDelayed( new Runnable() {
                            @Override
                            public void run() {

                                if (getStopped()) {
                                    completion.completion(null);
                                    return;
                                }

                                //3 set cue
                                if (trial.getCue() == CTFGoNoGoTrial.CTFGoNoGoCueType.GO) {
                                    CTFGoNoGoStepLayout.this.setViewState(CTFGoNoGoViewState.GO_CUE);
                                }
                                else {
                                    CTFGoNoGoStepLayout.this.setViewState(CTFGoNoGoViewState.NO_GO_CUE);
                                }

                                new Handler().postDelayed( new Runnable() {
                                    @Override
                                    public void run() {

                                        if (getStopped()) {
                                            completion.completion(null);
                                            return;
                                        }


                                        //4 set target, start counter
                                        if (trial.getCue() == CTFGoNoGoTrial.CTFGoNoGoCueType.GO) {
                                            if (trial.getTarget() == CTFGoNoGoTrial.CTFGoNoGoTargetType.GO) {
                                                CTFGoNoGoStepLayout.this.setViewState(CTFGoNoGoViewState.GO_CUE_GO_TARGET);
                                            }
                                            else {
                                                CTFGoNoGoStepLayout.this.setViewState(CTFGoNoGoViewState.GO_CUE_NO_GO_TARGET);
                                            }
                                        }
                                        else {
                                            if (trial.getTarget() == CTFGoNoGoTrial.CTFGoNoGoTargetType.GO) {
                                                CTFGoNoGoStepLayout.this.setViewState(CTFGoNoGoViewState.NO_GO_CUE_GO_TARGET);
                                            }
                                            else {
                                                CTFGoNoGoStepLayout.this.setViewState(CTFGoNoGoViewState.NO_GO_CUE_NO_GO_TARGET);
                                            }
                                        }

                                        long startTime = SystemClock.elapsedRealtime();
                                        CTFGoNoGoStepLayout.this.tapTime = 0;

                                        new Handler().postDelayed( new Runnable() {
                                            @Override
                                            public void run() {

                                                if (getStopped()) {
                                                    completion.completion(null);
                                                    return;
                                                }


                                                //5 delay until trial over, process results, call completion handler
                                                boolean tapped = CTFGoNoGoStepLayout.this.tapTime != 0;
                                                long responseTime = tapped ? CTFGoNoGoStepLayout.this.tapTime - startTime : trial.getFillTime();


                                                CTFGoNoGoStepLayout.this.setViewState(CTFGoNoGoViewState.BLANK);


                                                if ( tapped ) {
                                                    if(trial.getTarget() == CTFGoNoGoTrial.CTFGoNoGoTargetType.GO) {
                                                        feedbackLabel.setText("Correct! "+responseTime+" ms");
                                                        feedbackLabel.setTextColor(ContextCompat.getColor(getContext(), R.color.validColor));
                                                    }
                                                    else {
                                                        feedbackLabel.setText("Incorrect!");
                                                        feedbackLabel.setTextColor(ContextCompat.getColor(getContext(), R.color.invalidColor));
                                                    }

                                                    feedbackLabel.animate().alpha(1.f);

                                                    new Handler().postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            feedbackLabel.animate().setDuration(100).alpha(0.f);
                                                        }
                                                    }, 600);
                                                }

                                                new Handler().postDelayed( new Runnable() {
                                                    @Override
                                                    public void run() {

                                                        if (getStopped()) {
                                                            completion.completion(null);
                                                            return;
                                                        }


                                                        CTFGoNoGoTrialResult result = new CTFGoNoGoTrialResult(trial, responseTime / 1000.0, tapped);
                                                        completion.completion(result);


                                                    }
                                                }, trial.getWaitTime());

                                            }
                                        }, trial.getFillTime());

                                    }
                                }, trial.getCueTime());

                            }
                        }, trial.getBlankTime());

                    }
                }, trial.getCrossTime());

            }
        }, trial.getWaitTime());

    }

    private void setViewState(CTFGoNoGoViewState state) {
        switch(state) {
            case BLANK:
                this.horizontalView.setAlpha(0.0f);
                this.verticalView.setAlpha(0.0f);

                this.plusSign.setAlpha(0.0f);

                this.mainLayout.setClickable(false);

                break;

            case CROSS:
                this.horizontalView.setAlpha(0.0f);
                this.verticalView.setAlpha(0.0f);

                this.plusSign.setAlpha(1.0f);

                this.mainLayout.setClickable(false);

                break;

            case GO_CUE:

                this.horizontalView.setAlpha(1.0f);
                this.horizontalView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.no_color_background));

                this.verticalView.setAlpha(0.0f);

                this.plusSign.setAlpha(0.0f);

                this.mainLayout.setClickable(false);
                break;


            case NO_GO_CUE:

                this.horizontalView.setAlpha(0.0f);

                this.verticalView.setAlpha(1.0f);
                this.verticalView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.no_color_background));


                this.plusSign.setAlpha(0.0f);

                this.mainLayout.setClickable(false);
                break;

            case GO_CUE_NO_GO_TARGET:

                this.horizontalView.setAlpha(1.0f);
                this.horizontalView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.invalid_color_background));

                this.verticalView.setAlpha(0.0f);

                this.plusSign.setAlpha(0.0f);

                this.mainLayout.setClickable(true);
                break;


            case NO_GO_CUE_NO_GO_TARGET:

                this.horizontalView.setAlpha(0.0f);

                this.verticalView.setAlpha(1.0f);
                this.verticalView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.invalid_color_background));

                this.plusSign.setAlpha(0.0f);

                this.mainLayout.setClickable(true);
                break;

            case GO_CUE_GO_TARGET:

                this.horizontalView.setAlpha(1.0f);
                this.horizontalView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.valid_color_background));

                this.verticalView.setAlpha(0.0f);

                this.plusSign.setAlpha(0.0f);

                this.mainLayout.setClickable(true);
                break;

            case NO_GO_CUE_GO_TARGET:

                this.horizontalView.setAlpha(0.0f);

                this.verticalView.setAlpha(1.0f);
                this.verticalView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.valid_color_background));

                this.plusSign.setAlpha(0.0f);

                this.mainLayout.setClickable(true);
                break;

            default:

                this.horizontalView.setAlpha(0.0f);
                this.verticalView.setAlpha(0.0f);

                this.plusSign.setAlpha(0.0f);

                this.mainLayout.setClickable(false);

                break;

        }
    }

    private void tappedSquare() {
        if (this.tapTime == 0) {
            this.tapTime = SystemClock.elapsedRealtime();
        }
    }

    private CTFGoNoGoTrial[] generateTrials(CTFGoNoGoStepParameters params) {

        CTFGoNoGoTrial[] trials = new CTFGoNoGoTrial[params.getNumberOfTrials()];
        Random rand = new Random();
        int[] cueTimes = params.getCueTimeOptions();

        for(int i = 0; i < params.getNumberOfTrials(); i++) {
            int cueTime = cueTimes[rand.nextInt(cueTimes.length)];

            CTFGoNoGoTrial.CTFGoNoGoCueType cueType = BXHelpers.coinFlip(
                    CTFGoNoGoTrial.CTFGoNoGoCueType.GO,
                    CTFGoNoGoTrial.CTFGoNoGoCueType.NOGO,
                    0.5);

            double goCueGoTargetProbability = params.getGoCueTargetProb() != 0.0 ? params.getGoCueTargetProb() : 0.7;
            double noGoCueGoTargetProbability = 1.0 - (params.getNoGoCueTargetProb() != 0 ? params.getNoGoCueTargetProb() : 0.7);
            CTFGoNoGoTrial.CTFGoNoGoTargetType targetType = BXHelpers.coinFlip(
                    CTFGoNoGoTrial.CTFGoNoGoTargetType.GO,
                    CTFGoNoGoTrial.CTFGoNoGoTargetType.NOGO,
                    (cueType == CTFGoNoGoTrial.CTFGoNoGoCueType.GO) ? goCueGoTargetProbability : noGoCueGoTargetProbability
            );

            trials[i] = new CTFGoNoGoTrial(
                    params.getWaitTime(),
                    params.getCrossTime(),
                    params.getBlankTime(),
                    cueTime,
                    params.getFillTime(),
                    cueType,
                    targetType,
                    i
            );
        }
        return trials;

    }

    @Override
    public View getLayout() {
        return this;
    }

    //handle resume, see https://github.com/ResearchStack/ResearchStack/issues/377
    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        if  ((!this.hasStarted || this.stopped) && visibility == VISIBLE) {
            this.startTrials(this.stepResult);
        }

        super.onVisibilityChanged(changedView, visibility);
    }



    @Override
    public boolean isBackEventConsumed()
    {
        this.stopped = true;
        callbacks.onSaveStep(StepCallbacks.ACTION_PREV, this.getStep(), this.getStepResult(true));
        return false;
    }

    @Override
    public void setCallbacks(StepCallbacks callbacks)
    {
        this.callbacks = callbacks;
    }


    @Override
    public Parcelable onSaveInstanceState()
    {
        this.stopped = true;
        callbacks.onSaveStep(StepCallbacks.ACTION_NONE, getStep(), this.getStepResult(false));
        return super.onSaveInstanceState();
    }

    protected void onNextClicked()
    {
        this.stopped = true;
        callbacks.onSaveStep(StepCallbacks.ACTION_NEXT,
                this.getStep(),
                this.getStepResult(false));

    }

    public void onSkipClicked()
    {
        this.stopped = true;
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
            CTFGoNoGoResult result = new CTFGoNoGoResult(step.getIdentifier());
            result.setStartDate(stepResult.getStartDate());
            result.setEndDate(stepResult.getEndDate());
            if (this.trialResults != null) {
                result.setTrialResults(this.trialResults);
                result.setPendingTrialIndex(null);
                result.setInProgressTrialResults(null);
            }
            else {
                result.setTrialResults(null);
                result.setPendingTrialIndex(this.pendingTrialIndex);
                result.setInProgressTrialResults(this.inProgressTrialResults);
            }
            stepResult.setResult(result);
        }

        return stepResult;
    }




}
