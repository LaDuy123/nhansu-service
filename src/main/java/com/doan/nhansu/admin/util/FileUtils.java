package com.doan.nhansu.admin.util;


import jakarta.xml.bind.DatatypeConverter;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileUtils {


    public static String getFileExtension(String name) {
        String extension;
        try {
            extension = name.substring(name.lastIndexOf("."));
        } catch (Exception e) {
            extension = "";
        }
        return extension;
    }
    public static String getNameFileMD5(String input, String extension) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            // replay MD5, SHA-512
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return DatatypeConverter.printHexBinary(hash) + extension;
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
}

