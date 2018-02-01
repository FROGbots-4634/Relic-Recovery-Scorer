package net.frogbots.relicrecoveryscorecalculator.backend;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.os.Environment;

import java.io.File;
import java.net.InetAddress;

public class Utils
{
    public static boolean areWeOnline (Context context)
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

    public static String getExportDirPath()
    {
        return Environment.getExternalStorageDirectory() + "/RelicRecoveryScorer/";
    }
}
