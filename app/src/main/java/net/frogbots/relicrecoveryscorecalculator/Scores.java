package net.frogbots.relicrecoveryscorecalculator;

class Scores
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

    void setAutonomousJewelLevel(int level)
    {
        autonomousJewelLevel = level;
    }

    int getAutonomousJewelLevel()
    {
        return autonomousJewelLevel;
    }

    void setAutonomousPreloadedGlyphLevel(int level)
    {
        autonomousPreloadedGlyphLevel = level;
    }

    int getAutonomousPreloadedGlyphLevel()
    {
        return autonomousPreloadedGlyphLevel;
    }

    void positiveIncrementAutonomousGlyphsScored()
    {
        autonomousGlyphsScored += 1;
    }

    void negativeIncrementAutonomousGlyphsScored()
    {
        autonomousGlyphsScored -= 1;
    }

    int getAutonomousGlyphsScored()
    {
        return autonomousGlyphsScored;
    }

    void setParkingLevel(int parkingLevel)
    {
        this.autonomousParkingLevel = parkingLevel;
    }

    int getParkingLevel()
    {
        return autonomousParkingLevel;
    }

    /*
     * Teleop getters & setters
     */

    int getTeleOpGlyphsScored()
    {
        return teleopGlyphsScored;
    }

    void positiveIncrementTeleopGlyphsScored()
    {
        teleopGlyphsScored += 1;
    }
    void negativeIncrementTeleopGlyphsScored()
    {
        teleopGlyphsScored -= 1;
    }

    void positiveIncrementTeleopCryptoboxRowsComplete()
    {
        teleopCryptoboxRowsComplete += 1;
    }

    void negativeIncrementTeleopCryptoboxRowsComplete()
    {
        teleopCryptoboxRowsComplete -= 1;
    }

    int getTeleopCryptoboxRowsComplete()
    {
        return teleopCryptoboxRowsComplete;
    }

    void positiveIncrementTeleopCryptoboxColumnsComplete()
    {
        teleopCryptoboxColumnsComplete += 1;
    }

    void negativeIncrementTeleopCryptoboxColumnsComplete()
    {
        teleopCryptoboxColumnsComplete -= 1;
    }

    int getTeleopCryptoboxColumnsComplete()
    {
        return teleopCryptoboxColumnsComplete;
    }

    void setTeleopCipherLevel(int i)
    {
        teleopCipherLevel = i;
    }

    int getTeleopCipherLevel()
    {
        return teleopCipherLevel;
    }

    /*
     * Endgame getters & setters
     */

    void setEndgameRelicPosition(int i)
    {
        endgameRelicPosition = i;
    }

    int getEndgameRelicPosition()
    {
        return endgameRelicPosition;
    }

    void setEndgameRelicOrientation(int i)
    {
        endgameRelicOrientation = i;
    }

    int getEndgameRelicOrientation()
    {
        return endgameRelicOrientation;
    }

    void setEndgameRobotBalanced(int i)
    {
        endgameRobotBalanced = i;
    }

    int getEndgameRobotBalanced()
    {
        return endgameRobotBalanced;
    }

    /*
     * Penalty getters & setters
     */

    void positiveIncrementPenaltiesMinor()
    {
        penaltiesMinor += 1;
    }

    void negativeIncrementPenaltiesMinor()
    {
        penaltiesMinor -= 1;
    }

    int getNumMinorPenalties()
    {
        return penaltiesMinor;
    }

    void positiveIncrementPenaltiesMajor()
    {
        penaltiesMajor += 1;
    }

    void negativeIncrementPenaltiesMajor()
    {
        penaltiesMajor -= 1;
    }

    int getNumMajorPenalties()
    {
        return penaltiesMajor;
    }

    int getTotalScore()
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

    void clearAllScores()
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
