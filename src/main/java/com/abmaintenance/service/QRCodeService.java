package com.abmaintenance.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Service
public class QRCodeService {

    @Value("${app.base-url}")
    private String baseUrl;

    public String genererQRCode(Long idEquipement) throws Exception {

        String url = baseUrl
                + "/equipements/"
                + idEquipement;

        String nomFichier =
                "equipement-" + idEquipement + ".png";

        Path dossier = Paths.get(
                "src/main/resources/static/qrcodes"
        );

        if (!Files.exists(dossier)) {
            Files.createDirectories(dossier);
        }

        Path chemin =
                dossier.resolve(nomFichier);

        Map<EncodeHintType, Object> hints =
                new HashMap<>();

        hints.put(
                EncodeHintType.CHARACTER_SET,
                "UTF-8"
        );

        BitMatrix matrix =
                new MultiFormatWriter().encode(
                        url,
                        BarcodeFormat.QR_CODE,
                        300,
                        300,
                        hints
                );

        MatrixToImageWriter.writeToPath(
                matrix,
                "PNG",
                chemin
        );

        return "/qrcodes/" + nomFichier;
    }
}