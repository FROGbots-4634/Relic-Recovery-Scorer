package net.frogbots.relicrecoveryscorecalculator.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import net.frogbots.relicrecoveryscorecalculator.R;
import net.frogbots.relicrecoveryscorecalculator.backend.Scores;
import net.frogbots.relicrecoveryscorecalculator.backend.export.Export;
import net.frogbots.relicrecoveryscorecalculator.backend.export.ExportBundle;
import net.frogbots.relicrecoveryscorecalculator.backend.export.ExportDirAdapter;
import net.frogbots.relicrecoveryscorecalculator.backend.export.ExportType;

import java.io.File;
import java.io.IOException;

public class ExportActivity extends Activity
{
    Spinner typeSpinner;
    ArrayAdapter<CharSequence> typeAdapter;

    ProgressDialog progressDialog;
    Button exportButton;

    ExportType exportType;
    Scores scores;

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);

        exportButton = (Button) findViewById(R.id.exportButton);
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
                ExportBundle bundle = new ExportBundle();
                bundle.activity = ExportActivity.this;
                bundle.exportType = exportType;
                bundle.match = "Q-37";
                bundle.comment = "8619 died from ESD";
                bundle.scores = scores;
                bundle.filename = "hellu";

                Export.exportWithPermissionsWrapper(bundle);
            }
        });
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

    private void alertDialogTest() throws IOException
    {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
        builderSingle.setTitle("Select a file to add to:");

        final ArrayAdapter<File> arrayAdapter = new ExportDirAdapter(this, "/sdcard/RelicRecoveryScorer");

        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                String strName = arrayAdapter.getItem(which).getName();
                AlertDialog.Builder builderInner = new AlertDialog.Builder(ExportActivity.this);
                builderInner.setMessage(strName);
                builderInner.setTitle("Your Selected Item is");
                builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builderInner.show();
            }
        });
        builderSingle.show();
    }

    private void setupTypeSpinner()
    {
        // Create an ArrayAdapter using the string array and a default spinner layout
        typeAdapter = ArrayAdapter.createFromResource(this, R.array.exportTypeOptions, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        typeSpinner.setAdapter(typeAdapter);

        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l)
            {
                if(pos == 0) //Plaintext
                {
                    exportType = ExportType.PLAINTEXT;
                }
                else if(pos == 1) //CSV
                {
                    exportType = ExportType.CSV;
                }
                else if(pos == 2) //Google sheets
                {
                    typeSpinner.setSelection(1);
                    exportType = ExportType.GOOGLE_SHEETS;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });
    }
}
