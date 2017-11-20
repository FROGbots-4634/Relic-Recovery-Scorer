package net.frogbots.relicrecoveryscorecalculator.backend;

public class Highscore
{
    public String matchKey;
    public String alliance;
    public int score;

    public Highscore(String matchKey, String alliance, int score)
    {
        this.matchKey = matchKey;
        this.alliance = alliance;
        this.score = score;
    }
}
