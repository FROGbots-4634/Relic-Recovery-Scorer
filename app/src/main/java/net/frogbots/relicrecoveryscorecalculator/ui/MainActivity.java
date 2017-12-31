package net.frogbots.relicrecoveryscorecalculator.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import net.frogbots.relicrecoveryscorecalculator.R;
import net.frogbots.relicrecoveryscorecalculator.backend.Scores;
import net.frogbots.relicrecoveryscorecalculator.backend.TOA_queryHighscore;

import java.util.ArrayList;
import java.util.HashMap;

import static net.frogbots.relicrecoveryscorecalculator.backend.export.Export.REQUEST_EXTERNAL_STORAGE_PERMISSIONS;

@SuppressLint("SetTextI18n")
public class MainActivity extends Activity
{
    Summary summary;

    Spinner[] spinners;
    String[] spinnerNames = new String[] {
            "parkingSpinner",
            "jewelSpinner",
            "glyphSpinner",
            "cipherSpinner",
            "relicPositionSpinner",
            "relicOrientationSpinner",
            "robotBalancedSpinner"
    };

    int[] spinnerIds = new int[] {
            R.id.card_autonomous_parking_spinner,
            R.id.card_autonomous_jewel_spinner,
            R.id.card_autonomous_glyph_spinner,
            R.id.card_teleop_cipher_spinner,
            R.id.card_endgame_relic_position_spinner,
            R.id.card_endgame_relic_orientation_spinner,
            R.id.card_endgame_robot_balanced_spinner
    };

    int[] spinnerLayouts = new int[] {
            R.array.parking,
            R.array.jewelOptions,
            R.array.glyphOptions,
            R.array.CipherOptions,
            R.array.relicPositionOptions,
            R.array.relicOrientationOptions,
            R.array.robotBalancedOptions
    };

    /*
     * Autonomous
     * ---------------------------------------------------------------------------------
     */

    //Number of Glyphs scored in Cryptobox [Autonomous]
    ImageButton autonomousGlyphsScoredPlus;
    TextView autonomousGlyphsScoredTxtView;
    ImageButton autonomousGlyphsScoredMinus;

    /*
     * Teleop
     * ---------------------------------------------------------------------------------
     */

    //Number of Glyphs scored in Cryptobox [Teleop]
    ImageButton teleopGlyphsInCryptoboxPlus;
    TextView teleopGlyphsInCryptoboxTxtView;
    ImageButton teleopGlyphsInCryptoboxMinus;

    //Number of cryptobox rows complete
    ImageButton teleopCryptoboxRowsCompletePlus;
    TextView teleopCryptoboxRowsCompleteTxtView;
    ImageButton teleopCryptoboxRowsCompleteMinus;

    //Number of cryptobox columns complete
    ImageButton teleopCryptoboxColumnsCompletePlus;
    TextView teleopCryptoboxColumnsCompleteTxtView;
    ImageButton teleopCryptoboxColumnsCompleteMinus;

    /*
     * Penalties
     * ----------------------------------------------------------------------------------
     */

    //Number of minor penalties
    ImageButton penaltiesMinorPlus;
    TextView penaltiesMinorTxtView;
    ImageButton penaltiesMinorMinus;

    //Number of major penalties
    ImageButton penaltiesMajorPlus;
    TextView penaltiesMajorTxtView;
    ImageButton penaltiesMajorMinus;

    ScrollView mainScrollView;
    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        summary = new Summary((RelativeLayout) findViewById(R.id.summary_relative_layout));
        doInit();

        timer = new Timer(this);

        (findViewById(R.id.imageView2)).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(!timer.isRunning())
                {
                    timer.showStartDialog(MainActivity.this);
                }
                else
                {
                    timer.stop();
                }
            }
        });

    }

    private void setupSpinners()
    {
        spinners = new Spinner[spinnerNames.length];
        for(int i = 0; i < spinners.length; i++)
        {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, spinnerLayouts[i], android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinners[i] = (Spinner) findViewById(spinnerIds[i]);
            spinners[i].setAdapter(adapter);
            spinners[i].setOnItemSelectedListener(spinnerListener);
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        timer.stop();
    }

    private void doInit()
    {
        showFirstRunGreeting();

        /*
         * Grab handles to all the buttons and text views that we'll be updating
         */
        mainScrollView = (ScrollView) findViewById(R.id.mainScrollView);

        //Number of Glyphs scored in Cryptobox [Autonomous]
        autonomousGlyphsScoredPlus = (ImageButton) findViewById(R.id.autonomous_glyphsInCryptoboxPlus);
        autonomousGlyphsScoredTxtView = (TextView) findViewById(R.id.autonomous_glyphsInCryptoboxTxtView);
        autonomousGlyphsScoredMinus = (ImageButton) findViewById(R.id.autonomous_glyphsInCryptoboxMinus);

        //Number of Glyphs scored in Cryptobox [Teleop]
        teleopGlyphsInCryptoboxPlus = (ImageButton) findViewById(R.id.teleop_glyphsInCryptoboxPlus);
        teleopGlyphsInCryptoboxMinus = (ImageButton) findViewById(R.id.teleop_glyphsInCryptoboxMinus);
        teleopGlyphsInCryptoboxTxtView = (TextView) findViewById(R.id.teleop_glyphsInCryptoboxTxtView);

        //Number of cryptobox rows complete
        teleopCryptoboxRowsCompletePlus = (ImageButton) findViewById(R.id.teleop_cryptoboxRowsCompletePlus);
        teleopCryptoboxRowsCompleteTxtView = (TextView) findViewById(R.id.teleop_cryptoboxRowsCompleteTxtView);
        teleopCryptoboxRowsCompleteMinus = (ImageButton) findViewById(R.id.teleop_cryptoboxRowsCompleteMinus);

        //Number of cryptobox columns complete
        teleopCryptoboxColumnsCompletePlus = (ImageButton) findViewById(R.id.teleop_cryptoboxColumnsCompletePlus);
        teleopCryptoboxColumnsCompleteTxtView = (TextView) findViewById(R.id.teleop_cryptoboxColumnsCompleteTxtView);
        teleopCryptoboxColumnsCompleteMinus = (ImageButton) findViewById(R.id.teleop_cryptoboxColumnsCompleteMinus);

        //Number of minor penalties
        penaltiesMinorPlus = (ImageButton) findViewById(R.id.penalty_minor_plus);
        penaltiesMinorTxtView = (TextView) findViewById(R.id.penalties_minor_txtView);
        penaltiesMinorMinus = (ImageButton) findViewById(R.id.penalty_minor_minus);

        //Number of major penalties
        penaltiesMajorPlus = (ImageButton) findViewById(R.id.penalty_major_plus);
        penaltiesMajorTxtView = (TextView) findViewById(R.id.penalties_major_txtView);
        penaltiesMajorMinus = (ImageButton) findViewById(R.id.penalty_major_minus);

        setupSpinners();

        /*
         * Init all the listeners
         */
        setupAutonomousGlyphsScored();
        setupTeleopGlyphsScored();
        setupTeleopCryptoboxRowsComplete();
        setupTeleopCryptoboxColumnsComplete();
        setupPenaltiesMinor();
        setupPenaltiesMajor();
    }

    private void calcScore()
    {
        TextView scoreView = (TextView) findViewById(R.id.scoreView);
        scoreView.setText("Score: " + Integer.toString(Scores.getTotalScore()));
        summary.updateSummary();
    }

    private void resetScores()
    {
        for (Spinner spinner : spinners)
        {
            spinner.setSelection(0);
        }

        //Auto
        autonomousGlyphsScoredMinus.setEnabled(false);
        autonomousGlyphsScoredTxtView.setText("0");

        //Teleop
        teleopGlyphsInCryptoboxMinus.setEnabled(false);
        teleopGlyphsInCryptoboxTxtView.setText("0");
        teleopCryptoboxRowsCompleteMinus.setEnabled(false);
        teleopCryptoboxRowsCompleteTxtView.setText("0");
        teleopCryptoboxRowsCompletePlus.setEnabled(true);
        teleopCryptoboxColumnsCompleteMinus.setEnabled(false);
        teleopCryptoboxColumnsCompleteTxtView.setText("0");
        teleopCryptoboxColumnsCompletePlus.setEnabled(true);

        //Penalty
        penaltiesMinorMinus.setEnabled(false);
        penaltiesMinorTxtView.setText("0");

        penaltiesMajorMinus.setEnabled(false);
        penaltiesMajorTxtView.setText("0");

        Scores.clearAllScores();
        calcScore();

        timer.stop();

        mainScrollView.smoothScrollTo(0, 0);
    }

    private void setupAutonomousGlyphsScored()
    {
        autonomousGlyphsScoredMinus.setEnabled(false);

        autonomousGlyphsScoredPlus.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Scores.positiveIncrementAutonomousGlyphsScored();
                autonomousGlyphsScoredTxtView.setText(Integer.toString(Scores.getAutonomousGlyphsScored()));
                calcScore();

                if (Scores.getAutonomousGlyphsScored() > 0)
                {
                    autonomousGlyphsScoredMinus.setEnabled(true);
                }
            }
        });

        autonomousGlyphsScoredMinus.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Scores.negativeIncrementAutonomousGlyphsScored();
                autonomousGlyphsScoredTxtView.setText(Integer.toString(Scores.getAutonomousGlyphsScored()));
                calcScore();

                if (Scores.getAutonomousGlyphsScored() < 1)
                {
                    autonomousGlyphsScoredMinus.setEnabled(false);
                }
            }
        });
    }

    private void setupTeleopGlyphsScored()
    {
        teleopGlyphsInCryptoboxMinus.setEnabled(false);

        teleopGlyphsInCryptoboxPlus.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Scores.positiveIncrementTeleopGlyphsScored();
                teleopGlyphsInCryptoboxTxtView.setText(Integer.toString(Scores.getTeleOpGlyphsScored()));
                calcScore();

                if (Scores.getTeleOpGlyphsScored() > 0)
                {
                    teleopGlyphsInCryptoboxMinus.setEnabled(true);
                }
            }
        });

        teleopGlyphsInCryptoboxMinus.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Scores.negativeIncrementTeleopGlyphsScored();
                teleopGlyphsInCryptoboxTxtView.setText(Integer.toString(Scores.getTeleOpGlyphsScored()));
                calcScore();

                if (Scores.getTeleOpGlyphsScored() < 1)
                {
                    teleopGlyphsInCryptoboxMinus.setEnabled(false);
                }
            }
        });
    }

    private void setupTeleopCryptoboxRowsComplete()
    {
        teleopCryptoboxRowsCompleteMinus.setEnabled(false);

        teleopCryptoboxRowsCompletePlus.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Scores.positiveIncrementTeleopCryptoboxRowsComplete();
                teleopCryptoboxRowsCompleteTxtView.setText(Integer.toString(Scores.getTeleopCryptoboxRowsComplete()));
                calcScore();

                if (Scores.getTeleopCryptoboxRowsComplete() > 0)
                {
                    teleopCryptoboxRowsCompleteMinus.setEnabled(true);
                }

                if(Scores.getTeleopCryptoboxRowsComplete() > 3)
                {
                    teleopCryptoboxRowsCompletePlus.setEnabled(false);
                }
            }
        });

        teleopCryptoboxRowsCompleteMinus.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Scores.negativeIncrementTeleopCryptoboxRowsComplete();
                teleopCryptoboxRowsCompleteTxtView.setText(Integer.toString(Scores.getTeleopCryptoboxRowsComplete()));
                calcScore();

                if (Scores.getTeleopCryptoboxRowsComplete() < 1)
                {
                    teleopCryptoboxRowsCompleteMinus.setEnabled(false);
                }

                if(Scores.getTeleopCryptoboxRowsComplete() < 4)
                {
                    teleopCryptoboxRowsCompletePlus.setEnabled(true);
                }
            }
        });
    }

    private void setupTeleopCryptoboxColumnsComplete()
    {
        teleopCryptoboxColumnsCompleteMinus.setEnabled(false);

        teleopCryptoboxColumnsCompletePlus.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Scores.positiveIncrementTeleopCryptoboxColumnsComplete();
                teleopCryptoboxColumnsCompleteTxtView.setText(Integer.toString(Scores.getTeleopCryptoboxColumnsComplete()));
                calcScore();

                if (Scores.getTeleopCryptoboxColumnsComplete() > 0)
                {
                    teleopCryptoboxColumnsCompleteMinus.setEnabled(true);
                }

                if(Scores.getTeleopCryptoboxColumnsComplete() > 2)
                {
                    teleopCryptoboxColumnsCompletePlus.setEnabled(false);
                }
            }
        });

        teleopCryptoboxColumnsCompleteMinus.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Scores.negativeIncrementTeleopCryptoboxColumnsComplete();
                teleopCryptoboxColumnsCompleteTxtView.setText(Integer.toString(Scores.getTeleopCryptoboxColumnsComplete()));
                calcScore();

                if (Scores.getTeleopCryptoboxColumnsComplete() < 1)
                {
                    teleopCryptoboxColumnsCompleteMinus.setEnabled(false);
                }

                if(Scores.getTeleopCryptoboxColumnsComplete() < 3)
                {
                    teleopCryptoboxColumnsCompletePlus.setEnabled(true);
                }
            }
        });
    }

    private void setupPenaltiesMinor()
    {
        penaltiesMinorMinus.setEnabled(false);

        penaltiesMinorPlus.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Scores.positiveIncrementPenaltiesMinor();
                penaltiesMinorTxtView.setText(Integer.toString(Scores.getNumMinorPenalties()));
                calcScore();

                if (Scores.getNumMinorPenalties() > 0)
                {
                    penaltiesMinorMinus.setEnabled(true);
                }
            }
        });

        penaltiesMinorMinus.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Scores.negativeIncrementPenaltiesMinor();
                penaltiesMinorTxtView.setText(Integer.toString(Scores.getNumMinorPenalties()));
                calcScore();

                if (Scores.getNumMinorPenalties() < 1)
                {
                    penaltiesMinorMinus.setEnabled(false);
                }
            }
        });
    }

    private void setupPenaltiesMajor()
    {
        penaltiesMajorMinus.setEnabled(false);

        penaltiesMajorPlus.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Scores.positiveIncrementPenaltiesMajor();
                penaltiesMajorTxtView.setText(Integer.toString(Scores.getNumMajorPenalties()));
                calcScore();

                if (Scores.getNumMajorPenalties() > 0)
                {
                    penaltiesMajorMinus.setEnabled(true);
                }
            }
        });

        penaltiesMajorMinus.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Scores.negativeIncrementPenaltiesMajor();
                penaltiesMajorTxtView.setText(Integer.toString(Scores.getNumMajorPenalties()));
                calcScore();

                if (Scores.getNumMajorPenalties() < 1)
                {
                    penaltiesMajorMinus.setEnabled(false);
                }
            }
        });
    }

    AdapterView.OnItemSelectedListener spinnerListener = new AdapterView.OnItemSelectedListener()
    {
        @Override
        public void onItemSelected (AdapterView<?> parent, View view, int position, long id)
        {
            switch (parent.getId())
            {
                case R.id.card_autonomous_parking_spinner:
                    Scores.setParkingLevel(position);
                    break;

                case R.id.card_autonomous_jewel_spinner:
                    Scores.setAutonomousJewelLevel(position);
                    break;

                case R.id.card_autonomous_glyph_spinner:
                    Scores.setAutonomousPreloadedGlyphLevel(position);
                    break;

                case R.id.card_teleop_cipher_spinner:
                    Scores.setTeleopCipherLevel(position);
                    break;

                case R.id.card_endgame_relic_position_spinner:
                    Scores.setEndgameRelicPosition(position);
                    break;

                case R.id.card_endgame_relic_orientation_spinner:
                    Scores.setEndgameRelicOrientation(position);
                    break;

                case R.id.card_endgame_robot_balanced_spinner:
                    Scores.setEndgameRobotBalanced(position);
                    break;
            }
            calcScore();
        }

        @Override
        public void onNothingSelected (AdapterView<?> parent)
        {

        }
    };

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.aboutMenuItem)
        {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            return true;
        }

        else if (id == R.id.clearMenuItem)
        {
            resetScores();
        }

        else if (id == R.id.exportMenuItem)
        {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                int hasWriteStoragePermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE_PERMISSIONS);
                }
                else
                {
                    launchExportActivity();
                }
            }
            else
            {
                launchExportActivity();
            }

            return true;
        }

        else if (id == R.id.exitMenuItem)
        {
            finish();
        }

        else if (id == R.id.queryHighscoreMenuItem)
        {
            TOA_queryHighscore.query(this);
        }

        return super.onOptionsItemSelected(item);
    }

    private void launchExportActivity()
    {
        Intent intent = new Intent(this, ExportActivity.class);
        startActivity(intent);
    }

    @SuppressLint("NewApi")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        switch (requestCode)
        {
            case REQUEST_EXTERNAL_STORAGE_PERMISSIONS:
                if(shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE))
                {
                    //denied
                    UiUtils.showSimpleOkDialogWithTitle(MainActivity.this, "Permission denied!", "In order for the exported file to be written to disk, you need to grant the external storage permission.");
                }
                else
                {
                    if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                    {
                        //allowed
                        launchExportActivity();
                    }

                    else
                    {
                        //set to never ask again
                        UiUtils.showSimpleOkDialogWithTitle(MainActivity.this, "Permission denied!", "You have permanently denied the external storage permission. In order to use the Export Scores feature, you need to go to your system settings and grant the permission.");
                    }
                }
                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void showFirstRunGreeting()
    {
        /*
         * Grab a handle to the SharedPrefs
         */
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = preferences.edit();

        /*
         * Check if it's the first run
         */
        if(preferences.getBoolean("isFirstRun_b", true))
        {
            /*
             * Yup, it's the first run; build an alert dialog
             */

            String msg = "This app is now open source!<br><br><a href='https://github.com/FROGbots-4634/Relic-Recovery-Scorer'>Check out the repository on GitHub!</a>";

            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Hi there!")
                    .setMessage(Html.fromHtml(msg))
                    .setCancelable(false)
                    .setNegativeButton(
                            "Ok",
                            new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int id)
                                {
                                    dialog.cancel();

                            /*
                             * Now that the user has seen the dialog, set the isFirstRun boolean
                             * to false so that it won't be shown again
                             */
                                    editor.putBoolean("isFirstRun_b", false).apply();
                                }
                            }).create();

            dialog.show();
            ((TextView) dialog.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
        }
    }
}
