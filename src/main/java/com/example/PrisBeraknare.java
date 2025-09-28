package com.example;
import com.example.api.ElpriserAPI;
import java.util.ArrayList;
import java.util.List;


public class PrisBeraknare {

    public double minPrice(List<ElpriserAPI.Elpris> priser) {
        if (priser.isEmpty()) return 0.0;

        double min = priser.get(0).sekPerKWh();
        for (ElpriserAPI.Elpris p : priser) {
            if (p.sekPerKWh() < min) {
                min = p.sekPerKWh();
            }
        } return min;
    }

    public double maxPrice(List<ElpriserAPI.Elpris> priser) {
        if (priser.isEmpty()) return 0.0;

        double max = priser.get(0).sekPerKWh();
        for (ElpriserAPI.Elpris p : priser) {
            if (p.sekPerKWh() > max) {
                max = p.sekPerKWh();
            }
        } return max;
    }

    public double meanPrice(List<ElpriserAPI.Elpris> priser) {
        if (priser.isEmpty()) return 0.0;

        double sum = 0.0;
        for (ElpriserAPI.Elpris p : priser) {
            sum += p.sekPerKWh();
        } return sum / priser.size();
    }

    public List<Double> meanTimPris(List<ElpriserAPI.Elpris> priser, List<Integer> timmar) {
        List<Double> medelprisPerTimme = new ArrayList<>();
        if (priser.isEmpty()) return medelprisPerTimme;

        int currentHour = priser.get(0).timeStart().getHour();
        List<ElpriserAPI.Elpris> priserFörTimme = new ArrayList<>();

        for (ElpriserAPI.Elpris pris : priser) {
            int hour = pris.timeStart().getHour();

            if (hour != currentHour) {
                timmar.add(currentHour);
                medelprisPerTimme.add(meanPrice(priserFörTimme));


                priserFörTimme.clear();
                currentHour = hour;
            }
            priserFörTimme.add(pris);
        }

        timmar.add(currentHour);
        medelprisPerTimme.add(meanPrice(priserFörTimme));

        return medelprisPerTimme;
    }

    public void sortDescending(List<Integer> timmar, List<Double> medelprisPerTimme) {
        for (int i = 0; i < medelprisPerTimme.size() - 1; i++) {
            for (int j = 0; j < medelprisPerTimme.size() - 1 - i; j++) {
                if (medelprisPerTimme.get(j) < medelprisPerTimme.get(j + 1)) {

                    double tempPris = medelprisPerTimme.get(j);
                    medelprisPerTimme.set(j, medelprisPerTimme.get(j + 1));
                    medelprisPerTimme.set(j + 1, tempPris);

                    int tempTimme = timmar.get(j);
                    timmar.set(j, timmar.get(j + 1));
                    timmar.set(j + 1, tempTimme);
                }
            }
        }
    }

    public List<ElpriserAPI.Elpris> billigasteIntervallet(List<ElpriserAPI.Elpris> priser, int timmar) {
        List<ElpriserAPI.Elpris> billigasteFonster = new ArrayList<>();

        if (priser.isEmpty() || timmar <= 0 || priser.size() < timmar) {
            return billigasteFonster;
        }

        int totaltAntalPriser = priser.size();

        double minSumma = 0;
        for (int i = 0; i < timmar; i++) {
            minSumma += priser.get(i).sekPerKWh();
        }
        int startIndexBilligaste = 0;

        for (int start = 1; start <= totaltAntalPriser - timmar; start++) {
            double fönsterSumma = 0;
            for (int i = 0; i < timmar; i++) {
                fönsterSumma += priser.get(start + i).sekPerKWh();
            }
            if (fönsterSumma < minSumma) {
                minSumma = fönsterSumma;
                startIndexBilligaste = start;
            }
        }

        for (int i = 0; i < timmar; i++) {
            billigasteFonster.add(priser.get(startIndexBilligaste + i));
        }

        return billigasteFonster;
    }
}
