package net.frogbots.relicrecoveryscorecalculator.backend.export.csv;

import net.frogbots.relicrecoveryscorecalculator.backend.Utils;
import net.frogbots.relicrecoveryscorecalculator.backend.export.ExportBundle;
import java.io.File;
import java.io.IOException;

public class CsvNewExport
{
    public static File exportCSV (ExportBundle exportBundle) throws IOException, FileAlreadyExistsException
    {
        File file = new File(Utils.getExportDirPath() + exportBundle.filename + ".csv");

        if(file.exists())
        {
            throw new FileAlreadyExistsException();
        }

        String array[][] = new String[2][CsvCommon.columns.length];

        System.arraycopy(CsvCommon.columns, 0, array[0], 0, CsvCommon.columns.length);

        CsvCommon.writeScoresToRow(array, exportBundle, 1);
        CsvCommon.saveToCSV(file, array);

        return file;
    }
}
