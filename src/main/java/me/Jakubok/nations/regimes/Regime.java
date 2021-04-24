package me.Jakubok.nations.regimes;


import me.Jakubok.nations.administration.UnitsOfPower;

public interface Regime {

    boolean commonElectionsExist();
    boolean canAIVote();
    float happinessFactor();
    boolean onlyRulerCanVote();
    boolean onlyPartyCanVote();
    int cadenceDuration();
    UnitsOfPower judgeFunctionExecutor();
    UnitsOfPower dependenceOfJudges();
    UnitsOfPower presidentFunctionExecutor();
    UnitsOfPower dependenceOfPresident();
    UnitsOfPower parliamentFunctionExecutor();
    UnitsOfPower dependenceOfParliament();
}
