package net.frogbots.relicrecoveryscorecalculator;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

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

class ExportScores
{
    enum ExportType
    {
        PLAINTEXT,
        CSV,
        GOOGLE_SHEETS,
        CSVnew
    }

    static void export(final Context context, final Scores scores)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Export to...");

        // add a list
        String[] periods = {"Plaintext", "CSV File (allows multiple saves)", "Google Sheets (Coming soon)"};
        builder.setItems(periods, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                switch (which)
                {
                    case 0: //Plaintext
                        beginPlaintextExport(context, scores);
                        break;

                    case 1: //CSV
                        beginCSVExport(context,scores);
                        break;

                    case 2: //Google Sheets
                        break;
                }
            }
        });

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /*
     * Check the filename
     */
    @SuppressLint("SdCardPath")
    static void beginPlaintextExport(final Context context, final Scores scores)
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
                        builder1.setMessage("You requested that the current scores be saved to:\n" + exportFile.getAbsolutePath() + "\n\nHowever, that file already exists. Would you like to overwrite it?");
                        builder1.setCancelable(false);

                        builder1.setPositiveButton(
                                "Yes, overwrite",
                                new DialogInterface.OnClickListener()
                                {
                                    public void onClick(DialogInterface dialog, int id)
                                    {
                                        export(context, ExportType.PLAINTEXT, exportFile, scores);
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
                        export(context, ExportType.PLAINTEXT, exportFile, scores);
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

    static private void export (Context context, ExportType exportType, File exportFile, Scores scores)
    {
        switch (exportType)
        {
            case PLAINTEXT:
                plaintextExport(context, exportFile, scores);
                break;

            case CSV:
                try
                {
                    exportCSV(exportFile,scores);
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
                    saveNewEntry(exportFile,scores);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                break;
        }
    }

    private static void plaintextExport(Context context, File exportFile, Scores scores)
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

            outputWriter.write("\r\n\r\nJewel: "                  + jewelForExport(scores.getAutonomousJewelLevel()));
            outputWriter.write("\r\nPre-loaded glyph: "           + glyphForExport(scores.getAutonomousPreloadedGlyphLevel()));
            outputWriter.write("\r\n[Auto] glyphs scored: "       + scores.getAutonomousGlyphsScored());
            outputWriter.write("\r\nAutonomous parking: "         + (scores.getParkingLevel() > 0));
            outputWriter.write("\r\n[Tele-Op] glyphs scored: "    + scores.getTeleOpGlyphsScored());
            outputWriter.write("\r\nCryptobox rows complete: "    + scores.getTeleopCryptoboxRowsComplete());
            outputWriter.write("\r\nCryptobox columns complete: " + scores.getTeleopCryptoboxColumnsComplete());
            outputWriter.write("\r\nCipher completed: "           + (scores.getTeleopCipherLevel() > 0));
            outputWriter.write("\r\nRelic position: "             + scores.getEndgameRelicPosition());
            outputWriter.write("\r\nRelic upright: "              + (scores.getEndgameRelicOrientation() > 0));
            outputWriter.write("\r\nRobot balanced: "             + (scores.getEndgameRobotBalanced() > 0));
            outputWriter.write("\r\nMinor penalties: "            + scores.getNumMinorPenalties());
            outputWriter.write("\r\nMajor penalties: "            + scores.getNumMajorPenalties());
            outputWriter.write("\r\n------------------------");
            outputWriter.write("\r\nTOTAL SCORE: " + scores.getTotalScore());

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
                    .setMessage("The current scores have been exported to:\n\n" + exportFile.getAbsolutePath())
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

    private static void exportCSV (File file, Scores scores) throws IOException
    {
        String array[][] = new String[2][16];

        array[0][0]  = ("Time");
        array[0][1]  = ("Jewel");
        array[0][2]  = ("Pre-loaded glyph");
        array[0][3]  = ("[Auto] glyphs scored");
        array[0][4]  = ("Autonomous parking");
        array[0][5]  = ("[Tele-Op] glyphs scored");
        array[0][6]  = ("Cryptobox rows complete");
        array[0][7]  = ("Cryptobox columns complete");
        array[0][8]  = ("Cipher completed");
        array[0][9]  = ("Relic position");
        array[0][10] = ("Relic upright");
        array[0][11] = ("Robot balanced");
        array[0][12] = ("Minor penalties");
        array[0][13] = ("Major penalties");
        array[0][14] = ("TOTAL SCORE");

        writeScoresToRow(array,scores,1);
        saveToCSV(file,array);
    }

    private static void writeScoresToRow (String[][] array, Scores scores, int rowNumber)
    {
        @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("EEE d MMM yyyy HH:mm");

        array[rowNumber][0]  = (df.format(Calendar.getInstance().getTime()));
        array[rowNumber][1]  = jewelForExport(scores.getAutonomousJewelLevel());
        array[rowNumber][2]  = (glyphForExport(scores.getAutonomousPreloadedGlyphLevel()));
        array[rowNumber][3]  = Integer.toString(scores.getAutonomousGlyphsScored());
        array[rowNumber][4]  = Boolean.toString(scores.getParkingLevel() > 0);
        array[rowNumber][5]  = Integer.toString(scores.getTeleOpGlyphsScored());
        array[rowNumber][6]  = Integer.toString(scores.getTeleopCryptoboxRowsComplete());
        array[rowNumber][7]  = Integer.toString(scores.getTeleopCryptoboxColumnsComplete());
        array[rowNumber][8]  = Boolean.toString(scores.getTeleopCipherLevel() > 0);
        array[rowNumber][9]  = Integer.toString(scores.getEndgameRelicPosition());
        array[rowNumber][10] = Boolean.toString(scores.getEndgameRelicOrientation() > 0);
        array[rowNumber][11] = Boolean.toString(scores.getEndgameRobotBalanced() > 0);
        array[rowNumber][12] = Integer.toString(scores.getNumMinorPenalties());
        array[rowNumber][13] = Integer.toString(scores.getNumMajorPenalties());
        array[rowNumber][14] = Integer.toString(scores.getTotalScore());
    }

    private static void saveToCSV (File file, String[][] array) throws IOException
    {
        CSVWriter writer = new CSVWriter(new FileWriter(file));
        writer.writeAll(Arrays.asList(array));
        writer.close();
    }

    @SuppressLint("SdCardPath")
    private static void beginCSVExport (final Context context, final Scores scores)
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
                        builder1.setMessage("You requested that the current scores be saved to:\n" + exportFile.getAbsolutePath() + "\n\nHowever, that file already exists. Would you like to add to it?");
                        builder1.setCancelable(false);

                        builder1.setPositiveButton(
                                "Yes, add this score to it",
                                new DialogInterface.OnClickListener()
                                {
                                    public void onClick(DialogInterface dialog, int id)
                                    {
                                        export(context, ExportType.CSVnew, exportFile, scores);
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
                        export(context, ExportType.CSV, exportFile, scores);
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

    private static void saveNewEntry (File file, Scores scores) throws IOException
    {
        String[][] readArray = readCSV(file);
        int count = readArray.length;
        String[][] out = new String[count+1][16];
        arrayCopy(readArray,out);
        writeScoresToRow(out,scores,count);
        saveToCSV(file,out);
    }

    private static void arrayCopy(String[][] aSource, String[][] aDestination)
    {
        for (int i = 0; i < aSource.length; i++) {
            System.arraycopy(aSource[i], 0, aDestination[i], 0, aSource[i].length);
        }
    }
}
