package com.recycup.recycup_cafe;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Crypto {   //SHA-256

    public static String sha256(String msg)  throws NoSuchAlgorithmException {

        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");

        messageDigest.update(msg.getBytes());

        return byteToHexString(messageDigest.digest());

    }

    public static String byteToHexString(byte[] data) {

        StringBuilder stringBuilder = new StringBuilder();

        for(byte b : data) {

            stringBuilder.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));

        }

        return stringBuilder.toString();

    }
}