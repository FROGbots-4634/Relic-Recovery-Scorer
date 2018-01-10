package net.frogbots.relicrecoveryscorecalculator.backend.export.csv;

import com.opencsv.CSVReader;
import net.frogbots.relicrecoveryscorecalculator.backend.export.ExportBundle;
import net.frogbots.relicrecoveryscorecalculator.ui.UiUtils;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class CsvAddExport
{
    private static String[][] readCSV (File file) throws IOException
    {
        CSVReader csvReader = new CSVReader(new FileReader(file));
        List<String[]> list = csvReader.readAll();
        String[][] dataArr = new String[list.size()][];
        dataArr = list.toArray(dataArr);
        return dataArr;
    }

    public static File export (ExportBundle exportBundle) throws IOException, CsvNotCompatibleException
    {
        /*
         * Read in the file that we need to append to
         */
        String[][] readArray = readCSV(exportBundle.fileForCsvAdd);
        int count = readArray.length;

        /*
         * Verify that it's a compatible file
         */
        if(!verifyCompatibility(readArray[0]))
        {
            /*
             * Well, it's not compatible :(
             * Show the user a dialog and abort the export
             */
            throw new CsvNotCompatibleException();
        }
        else
        {
            /*
             * It is compatible!
             * Continue with the export
             */
            String[][] out = new String[count+1][CsvCommon.columns.length];
            arrayCopy(readArray, out);
            CsvCommon.writeScoresToRow(out, exportBundle, count);
            CsvCommon.saveToCSV(exportBundle.activity, exportBundle.fileForCsvAdd, out);
        }
        return exportBundle.fileForCsvAdd;
    }

    private static void arrayCopy(String[][] aSource, String[][] aDestination)
    {
        for (int i = 0; i < aSource.length; i++)
        {
            System.arraycopy(aSource[i], 0, aDestination[i], 0, aSource[i].length);
        }
    }

    private static boolean verifyCompatibility(String[] arr)
    {
        /*
         * Verify that it has all of the columns inside
         * CsvCommon.columns and doesn't have anything
         * else...
         */
        return Arrays.equals(arr, CsvCommon.columns);
    }
}
