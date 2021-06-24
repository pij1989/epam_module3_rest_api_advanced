package com.epam.esm.util;

public class PaginationUtil {

    private PaginationUtil() {
    }

    public static int defineTotalPages(long totalElements, int size) {
        return (int) (totalElements % size == 0 ? totalElements / size : (totalElements / size) + 1);
    }
}
