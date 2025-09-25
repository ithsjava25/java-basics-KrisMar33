package com.example;

import com.example.api.ElpriserAPI;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;

public class Main {
    public static void main(String[] args) {

        //Test, funkade.
        //args = new String[]{"--zone", "SE1", "--date", "2025-09-15", "--sorted", "--charging", "4h"};

        String zone = null;
        String date = null;
        Boolean sorted = false;
        String charging = null;
        Boolean help = false;


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
            showHelp();
            return;
        }

        //handleMissingZoneArgument()
        if (zone == null) {
            System.out.println("Error, --zone is required.");
            showHelp();
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


        if (sorted) {
            bubbleSortDescending(dagensPriser);
        }
        // displaySortedPrices()
        for (int i = 0; i < dagensPriser.size(); i++) {
            ElpriserAPI.Elpris p = dagensPriser.get(i);
            String period = String.format("%02d-%02d", p.timeStart().getHour(), p.timeEnd().getHour());
            System.out.println(period + " " + formatOre(p.sekPerKWh()) + " öre");
        }
    }


    public static void bubbleSortDescending(List<ElpriserAPI.Elpris> priser) {
        int n = priser.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - 1 - i; j++) {
                // Om priset på index j är mindre än priset på index j+1, byt plats
                if (priser.get(j).sekPerKWh() < priser.get(j + 1).sekPerKWh()) {
                    ElpriserAPI.Elpris tmp = priser.get(j);
                    priser.set(j, priser.get(j + 1));
                    priser.set(j + 1, tmp);
                }
            }
        }
    }

    //showHelp_withHelpflag()
    public static void showHelp() {
        System.out.println("""
                Electricity Price Optimizer CLI
                ------------------------------
                Usage:
                --zone SE1|SE2|SE3|SE4    (required) Specify electricity zone
                --date YYYY-MM-DD         (optional) Defaults to today
                --sorted                  (optional) Display prices in ascending order
                --charging 2h|4h|8h       (optional) Find optimal charging windows
                --help                    (optional) Show this help message
                Example usage:
                java -cp target/classes com.example.Main --zone SE3 --date 2025-09-04
                java -cp target/classes com.example.Main --zone SE1 --charging 4h
                java -cp target/classes com.example.Main --zone SE2 --date 2025-09-04 --sorted
                java -cp target/classes com.example.Main --help
                """);

    }

    private static boolean isValidZone(String zone) {
        String[] validZones = {"SE1", "SE2", "SE3", "SE4"};
        for (int i = 0; i < validZones.length; i++) {
            if (validZones[i].equals(zone)) {
                return true;
            }
        }
        return false;
    }


    private static String formatOre(double sekPerKWh) {
        double ore = sekPerKWh * 100.0;
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("sv", "SE"));
        DecimalFormat df = new DecimalFormat("0.00", symbols);
        return df.format(ore);
    }
}