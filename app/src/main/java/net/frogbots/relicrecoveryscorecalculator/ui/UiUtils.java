package net.frogbots.relicrecoveryscorecalculator.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class UiUtils
{
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

    public static void showSimpleOkDialogWithTitle (final Activity activity, String title, String msg)
    {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
        builder1.setMessage(msg);
        builder1.setTitle(title);
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

    public static void showNoInternetDialog(final Activity activity)
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
