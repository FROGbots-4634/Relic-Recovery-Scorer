package net.frogbots.relicrecoveryscorecalculator.backend;

import android.content.Context;
import android.net.ConnectivityManager;
import java.net.InetAddress;

class Utils
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
}
