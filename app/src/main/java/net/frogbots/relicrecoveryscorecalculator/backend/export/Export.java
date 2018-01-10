package net.frogbots.relicrecoveryscorecalculator.backend.export;

import net.frogbots.relicrecoveryscorecalculator.backend.export.csv.CsvAddExport;
import net.frogbots.relicrecoveryscorecalculator.backend.export.csv.CsvNewExport;
import net.frogbots.relicrecoveryscorecalculator.backend.export.csv.CsvNotCompatibleException;
import net.frogbots.relicrecoveryscorecalculator.backend.export.csv.FileAlreadyExistsException;

import java.io.File;
import java.io.IOException;

public class Export
{
    public static final int REQUEST_EXTERNAL_STORAGE_PERMISSIONS = 123;

    public static File doExport (ExportBundle exportBundle) throws IOException, CsvNotCompatibleException, FileAlreadyExistsException
    {
        switch (exportBundle.exportType)
        {
            case PLAINTEXT:
                return PlaintextExport.export(exportBundle);

            case CSV_NEW:
                return CsvNewExport.exportCSV(exportBundle);

            case CSV_ADD:
                return CsvAddExport.export(exportBundle);

            case GOOGLE_SHEETS:
                break;
        }
        return null;
    }
}
