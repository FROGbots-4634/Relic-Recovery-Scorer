package net.frogbots.relicrecoveryscorecalculator;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

class ExportScores
{
    enum ExportType
    {
        PLAINTEXT,
        CSV,
        GOOGLE_SHEETS
    }

    static void export(final Context context, final Scores scores)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Export to...");

        // add a list
        String[] periods = {"Plaintext", "CSV (Coming soon)", "Google Sheets (Coming soon)"};
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
                break;

            case GOOGLE_SHEETS:
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
}
