package com.example.filetransfer.controller;

import com.example.filetransfer.util.AESUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/files")
@CrossOrigin(origins = "*")
public class FileController {

    private static final String UPLOAD_DIR = "uploads/";
    private static final String SECRET_KEY = "SecretKey1234567"; // 16 characters for AES

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) {
        try {
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) uploadDir.mkdirs();

            byte[] encrypted = AESUtil.encrypt(file.getBytes(), SECRET_KEY);
            Path path = Paths.get(UPLOAD_DIR + file.getOriginalFilename() + ".aes");
            FileOutputStream fos = new FileOutputStream(path.toFile());
            fos.write(encrypted);
            fos.close();

            return ResponseEntity.ok("File encrypted and saved successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error during encryption.");
        }
    }

    @PostMapping("/decrypt")
    public ResponseEntity<String> decrypt(@RequestParam("file") MultipartFile file) {
        try {
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) uploadDir.mkdirs();

            byte[] decrypted = AESUtil.decrypt(file.getBytes(), SECRET_KEY);
            String originalFilename = file.getOriginalFilename().replace(".aes", "");
            Path path = Paths.get(UPLOAD_DIR + originalFilename);
            FileOutputStream fos = new FileOutputStream(path.toFile());
            fos.write(decrypted);
            fos.close();

            return ResponseEntity.ok("File decrypted and saved successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error during decryption.");
        }
    }
}