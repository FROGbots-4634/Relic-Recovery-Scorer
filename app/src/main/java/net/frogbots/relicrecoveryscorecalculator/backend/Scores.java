package net.frogbots.relicrecoveryscorecalculator.backend;

import net.frogbots.relicrecoveryscorecalculator.backend.CalculateScores;

public class Scores
{
    /*
     * Number of tasks done
     * ---------------------------------------------------------------------------
     */

    /*
     * Autonomous
     */
    private int autonomousJewelLevel = 0;
    private int autonomousPreloadedGlyphLevel = 0;
    private int autonomousGlyphsScored = 0;
    private int autonomousParkingLevel = 0;

    /*
     * Teleop
     */
    private int teleopGlyphsScored = 0;
    private int teleopCryptoboxRowsComplete = 0;
    private int teleopCryptoboxColumnsComplete = 0;
    private int teleopCipherLevel = 0;

    /*
     * Endgame
     */
    private int endgameRelicPosition = 0;
    private int endgameRelicOrientation = 0;
    private int endgameRobotBalanced = 0;

    /*
     * Penalty
     */
    private int penaltiesMinor = 0;
    private int penaltiesMajor = 0;

    //----------------------------------------------------------------------------

    /*
     * Autonomous getters & setters
     */

    public void setAutonomousJewelLevel(int level)
    {
        autonomousJewelLevel = level;
    }

    public int getAutonomousJewelLevel()
    {
        return autonomousJewelLevel;
    }

    public void setAutonomousPreloadedGlyphLevel(int level)
    {
        autonomousPreloadedGlyphLevel = level;
    }

    public int getAutonomousPreloadedGlyphLevel()
    {
        return autonomousPreloadedGlyphLevel;
    }

    public void positiveIncrementAutonomousGlyphsScored()
    {
        autonomousGlyphsScored += 1;
    }

    public void negativeIncrementAutonomousGlyphsScored()
    {
        autonomousGlyphsScored -= 1;
    }

    public int getAutonomousGlyphsScored()
    {
        return autonomousGlyphsScored;
    }

    public void setParkingLevel(int parkingLevel)
    {
        this.autonomousParkingLevel = parkingLevel;
    }

    public int getParkingLevel()
    {
        return autonomousParkingLevel;
    }

    /*
     * Teleop getters & setters
     */

    public int getTeleOpGlyphsScored()
    {
        return teleopGlyphsScored;
    }

    public void positiveIncrementTeleopGlyphsScored()
    {
        teleopGlyphsScored += 1;
    }
    public void negativeIncrementTeleopGlyphsScored()
    {
        teleopGlyphsScored -= 1;
    }

    public void positiveIncrementTeleopCryptoboxRowsComplete()
    {
        teleopCryptoboxRowsComplete += 1;
    }

    public void negativeIncrementTeleopCryptoboxRowsComplete()
    {
        teleopCryptoboxRowsComplete -= 1;
    }

    public int getTeleopCryptoboxRowsComplete()
    {
        return teleopCryptoboxRowsComplete;
    }

    public void positiveIncrementTeleopCryptoboxColumnsComplete()
    {
        teleopCryptoboxColumnsComplete += 1;
    }

    public void negativeIncrementTeleopCryptoboxColumnsComplete()
    {
        teleopCryptoboxColumnsComplete -= 1;
    }

    public int getTeleopCryptoboxColumnsComplete()
    {
        return teleopCryptoboxColumnsComplete;
    }

    public void setTeleopCipherLevel(int i)
    {
        teleopCipherLevel = i;
    }

    public int getTeleopCipherLevel()
    {
        return teleopCipherLevel;
    }

    /*
     * Endgame getters & setters
     */

    public void setEndgameRelicPosition(int i)
    {
        endgameRelicPosition = i;
    }

    public int getEndgameRelicPosition()
    {
        return endgameRelicPosition;
    }

    public void setEndgameRelicOrientation(int i)
    {
        endgameRelicOrientation = i;
    }

    public int getEndgameRelicOrientation()
    {
        return endgameRelicOrientation;
    }

    public void setEndgameRobotBalanced(int i)
    {
        endgameRobotBalanced = i;
    }

    public int getEndgameRobotBalanced()
    {
        return endgameRobotBalanced;
    }

    /*
     * Penalty getters & setters
     */

    public void positiveIncrementPenaltiesMinor()
    {
        penaltiesMinor += 1;
    }

    public void negativeIncrementPenaltiesMinor()
    {
        penaltiesMinor -= 1;
    }

    public int getNumMinorPenalties()
    {
        return penaltiesMinor;
    }

    public void positiveIncrementPenaltiesMajor()
    {
        penaltiesMajor += 1;
    }

    public void negativeIncrementPenaltiesMajor()
    {
        penaltiesMajor -= 1;
    }

    public int getNumMajorPenalties()
    {
        return penaltiesMajor;
    }

    public int getTotalScore()
    {
        /*
         * Autonomous
         */
        int autoJewelScore = CalculateScores.calculateAutonomousJewelScore(autonomousJewelLevel);
        int autoPreloadedGlyphScore = CalculateScores.calculateAutonomousPreLoadedGlyphScore(autonomousPreloadedGlyphLevel);
        int autoGlyphsScore = CalculateScores.calculateAutonomousGlyphsScore(autonomousGlyphsScored);
        int autoParkingScore = CalculateScores.calculateAutonomousParkingScore(autonomousParkingLevel);

        /*
         * Teleop
         */
        int teleopGlyphsScore = CalculateScores.calculateTeleopGlyphsScore(teleopGlyphsScored);
        int teleopCryptoboxRowsCompleteScore = CalculateScores.calculateTeleopCryptoboxRowsCompletedScore(teleopCryptoboxRowsComplete);
        int teleopCryptoboxColumnsCompleteScore = CalculateScores.calculateTeleopCryptoboxColumnsCompleteScore(teleopCryptoboxColumnsComplete);
        int teleopCipherScore = CalculateScores.calculateTeleopCompletedCipherScore(teleopCipherLevel);

        /*
         * Endgame
         */
        int endgameRelicPositionScore = CalculateScores.calculateEndgameRelicPositionScore(endgameRelicPosition);
        int endgameRelicOrientationScore = 0;
        if(endgameRelicPositionScore != 0)
        {
            endgameRelicOrientationScore = CalculateScores.calculateEndgameRelicOrientationScore(endgameRelicOrientation);
        }
        int endgameRobotBalancedScore = CalculateScores.calculateEndgameRobotBalancedScore(endgameRobotBalanced);

        /*
         * Penalties
         */
        int penaltiesMinorScore = CalculateScores.calculatePenaltyMinorScore(penaltiesMinor);
        int penaltiesMajorScore = CalculateScores.calculatePenaltyMajorScore(penaltiesMajor);

        return autoJewelScore
                + autoPreloadedGlyphScore
                + autoGlyphsScore
                + autoParkingScore
                + teleopGlyphsScore
                + teleopCryptoboxRowsCompleteScore
                + teleopCryptoboxColumnsCompleteScore
                + teleopCipherScore
                + endgameRelicPositionScore
                + endgameRelicOrientationScore
                + endgameRobotBalancedScore
                + penaltiesMinorScore
                + penaltiesMajorScore;
    }

    public void clearAllScores()
    {
        /*
         * Autonomous
         */
        autonomousJewelLevel = 0;
        autonomousPreloadedGlyphLevel = 0;
        autonomousGlyphsScored = 0;
        autonomousParkingLevel = 0;

        /*
         * Teleop
         */
        teleopGlyphsScored = 0;
        teleopCryptoboxRowsComplete = 0;
        teleopCryptoboxColumnsComplete = 0;
        teleopCipherLevel = 0;

        /*
         * Endgame
         */
        endgameRelicPosition = 0;
        endgameRelicOrientation = 0;
        endgameRobotBalanced = 0;

        /*
         * Penalties
         */
        penaltiesMinor = 0;
        penaltiesMajor = 0;
    }
}
