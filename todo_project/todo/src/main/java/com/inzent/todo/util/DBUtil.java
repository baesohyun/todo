package com.inzent.todo.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class DBUtil {

    private static SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddhhmmss");
    private static Random rd = new Random();

    public static String generateKey(String prefix) {
        return prefix + formatter.format(new Date()) + (rd.nextInt(90) + 10);
    }

    public static String generateSaveFileName(String ext) {
        return formatter.format(new Date()) + (rd.nextInt(90) + 10) + "." + ext;
    }

}