package com.example;
import com.example.api.ElpriserAPI;
import com.example.utils.ConsoleHjalp;
import java.time.LocalDate;
import java.util.List;

public class Flode {
    private final Argument argument;

    public Flode(String[] args) {
        this.argument = new Argument(args);
    }

    public void run() {
        if (argument.isHelp()) {
            ConsoleHjalp.showHelp();
            return;
        }

        ElpriserAPI.Prisklass zonEnum = argument.getZone();
        if (zonEnum == null) return;

        String valdZon = zonEnum.name();

        LocalDate angivetDatum = argument.getDate();
        LocalDate dagenEfter = angivetDatum.plusDays(1);

        ElpriserAPI api = new ElpriserAPI();
        PrisService prisService = new PrisService(api);

        List<ElpriserAPI.Elpris> priserAngivetDatum = prisService.hamtaPriser(angivetDatum, zonEnum);
        List<ElpriserAPI.Elpris> priserImorgon = prisService.hamtaPriser(dagenEfter, zonEnum);
        priserAngivetDatum.addAll(priserImorgon);

        prisService.ingaPriser(priserAngivetDatum, valdZon, angivetDatum);

        Utskrift utskrift = new Utskrift();
        utskrift.printZon(zonEnum);
        utskrift.printMinMaxMean(priserAngivetDatum);
        utskrift.printMeanTimPris(priserAngivetDatum, argument.isSorted());

        int timmar = argument.getChargingHours();
        if (timmar > 0) {
            List<ElpriserAPI.Elpris> fonster = utskrift.getBeraknare().billigasteIntervallet(priserAngivetDatum, timmar);
            utskrift.printFonster(fonster, timmar);
        }
    }
}