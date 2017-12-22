package net.frogbots.relicrecoveryscorecalculator.backend.export.csv;

import com.opencsv.CSVReader;
import net.frogbots.relicrecoveryscorecalculator.backend.export.ExportBundle;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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

    public static void export (ExportBundle exportBundle) throws IOException
    {
        String[][] readArray = readCSV(exportBundle.fileForCsvAdd);
        int count = readArray.length;
        String[][] out = new String[count+1][17];
        arrayCopy(readArray, out);
        CsvCommon.writeScoresToRow(out, exportBundle.comment, exportBundle.match, count);
        CsvCommon.saveToCSV(exportBundle.activity, exportBundle.fileForCsvAdd, out);
    }

    private static void arrayCopy(String[][] aSource, String[][] aDestination)
    {
        for (int i = 0; i < aSource.length; i++) {
            System.arraycopy(aSource[i], 0, aDestination[i], 0, aSource[i].length);
        }
    }
}
