package com.example.utils;

import com.example.PrisBeraknare;
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

    //printMinMaxPrices_WithValidData()
    //printMeanPrice_WithValidData()

    public void printPrisInfo(List<ElpriserAPI.Elpris> dagensPriser) {
        double minpris = beraknare.minPrice(dagensPriser);
        double maxpris = beraknare.maxPrice(dagensPriser);
        double medelpris = beraknare.meanPrice(dagensPriser);
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