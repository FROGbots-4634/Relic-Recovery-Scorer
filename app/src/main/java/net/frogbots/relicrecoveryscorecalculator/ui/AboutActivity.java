package net.frogbots.relicrecoveryscorecalculator.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import net.frogbots.relicrecoveryscorecalculator.R;

public class AboutActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
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
            public void onClick(View view)
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
            public void onClick(View view)
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
            public void onClick(View view)
            {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto: apps@frogbots.net"));
                startActivity(Intent.createChooser(emailIntent, "Send feedback"));
            }
        });
    }

    /*
     * The method that's called when the user presses the title back button
     */
    @Override
    public boolean onNavigateUp()
    {
        finish();
        return true;
    }
}
