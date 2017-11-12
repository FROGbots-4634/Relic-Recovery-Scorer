package net.frogbots.relicrecoveryscorecalculator;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressLint("SetTextI18n")
class Summary
{
    private RelativeLayout relativeLayout;

    private TextView summaryJewelScoreValue;
    private TextView summaryJewelScore;

    private TextView summaryPreloadedGlyphScore;
    private TextView summaryPreloadedGlyphScoreValue;

    private TextView summaryAutoGlyphsScore;
    private TextView summaryAutoGlyphsScoreValue;

    private TextView summaryAutoRobotParkedScore;
    private TextView summaryAutoRobotParkedScoreValue;

    private TextView summaryTeleopGlyphsScore;
    private TextView summaryTeleopGlyphsScoreValue;

    private TextView summaryCryptoboxRowsCompleteBonusScore;
    private TextView summaryCryptoboxRowsCompleteBonusScoreValue;

    private TextView summaryCryptoboxColumnsCompleteBonusScore;
    private TextView summaryCryptoboxColumnsCompleteBonusScoreValue;

    private TextView summaryCompletedCipherBonusScore;
    private TextView summaryCompletedCipherBonusScoreValue;

    private TextView summaryRelicPositionScore;
    private TextView summaryRelicPositionScoreValue;

    private TextView summaryRelicOrientationScore;
    private TextView summaryRelicOrientationScoreValue;

    private TextView summaryRobotBalancedScore;
    private TextView summaryRobotBalancedScoreValue;

    private TextView summaryMinorPenaltiesScore;
    private TextView summaryMinorPenaltiesScoreValue;

    private TextView summaryMajorPenaltiesScore;
    private TextView summaryMajorPenaltiesScoreValue;

    Summary(RelativeLayout relativeLayout)
    {
        this.relativeLayout = relativeLayout;

        summaryJewelScoreValue =                         (TextView) relativeLayout.findViewById(R.id.summary_item_jewel_value_txtView);
        summaryJewelScore =                              (TextView) relativeLayout.findViewById(R.id.summary_item_jewel_txtView);

        summaryPreloadedGlyphScoreValue =                (TextView) relativeLayout.findViewById(R.id.summary_item_preloadedGlyph_value_txtView);
        summaryPreloadedGlyphScore =                     (TextView) relativeLayout.findViewById(R.id.summary_item_preloadedGlyph_txtView);

        summaryAutoGlyphsScoreValue =                    (TextView) relativeLayout.findViewById(R.id.summary_item_autonomousGlyphs_value_txtView);
        summaryAutoGlyphsScore =                         (TextView) relativeLayout.findViewById(R.id.summary_item_autonomousGlyphs_txtView);

        summaryAutoRobotParkedScore =                    (TextView) relativeLayout.findViewById(R.id.summary_item_autonomousPark_txtView);
        summaryAutoRobotParkedScoreValue =               (TextView) relativeLayout.findViewById(R.id.summary_item_autonomousPark_value_txtView);

        summaryTeleopGlyphsScore =                       (TextView) relativeLayout.findViewById(R.id.summary_item_teleopGlyphs_txtView);
        summaryTeleopGlyphsScoreValue =                  (TextView) relativeLayout.findViewById(R.id.summary_item_teleopGlyphs_value_txtView);

        summaryCryptoboxRowsCompleteBonusScore =         (TextView) relativeLayout.findViewById(R.id.summary_item_rowsComplete_txtView);
        summaryCryptoboxRowsCompleteBonusScoreValue =    (TextView) relativeLayout.findViewById(R.id.summary_item_rowsComplete_score_txtView);

        summaryCryptoboxColumnsCompleteBonusScore =      (TextView) relativeLayout.findViewById(R.id.summary_item_columnsComplete_txtView);
        summaryCryptoboxColumnsCompleteBonusScoreValue = (TextView) relativeLayout.findViewById(R.id.summary_item_columnsComplete_score_txtView);

        summaryCompletedCipherBonusScore =               (TextView) relativeLayout.findViewById(R.id.summary_item_cipherComplete_txtView);
        summaryCompletedCipherBonusScoreValue =          (TextView) relativeLayout.findViewById(R.id.summary_item_cipherComplete_score_txtView);

        summaryRelicPositionScore =                      (TextView) relativeLayout.findViewById(R.id.summary_item_relicPosition_txtView);
        summaryRelicPositionScoreValue =                 (TextView) relativeLayout.findViewById(R.id.summary_item_relicPosition_score_txtView);

        summaryRelicOrientationScore =                   (TextView) relativeLayout.findViewById(R.id.summary_item_relicOrientation_txtView);
        summaryRelicOrientationScoreValue =              (TextView) relativeLayout.findViewById(R.id.summary_item_relicOrientation_score_txtView);

        summaryRobotBalancedScore =                      (TextView) relativeLayout.findViewById(R.id.summary_item_teleopRobotBalanced_txtView);
        summaryRobotBalancedScoreValue =                 (TextView) relativeLayout.findViewById(R.id.summary_item_teleopRobotBalanced_score_txtView);

        summaryMinorPenaltiesScore =                     (TextView) relativeLayout.findViewById(R.id.summary_item_penaltiesMinor_txtView);
        summaryMinorPenaltiesScoreValue =                (TextView) relativeLayout.findViewById(R.id.summary_item_penaltiesMinor_score_txtView);

        summaryMajorPenaltiesScore =                     (TextView) relativeLayout.findViewById(R.id.summary_item_penaltiesMajor_txtView);
        summaryMajorPenaltiesScoreValue =                (TextView) relativeLayout.findViewById(R.id.summary_item_penaltiesMajor_score_txtView);
    }

    void updateSummary(Scores scores)
    {
        setVisibilityAndText(summaryJewelScore,                         summaryJewelScoreValue,                         CalculateScores.calculateAutonomousJewelScore(                scores.getAutonomousJewelLevel()            ));
        setVisibilityAndText(summaryPreloadedGlyphScore,                summaryPreloadedGlyphScoreValue,                CalculateScores.calculateAutonomousPreLoadedGlyphScore(       scores.getAutonomousPreloadedGlyphLevel()   ));
        setVisibilityAndText(summaryAutoGlyphsScore,                    summaryAutoGlyphsScoreValue,                    CalculateScores.calculateAutonomousGlyphsScore(               scores.getAutonomousGlyphsScored()          ));
        setVisibilityAndText(summaryAutoRobotParkedScore,               summaryAutoRobotParkedScoreValue,               CalculateScores.calculateAutonomousParkingScore(              scores.getParkingLevel()                    ));
        setVisibilityAndText(summaryTeleopGlyphsScore,                  summaryTeleopGlyphsScoreValue,                  CalculateScores.calculateTeleopGlyphsScore(                   scores.getTeleOpGlyphsScored()              ));
        setVisibilityAndText(summaryCryptoboxRowsCompleteBonusScore,    summaryCryptoboxRowsCompleteBonusScoreValue,    CalculateScores.calculateTeleopCryptoboxRowsCompletedScore(   scores.getTeleopCryptoboxRowsComplete()     ));
        setVisibilityAndText(summaryCryptoboxColumnsCompleteBonusScore, summaryCryptoboxColumnsCompleteBonusScoreValue, CalculateScores.calculateTeleopCryptoboxColumnsCompleteScore( scores.getTeleopCryptoboxColumnsComplete()  ));
        setVisibilityAndText(summaryCompletedCipherBonusScore,          summaryCompletedCipherBonusScoreValue,          CalculateScores.calculateTeleopCompletedCipherScore(          scores.getTeleopCipherLevel()               ));
        setVisibilityAndText(summaryRelicPositionScore,                 summaryRelicPositionScoreValue,                 CalculateScores.calculateEndgameRelicPositionScore(           scores.getEndgameRelicPosition()            ));
        setVisibilityAndText(summaryRelicOrientationScore,              summaryRelicOrientationScoreValue,              CalculateScores.calculateEndgameRelicOrientationScore(        scores.getEndgameRelicOrientation()         ));
        setVisibilityAndText(summaryRobotBalancedScore,                 summaryRobotBalancedScoreValue,                 CalculateScores.calculateEndgameRobotBalancedScore(           scores.getEndgameRobotBalanced()            ));
        setVisibilityAndText(summaryMinorPenaltiesScore,                summaryMinorPenaltiesScoreValue,                CalculateScores.calculatePenaltyMinorScore(                   scores.getNumMinorPenalties()               ));
        setVisibilityAndText(summaryMajorPenaltiesScore,                summaryMajorPenaltiesScoreValue,                CalculateScores.calculatePenaltyMajorScore(                   scores.getNumMajorPenalties()               ));
        nothingToSee();
    }

    private void nothingToSee()
    {
        TextView textView = (TextView) relativeLayout.findViewById(R.id.summary_item_notin_to_see);

        if(nothingIsInView())
        {
            textView.setVisibility(View.VISIBLE);
        }
        else
        {
            textView.setVisibility(View.GONE);
        }
    }

    private boolean nothingIsInView()
    {
        LinearLayout summaryItems = (LinearLayout) relativeLayout.findViewById(R.id.summary_items_linear_layout);
        for (int i = 1; i < summaryItems.getChildCount(); i++)
        {
            if((summaryItems.getChildAt(i).getVisibility() != View.GONE))
            {
                return false;
            }
        }
        return true;
    }

    private void setVisibilityAndText(TextView item, TextView value, int i)
    {
        if(i != 0)
        {
            if(value.getVisibility() == View.GONE)
            {
                value.setVisibility(View.VISIBLE);
                item.setVisibility(View.VISIBLE);
            }
            value.setText(Integer.toString(i));
        }
        else
        {
            if(value.getVisibility() == View.VISIBLE)
            {
                value.setVisibility(View.GONE);
                item.setVisibility(View.GONE);
            }
        }
    }
}
