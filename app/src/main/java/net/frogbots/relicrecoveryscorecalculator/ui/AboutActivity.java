package net.frogbots.relicrecoveryscorecalculator.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.frogbots.relicrecoveryscorecalculator.BuildConfig;
import net.frogbots.relicrecoveryscorecalculator.R;

import java.util.Date;

public class AboutActivity extends Activity
{
    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        /*
         * Make the action bar a button to navigate back
         */
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("About");

        /*
         * Visit our website
         */
        Button vistOurWebsiteBtn = (Button) findViewById(R.id.visitOurWebsite);
        vistOurWebsiteBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View view)
            {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://frogbots.net"));
                startActivity(i);
            }
        });

        /*
         * Visit our YouTube channel
         */
        Button vistOurYoutubeChannelBtn = (Button) findViewById(R.id.visitOurYoutubeChannel);
        vistOurYoutubeChannelBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View view)
            {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://www.youtube.com/channel/UCDNv6JsbwZG4mD7O31jFW-Q"));
                startActivity(i);
            }
        });

        /*
         * Send us an email
         */
        Button sendUsAnEmailBtn = (Button) findViewById(R.id.sendUsAnEmail);
        sendUsAnEmailBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View view)
            {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto: apps@frogbots.net"));
                startActivity(Intent.createChooser(emailIntent, "Send feedback"));
            }
        });

        Date buildDate = new Date(BuildConfig.TIMESTAMP);
        String version = BuildConfig.VERSION_NAME;
        TextView versionTextView = (TextView) findViewById(R.id.versionTextView);
        versionTextView.setText("App v" + version + "\nBuilt on " + buildDate.toString());
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
}
