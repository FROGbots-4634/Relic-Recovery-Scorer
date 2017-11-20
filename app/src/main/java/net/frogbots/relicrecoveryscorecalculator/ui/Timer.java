package net.frogbots.relicrecoveryscorecalculator.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import net.frogbots.relicrecoveryscorecalculator.R;

@SuppressLint("SetTextI18n")
class Timer
{
    private Activity activity;
    private final TextView timer;
    private final ProgressBar progressBar;
    private CountDownTimer countDownTimer;
    private boolean running = false;
    private ImageView imageView;
    private MediaPlayer mediaPlayer;

    Timer(Activity activity)
    {
        this.activity = activity;
        timer = (TextView) activity.findViewById(R.id.timer);
        progressBar = (ProgressBar) activity.findViewById(R.id.progressBar);
        imageView = (ImageView) activity.findViewById(R.id.imageView2);
    }

    void showStartDialog(Context context)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Start from...");

        // add a list
        String[] periods = {"Autonomous", "Auto --> Tele-Op transition", "Tele-Op", "Endgame"};
        builder.setItems(periods, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                switch (which)
                {
                    case 0: //Auton
                        autoTimer();
                        running = true;
                        imageView.setImageResource(R.drawable.ic_stop);
                        break;

                    case 1: //Auto --> tele transition
                        autoToTeleTransitionTimer();
                        running = true;
                        imageView.setImageResource(R.drawable.ic_stop);
                        break;

                    case 2: //Tele-Op
                        teleopTimer();
                        running = true;
                        imageView.setImageResource(R.drawable.ic_stop);
                        break;

                    case 3: //Endgame
                        endTimer();
                        running = true;
                        imageView.setImageResource(R.drawable.ic_stop);
                        break;
                }
            }
        });

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    void stop()
    {
        running = false;
        try
        {
            countDownTimer.cancel();
            if (mediaPlayer.isPlaying())
            {
                mediaPlayer.stop();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        progressBar.setProgress(0);
        timer.setText("Timer not running");
        imageView.setImageResource(R.drawable.ic_play_arrow);
    }

    boolean isRunning()
    {
        return running;
    }

    private void autoTimer()
    {
        progressBar.setMax(300);
        progressBar.setProgressDrawable(activity.getResources().getDrawable(R.drawable.progress_drawable_green));

        mediaPlayer = MediaPlayer.create(activity, R.raw.charge);
        mediaPlayer.start();

        countDownTimer = new CountDownTimer(30000, 100)
        {
            public void onTick(long millisUntilFinished)
            {
                String time;


                int seconds = (int) (millisUntilFinished / 1000) % 60;
                int minutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);

                time = "2:" + formatSeconds(seconds);

                timer.setText("[Autonomous] " + time);

                progressBar.setProgress((int) ((millisUntilFinished) / 100));
            }

            public void onFinish()
            {
                MediaPlayer mPlayer = MediaPlayer.create(activity, R.raw.endauto);
                mPlayer.start();
                mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
                {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer)
                    {
                        autoToTeleTransitionTimer();
                    }
                });
            }
        }.start();
    }

    private void autoToTeleTransitionTimer()
    {
        progressBar.setMax(50);
        progressBar.setProgressDrawable(activity.getResources().getDrawable(R.drawable.progress_drawable_orange));

        mediaPlayer = MediaPlayer.create(activity, R.raw.factwhistle);
        mediaPlayer.start();

        countDownTimer = new CountDownTimer(5000, 100)
        {
            public void onTick(long millisUntilFinished)
            {
                String time;


                int seconds = (int) (millisUntilFinished / 1000) % 60;
                int minutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);

                time = "0:" + formatSeconds(seconds);

                timer.setText("[A-->T transition] " + time);

                progressBar.setProgress((int) ((millisUntilFinished) / 100));
            }

            public void onFinish()
            {
                teleopTimer();
            }

        }.start();
    }

    private void teleopTimer()
    {
        progressBar.setMax(900);
        progressBar.setProgressDrawable(activity.getResources().getDrawable(R.drawable.progress_drawable_blue));

        mediaPlayer = MediaPlayer.create(activity, R.raw.firebell);
        mediaPlayer.start();

        countDownTimer = new CountDownTimer(120000, 100)
        {
            public void onTick(long millisUntilFinished)
            {
                String time;

                int seconds = (int) (millisUntilFinished / 1000) % 60;
                int minutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);

                time = minutes + ":" + formatSeconds(seconds);

                timer.setText("[Tele-Op] " + time);

                progressBar.setProgress((int) ((millisUntilFinished) / 100) - 300);

                if(millisUntilFinished <= 30000)
                {
                    cancel();
                    endTimer();
                }
            }

            public void onFinish()
            {
                //endTimer();
            }
        }.start();
    }

    private void endTimer()
    {
        progressBar.setMax(300);
        progressBar.setProgressDrawable(activity.getResources().getDrawable(R.drawable.progress_drawable_red));

        mediaPlayer = MediaPlayer.create(activity, R.raw.factwhistle);
        mediaPlayer.start();

        countDownTimer = new CountDownTimer(30000, 100)
        {
            public void onTick(long millisUntilFinished)
            {
                String time;


                int seconds = (int) (millisUntilFinished / 1000) % 60;
                int minutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);

                time = "0:" + formatSeconds(seconds);

                timer.setText("[Endgame] " + time);

                progressBar.setProgress((int) ((millisUntilFinished) / 100));
            }

            public void onFinish()
            {
                timer.setText("That's the end of the match!");
                mediaPlayer = MediaPlayer.create(activity, R.raw.endmatch);
                mediaPlayer.start();
            }
        }.start();
    }

    private String formatSeconds(int seconds)
    {
        if(seconds < 10)
        {
            return "0" + Integer.toString(seconds);
        }
        else
        {
            return Integer.toString(seconds);
        }
    }
}
