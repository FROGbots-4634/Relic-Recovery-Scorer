package net.frogbots.relicrecoveryscorecalculator.backend;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;

import java.net.InetAddress;

public class Utils
{
    static boolean areWeOnline (Context context)
    {
        return areWeConnectedToANetwork(context) && canWeActuallyAccessTheInternet();
    }

    private static boolean areWeConnectedToANetwork (Context context)
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    private static boolean canWeActuallyAccessTheInternet ()
    {
        try
        {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            return !ipAddr.equals("");
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public static void showSimpleOkDialog(final Activity activity, String msg)
    {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
        builder1.setMessage(msg);
        builder1.setCancelable(false);
        builder1.setNegativeButton(
                "Ok",
                new DialogInterface.OnClickListener()
                {
                    @SuppressLint("NewApi")
                    public void onClick(DialogInterface dialog, int id)
                    {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    static void showNoInternetDialog(final Activity activity)
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
}
