package net.frogbots.relicrecoveryscorecalculator.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import net.frogbots.relicrecoveryscorecalculator.backend.ExportScores;
import net.frogbots.relicrecoveryscorecalculator.backend.Scores;
import net.frogbots.relicrecoveryscorecalculator.backend.TOA_queryHighscore;

@SuppressLint("SetTextI18n")
public class MainActivity extends Activity
{
    Scores scores = new Scores();
    Summary summary;

    /*
     * Parking spinner
     */
    Spinner parkingSpinner;
    ArrayAdapter<CharSequence> parkingAdapter;

    /*
     * Jewel spinner
     */
    Spinner jewelSpinner;
    ArrayAdapter<CharSequence> jewelAdapter;

    /*
     * Glyph spinner
     */
    Spinner glyphSpinner;
    ArrayAdapter<CharSequence> glyphAdapter;

    /*
     * Cipher spinner
     */
    Spinner cipherSpinner;
    ArrayAdapter<CharSequence> cipherAdapter;

    /*
     * Relic position spinner
     */
    Spinner relicPositionSpinner;
    ArrayAdapter<CharSequence> relicPositionAdapter;

    /*
     * Relic orientation spinner
     */
    Spinner relicOrientationSpinner;
    ArrayAdapter<CharSequence> relicOrientationAdapter;

    /*
     * Robot balanced spinner
     */
    Spinner robotBalancedSpinner;
    ArrayAdapter<CharSequence> robotBalancedAdapter;

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

        /*
         * Setup spinners
         */
        setupParkingSpinner();
        setupJewelSpinner();
        setupGlyphSpinner();
        setupCipherSpinner();
        setupRelicPositionSpinner();
        setupRelicOrientationSpinner();
        setupRobotBalancedSpinner();

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
        if(relicPositionSpinner.getSelectedItemPosition() == 0)
        {
            relicOrientationSpinner.setEnabled(false);
            relicOrientationSpinner.setSelection(0);
        }
        else
        {
            relicOrientationSpinner.setEnabled(true);
        }
        TextView scoreView = (TextView) findViewById(R.id.scoreView);
        scoreView.setText("Score: " + Integer.toString(scores.getTotalScore()));
        summary.updateSummary(scores);
    }

    private void resetScores()
    {
        //Auto
        jewelSpinner.setSelection(0);
        glyphSpinner.setSelection(0);
        autonomousGlyphsScoredMinus.setEnabled(false);
        autonomousGlyphsScoredTxtView.setText("0");
        parkingSpinner.setSelection(0);

        //Teleop
        teleopGlyphsInCryptoboxMinus.setEnabled(false);
        teleopGlyphsInCryptoboxTxtView.setText("0");
        teleopCryptoboxRowsCompleteMinus.setEnabled(false);
        teleopCryptoboxRowsCompleteTxtView.setText("0");
        teleopCryptoboxRowsCompletePlus.setEnabled(true);
        teleopCryptoboxColumnsCompleteMinus.setEnabled(false);
        teleopCryptoboxColumnsCompleteTxtView.setText("0");
        teleopCryptoboxColumnsCompletePlus.setEnabled(true);
        cipherSpinner.setSelection(0);

        //Endgame
        relicPositionSpinner.setSelection(0);
        relicOrientationSpinner.setSelection(0);
        robotBalancedSpinner.setSelection(0);

        //Penalty
        penaltiesMinorMinus.setEnabled(false);
        penaltiesMinorTxtView.setText("0");

        penaltiesMajorMinus.setEnabled(false);
        penaltiesMajorTxtView.setText("0");

        scores.clearAllScores();
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
                scores.positiveIncrementAutonomousGlyphsScored();
                autonomousGlyphsScoredTxtView.setText(Integer.toString(scores.getAutonomousGlyphsScored()));
                calcScore();

                if (scores.getAutonomousGlyphsScored() > 0)
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
                scores.negativeIncrementAutonomousGlyphsScored();
                autonomousGlyphsScoredTxtView.setText(Integer.toString(scores.getAutonomousGlyphsScored()));
                calcScore();

                if (scores.getAutonomousGlyphsScored() < 1)
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
                scores.positiveIncrementTeleopGlyphsScored();
                teleopGlyphsInCryptoboxTxtView.setText(Integer.toString(scores.getTeleOpGlyphsScored()));
                calcScore();

                if (scores.getTeleOpGlyphsScored() > 0)
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
                scores.negativeIncrementTeleopGlyphsScored();
                teleopGlyphsInCryptoboxTxtView.setText(Integer.toString(scores.getTeleOpGlyphsScored()));
                calcScore();

                if (scores.getTeleOpGlyphsScored() < 1)
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
                scores.positiveIncrementTeleopCryptoboxRowsComplete();
                teleopCryptoboxRowsCompleteTxtView.setText(Integer.toString(scores.getTeleopCryptoboxRowsComplete()));
                calcScore();

                if (scores.getTeleopCryptoboxRowsComplete() > 0)
                {
                    teleopCryptoboxRowsCompleteMinus.setEnabled(true);
                }

                if(scores.getTeleopCryptoboxRowsComplete() > 3)
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
                scores.negativeIncrementTeleopCryptoboxRowsComplete();
                teleopCryptoboxRowsCompleteTxtView.setText(Integer.toString(scores.getTeleopCryptoboxRowsComplete()));
                calcScore();

                if (scores.getTeleopCryptoboxRowsComplete() < 1)
                {
                    teleopCryptoboxRowsCompleteMinus.setEnabled(false);
                }

                if(scores.getTeleopCryptoboxRowsComplete() < 4)
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
                scores.positiveIncrementTeleopCryptoboxColumnsComplete();
                teleopCryptoboxColumnsCompleteTxtView.setText(Integer.toString(scores.getTeleopCryptoboxColumnsComplete()));
                calcScore();

                if (scores.getTeleopCryptoboxColumnsComplete() > 0)
                {
                    teleopCryptoboxColumnsCompleteMinus.setEnabled(true);
                }

                if(scores.getTeleopCryptoboxColumnsComplete() > 2)
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
                scores.negativeIncrementTeleopCryptoboxColumnsComplete();
                teleopCryptoboxColumnsCompleteTxtView.setText(Integer.toString(scores.getTeleopCryptoboxColumnsComplete()));
                calcScore();

                if (scores.getTeleopCryptoboxColumnsComplete() < 1)
                {
                    teleopCryptoboxColumnsCompleteMinus.setEnabled(false);
                }

                if(scores.getTeleopCryptoboxColumnsComplete() < 3)
                {
                    teleopCryptoboxColumnsCompletePlus.setEnabled(true);
                }
            }
        });
    }

    private void setupParkingSpinner()
    {
        /*
         * Grab a handle to the parking spinner
         */
        parkingSpinner = (Spinner) findViewById(R.id.card_autonomous_parking_spinner);

        // Create an ArrayAdapter using the string array and a default spinner layout
        parkingAdapter = ArrayAdapter.createFromResource(this, R.array.parking, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        parkingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        parkingSpinner.setAdapter(parkingAdapter);

        parkingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l)
            {
                if (adapterView.getId() == R.id.card_autonomous_parking_spinner)
                {
                    scores.setParkingLevel(pos);
                    calcScore();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });
    }

    private void setupJewelSpinner()
    {
        /*
         * Grab a handle to the jewel spinner
         */
        jewelSpinner = (Spinner) findViewById(R.id.card_autonomous_jewel_spinner);

        // Create an ArrayAdapter using the string array and a default spinner layout
        jewelAdapter = ArrayAdapter.createFromResource(this, R.array.jewelOptions, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        jewelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        jewelSpinner.setAdapter(jewelAdapter);

        jewelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l)
            {
                if (adapterView.getId() == R.id.card_autonomous_jewel_spinner)
                {
                    scores.setAutonomousJewelLevel(pos);
                    calcScore();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });
    }

    private void setupGlyphSpinner()
    {
        /*
         * Grab a handle to the glyph spinner
         */
        glyphSpinner = (Spinner) findViewById(R.id.card_autonomous_glyph_spinner);

        // Create an ArrayAdapter using the string array and a default spinner layout
        glyphAdapter = ArrayAdapter.createFromResource(this, R.array.glyphOptions, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        glyphAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        glyphSpinner.setAdapter(glyphAdapter);

        glyphSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l)
            {
                if (adapterView.getId() == R.id.card_autonomous_glyph_spinner)
                {
                    scores.setAutonomousPreloadedGlyphLevel(pos);
                    calcScore();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });
    }

    private void setupCipherSpinner()
    {
        /*
         * Grab a handle to the cipher spinner
         */
        cipherSpinner = (Spinner) findViewById(R.id.card_teleop_cipher_spinner);

        // Create an ArrayAdapter using the string array and a default spinner layout
        cipherAdapter = ArrayAdapter.createFromResource(this, R.array.CipherOptions, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        cipherAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        cipherSpinner.setAdapter(cipherAdapter);

        cipherSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l)
            {
                if (adapterView.getId() == R.id.card_teleop_cipher_spinner)
                {
                    scores.setTeleopCipherLevel(pos);
                    calcScore();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });
    }

    private void setupRelicPositionSpinner()
    {
        /*
         * Grab a handle to the relic position spinner
         */
        relicPositionSpinner = (Spinner) findViewById(R.id.card_endgame_relic_position_spinner);

        // Create an ArrayAdapter using the string array and a default spinner layout
        relicPositionAdapter = ArrayAdapter.createFromResource(this, R.array.relicPositionOptions, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        relicPositionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        relicPositionSpinner.setAdapter(relicPositionAdapter);

        relicPositionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l)
            {
                if (adapterView.getId() == R.id.card_endgame_relic_position_spinner)
                {
                    scores.setEndgameRelicPosition(pos);
                    calcScore();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });
    }

    private void setupRelicOrientationSpinner()
    {
        /*
         * Grab a handle to the relic orientation spinner
         */
        relicOrientationSpinner = (Spinner) findViewById(R.id.card_endgame_relic_orientation_spinner);

        // Create an ArrayAdapter using the string array and a default spinner layout
        relicOrientationAdapter = ArrayAdapter.createFromResource(this, R.array.relicOrientationOptions, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        relicOrientationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        relicOrientationSpinner.setAdapter(relicOrientationAdapter);

        relicOrientationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l)
            {
                if (adapterView.getId() == R.id.card_endgame_relic_orientation_spinner)
                {
                    scores.setEndgameRelicOrientation(pos);
                    calcScore();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });
    }

    private void setupRobotBalancedSpinner()
    {
        /*
         * Grab a handle to the relic orientation spinner
         */
        robotBalancedSpinner = (Spinner) findViewById(R.id.card_endgame_robot_balanced_spinner);

        // Create an ArrayAdapter using the string array and a default spinner layout
        robotBalancedAdapter = ArrayAdapter.createFromResource(this, R.array.robotBalancedOptions, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        robotBalancedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        robotBalancedSpinner.setAdapter(robotBalancedAdapter);

        robotBalancedSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l)
            {
                if (adapterView.getId() == R.id.card_endgame_robot_balanced_spinner)
                {
                    scores.setEndgameRobotBalanced(pos);
                    calcScore();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

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
                scores.positiveIncrementPenaltiesMinor();
                penaltiesMinorTxtView.setText(Integer.toString(scores.getNumMinorPenalties()));
                calcScore();

                if (scores.getNumMinorPenalties() > 0)
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
                scores.negativeIncrementPenaltiesMinor();
                penaltiesMinorTxtView.setText(Integer.toString(scores.getNumMinorPenalties()));
                calcScore();

                if (scores.getNumMinorPenalties() < 1)
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
                scores.positiveIncrementPenaltiesMajor();
                penaltiesMajorTxtView.setText(Integer.toString(scores.getNumMajorPenalties()));
                calcScore();

                if (scores.getNumMajorPenalties() > 0)
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
                scores.negativeIncrementPenaltiesMajor();
                penaltiesMajorTxtView.setText(Integer.toString(scores.getNumMajorPenalties()));
                calcScore();

                if (scores.getNumMajorPenalties() < 1)
                {
                    penaltiesMajorMinus.setEnabled(false);
                }
            }
        });
    }

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
            ExportScores.export(this, scores);
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
        if(preferences.getBoolean("isFirstRun", true))
        {
            /*
             * Yup, it's the first run; build an alert dialog
             */
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setTitle("Hi there!");
            builder1.setMessage("Thanks for downloading our app! We hope you like it!");
            builder1.setCancelable(false);
            builder1.setNegativeButton(
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
                            editor.putBoolean("isFirstRun", false).apply();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        }
    }

    private void exportScoresComingSoon()
    {
        AlertDialog.Builder finishedDialog = new AlertDialog.Builder(MainActivity.this);
        finishedDialog.
                setTitle("Coming soon!")
                .setMessage("Export functionality coming soon!")
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
