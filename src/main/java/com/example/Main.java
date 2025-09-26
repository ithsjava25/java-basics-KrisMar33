package com.example;

import com.example.api.ElpriserAPI;
import com.example.utils.ConsoleHelp;
import com.example.utils.Utskrift;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Flode flode = new Flode();
        flode.run(args);
    }
}