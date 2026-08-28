package com.abmaintenance.service;

import com.abmaintenance.entity.Equipement;
import com.abmaintenance.repository.EquipementRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquipementService {

    private final EquipementRepository equipementRepository;
    private final QRCodeService qrCodeService;

    // Constructeur
    public EquipementService(
            EquipementRepository equipementRepository,
            QRCodeService qrCodeService) {

        this.equipementRepository = equipementRepository;
        this.qrCodeService = qrCodeService;
    }

    // Récupérer tous les équipements
    public List<Equipement> findAll() {
        return equipementRepository.findAll();
    }

    // Récupérer un équipement par son ID
    public Equipement findById(Long id) {

        return equipementRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Équipement introuvable"
                        )
                );
    }

    // Enregistrer un équipement et générer son QR Code
    public Equipement save(Equipement equipement) {

        try {

            // 1. Enregistrer l'équipement dans la base de données
            // pour récupérer son ID
            Equipement saved =
                    equipementRepository.save(equipement);

            // 2. Vérifier si l'équipement possède déjà un QR Code
            if (saved.getQrCode() == null
                    || saved.getQrCode().isBlank()) {

                // 3. Générer le QR Code à partir de l'ID
                String qrCode =
                        qrCodeService.genererQRCode(
                                saved.getId()
                        );

                // 4. Ajouter le QR Code à l'équipement
                saved.setQrCode(qrCode);

                // 5. Mettre à jour l'équipement dans la BDD
                saved =
                        equipementRepository.save(saved);
            }

            return saved;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Erreur lors de la génération du QR Code",
                    e
            );
        }
    }

    // Supprimer un équipement
    public void delete(Long id) {
        equipementRepository.deleteById(id);
    }
}