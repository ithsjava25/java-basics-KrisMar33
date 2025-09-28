package com.example;
import com.example.api.ElpriserAPI;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Utskrift {
    private final PrisBeraknare beraknare;

    public Utskrift() {
        this.beraknare = new PrisBeraknare();
    }

    public PrisBeraknare getBeraknare() {
        return this.beraknare;
    }

    public void printZon(ElpriserAPI.Prisklass prisklass) {
        System.out.println("Elpriszon: " + prisklass.name());
    }

    public void printMinMaxMean(List<ElpriserAPI.Elpris> priser) {
        System.out.println("Lägsta pris: " + formatOre(beraknare.minPrice(priser)) + " öre");
        System.out.println("Högsta pris: " + formatOre(beraknare.maxPrice(priser)) + " öre");
        System.out.println("Medelpris: " + formatOre(beraknare.meanPrice(priser)) + " öre");
    }

    public void printMeanTimPris(List<ElpriserAPI.Elpris> priser, boolean sorterat) {
        List<Integer> timmar = new ArrayList<>();
        List<Double> medelprisLista = beraknare.meanTimPris(priser, timmar);
        if (sorterat) beraknare.sortDescending(timmar, medelprisLista);

        for (int i = 0; i < timmar.size(); i++) {
            System.out.printf("%02d-%02d %s öre%n",
                    timmar.get(i),
                    (timmar.get(i) + 1) % 24,
                    formatOre(medelprisLista.get(i))
            );
        }
    }

    public void printFonster(List<ElpriserAPI.Elpris> window, int timmar) {
        System.out.println("Påbörja laddning under billigaste " + timmar + "-timmarsfönstret:");
        System.out.println("Medelpris för fönster: " + formatOre(beraknare.meanPrice(window)) + " öre");

        for (ElpriserAPI.Elpris pris : window) {
            String start = String.format("%02d:%02d", pris.timeStart().getHour(), pris.timeStart().getMinute());
            String end   = String.format("%02d:%02d", pris.timeEnd().getHour(), pris.timeEnd().getMinute());

            System.out.println("kl " + start + "-" + end + " " + formatOre(pris.sekPerKWh()) + " öre");
        }
    }

    private String formatOre(double sekPerKWh) {
        double ore = sekPerKWh * 100.0;
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("sv", "SE"));
        DecimalFormat df = new DecimalFormat("0.00", symbols);
        return df.format(ore);
    }
}