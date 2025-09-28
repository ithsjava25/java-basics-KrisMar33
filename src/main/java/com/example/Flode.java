package com.example;
import com.example.api.ElpriserAPI;
import com.example.utils.ConsoleHelp;
import java.time.LocalDate;
import java.util.List;

public class Flode {
    private final ArgHandler argHandler;

    public Flode(String[] args) {
        this.argHandler = new ArgHandler(args);
    }

    public void run() {
        if (argHandler.isHelp()) {
            ConsoleHelp.showHelp();
            return;
        }

        ElpriserAPI.Prisklass zonEnum = argHandler.getZone();
        if (zonEnum == null) return;

        String valdZon = zonEnum.name();

        LocalDate angivetDatum = argHandler.getDate();
        LocalDate dagenEfter = angivetDatum.plusDays(1);

        PrisService prisService = new PrisService();
        List<ElpriserAPI.Elpris> priserAngivetDatum = prisService.hamtaPriser(angivetDatum, zonEnum);
        List<ElpriserAPI.Elpris> priserImorgon = prisService.hamtaPriser(dagenEfter, zonEnum);

        if (argHandler.InkluderaMorgondagensPriser()) {
            priserAngivetDatum.addAll(priserImorgon);
        }

        prisService.ingaPriser(priserAngivetDatum, valdZon, angivetDatum);

        Utskrift utskrift = new Utskrift();
        utskrift.printZon(zonEnum);
        utskrift.printMinMaxMean(priserAngivetDatum);
        utskrift.printMeanTimPris(priserAngivetDatum, argHandler.isSorted());

        int timmar = argHandler.getChargingHours();
        if (timmar > 0) {
            List<ElpriserAPI.Elpris> fonster = utskrift.getBeraknare().billigasteIntervallet(priserAngivetDatum, timmar);
            utskrift.printFonster(fonster, timmar);
        }
    }
}