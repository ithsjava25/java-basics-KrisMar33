package com.example;

import com.example.api.ElpriserAPI;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

public class Utskrift {

    private final PrisBeraknare beraknare;

    public Utskrift() {
        this.beraknare = new PrisBeraknare();
    }

    // Skriv ut enum-zonen som sträng
    public void printZon(ElpriserAPI.Prisklass prisklass) {
        System.out.println("Elpriszon: " + prisklass.name());
    }

    public void printMinMaxMean(List<ElpriserAPI.Elpris> priser) {
        //printMinMaxPrices_WithValidData()
        //printMeanPrice_WithValidData()
        double minpris = beraknare.minPrice(priser);
        double maxpris = beraknare.maxPrice(priser);
        double medelpris = beraknare.meanPrice(priser);
        System.out.println("Lägsta pris: " + formatOre(minpris) + " öre");
        System.out.println("Högsta pris: " + formatOre(maxpris) + " öre");
        System.out.println("Medelpris: " + formatOre(medelpris) + " öre");
    }

    private String formatOre(double sekPerKWh) {
        double ore = sekPerKWh * 100.0;
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("sv", "SE"));
        DecimalFormat df = new DecimalFormat("0.00", symbols);
        return df.format(ore);
    }
}