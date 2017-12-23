package net.frogbots.relicrecoveryscorecalculator.backend.export;

import net.frogbots.relicrecoveryscorecalculator.backend.export.csv.CsvAddExport;
import net.frogbots.relicrecoveryscorecalculator.backend.export.csv.CsvNewExport;
import java.io.IOException;

public class Export
{
    public static final int REQUEST_EXTERNAL_STORAGE_PERMISSIONS = 123;

    public static void doExport (ExportBundle exportBundle)
    {
        switch (exportBundle.exportType)
        {
            case PLAINTEXT:
                PlaintextExport.export(exportBundle);
                break;

            case CSV_ADD:
                try
                {
                    CsvAddExport.export(exportBundle);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                break;

            case GOOGLE_SHEETS:
                break;

            case CSV_NEW:
                try
                {
                    CsvNewExport.exportCSV(exportBundle);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                break;
        }
    }
}
