package net.frogbots.relicrecoveryscorecalculator.backend.export;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;

import net.frogbots.relicrecoveryscorecalculator.backend.export.csv.CsvNewExport;

import java.io.IOException;

public class Export
{
    public static final int REQUEST_EXTERNAL_STORAGE_PERMISSIONS = 123;

    public static void exportWithPermissionsWrapper(ExportBundle exportBundle)
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            int hasWriteStoragePermission = exportBundle.activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED)
            {
                exportBundle.activity.requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE_PERMISSIONS);
                return;
            }
        }
        export(exportBundle);
    }

    static private void export (ExportBundle exportBundle)
    {
        switch (exportBundle.exportType)
        {
            case PLAINTEXT:
                PlaintextExport.export(exportBundle);
                break;

            /*case CSV:
                try
                {
                    exportCSV(context, exportFile, comment, scores);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                break;*/

            case GOOGLE_SHEETS:
                break;

            case CSVnew:
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
