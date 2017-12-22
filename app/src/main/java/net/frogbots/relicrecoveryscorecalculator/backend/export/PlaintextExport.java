package net.frogbots.relicrecoveryscorecalculator.backend.export;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;
import net.frogbots.relicrecoveryscorecalculator.backend.Scores;
import net.frogbots.relicrecoveryscorecalculator.backend.Utils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PlaintextExport
{
    public static void export(ExportBundle exportBundle)
    {
        try
        {
            File file = new File("/sdcard/RelicRecoveryScorer/" + exportBundle.filename + ".txt");

            /*
             * Create the output stream
             */
            FileOutputStream stream = new FileOutputStream(file);
            OutputStreamWriter outputWriter = new OutputStreamWriter(stream);

            /*
             * Write a line for each thing
             */
            @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
            outputWriter.write(df.format(Calendar.getInstance().getTime()));

            outputWriter.write("\r\n\r\nMatch: "                  + exportBundle.match);
            outputWriter.write("\r\nComment: "                    + exportBundle.comment);
            outputWriter.write("\r\nJewel: "                      + Utils.jewelForExport(Scores.getAutonomousJewelLevel()));
            outputWriter.write("\r\nPre-loaded glyph: "           + Utils.glyphForExport(Scores.getAutonomousPreloadedGlyphLevel()));
            outputWriter.write("\r\n[Auto] glyphs scored: "       + Scores.getAutonomousGlyphsScored());
            outputWriter.write("\r\nAutonomous parking: "         + (Scores.getParkingLevel() > 0));
            outputWriter.write("\r\n[Tele-Op] glyphs scored: "    + Scores.getTeleOpGlyphsScored());
            outputWriter.write("\r\nCryptobox rows complete: "    + Scores.getTeleopCryptoboxRowsComplete());
            outputWriter.write("\r\nCryptobox columns complete: " + Scores.getTeleopCryptoboxColumnsComplete());
            outputWriter.write("\r\nCipher completed: "           + (Scores.getTeleopCipherLevel() > 0));
            outputWriter.write("\r\nRelic position: "             + Scores.getEndgameRelicPosition());
            outputWriter.write("\r\nRelic upright: "              + (Scores.getEndgameRelicOrientation() > 0));
            outputWriter.write("\r\nRobot balanced: "             + (Scores.getEndgameRobotBalanced() > 0));
            outputWriter.write("\r\nMinor penalties: "            + Scores.getNumMinorPenalties());
            outputWriter.write("\r\nMajor penalties: "            + Scores.getNumMajorPenalties());
            outputWriter.write("\r\n------------------------");
            outputWriter.write("\r\nTOTAL SCORE: " + Scores.getTotalScore());

            /*
             * Close everything down; we're all done
             */
            outputWriter.close();
            stream.close();

            /*
             * Show the user an alert letting them know it was exported
             */
            AlertDialog.Builder finishedDialog = new AlertDialog.Builder(exportBundle.activity);
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
        catch (IOException e)
        {
            e.printStackTrace();
            Toast.makeText(exportBundle.activity, "Export failed!",
                           Toast.LENGTH_LONG).show();
        }
    }
}