package net.frogbots.relicrecoveryscorecalculator.backend.export;

import android.annotation.SuppressLint;

import net.frogbots.relicrecoveryscorecalculator.backend.Scores;
import net.frogbots.relicrecoveryscorecalculator.backend.Utils;
import net.frogbots.relicrecoveryscorecalculator.backend.export.csv.FileAlreadyExistsException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

class PlaintextExport
{
    public static File export(ExportBundle exportBundle) throws IOException, FileAlreadyExistsException
    {
        File file = new File(Utils.getExportDirPath() + exportBundle.filename + ".txt");

        if(file.exists())
        {
            throw new FileAlreadyExistsException();
        }

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
        outputWriter.write("\r\nTeam: "                       + exportBundle.team);
        outputWriter.write("\r\nComment: "                    + exportBundle.comment);
        outputWriter.write("\r\nJewel: "                      + ExportFormatting.jewelForExport(Scores.getAutonomousJewelLevel()));
        outputWriter.write("\r\nPre-loaded glyph scored: "    + ExportFormatting.preloadGlyphForExport());
        outputWriter.write("\r\nPreloaded in VuMark col: "    + ExportFormatting.preloadGlyphVuMarkForExport());
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

        return file;
    }
}