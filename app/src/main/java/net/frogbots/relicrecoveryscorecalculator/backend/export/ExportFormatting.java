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
                result =  "Null";
                break;

            case 1:
                result = "Opponent's jewel removed";
                break;

            case 2:
                result = "Your jewel removed";
                break;
        }

        return result;
    }

    public static String glyphForExport(int i)
    {
        String result = null;

        switch (i)
        {
            case 0:
                result = "Null";
                break;

            case 1:
                result = "Glyph scored";
                break;

            case 2:
                result = "Glyph scored in column indicated by key";
                break;
        }

        return result;
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
