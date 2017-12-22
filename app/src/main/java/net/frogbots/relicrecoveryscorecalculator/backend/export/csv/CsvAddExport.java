package net.frogbots.relicrecoveryscorecalculator.backend.export.csv;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import net.frogbots.relicrecoveryscorecalculator.backend.Scores;
import net.frogbots.relicrecoveryscorecalculator.backend.Utils;
import net.frogbots.relicrecoveryscorecalculator.backend.export.ExportBundle;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
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
        File file = new File("/sdcard/RelicRecoveryScorer/" + exportBundle.filename + ".csv");

        String[][] readArray = readCSV(new File(""));
        int count = readArray.length;
        String[][] out = new String[count+1][16];
        arrayCopy(readArray, out);
        writeScoresToRow(out, exportBundle.comment, count);
        saveToCSV(exportBundle.activity, file, out);
    }

    private static void arrayCopy(String[][] aSource, String[][] aDestination)
    {
        for (int i = 0; i < aSource.length; i++) {
            System.arraycopy(aSource[i], 0, aDestination[i], 0, aSource[i].length);
        }
    }

    private static void writeScoresToRow (String[][] array, String comment, int rowNumber)
    {
        @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("EEE d MMM yyyy HH:mm");

        array[rowNumber][0]  = (df.format(Calendar.getInstance().getTime()));
        array[rowNumber][1]  = comment;
        array[rowNumber][2]  = Utils.jewelForExport(Scores.getAutonomousJewelLevel());
        array[rowNumber][3]  = (Utils.glyphForExport(Scores.getAutonomousPreloadedGlyphLevel()));
        array[rowNumber][4]  = Integer.toString(Scores.getAutonomousGlyphsScored());
        array[rowNumber][5]  = Boolean.toString(Scores.getParkingLevel() > 0);
        array[rowNumber][6]  = Integer.toString(Scores.getTeleOpGlyphsScored());
        array[rowNumber][7]  = Integer.toString(Scores.getTeleopCryptoboxRowsComplete());
        array[rowNumber][8]  = Integer.toString(Scores.getTeleopCryptoboxColumnsComplete());
        array[rowNumber][9]  = Boolean.toString(Scores.getTeleopCipherLevel() > 0);
        array[rowNumber][10]  = Integer.toString(Scores.getEndgameRelicPosition());
        array[rowNumber][11] = Boolean.toString(Scores.getEndgameRelicOrientation() > 0);
        array[rowNumber][12] = Boolean.toString(Scores.getEndgameRobotBalanced() > 0);
        array[rowNumber][13] = Integer.toString(Scores.getNumMinorPenalties());
        array[rowNumber][14] = Integer.toString(Scores.getNumMajorPenalties());
        array[rowNumber][15] = Integer.toString(Scores.getTotalScore());
    }

    private static void saveToCSV (Context context, File file, String[][] array) throws IOException
    {
        CSVWriter writer = new CSVWriter(new FileWriter(file));
        writer.writeAll(Arrays.asList(array));
        writer.close();

        /*
         * Show the user an alert letting them know it was exported
         */
        AlertDialog.Builder finishedDialog = new AlertDialog.Builder(context);
        finishedDialog.
                setTitle("File exported!")
                .setMessage("The current Scores have been exported to:\n\n" + file.getAbsolutePath())
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        //do things
                    }
                });
        AlertDialog alert = finishedDialog.create();
        alert.show();
    }
}
