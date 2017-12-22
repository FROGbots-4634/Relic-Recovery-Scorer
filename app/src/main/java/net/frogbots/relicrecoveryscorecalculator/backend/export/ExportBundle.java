package net.frogbots.relicrecoveryscorecalculator.backend.export;

import android.app.Activity;
import net.frogbots.relicrecoveryscorecalculator.backend.Scores;
import java.io.File;

public class ExportBundle
{
    public ExportType exportType;
    public Activity activity;
    public String match;
    public String comment;
    public Scores scores;
    public String filename;
    public File fileForCsvAdd;
}
