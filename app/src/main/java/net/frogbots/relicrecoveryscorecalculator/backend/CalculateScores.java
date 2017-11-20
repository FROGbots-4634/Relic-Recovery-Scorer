package net.frogbots.relicrecoveryscorecalculator.backend;

public class CalculateScores
{
    /*
     * Autonomous
     */
    private static final int AUTONOMOUS_PARKING                       =  10;
    private static final int AUTONOMOUS_JEWEL_POINTS_NO_MOVEMENT      =  0;
    private static final int AUTONOMOUS_JEWEL_POINTS_OPPONENT_REMOVED =  30;
    private static final int AUTONOMOUS_JEWEL_POINTS_YOUR_REMOVED     = -30;
    private static final int AUTONOMOUS_GLYPH_NOT_SCORED              =  0;
    private static final int AUTONOMOUS_GLYPH_SCORED                  =  15;
    private static final int AUTONOMOUS_GLYPH_SCORED_COLUMN_KEY       =  45;

    /*
     * Teleop
     */
    private static final int TELEOP_GLYPH_SCORED                      =   2;
    private static final int TELEOP_CRYPTOBOX_ROW_COMPLETE            =  10;
    private static final int TELEOP_CRYPTOBOX_COLUMN_COMPLETE         =  20;
    private static final int TELEOP_NO_CIPHER                         =   0;
    private static final int TELEOP_COMPLETED_CIPHER                  =  30;

    /*
     * Endgame
     */
    private static final int ENDGAME_RELIC_POSITION_NOT_IN_ZONE       =   0;
    private static final int ENDGAME_RELIC_POSITION_ZONE_1            =  10;
    private static final int ENDGAME_RELIC_POSITION_ZONE_2            =  20;
    private static final int ENDGAME_RELIC_POSITION_ZONE_3            =  40;
    private static final int ENDGAME_RELIC_ORIENTATION_LAYING_DOWN    =   0;
    private static final int ENDGAME_RELIC_ORIENTATION_UPRIGHT        =  15;
    private static final int ENDGAME_ROBOT_NOT_BALANCED               =   0;
    private static final int ENDGAME_ROBOT_BALANCED                   =  20;

    /*
     * Penalty
     */
    private static final int PENALTY_MAJOR                            = -40;
    private static final int PENALTY_MINOR                            = -10;

    public static int calculateAutonomousJewelScore(int i)
    {
        int result = 0;

        switch (i)
        {
            case 0:
                result =  AUTONOMOUS_JEWEL_POINTS_NO_MOVEMENT;
                break;

            case 1:
                result = AUTONOMOUS_JEWEL_POINTS_OPPONENT_REMOVED;
                break;

            case 2:
                result = AUTONOMOUS_JEWEL_POINTS_YOUR_REMOVED;
                break;
        }

        return result;
    }

    public static int calculateAutonomousPreLoadedGlyphScore(int i)
    {
        int result = 0;

        switch (i)
        {
            case 0:
                result = AUTONOMOUS_GLYPH_NOT_SCORED;
                break;

            case 1:
                result = AUTONOMOUS_GLYPH_SCORED;
                break;

            case 2:
                result = AUTONOMOUS_GLYPH_SCORED_COLUMN_KEY;
                break;
        }

        return result;
    }

    public static int calculateAutonomousGlyphsScore(int i)
    {
        return i * AUTONOMOUS_GLYPH_SCORED;
    }

    public static int calculateAutonomousParkingScore(int i)
    {
        int result = 0;

        switch (i)
        {
            case 0:
                result = 0;
                break;

            case 1:
                result = AUTONOMOUS_PARKING;
                break;
        }

        return result;
    }

    public static int calculateTeleopGlyphsScore(int i)
    {
        return i * TELEOP_GLYPH_SCORED;
    }

    public static int calculateTeleopCryptoboxRowsCompletedScore(int i)
    {
        return i * TELEOP_CRYPTOBOX_ROW_COMPLETE;
    }

    public static int calculateTeleopCryptoboxColumnsCompleteScore(int i)
    {
        return i * TELEOP_CRYPTOBOX_COLUMN_COMPLETE;
    }

    public static int calculateTeleopCompletedCipherScore(int i)
    {
        int result = 0;

        switch (i)
        {
            case 0:
                result = TELEOP_NO_CIPHER;
                break;

            case 1:
                result = TELEOP_COMPLETED_CIPHER;
                break;
        }

        return result;
    }

    public static int calculateEndgameRelicPositionScore(int i)
    {
        int result = 0;

        switch (i)
        {
            case 0:
                result = ENDGAME_RELIC_POSITION_NOT_IN_ZONE;
                break;

            case 1:
                result = ENDGAME_RELIC_POSITION_ZONE_1;
                break;

            case 2:
                result = ENDGAME_RELIC_POSITION_ZONE_2;
                break;

            case 3:
                result = ENDGAME_RELIC_POSITION_ZONE_3;
                break;
        }

        return result;
    }

    public static int calculateEndgameRelicOrientationScore(int i)
    {
        int result = 0;

        switch (i)
        {
            case 0:
                result = ENDGAME_RELIC_ORIENTATION_LAYING_DOWN;
                break;

            case 1:
                result = ENDGAME_RELIC_ORIENTATION_UPRIGHT;
                break;
        }

        return result;
    }

    public static int calculateEndgameRobotBalancedScore(int i)
    {
        int result = 0;

        switch (i)
        {
            case 0:
                result = ENDGAME_ROBOT_NOT_BALANCED;
                break;

            case 1:
                result = ENDGAME_ROBOT_BALANCED;
                break;
        }

        return result;
    }

    public static int calculatePenaltyMinorScore(int i)
    {
        return i * PENALTY_MINOR;
    }

    public static int calculatePenaltyMajorScore(int i)
    {
        return i * PENALTY_MAJOR;
    }
}
