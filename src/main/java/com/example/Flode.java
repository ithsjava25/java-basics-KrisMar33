package com.example;

import com.example.api.ElpriserAPI;
import com.example.utils.ConsoleHelp;

import java.time.LocalDate;
import java.util.List;

public class Flode {

    // ArgHandler sparar användarens argument och används internt i hela klassen
    // private final = skyddad från externa ändringar och kan inte bytas ut efter skapande
    private final ArgHandler argHandler;


    public Flode(String[] args) {
        this.argHandler = new ArgHandler(args);
    }

    /**
     * Kör hela programmets flöde:
     * 1. Kollar om användaren vill ha hjälp
     * 2. Hämtar zon och datum
     * 3. Hämtar priser via PrisService
     * 4. Skriver ut priser via Utskrift
     * 5. Beräknar billigaste laddtimmar om det anges
     */
    public void run() {

        // Visa CLI-hjälp om användaren begär det eller inte skickar argument
        if (argHandler.isHelp()) {
            ConsoleHelp.showHelp();
            return;
        }

        // Hämta elpriszon som enum
        // Avsluta om zonen är ogiltig
        ElpriserAPI.Prisklass enumPrisklass = argHandler.getZone();
        if (enumPrisklass == null) return;
        // enumPrisklass kan vara null om användaren inte skickade en giltig zon
        // Om vi försöker använda den utan kontroll kan programmet krascha med NullPointerException
        // Därför kollar vi först: if (enumPrisklass == null) return

        // Spara zonens enum-namn som sträng (SE1, SE2, SE3, SE4)
        String valdZon = enumPrisklass.name();

        // Hämta datum, standard är idag
        LocalDate angivetDatum = argHandler.getDate();
        LocalDate dagenEfter = angivetDatum.plusDays(1);

        // Hämta priser från API via PrisService
        PrisService prisService = new PrisService();
        List<ElpriserAPI.Elpris> priserAngivetDatum = prisService.hamtaPriser(angivetDatum, enumPrisklass);
        List<ElpriserAPI.Elpris> morgondagensPriser = prisService.hamtaPriser(dagenEfter, enumPrisklass);
        // Lägg till alla priser från morgondagen i listan för dagens priser
        // Nu innehåller dagensPriser både dagens och morgondagens elpriser


        if (argHandler.includeTomorrowPrices()) {
            priserAngivetDatum.addAll(morgondagensPriser);
        }

        // Kontrollera att det finns priser, annars visa fel
        prisService.ingaPriser(priserAngivetDatum, valdZon, angivetDatum);

        // Skriv ut priser till konsolen
        Utskrift utskrift = new Utskrift();
        utskrift.printZon(enumPrisklass);
        utskrift.printMinMaxMean(priserAngivetDatum);
    }
}