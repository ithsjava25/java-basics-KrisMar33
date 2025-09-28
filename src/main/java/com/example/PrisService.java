package com.example;
import com.example.api.ElpriserAPI;
import java.time.LocalDate;
import java.util.List;

public class PrisService {
private final ElpriserAPI api;

public PrisService() {
    this.api = new ElpriserAPI();
}
    public List<ElpriserAPI.Elpris> hamtaPriser(LocalDate datum, ElpriserAPI.Prisklass prisklass) {
        return api.getPriser(datum, prisklass);
    }

    public void ingaPriser(List<ElpriserAPI.Elpris> priser, String zone, LocalDate datum) {
        if (priser.isEmpty()) {
            System.out.println("Inga priser tillgängliga för " + datum + " i " + zone);
            return;
        }
    }
}