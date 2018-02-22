package net.frogbots.relicrecoveryscorecalculator.backend.export;

import net.frogbots.relicrecoveryscorecalculator.backend.Scores;

public class ExportFormatting
{
    public static String jewelForExport(int i)
    {
        String result = null;

        switch (i)
        {
            case 0:
                result =  "";
                break;

            case 1:
                result = "C";
                break;

            case 2:
                result = "W";
                break;
        }

        return result;
    }

    public static String preloadGlyphVuMarkForExport ()
    {
        return yN(Scores.getAutonomousPreloadedGlyphLevel() == 2);
    }

    public static String preloadGlyphForExport ()
    {
         return yN(Scores.getAutonomousPreloadedGlyphLevel() > 0);
    }

    public static String robotBalancedForExport()
    {
        return yN(Scores.getEndgameRobotBalanced() > 0);
    }


    public static String autoParkingForExport()
    {
        return yN(Scores.getParkingLevel() > 0);
    }

    public static String relicStandingForExport()
    {
        return yN(Scores.getEndgameRelicOrientation() > 0);
    }

    public static String cipherForExport()
    {
        return yN(Scores.getTeleopCipherLevel() > 0);
    }

    private static String yN(boolean b)
    {
        if(b)
        {
            return "Y";
        }
        else
        {
            return "";
        }
    }
}
