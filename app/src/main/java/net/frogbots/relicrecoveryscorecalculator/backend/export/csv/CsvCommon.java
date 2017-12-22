package net.frogbots.relicrecoveryscorecalculator.backend.export.csv;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import com.opencsv.CSVWriter;
import net.frogbots.relicrecoveryscorecalculator.backend.Scores;
import net.frogbots.relicrecoveryscorecalculator.backend.Utils;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

class CsvCommon
{
    static void saveToCSV (Context context, File file, String[][] array) throws IOException
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

    static void writeScoresToRow (String[][] array, String comment, String match, int rowNumber)
    {
        @SuppressLint("SimpleDateFormat")
        DateFormat df = new SimpleDateFormat("EEE d MMM yyyy HH:mm");

        array[rowNumber][0]  = (df.format(Calendar.getInstance().getTime()));
        array[rowNumber][1]  = match;
        array[rowNumber][2]  = comment;
        array[rowNumber][3]  = Utils.jewelForExport(Scores.getAutonomousJewelLevel());
        array[rowNumber][4]  = (Utils.glyphForExport(Scores.getAutonomousPreloadedGlyphLevel()));
        array[rowNumber][5]  = Integer.toString(Scores.getAutonomousGlyphsScored());
        array[rowNumber][6]  = Boolean.toString(Scores.getParkingLevel() > 0);
        array[rowNumber][7]  = Integer.toString(Scores.getTeleOpGlyphsScored());
        array[rowNumber][8]  = Integer.toString(Scores.getTeleopCryptoboxRowsComplete());
        array[rowNumber][9]  = Integer.toString(Scores.getTeleopCryptoboxColumnsComplete());
        array[rowNumber][10] = Boolean.toString(Scores.getTeleopCipherLevel() > 0);
        array[rowNumber][11] = Integer.toString(Scores.getEndgameRelicPosition());
        array[rowNumber][12] = Boolean.toString(Scores.getEndgameRelicOrientation() > 0);
        array[rowNumber][13] = Boolean.toString(Scores.getEndgameRobotBalanced() > 0);
        array[rowNumber][14] = Integer.toString(Scores.getNumMinorPenalties());
        array[rowNumber][15] = Integer.toString(Scores.getNumMajorPenalties());
        array[rowNumber][16] = Integer.toString(Scores.getTotalScore());
    }
}
