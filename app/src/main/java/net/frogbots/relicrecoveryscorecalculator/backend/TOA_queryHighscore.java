package net.frogbots.relicrecoveryscorecalculator.backend;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import net.frogbots.relicrecoveryscorecalculator.BuildConfig;
import net.frogbots.relicrecoveryscorecalculator.ui.UiUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TOA_queryHighscore
{
    private static final String apiApplicationName = "FTC Relic Recovery Scorer";
    private static final String apiKey = BuildConfig.TOA_KEY;
    private static ProgressDialog toaProgressDialog;
    private static final String TOA_HIGHSCORE_ELIM_WITHOUT_PENALTY_URL = "https://theorangealliance.org/apiv2/matches/1718/high-scores/elim-no-penalty";
    private static final String TOA_HIGHSCORE_QUAL_WITHOUT_PENALTY_URL = "https://theorangealliance.org/apiv2/matches/1718/high-scores/qual-no-penalty";
    private static final String TOA_HIGHSCORE_WITH_PENALTY_URL = "https://theorangealliance.org/apiv2/matches/1718/high-scores/with-penalty";
    private static final String RED_SCORE_KEY = "red_score";
    private static final String BLUE_SCORE_KEY = "blue_score";
    private static final String BLUE_PENALTY_KEY = "blue_penalty";
    private static final String RED_PENALTY_KEY = "red_penalty";
    private static final String MATCH_STRING_KEY = "match_key";

    public static int query (final Activity activity)
    {
        toaProgressDialog = new ProgressDialog(activity);
        toaProgressDialog.setTitle("Querying TOA...");
        toaProgressDialog.setMessage("Please wait.");
        toaProgressDialog.setCancelable(false);
        toaProgressDialog.show();

        new Thread(new Runnable()
        {
            @Override
            public void run ()
            {
                try
                {
                    toaProgressDialog.setMessage("Checking internet connectivity...");
                    if (Utils.areWeOnline(activity))
                    {
                        //Thread.sleep(1000);

                        showResult(activity, formatHighscoreMessage(getHighscore(activity, toaProgressDialog)));

                        dismissProgressDialog(activity);
                    }
                    else
                    {
                        UiUtils.showNoInternetDialog(activity);
                        dismissProgressDialog(activity);
                    }
                }
                catch (/*InterruptedException | */IOException | JSONException e)
                {
                    e.printStackTrace();

                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    e.printStackTrace(pw);
                    String sStackTrace = sw.toString(); // stack trace as a string

                    dismissProgressDialog(activity);
                    showSomethingWentWrongDialog(activity, sStackTrace);
                }
            }
        }).start();

        return 0;
    }

    private static void showResult (final Activity activity, final String msg)
    {
        activity.runOnUiThread(new Runnable()
        {
            @Override
            public void run ()
            {
                AlertDialog dialog = new AlertDialog.Builder(activity)
                        .setTitle("Highscore")
                        .setMessage(Html.fromHtml(msg))
                        .setCancelable(false)
                        .setNegativeButton(
                                "Ok",
                                new DialogInterface.OnClickListener()
                                {
                                    public void onClick (DialogInterface dialog, int id)
                                    {
                                        dialog.cancel();
                                    }
                                })
                        .create();

                dialog.show();
                ((TextView) dialog.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
            }
        });
    }

    private static String getJsonFromToa (String url) throws IOException
    {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .header("X-Application-Origin", apiApplicationName)
                .header("X-TOA-Key", apiKey)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    private static void dismissProgressDialog (Activity activity)
    {
        activity.runOnUiThread(new Runnable()
        {
            @Override
            public void run ()
            {
                toaProgressDialog.dismiss();
            }
        });
    }

    private static String formatHighscoreMessage (Highscore highscore)
    {
        return "According to The Orange Alliance, the current high score is " + highscore.score
                + " and was set by the " + highscore.alliance
                + " alliance in match " + "<a href='https://theorangealliance.org/matches/" + highscore.matchKey + "'>" + highscore.matchKey + "</a>";
    }

    private static Highscore getHighscore (Activity activity, final ProgressDialog toaProgressDialog) throws JSONException, IOException
    {
        activity.runOnUiThread(new Runnable()
        {
            @Override
            public void run ()
            {
                toaProgressDialog.setMessage("Downloading 1/3");
            }
        });
        Highscore noPenaltyElims = downloadAndParseHighscore(TOA_HIGHSCORE_ELIM_WITHOUT_PENALTY_URL);
        activity.runOnUiThread(new Runnable()
        {
            @Override
            public void run ()
            {
                toaProgressDialog.setMessage("Downloading 2/3");
            }
        });
        Highscore noPenaltyQuals = downloadAndParseHighscore(TOA_HIGHSCORE_QUAL_WITHOUT_PENALTY_URL);
        activity.runOnUiThread(new Runnable()
        {
            @Override
            public void run ()
            {
                toaProgressDialog.setMessage("Downloading 3/3");
            }
        });
        Highscore highWithPenaltyAfterSubtraction = downloadAndParseHighscore(TOA_HIGHSCORE_WITH_PENALTY_URL);

        if (noPenaltyElims.score > noPenaltyQuals.score)
        {
            if (highWithPenaltyAfterSubtraction.score > noPenaltyElims.score)
            {
                return highWithPenaltyAfterSubtraction;
            }
            else // noPenaltyElims.score > highWithPenaltyAfterSubtraction.score
            {
                return noPenaltyElims;
            }
        }
        else // noPenaltyQuals.score > noPenaltyElims.score
        {
            if (highWithPenaltyAfterSubtraction.score > noPenaltyQuals.score)
            {
                return highWithPenaltyAfterSubtraction;
            }
            else // noPenaltyQuals.score > highWithPenaltyAfterSubtraction.score
            {
                return noPenaltyQuals;
            }
        }
    }

    private static Highscore downloadAndParseHighscore (String url) throws JSONException, IOException
    {
        String data = getJsonFromToa(url);
        JSONArray jsonArray = new JSONArray(data);
        JSONObject jsonObject = jsonArray.getJSONObject(0);

        int blueScore;
        int redScore;
        String matchKey;

        blueScore = Integer.parseInt(jsonObject.getString(BLUE_SCORE_KEY));
        redScore = Integer.parseInt(jsonObject.getString(RED_SCORE_KEY));
        matchKey = jsonObject.getString(MATCH_STRING_KEY);

        System.out.println("Red: " + blueScore);
        System.out.println("Blue: " + redScore);

        if (url.equals(TOA_HIGHSCORE_WITH_PENALTY_URL))
        {
            int bluePenalty;
            int blueScoreMinusPenalty;
            int redPenalty;
            int redScoreMinusPenalty;

            blueScore = Integer.parseInt(jsonObject.getString(BLUE_SCORE_KEY));
            redScore = Integer.parseInt(jsonObject.getString(RED_SCORE_KEY));
            matchKey = jsonObject.getString(MATCH_STRING_KEY);

            bluePenalty = Integer.parseInt(jsonObject.getString(BLUE_PENALTY_KEY));
            redPenalty = Integer.parseInt(jsonObject.getString(RED_PENALTY_KEY));

            redScoreMinusPenalty = redScore - redPenalty;
            blueScoreMinusPenalty = blueScore - bluePenalty;

            if (redScoreMinusPenalty > blueScoreMinusPenalty)
            {
                return new Highscore(matchKey, "red", redScoreMinusPenalty);
            }
            else
            {
                return new Highscore(matchKey, "blue", blueScoreMinusPenalty);
            }
        }
        else
        {
            if (blueScore > redScore)
            {
                return new Highscore(matchKey, "blue", blueScore);
            }
            else
            {
                return new Highscore(matchKey, "red", redScore);
            }
        }
    }

    private static void showSomethingWentWrongDialog (final Activity activity, final String err)
    {
        final String msg = "<b><font color='red'>Something went wrong :(</font></b><br><br>" + err;

        activity.runOnUiThread(new Runnable()
        {
            @Override
            public void run ()
            {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
                builder1.setTitle("Ruh-roh!");
                builder1.setMessage(Html.fromHtml(msg));
                builder1.setCancelable(false);
                builder1.setNegativeButton(
                        "Ok",
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick (DialogInterface dialog, int id)
                            {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });
    }
}
