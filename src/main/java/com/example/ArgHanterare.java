package com.example;

import com.example.api.ElpriserAPI;
import com.example.utils.ConsoleHjalp;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class ArgHanterare {

    // Sparar argument
    private String zone;
    private String date;
    private String charging;
    private boolean sorted = false;
    private boolean help = false;

    public ArgHanterare(String[] args) {
        parseArgs(args);
    }

    private void parseArgs(String[] args) {
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];

            switch (arg) {
                case "--zone" -> {
                    String next = nextArg(args, i);
                    if (!checkRequiredArg(next, "--zone")) return;
                    zone = next.toUpperCase();
                    i++;
                }
                case "--date" -> {
                    String next = nextArg(args, i);
                    if (!checkRequiredArg(next, "--date")) return;
                    date = next.trim();
                    i++;
                }
                case "--charging" -> {
                    String next = nextArg(args, i);
                    if (!checkRequiredArg(next, "--charging")) return;
                    charging = next.trim();
                    i++;
                }
                case "--sorted" -> sorted = true;
                case "--help" -> help = true;
                default -> {
                    System.out.println("Okänt argument eller saknat mellanslag: " + arg);
                    help = true;
                    return;
                }
            }
        }
    }

    private String nextArg(String[] args, int i) {
        return i + 1 < args.length ? args[i + 1] : null;
    }

    private boolean checkRequiredArg(String value, String flag) {
        if (value == null) {
            System.out.println("Error: " + flag + " requires a value");
            help = true;
            return false;
        }
        return true;
    }

    //Hjälp och default
    public boolean isHelp() {
        return help || noArguments();
    }

    private boolean noArguments() {
        return zone == null && date == null && !sorted && charging == null;
    }

    // Argument access
    public ElpriserAPI.Prisklass getZone() {
        if (zone == null) {
            ConsoleHjalp.showHelp();
            System.out.println("Error: --zone is required");
            help = true;
            return null; // nödvändigt för tester
        }
        try {
            return ElpriserAPI.Prisklass.valueOf(zone.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Ogiltig zon: " + zone);
            help = true;
            return null; // nödvändigt för tester
        }
    }

    public LocalDate getDate() {
        if (date == null) return LocalDate.now(); // default
        try {
            return LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            System.out.println("Ogiltigt datum: " + date);
            help = true;
            return LocalDate.now(); // default vid fel
        }
    }

    public int getChargingHours() {
        if (charging == null) return 0; // default
        String antalTim = charging.replaceAll("\\D+", "");
        if (antalTim.isEmpty()) {
            System.out.println("Ogiltigt värde för --charging: " + charging);
            help = true;
            return 0; // default vid fel
        }
        return Integer.parseInt(antalTim);
    }

    public boolean isSorted() {
        return sorted;
    }
}