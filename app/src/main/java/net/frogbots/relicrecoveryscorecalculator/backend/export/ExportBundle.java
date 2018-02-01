package net.frogbots.relicrecoveryscorecalculator.backend.export;

import net.frogbots.relicrecoveryscorecalculator.backend.Scores;
import java.io.File;

public class ExportBundle
{
    public ExportType exportType;
    public String match;
    public String comment;
    public String team;
    public Scores scores;
    public String filename;
    public File fileForCsvAdd;
}
