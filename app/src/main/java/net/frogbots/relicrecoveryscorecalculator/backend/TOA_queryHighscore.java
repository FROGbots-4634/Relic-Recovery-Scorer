package net.frogbots.relicrecoveryscorecalculator.backend;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TOA_queryHighscore
{
    private static final String apiApplicationName = "FTC Relic Recovery Scorer";
    private static final String apiKey = "";
    private static ProgressDialog toaProgressDialog;
    private static final String TOA_HIGHSCORE_ELIM_WITHOUT_PENALTY = "https://theorangealliance.org/apiv2/matches/1718/high-scores/elim-no-penalty";
    private static final String RED_SCORE_STRING = "red_score";
    private static final String BLUE_SCORE_STRING = "blue_score";
    private static final String MATCH_KEY_STRING = "match_key";

    public static int query(final Activity activity)
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
                    if(Utils.areWeOnline(activity))
                    {
                        //Thread.sleep(1000);

                        showResult(activity, getHighscore(getJsonFromToa(TOA_HIGHSCORE_ELIM_WITHOUT_PENALTY)));

                        dismissProgressDialog(activity);
                    }
                    else
                    {
                        showNoInternetDialog(activity);
                        dismissProgressDialog(activity);
                    }
                }
                catch (/*InterruptedException | */IOException | JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }).start();

        return 0;
    }

    private static void showResult(final Activity activity, final String msg)
    {
        activity.runOnUiThread(new Runnable()
        {
            @Override
            public void run ()
            {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
                builder1.setTitle("Highscore");
                builder1.setMessage(msg);
                builder1.setCancelable(false);
                builder1.setNegativeButton(
                        "Ok",
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
        });
    }

    private static void showNoInternetDialog(final Activity activity)
    {
        activity.runOnUiThread(new Runnable()
        {
            @Override
            public void run ()
            {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
                builder1.setTitle("Ruh-roh!");
                builder1.setMessage("You don't appear to be connected to the internet :(");
                builder1.setCancelable(false);
                builder1.setNegativeButton(
                        "Ok",
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
        });
    }

    private static String getJsonFromToa(String url) throws IOException
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

    private static void dismissProgressDialog(Activity activity)
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

    private static Highscore getHighscore (String data) throws JSONException
    {
        JSONArray jsonArray = new JSONArray(data);
        JSONObject jsonObject = jsonArray.getJSONObject(0);

        int blueScore;
        int redScore;
        String matchKey;

        blueScore = Integer.parseInt(jsonObject.getString(BLUE_SCORE_STRING));
        redScore = Integer.parseInt(jsonObject.getString(RED_SCORE_STRING));
        matchKey = jsonObject.getString(MATCH_KEY_STRING);

        System.out.println("Red: " + blueScore);
        System.out.println("Blue: " + redScore);

        if(blueScore > redScore)
        {
            return "According to TOA data, the current high score is " + blueScore + " and was set by the blue alliance in match " + matchKey;
        }
        else
        {
            return "According to TOA data, the current high score is " + redScore + " and was set by the red alliance in match " + matchKey;
        }
    }

    private static Highscore getHighScoreAfterSubtractPenalty(String data) throws JSONException
    {
        JSONArray jsonArray = new JSONArray(data);
        JSONObject match1 = jsonArray.getJSONObject(0);
        JSONObject match2 = jsonArray.getJSONObject(0);

        int blueScore1 = Integer.parseInt(match1.getString(BLUE_SCORE_STRING));
        int redScore1 = Integer.parseInt(match1.getString(RED_SCORE_STRING));
        String matchKey1 = match1.getString(MATCH_KEY_STRING);
        int blueScore2 = Integer.parseInt(match2.getString(BLUE_SCORE_STRING));
        int redScore2 = Integer.parseInt(match2.getString(RED_SCORE_STRING));
        String matchKey2 = match1.getString(MATCH_KEY_STRING);


    }
}
