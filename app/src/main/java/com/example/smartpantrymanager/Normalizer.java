package com.example.smartpantrymanager;

import java.util.Locale;

/** Pure-Java helpers that make ingredient names and units comparable. */
public final class Normalizer {
    private Normalizer() { }

    /** Lowercase, trim and simply singularize: tomatoes->tomato, berries->berry, eggs->egg. */
    public static String normalizeName(String raw) {
        String s = raw.trim().toLowerCase(Locale.ROOT);
        if (s.length() > 3 && s.endsWith("ies")) return s.substring(0, s.length() - 3) + "y";
        if (s.length() > 3 && s.endsWith("oes")) return s.substring(0, s.length() - 2);
        if (s.length() > 1 && s.endsWith("s") && !s.endsWith("ss")) return s.substring(0, s.length() - 1);
        return s;
    }

    /** Which family a unit belongs to: "g" (weight), "ml" (volume) or "count" (no unit). */
    public static String baseUnit(String unit) {
        switch (unit.trim().toLowerCase(Locale.ROOT)) {
            case "g": case "kg": return "g";
            case "ml": case "l": case "cup": case "tbsp": case "tsp": return "ml";
            default: return "count";
        }
    }

    /** Converts a quantity into its base unit (kg->g, l/cup/tbsp/tsp->ml). */
    public static double toBaseQuantity(double qty, String unit) {
        switch (unit.trim().toLowerCase(Locale.ROOT)) {
            case "kg": case "l": return qty * 1000;
            case "cup": return qty * 240;
            case "tbsp": return qty * 15;
            case "tsp": return qty * 5;
            default: return qty;
        }
    }

    /** 3.0 -> "3", 2.5 -> "2.5" for display. */
    public static String formatQuantity(double q) {
        return q == Math.floor(q) ? String.valueOf((long) q) : String.valueOf(q);
    }
}
