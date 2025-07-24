/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.voting.app;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author Inupa Vihan
 */
public class KeyGeneratorUtil {
    public static void main(String[] args) throws Exception {
        generateAndSaveKeyPair("client");
        generateAndSaveKeyPair("server");
        System.out.println("Keys generated and saved.");
    }
	
    // for generating key pairs for client and the server
    public static void generateAndSaveKeyPair(String name) throws NoSuchAlgorithmException, IOException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair pair = keyGen.generateKeyPair();
        
        String basePath = "keys/";
        new File(basePath).mkdirs();

        try (FileOutputStream out = new FileOutputStream(basePath + name + "_public.key")) {
            out.write(pair.getPublic().getEncoded());
        }
        try (FileOutputStream out = new FileOutputStream(basePath + name + "_private.key")) {
            out.write(pair.getPrivate().getEncoded());
        }
    }
}
