package com.example;

import com.example.api.ElpriserAPI;
import java.util.List;


public class PrisBeraknare {

    public double minPrice(List<ElpriserAPI.Elpris> priser) {
        if (priser.isEmpty()) return 0.0;

        double min = priser.get(0).sekPerKWh();
        for (ElpriserAPI.Elpris p : priser) {
            if (p.sekPerKWh() < min) {
                min = p.sekPerKWh();
            }
        }
        return min;
    }

    public double maxPrice(List<ElpriserAPI.Elpris> priser) {
        if (priser.isEmpty()) return 0.0;

        double max = priser.get(0).sekPerKWh();
        for (ElpriserAPI.Elpris p : priser) {
            if (p.sekPerKWh() > max) {
                max = p.sekPerKWh();
            }
        }
        return max;
    }

    public double meanPrice(List<ElpriserAPI.Elpris> priser) {
        if (priser.isEmpty()) return 0.0;

        double sum = 0.0;
        for (ElpriserAPI.Elpris p : priser) {
            sum += p.sekPerKWh();
        }
        return sum / priser.size();
    }
}