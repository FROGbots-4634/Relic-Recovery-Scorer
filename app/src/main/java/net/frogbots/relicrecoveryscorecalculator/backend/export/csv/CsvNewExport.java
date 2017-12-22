package net.frogbots.relicrecoveryscorecalculator.backend.export.csv;

import net.frogbots.relicrecoveryscorecalculator.backend.export.ExportBundle;
import java.io.File;
import java.io.IOException;

public class CsvNewExport
{
    public static void exportCSV (ExportBundle exportBundle) throws IOException
    {
        File file = new File("/sdcard/RelicRecoveryScorer/" + exportBundle.filename + ".csv");
        String array[][] = new String[2][17];

        array[0][0]  = ("Time");
        array[0][1]  = ("Match");
        array[0][2]  = ("Comment");
        array[0][3]  = ("Jewel");
        array[0][4]  = ("Pre-loaded glyph");
        array[0][5]  = ("[Auto] glyphs scored");
        array[0][6]  = ("Autonomous parking");
        array[0][7]  = ("[Tele-Op] glyphs scored");
        array[0][8]  = ("Cryptobox rows complete");
        array[0][9]  = ("Cryptobox columns complete");
        array[0][10] = ("Cipher completed");
        array[0][11] = ("Relic position");
        array[0][12] = ("Relic upright");
        array[0][13] = ("Robot balanced");
        array[0][14] = ("Minor penalties");
        array[0][15] = ("Major penalties");
        array[0][16] = ("TOTAL SCORE");

        CsvCommon.writeScoresToRow(array, exportBundle.comment, exportBundle.match, 1);
        CsvCommon.saveToCSV(exportBundle.activity, file, array);
    }
}
