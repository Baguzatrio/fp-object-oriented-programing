package model;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.Arrays;
import java.util.List;

public class DateValidator {

    // Daftar formatter yang didukung
    private static final List<DateTimeFormatter> DATE_FORMATTERS = Arrays.asList(
        DateTimeFormatter.ofPattern("ddMMuuuu")
                        .withResolverStyle(ResolverStyle.STRICT),
        DateTimeFormatter.ofPattern("dd-MM-uuuu")
                        .withResolverStyle(ResolverStyle.STRICT),
        DateTimeFormatter.ofPattern("dd/MM/uuuu")
                        .withResolverStyle(ResolverStyle.STRICT)
    );

    public static boolean isValidDate(String input) {
        if (input == null || input.trim().isEmpty()) {
            return false;
        }

        // Coba semua format yang didukung
        for (DateTimeFormatter formatter : DATE_FORMATTERS) {
            try {
                LocalDate date = LocalDate.parse(input, formatter);
                
                // Validasi tahun (1900-2099)
                int year = date.getYear();
                if (year < 1900 || year > 2099) {
                    return false;
                }
                
                return true;
            } catch (DateTimeException e) {
                // Lanjut ke format berikutnya
            }
        }
        return false;
    }

    // Versi yang mengembalikan LocalDate jika valid
    public static LocalDate parseDate(String input) {
        if (input == null || input.trim().isEmpty()) {
            return null;
        }

        for (DateTimeFormatter formatter : DATE_FORMATTERS) {
            try {
                LocalDate date = LocalDate.parse(input, formatter);
                int year = date.getYear();
                if (year >= 1900 && year <= 2099) {
                    return date;
                }
            } catch (DateTimeException e) {
                // Continue to next format
            }
        }
        return null;
    }
}