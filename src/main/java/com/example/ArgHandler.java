package com.example;
import com.example.api.ElpriserAPI;
import com.example.utils.ConsoleHelp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;


public class ArgHandler {
    // Sparar argumenten som användaren skickar via CLI
    private String zone;
    private String date;
    private Boolean sorted = false;
    private String charging;
    private Boolean help = false;

    public ArgHandler(String[] args) {
        parseArgs(args);
    }

    private void parseArgs(String[] args) {
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--zone" -> zone = args[++i]; // Nästa argument är zon
                case "--date" -> date = args[++i]; // Nästa argument är datum
                case "--sorted" -> sorted = true; // Flagga, inget värde behövs
                case "--charging" -> charging = args[++i]; // Nästa argument är antal timmar
                case "--help" -> help = true; // Flagga för hjälp
                default -> System.out.println("Okända argument " + args[i]);
            }
        }
    }

    public boolean isHelp() {
        return help || noArguments();
    }

    private boolean noArguments() {
        return zone == null && date == null && !sorted && charging == null;
    }

    public ElpriserAPI.Prisklass getZone() {
        if (zone == null) {
            ConsoleHelp.showHelp();
            System.out.println("Error: --zone is required");
            return null;
        } try {
            return ElpriserAPI.Prisklass.valueOf(zone);
        } catch (IllegalArgumentException e) {
            System.out.println("Ogiltig zon: " + zone);
            return null;
        }
    }

    public LocalDate getDate() {
        if (date == null) return LocalDate.now();
        try {
            return LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            System.out.println("Ogiltigt datum: " + date);
            return LocalDate.now();
        }
    }

    public boolean isSorted() { return sorted; }

    public int getChargingHours() {
        if (charging == null) return 0;
        String antalTim = charging.replaceAll("\\D+", "");
        if (antalTim.isEmpty()) {
            System.out.println("Ogiltigt värde för --charging: " + charging);
            return 0;
        }
        return Integer.parseInt(antalTim);
    }

    public boolean InkluderaMorgondagensPriser() { return LocalTime.now().getHour() >= 13; }
}