package com.example.smartpantrymanager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/** One ingredient the user owns. Maps to a row in the pantry_item table. */
public class PantryItem {
    private final long id;
    private final String name;
    private final String normalizedName;
    private final double quantity;
    private final String unit;
    private final String expiryDate; // yyyy-MM-dd, or "" if none

    public PantryItem(long id, String name, double quantity, String unit, String expiryDate) {
        this.id = id;
        this.name = name;
        this.normalizedName = Normalizer.normalizeName(name);
        this.quantity = quantity;
        this.unit = unit;
        this.expiryDate = expiryDate == null ? "" : expiryDate;
    }

    public long getId() { return id; }
    public String getName() { return name; }
    public String getNormalizedName() { return normalizedName; }
    public double getQuantity() { return quantity; }
    public String getUnit() { return unit; }
    public String getExpiryDate() { return expiryDate; }
    public String getQuantityText() { return Normalizer.formatQuantity(quantity); }

    /** True if the item expires within the given number of days (or is already expired). */
    public boolean isExpiringSoon(int days) {
        if (expiryDate.isEmpty()) return false;
        try {
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            f.setLenient(false);
            Date d = f.parse(expiryDate);
            return d.getTime() - System.currentTimeMillis() <= days * 86400000L;
        } catch (ParseException e) {
            return false;
        }
    }
}
