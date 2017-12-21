package net.frogbots.relicrecoveryscorecalculator.backend;

public class Scores
{
    /*
     * Number of tasks done
     * ---------------------------------------------------------------------------
     */

    /*
     * Autonomous
     */
    private static int autonomousJewelLevel = 0;
    private static int autonomousPreloadedGlyphLevel = 0;
    private static int autonomousGlyphsScored = 0;
    private static int autonomousParkingLevel = 0;

    /*
     * Teleop
     */
    private static int teleopGlyphsScored = 0;
    private static int teleopCryptoboxRowsComplete = 0;
    private static int teleopCryptoboxColumnsComplete = 0;
    private static int teleopCipherLevel = 0;

    /*
     * Endgame
     */
    private static int endgameRelicPosition = 0;
    private static int endgameRelicOrientation = 0;
    private static int endgameRobotBalanced = 0;

    /*
     * Penalty
     */
    private static int penaltiesMinor = 0;
    private static int penaltiesMajor = 0;

    //----------------------------------------------------------------------------

    /*
     * Autonomous getters & setters
     */

    public static void setAutonomousJewelLevel(int level)
    {
        autonomousJewelLevel = level;
    }

    public static int getAutonomousJewelLevel()
    {
        return autonomousJewelLevel;
    }

    public static void setAutonomousPreloadedGlyphLevel(int level)
    {
        autonomousPreloadedGlyphLevel = level;
    }

    public static int getAutonomousPreloadedGlyphLevel()
    {
        return autonomousPreloadedGlyphLevel;
    }

    public static void positiveIncrementAutonomousGlyphsScored()
    {
        autonomousGlyphsScored += 1;
    }

    public static void negativeIncrementAutonomousGlyphsScored()
    {
        autonomousGlyphsScored -= 1;
    }

    public static int getAutonomousGlyphsScored()
    {
        return autonomousGlyphsScored;
    }

    public static void setParkingLevel(int parkingLevel)
    {
        autonomousParkingLevel = parkingLevel;
    }

    public static int getParkingLevel()
    {
        return autonomousParkingLevel;
    }

    /*
     * Teleop getters & setters
     */

    public static int getTeleOpGlyphsScored()
    {
        return teleopGlyphsScored;
    }

    public static void positiveIncrementTeleopGlyphsScored()
    {
        teleopGlyphsScored += 1;
    }
    public static void negativeIncrementTeleopGlyphsScored()
    {
        teleopGlyphsScored -= 1;
    }

    public static void positiveIncrementTeleopCryptoboxRowsComplete()
    {
        teleopCryptoboxRowsComplete += 1;
    }

    public static void negativeIncrementTeleopCryptoboxRowsComplete()
    {
        teleopCryptoboxRowsComplete -= 1;
    }

    public static int getTeleopCryptoboxRowsComplete()
    {
        return teleopCryptoboxRowsComplete;
    }

    public static void positiveIncrementTeleopCryptoboxColumnsComplete()
    {
        teleopCryptoboxColumnsComplete += 1;
    }

    public static void negativeIncrementTeleopCryptoboxColumnsComplete()
    {
        teleopCryptoboxColumnsComplete -= 1;
    }

    public static int getTeleopCryptoboxColumnsComplete()
    {
        return teleopCryptoboxColumnsComplete;
    }

    public static void setTeleopCipherLevel(int i)
    {
        teleopCipherLevel = i;
    }

    public static int getTeleopCipherLevel()
    {
        return teleopCipherLevel;
    }

    /*
     * Endgame getters & setters
     */

    public static void setEndgameRelicPosition(int i)
    {
        endgameRelicPosition = i;
    }

    public static int getEndgameRelicPosition()
    {
        return endgameRelicPosition;
    }

    public static void setEndgameRelicOrientation(int i)
    {
        endgameRelicOrientation = i;
    }

    public static int getEndgameRelicOrientation()
    {
        return endgameRelicOrientation;
    }

    public static void setEndgameRobotBalanced(int i)
    {
        endgameRobotBalanced = i;
    }

    public static int getEndgameRobotBalanced()
    {
        return endgameRobotBalanced;
    }

    /*
     * Penalty getters & setters
     */

    public static void positiveIncrementPenaltiesMinor()
    {
        penaltiesMinor += 1;
    }

    public static void negativeIncrementPenaltiesMinor()
    {
        penaltiesMinor -= 1;
    }

    public static int getNumMinorPenalties()
    {
        return penaltiesMinor;
    }

    public static void positiveIncrementPenaltiesMajor()
    {
        penaltiesMajor += 1;
    }

    public static void negativeIncrementPenaltiesMajor()
    {
        penaltiesMajor -= 1;
    }

    public static int getNumMajorPenalties()
    {
        return penaltiesMajor;
    }

    public static int getTotalScore()
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

    public static void clearAllScores()
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
