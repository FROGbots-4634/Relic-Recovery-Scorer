package net.frogbots.relicrecoveryscorecalculator.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import net.frogbots.relicrecoveryscorecalculator.R;
import net.frogbots.relicrecoveryscorecalculator.backend.Scores;
import net.frogbots.relicrecoveryscorecalculator.backend.Utils;
import net.frogbots.relicrecoveryscorecalculator.backend.export.Export;
import net.frogbots.relicrecoveryscorecalculator.backend.export.ExportBundle;
import net.frogbots.relicrecoveryscorecalculator.backend.export.ExportDirAdapter;
import net.frogbots.relicrecoveryscorecalculator.backend.export.ExportType;
import net.frogbots.relicrecoveryscorecalculator.backend.export.csv.CsvNotCompatibleException;
import net.frogbots.relicrecoveryscorecalculator.backend.export.csv.FileAlreadyExistsException;

import java.io.File;
import java.io.IOException;

public class ExportActivity extends Activity
{
    Spinner typeSpinner;
    ArrayAdapter<CharSequence> typeAdapter;

    EditText commentEditText;
    EditText matchEditText;
    EditText filenameEditText;
    EditText teamEditText;
    TextView filenameHeader;

    ProgressDialog progressDialog;
    Button exportButton;
    Button exportToLastFileBtn;

    ExportType exportType;
    Scores scores;
    ExportBundle bundle = new ExportBundle();

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor prefsEditor;

    private static final String KEY_LAST_CSV_FILE_PATH = "lastCsvExportFilePath";
    private static final String KEY_LAST_CSV_FILE_NAME = "lastCsvExportFileName";

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        prefsEditor = sharedPreferences.edit();

        setupSdcardDir();

        teamEditText = (EditText) findViewById(R.id.teamTxtView);
        commentEditText = (EditText) findViewById(R.id.commentTextView);
        matchEditText = (EditText) findViewById(R.id.matchTextView);
        filenameEditText = (EditText) findViewById(R.id.filenameEditTet);
        filenameHeader = (TextView) findViewById(R.id.filenameHeader);
        exportButton = (Button) findViewById(R.id.exportButton);
        exportToLastFileBtn = (Button) findViewById(R.id.exportToLastFileButton);
        typeSpinner = (Spinner) findViewById(R.id.typeSpinner);
        setupTypeSpinner();

        /*
         * Make the action bar a button to navigate back
         */
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("Export");

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Exporting");
        progressDialog.setMessage("Just a moment...");

        exportButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View view)
            {
                bundle.activity = ExportActivity.this;
                bundle.exportType = exportType;
                bundle.match = matchEditText.getText().toString();
                bundle.team = teamEditText.getText().toString();
                bundle.comment = commentEditText.getText().toString();
                bundle.scores = scores;
                bundle.filename = filenameEditText.getText().toString();

                if (exportType == ExportType.CSV_ADD)
                {
                    try
                    {
                        showCsvAppendFileChooserDialog();
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                {
                    handleExportAndErrors();
                }
            }
        });

        exportToLastFileBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View view)
            {
                bundle.activity = ExportActivity.this;
                bundle.exportType = exportType;
                bundle.match = matchEditText.getText().toString();
                bundle.team = teamEditText.getText().toString();
                bundle.comment = commentEditText.getText().toString();
                bundle.scores = scores;
                bundle.fileForCsvAdd = new File(sharedPreferences.getString(KEY_LAST_CSV_FILE_PATH, ""));

                handleExportAndErrors();
            }
        });

        System.out.println("Last file: " + sharedPreferences.getString(KEY_LAST_CSV_FILE_PATH, ""));
        System.out.println("Last filename: " + sharedPreferences.getString(KEY_LAST_CSV_FILE_NAME, ""));
    }

    /*
     * The method that's called when the user presses the title back button
     */
    @Override
    public boolean onNavigateUp ()
    {
        finish();
        return true;
    }

    private void handleExportAndErrors()
    {
        try
        {
            File resultFile = Export.doExport(bundle);

            if(bundle.exportType == ExportType.CSV_ADD || bundle.exportType == ExportType.CSV_NEW)
            {
                prefsEditor.putString(KEY_LAST_CSV_FILE_PATH, resultFile.getAbsolutePath()).apply();
                prefsEditor.putString(KEY_LAST_CSV_FILE_NAME, resultFile.getName()).apply();
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle("File exported!")
                    .setMessage("The current Scores have been exported to:\n\n" + resultFile.getAbsolutePath())
                    .setNegativeButton("Ok", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick (DialogInterface dialogInterface, int i)
                        {
                            finish();
                        }
                    })
                    /*.setPositiveButton("Share", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick (DialogInterface dialogInterface, int i)
                        {
                            if(exportType == ExportType.PLAINTEXT)
                            {
                                String shareBody = "Here is the share content body";
                                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                                sharingIntent.setType("text/csv");
                                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                                startActivity(sharingIntent);
                            }
                        }
                    })*/;
            builder.show();
        }
        catch (IOException e)
        {
            UiUtils.showSimpleOkDialogWithTitle(this, "Ruh-roh!", "Export failed!");
            e.printStackTrace();
        }
        catch (CsvNotCompatibleException e)
        {
            UiUtils.showSimpleOkDialogWithTitle(this, "Ruh-roh!", "This CSV file is not compatible with this version of the app!");
            e.printStackTrace();
        }
        catch (FileAlreadyExistsException e)
        {
            UiUtils.showSimpleOkDialogWithTitle(this, "File exists!", "The file you requested to export to already exists!");
            e.printStackTrace();
        }
    }

    private void showCsvAppendFileChooserDialog () throws IOException
    {
        final AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
        builderSingle.setTitle("Select a file to add to:");

        final ArrayAdapter<File> arrayAdapter = new ExportDirAdapter(this, Utils.getExportDirPath());

        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick (DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick (DialogInterface dialog, int which)
            {
                bundle.fileForCsvAdd = arrayAdapter.getItem(which);
                handleExportAndErrors();
            }
        });
        builderSingle.show();
    }

    private void setupSdcardDir()
    {
        File dir = new File(Utils.getExportDirPath());

        if(!dir.exists())
        {
            dir.mkdir();
        }
    }

    private void setupTypeSpinner ()
    {
        // Create an ArrayAdapter using the string array and a default spinner layout
        typeAdapter = ArrayAdapter.createFromResource(this, R.array.exportTypeOptions, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        typeSpinner.setAdapter(typeAdapter);

        /*
         * If the last export file exists, automatically set the export type to
         * CSV append
         */
        if (new File(sharedPreferences.getString(KEY_LAST_CSV_FILE_PATH, "")).exists())
        {
            typeSpinner.setSelection(2);
        }

        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected (AdapterView<?> adapterView, View view, int pos, long l)
            {
                if (pos == 0) //Plaintext
                {
                    handleFilenameVisibility(false);
                    handleAppendToLastBtnVisibility(false);
                    exportType = ExportType.PLAINTEXT;
                }
                else if (pos == 1) //CSV_ADD
                {
                    handleFilenameVisibility(false);
                    handleAppendToLastBtnVisibility(false);
                    exportType = ExportType.CSV_NEW;
                }
                else if (pos == 2) //CSV_ADD add to existing
                {
                    handleFilenameVisibility(true);
                    handleAppendToLastBtnVisibility(true);
                    exportType = ExportType.CSV_ADD;
                }
                else if (pos == 3) //Google sheets
                {
                    handleFilenameVisibility(false);
                    handleAppendToLastBtnVisibility(false);
                    typeSpinner.setSelection(1);
                    exportType = ExportType.GOOGLE_SHEETS;
                }
            }

            @Override
            public void onNothingSelected (AdapterView<?> adapterView)
            {

            }
        });
    }

    private void handleFilenameVisibility (boolean b)
    {
        if (b)
        {
            filenameHeader.setVisibility(View.GONE);
            filenameEditText.setVisibility(View.GONE);
            exportButton.setText("Select file to append to");
        }
        else
        {
            filenameHeader.setVisibility(View.VISIBLE);
            filenameEditText.setVisibility(View.VISIBLE);
            exportButton.setText("Export");
        }
    }

    private void handleAppendToLastBtnVisibility(boolean b)
    {
        if(b)
        {
            /*
             * Only show the button if the last file exists
             */
            if(new File(sharedPreferences.getString(KEY_LAST_CSV_FILE_PATH, "")).exists())
            {
                exportToLastFileBtn.setVisibility(View.VISIBLE);
                exportToLastFileBtn.setText(getAppendToLastBtnText());
            }
        }
        else
        {
            exportToLastFileBtn.setVisibility(View.GONE);
        }
    }

    private String getAppendToLastBtnText()
    {
        String name = sharedPreferences.getString(KEY_LAST_CSV_FILE_NAME, "");
        return "Append to last file [" + name + "]";
    }
}
