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

        System.arraycopy(CsvCommon.columns, 0, array[0], 0, 17);

        CsvCommon.writeScoresToRow(array, exportBundle.comment, exportBundle.match, 1);
        CsvCommon.saveToCSV(exportBundle.activity, file, array);
    }
}
