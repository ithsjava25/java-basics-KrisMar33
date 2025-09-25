package com.example;

import com.example.api.ElpriserAPI;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

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


        LocalDate idag = LocalDate.now();

        ElpriserAPI api = new ElpriserAPI();
        List<ElpriserAPI.Elpris> dagensPriser = api.getPriser(valtDatum, ElpriserAPI.Prisklass.valueOf(zone));

        // handleNoDataAvailable()
        if (dagensPriser.isEmpty()) {
            System.out.println("Inga priser tillgängliga för " + valtDatum + " i " + zone);
            return;
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
}