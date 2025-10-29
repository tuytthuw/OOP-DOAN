package com.spa.ui;

import java.time.format.DateTimeFormatter;

/**
 * Định nghĩa các hằng số dùng chung cho menu console.
 */
public final class MenuConstants {
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private MenuConstants() {
    }
}
