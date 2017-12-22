package net.frogbots.relicrecoveryscorecalculator.backend.export.csv;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.opencsv.CSVWriter;

import net.frogbots.relicrecoveryscorecalculator.backend.Scores;
import net.frogbots.relicrecoveryscorecalculator.backend.Utils;
import net.frogbots.relicrecoveryscorecalculator.backend.export.ExportBundle;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

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

        writeScoresToRow(array, exportBundle.comment, exportBundle.match, 1);
        saveToCSV(exportBundle.activity, file, array);
    }

    private static void writeScoresToRow (String[][] array, String comment, String match, int rowNumber)
    {
        @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("EEE d MMM yyyy HH:mm");

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
