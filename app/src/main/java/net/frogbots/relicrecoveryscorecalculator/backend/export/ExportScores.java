package net.frogbots.relicrecoveryscorecalculator.backend.export;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import net.frogbots.relicrecoveryscorecalculator.backend.Scores;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class ExportScores
{
    public static final int REQUEST_EXTERNAL_STORAGE_PERMISSIONS = 123;

    public static void exportWithPermissionsWrapper (final Activity activity)
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            int hasWriteStoragePermission = activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED)
            {
                activity.requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE_PERMISSIONS);
                return;
            }
        }
        export(activity);
    }

    private static void export(final Context context)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Export to...");

        // add a list
        String[] exportTypes = {"Plaintext", "CSV File (allows multiple saves)", "Google Sheets (Coming soon)"};
        builder.setItems(exportTypes, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                getCommentAndPerformExport(context, which);
            }
        });

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private static void getCommentAndPerformExport(final Context context, final int type)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setTitle("Enter a unique identifier comment");

        // Set up the input
        final EditText input = new EditText(context);

        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                String comment = input.getText().toString();

                switch (type)
                {
                    case 0: //Plaintext
                        beginPlaintextExport(context, comment);
                        break;

                    case 1: //CSV
                        beginCSVExport(context, comment);
                        break;

                    case 2: //Google Sheets
                        break;
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });

        builder.show();
    }

    /*
     * Check the filename
     */
    @SuppressLint("SdCardPath")
    private static void beginPlaintextExport(final Context context, final String comment)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setTitle("Enter name for file");

        // Set up the input
        final EditText input = new EditText(context);

        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Export", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                String filename = input.getText().toString();

                if(filename.isEmpty())
                {
                    AlertDialog.Builder finishedDialog = new AlertDialog.Builder(context);
                    finishedDialog.
                            setTitle("Hey!")
                            .setMessage("You didn't provide a filename!")
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
                else if (filename.contains(".txt"))
                {
                    AlertDialog.Builder finishedDialog = new AlertDialog.Builder(context);
                    finishedDialog.
                            setTitle("Hey!")
                            .setMessage("Don't put a .txt extension; it will be appended automatically!")
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
                else
                {
                    File path = new File("/sdcard/RelicRecoveryScorer/");
                    final File exportFile = new File("/sdcard/RelicRecoveryScorer/" + filename + ".txt");
                    if(!path.exists())
                    {
                        path.mkdirs();
                    }

                    if(exportFile.exists())
                    {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                        builder1.setTitle("File already exists");
                        builder1.setMessage("You requested that the current Scores be saved to:\n" + exportFile.getAbsolutePath() + "\n\nHowever, that file already exists. Would you like to overwrite it?");
                        builder1.setCancelable(false);

                        builder1.setPositiveButton(
                                "Yes, overwrite",
                                new DialogInterface.OnClickListener()
                                {
                                    public void onClick(DialogInterface dialog, int id)
                                    {
                                        export(context, ExportType.PLAINTEXT, exportFile, comment);
                                    }
                                });

                        builder1.setNegativeButton(
                                "No",
                                new DialogInterface.OnClickListener()
                                {
                                    public void onClick(DialogInterface dialog, int id)
                                    {
                                        dialog.cancel();
                                    }
                                });

                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                    }
                    else
                    {
                        export(context, ExportType.PLAINTEXT, exportFile, comment);
                    }
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });

        builder.show();
    }

    static private void export (Context context, ExportType exportType, File exportFile, String comment)
    {
        switch (exportType)
        {
            case PLAINTEXT:
                plaintextExport(context, exportFile, comment);
                break;

            case CSV:
                try
                {
                    exportCSV(context, exportFile, comment);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                break;

            case GOOGLE_SHEETS:
                break;

            case CSVnew:
                try
                {
                    saveNewEntry(context, exportFile, comment);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                break;
        }
    }

    private static void plaintextExport(Context context, File exportFile, String comment)
    {
        try
        {
            /*
             * Create the output stream
             */
            FileOutputStream stream = new FileOutputStream(exportFile);
            OutputStreamWriter outputWriter = new OutputStreamWriter(stream);

            /*
             * Write a line for each thing
             */
            @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
            outputWriter.write(df.format(Calendar.getInstance().getTime()));

            outputWriter.write("\r\n\r\nComment: "                + comment);
            outputWriter.write("\r\nJewel: "                      + jewelForExport(Scores.getAutonomousJewelLevel()));
            outputWriter.write("\r\nPre-loaded glyph: "           + glyphForExport(Scores.getAutonomousPreloadedGlyphLevel()));
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
            AlertDialog.Builder finishedDialog = new AlertDialog.Builder(context);
            finishedDialog.
                    setTitle("File exported!")
                    .setMessage("The current Scores have been exported to:\n\n" + exportFile.getAbsolutePath())
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
            Toast.makeText(context, "Export failed!",
                           Toast.LENGTH_LONG).show();
        }
    }

    private static String jewelForExport(int i)
    {
        String result = null;

        switch (i)
        {
            case 0:
                result =  "Null";
                break;

            case 1:
                result = "Opponent's jewel removed";
                break;

            case 2:
                result = "Your jewel removed";
                break;
        }

        return result;
    }

    static private String glyphForExport(int i)
    {
        String result = null;

        switch (i)
        {
            case 0:
                result = "Null";
                break;

            case 1:
                result = "Glyph scored";
                break;

            case 2:
                result = "Glyph scored in column indicated by key";
                break;
        }

        return result;
    }

    private static void exportCSV (Context context, File file, String comment) throws IOException
    {
        String array[][] = new String[2][16];

        array[0][0]  = ("Time");
        array[0][1]  = ("Comment");
        array[0][2]  = ("Jewel");
        array[0][3]  = ("Pre-loaded glyph");
        array[0][4]  = ("[Auto] glyphs scored");
        array[0][5]  = ("Autonomous parking");
        array[0][6]  = ("[Tele-Op] glyphs scored");
        array[0][7]  = ("Cryptobox rows complete");
        array[0][8]  = ("Cryptobox columns complete");
        array[0][9]  = ("Cipher completed");
        array[0][10] = ("Relic position");
        array[0][11] = ("Relic upright");
        array[0][12] = ("Robot balanced");
        array[0][13] = ("Minor penalties");
        array[0][14] = ("Major penalties");
        array[0][15] = ("TOTAL SCORE");

        writeScoresToRow(array, comment, 1);
        saveToCSV(context, file, array);
    }

    private static void writeScoresToRow (String[][] array, String comment, int rowNumber)
    {
        @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("EEE d MMM yyyy HH:mm");

        array[rowNumber][0]  = (df.format(Calendar.getInstance().getTime()));
        array[rowNumber][1]  = comment;
        array[rowNumber][2]  = jewelForExport(Scores.getAutonomousJewelLevel());
        array[rowNumber][3]  = (glyphForExport(Scores.getAutonomousPreloadedGlyphLevel()));
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

    @SuppressLint("SdCardPath")
    private static void beginCSVExport (final Context context, final String comment)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setTitle("Enter name for file");

        // Set up the input
        final EditText input = new EditText(context);

        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Export", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                String filename = input.getText().toString();

                if(filename.isEmpty())
                {
                    AlertDialog.Builder finishedDialog = new AlertDialog.Builder(context);
                    finishedDialog.
                            setTitle("Hey!")
                            .setMessage("You didn't provide a filename!")
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
                else if (filename.contains(".csv"))
                {
                    AlertDialog.Builder finishedDialog = new AlertDialog.Builder(context);
                    finishedDialog.
                            setTitle("Hey!")
                            .setMessage("Don't put a .csv extension; it will be appended automatically!")
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
                else
                {
                    File path = new File("/sdcard/RelicRecoveryScorer/");
                    final File exportFile = new File("/sdcard/RelicRecoveryScorer/" + filename + ".csv");
                    if(!path.exists())
                    {
                        path.mkdirs();
                    }

                    if(exportFile.exists())
                    {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                        builder1.setTitle("File already exists");
                        builder1.setMessage("You requested that the current Scores be saved to:\n" + exportFile.getAbsolutePath() + "\n\nHowever, that file already exists. Would you like to add to it?");
                        builder1.setCancelable(false);

                        builder1.setPositiveButton(
                                "Yes, add this score to it",
                                new DialogInterface.OnClickListener()
                                {
                                    public void onClick(DialogInterface dialog, int id)
                                    {
                                        export(context, ExportType.CSVnew, exportFile, comment);
                                    }
                                });

                        builder1.setNegativeButton(
                                "No",
                                new DialogInterface.OnClickListener()
                                {
                                    public void onClick(DialogInterface dialog, int id)
                                    {
                                        dialog.cancel();
                                    }
                                });

                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                    }
                    else
                    {
                        export(context, ExportType.CSV, exportFile, comment);
                    }
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private static String[][] readCSV (File file) throws IOException
    {
        CSVReader csvReader = new CSVReader(new FileReader(file));
        List<String[]> list = csvReader.readAll();
        String[][] dataArr = new String[list.size()][];
        dataArr = list.toArray(dataArr);
        return dataArr;
    }

    private static void saveNewEntry (Context context, File file, String comment) throws IOException
    {
        String[][] readArray = readCSV(file);
        int count = readArray.length;
        String[][] out = new String[count+1][16];
        arrayCopy(readArray, out);
        writeScoresToRow(out, comment, count);
        saveToCSV(context, file, out);
    }

    private static void arrayCopy(String[][] aSource, String[][] aDestination)
    {
        for (int i = 0; i < aSource.length; i++) {
            System.arraycopy(aSource[i], 0, aDestination[i], 0, aSource[i].length);
        }
    }
}
