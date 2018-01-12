package net.frogbots.relicrecoveryscorecalculator.ui;

import android.os.Bundle;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;

import net.frogbots.relicrecoveryscorecalculator.R;

public class AppIntroActivity extends IntroActivity
{
    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Add slides, edit configuration...

        addSlide(new SimpleSlide.Builder()
                         .title("Hello!")
                         .description("Thanks for downloading our app! Here's a quick showcase of what it can do.")
                         .image(R.drawable.ic_smiley)
                         .background(R.color.teleop)
                         .backgroundDark(R.color.teleop_dark)
                         .scrollable(false)
                         .build());

        addSlide(new SimpleSlide.Builder()
                         .title("Match Timer")
                         .description("There is an integrated match timer just under the score display. Click the play arrow to the left of the score to start the timer.")
                         .image(R.drawable.ic_timer)
                         .background(R.color.teleop)
                         .backgroundDark(R.color.teleop_dark)
                         .scrollable(false)
                         .build());

        addSlide(new SimpleSlide.Builder()
                         .title("Summary Section")
                         .description("The summary section at the bottom of the scroll pane provides a breakdown of the total score.")
                         .image(R.drawable.ic_bars)
                         .background(R.color.teleop)
                         .backgroundDark(R.color.teleop_dark)
                         .scrollable(false)
                         .build());

        addSlide(new SimpleSlide.Builder()
                         .title("Query Highscore")
                         .description("Click the query highscore button in the overflow menu to fetch the current world record from The Orange Alliance.")
                         .image(R.drawable.ic_file_download)
                         .background(R.color.teleop)
                         .backgroundDark(R.color.teleop_dark)
                         .scrollable(false)
                         .build());

        addSlide(new SimpleSlide.Builder()
                         .title("Export Function")
                         .description("The option to export scores can be very useful when scouting at competition or just keeping track of how your team gets better with practice. Scores can either be exported to a plaintext file, written to a new CSV file, or appended to an existing CSV file.")
                         .image(R.drawable.ic_import_export)
                         .background(R.color.teleop)
                         .backgroundDark(R.color.teleop_dark)
                         .scrollable(false)
                         .build());

        addSlide(new SimpleSlide.Builder()
                         .title("Protip")
                         .description("Don't forget about the clear button :)")
                         .image(R.drawable.ic_clear_all)
                         .background(R.color.teleop)
                         .backgroundDark(R.color.teleop_dark)
                         .scrollable(false)
                         .build());

        addSlide(new SimpleSlide.Builder()
                         .title("This app is open source!")
                         .description("Contribute to development on GitHub!")
                         .image(R.drawable.ic_code)
                         .background(R.color.teleop)
                         .backgroundDark(R.color.teleop_dark)
                         .scrollable(false)
                         .build());
    }
}