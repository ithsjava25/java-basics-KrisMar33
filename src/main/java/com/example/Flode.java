package com.example;

import com.example.api.ElpriserAPI;
import com.example.utils.ConsoleHelp;
import com.example.utils.Utskrift;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class Flode {


    String zone = null;
    String date = null;
    Boolean sorted = false;
    String charging = null;
    Boolean help = false;

    public void run(String[] args) {
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--zone" -> zone = args[++i];
                case "--date" -> date = args[++i];
                case "--sorted" -> sorted = true;
                case "--charging" -> charging = args[++i];
                case "--help" -> help = true;
                default -> System.out.println("Okända argument " + args[i]);
            }
        }


        //showhelp_whenNoArguments()
        if (args.length == 0 || help) {
            ConsoleHelp.showHelp();
            return;
        }

        //handleMissingZoneArgument()
        if (zone == null) {
            System.out.println("Error, --zone is required.");
            ConsoleHelp.showHelp();
            return;
        }

        //handleInvalidZone()
        if (!isValidZone(zone)) {
            System.out.println("Ogiltig zon");
            return;
        }


        //useCurrentDateWhenNotSpecified() //handleInvalidDate()
        LocalDate valtDatum;
        if (date == null) {
            valtDatum = LocalDate.now();
        } else {
            try {
                valtDatum = LocalDate.parse(date);
            } catch (DateTimeParseException e) {
                System.out.println("Ogiltigt datum");
                return;
            }
        }

        // För sorteringen
        LocalDate argDatum = valtDatum;
        LocalDate dagenEfter = argDatum.plusDays(1);

        ElpriserAPI api = new ElpriserAPI();
        List<ElpriserAPI.Elpris> dagensPriser = api.getPriser(valtDatum, ElpriserAPI.Prisklass.valueOf(zone));
        List<ElpriserAPI.Elpris> morgondagensPriser = api.getPriser(dagenEfter, ElpriserAPI.Prisklass.valueOf(zone));
        dagensPriser.addAll(morgondagensPriser);


        // handleNoDataAvailable()
        if (dagensPriser.isEmpty()) {
            System.out.println("Inga priser tillgängliga för " + valtDatum + " i " + zone);
            return;
        }

        Utskrift utskrift = new Utskrift();
        utskrift.printPrisInfo(dagensPriser);

    }

    private static boolean isValidZone(String zone) {
        return "SE1".equals(zone) || "SE2".equals(zone) || "SE3".equals(zone) || "SE4".equals(zone);
    }
}


