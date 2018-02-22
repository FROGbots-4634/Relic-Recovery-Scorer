package net.frogbots.relicrecoveryscorecalculator.backend.export.csv;

import android.annotation.SuppressLint;

import com.opencsv.CSVWriter;
import net.frogbots.relicrecoveryscorecalculator.backend.Scores;
import net.frogbots.relicrecoveryscorecalculator.backend.export.ExportBundle;
import net.frogbots.relicrecoveryscorecalculator.backend.export.ExportFormatting;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

class CsvCommon
{
    private static final String COLUMN_TIME                   = "Time";
    private static final String COLUMN_MATCH                  = "Match";
    private static final String COLUMN_TEAM                   = "Team";
    private static final String COLUMN_COMMENT                = "Comment";
    private static final String COLUMN_JEWEL                  = "Jewel";
    private static final String COLUMN_PRELOADED_GLPYH_SCORED = "Preload glyph";
    private static final String COLUMN_PRELOADED_GLPYH_VUMARK = "VuMark";
    private static final String COLUMN_AUTO_GLPYHS_SCORED     = "Ex. auto glyphs";
    private static final String COLUMN_AUTO_PARKING           = "Auto parked";
    private static final String COLUMN_TELE_GLYPHS_SCORED     = "Tele-Op glyphs";
    private static final String COLUMN_CRYPT_ROWS_COMPLETE    = "Crypt. rows";
    private static final String COLUMN_CRYPT_COLUMNS_COMPLETE = "Crypt. cols";
    private static final String COLUMN_CIPHER_COMPLETE        = "Cipher";
    private static final String COLUMN_RELIC_POSITION         = "Relic zone";
    private static final String COLUMN_RELIC_UPRIGHT          = "Relic upright";
    private static final String COLUMN_ROBOT_BALANCED         = "Balanced";
    private static final String COLUMN_MINOR_PENALTIES        = "Minor pen.";
    private static final String COLUMN_MAJOR_PENALTIES        = "Major pen.";
    private static final String COLUMN_TOTAL_SCORE            = "TOTAL SCORE";

    static final String[] columns = {
            COLUMN_TEAM,
            COLUMN_MATCH,
            COLUMN_JEWEL,
            COLUMN_PRELOADED_GLPYH_SCORED,
            COLUMN_PRELOADED_GLPYH_VUMARK,
            COLUMN_AUTO_GLPYHS_SCORED,
            COLUMN_AUTO_PARKING,
            COLUMN_TELE_GLYPHS_SCORED,
            COLUMN_CRYPT_ROWS_COMPLETE,
            COLUMN_CRYPT_COLUMNS_COMPLETE,
            COLUMN_CIPHER_COMPLETE,
            COLUMN_RELIC_POSITION,
            COLUMN_RELIC_UPRIGHT,
            COLUMN_ROBOT_BALANCED,
            COLUMN_MINOR_PENALTIES,
            COLUMN_MAJOR_PENALTIES,
            COLUMN_TOTAL_SCORE,
            COLUMN_TIME,
            COLUMN_COMMENT
    };

    private static int findIndexInColumnArray (String item)
    {
        for(int i = 0; i < columns.length; i ++)
        {
            if(columns[i].equals(item))
            {
                return i;
            }
        }
        throw new InvalidParameterException();
    }

    static void saveToCSV (File file, String[][] array) throws IOException
    {
        CSVWriter writer = new CSVWriter(new FileWriter(file));
        writer.writeAll(Arrays.asList(array));
        writer.close();
    }

    static void writeScoresToRow (String[][] array, ExportBundle bundle, int rowNumber)
    {
        @SuppressLint("SimpleDateFormat")
        DateFormat df = new SimpleDateFormat("EEE d MMM yyyy HH:mm");

        array[rowNumber][findIndexInColumnArray(COLUMN_TIME)]                    = (df.format(Calendar.getInstance().getTime()));
        array[rowNumber][findIndexInColumnArray(COLUMN_MATCH)]                   = bundle.match;
        array[rowNumber][findIndexInColumnArray(COLUMN_TEAM)]                    = bundle.team;
        array[rowNumber][findIndexInColumnArray(COLUMN_COMMENT)]                 = bundle.comment;
        array[rowNumber][findIndexInColumnArray(COLUMN_JEWEL)]                   = ExportFormatting.jewelForExport(Scores.getAutonomousJewelLevel());
        array[rowNumber][findIndexInColumnArray(COLUMN_PRELOADED_GLPYH_SCORED)]  = ExportFormatting.preloadGlyphForExport();
        array[rowNumber][findIndexInColumnArray(COLUMN_PRELOADED_GLPYH_VUMARK)]  = ExportFormatting.preloadGlyphVuMarkForExport();
        array[rowNumber][findIndexInColumnArray(COLUMN_AUTO_GLPYHS_SCORED)]      = Integer.toString(Scores.getAutonomousGlyphsScored());
        array[rowNumber][findIndexInColumnArray(COLUMN_AUTO_PARKING)]            = ExportFormatting.autoParkingForExport();
        array[rowNumber][findIndexInColumnArray(COLUMN_TELE_GLYPHS_SCORED)]      = Integer.toString(Scores.getTeleOpGlyphsScored());
        array[rowNumber][findIndexInColumnArray(COLUMN_CRYPT_ROWS_COMPLETE)]     = Integer.toString(Scores.getTeleopCryptoboxRowsComplete());
        array[rowNumber][findIndexInColumnArray(COLUMN_CRYPT_COLUMNS_COMPLETE)]  = Integer.toString(Scores.getTeleopCryptoboxColumnsComplete());
        array[rowNumber][findIndexInColumnArray(COLUMN_CIPHER_COMPLETE)]         = ExportFormatting.cipherForExport();
        array[rowNumber][findIndexInColumnArray(COLUMN_RELIC_POSITION)]          = Integer.toString(Scores.getEndgameRelicPosition());
        array[rowNumber][findIndexInColumnArray(COLUMN_RELIC_UPRIGHT)]           = ExportFormatting.relicStandingForExport();
        array[rowNumber][findIndexInColumnArray(COLUMN_ROBOT_BALANCED)]          = ExportFormatting.robotBalancedForExport();
        array[rowNumber][findIndexInColumnArray(COLUMN_MINOR_PENALTIES)]         = Integer.toString(Scores.getNumMinorPenalties());
        array[rowNumber][findIndexInColumnArray(COLUMN_MAJOR_PENALTIES)]         = Integer.toString(Scores.getNumMajorPenalties());
        array[rowNumber][findIndexInColumnArray(COLUMN_TOTAL_SCORE)]             = Integer.toString(Scores.getTotalScore());
    }
}
