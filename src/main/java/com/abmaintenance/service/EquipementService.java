package com.abmaintenance.service;

import com.abmaintenance.entity.Equipement;

import com.abmaintenance.repository.EquipementRepository;
import com.abmaintenance.repository.MaintenanceRepository;
import com.abmaintenance.repository.InterventionRepository;
import com.abmaintenance.repository.PanneRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EquipementService {

    private final EquipementRepository equipementRepository;

    private final MaintenanceRepository maintenanceRepository;

    private final InterventionRepository interventionRepository;

    private final PanneRepository panneRepository;

    private final QRCodeService qrCodeService;


    // ==========================================
    // CONSTRUCTEUR
    // ==========================================

    public EquipementService(
            EquipementRepository equipementRepository,
            MaintenanceRepository maintenanceRepository,
            InterventionRepository interventionRepository,
            PanneRepository panneRepository,
            QRCodeService qrCodeService) {

        this.equipementRepository =
                equipementRepository;

        this.maintenanceRepository =
                maintenanceRepository;

        this.interventionRepository =
                interventionRepository;

        this.panneRepository =
                panneRepository;

        this.qrCodeService =
                qrCodeService;
    }


    // ==========================================
    // RÉCUPÉRER TOUS LES ÉQUIPEMENTS
    // ==========================================

    public List<Equipement> findAll() {

        return equipementRepository.findAll();
    }


    // ==========================================
    // RÉCUPÉRER UN ÉQUIPEMENT PAR ID
    // ==========================================

    public Equipement findById(Long id) {

        return equipementRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Équipement introuvable avec l'id : " + id
                        )
                );
    }


    // ==========================================
    // ENREGISTRER UN ÉQUIPEMENT
    // ==========================================

    public Equipement save(
            Equipement equipement) {

        try {

            Equipement saved =
                    equipementRepository.save(
                            equipement
                    );


            /*
             * Si aucun QR Code n'existe encore,
             * on le génère automatiquement.
             */

            if (saved.getQrCode() == null
                    || saved.getQrCode().isBlank()) {

                String qrCode =
                        qrCodeService.genererQRCode(
                                saved.getId()
                        );


                saved.setQrCode(qrCode);


                saved =
                        equipementRepository.save(
                                saved
                        );
            }


            return saved;


        } catch (Exception e) {

            throw new RuntimeException(
                    "Erreur lors de l'enregistrement de l'équipement "
                            + "ou de la génération du QR Code",
                    e
            );
        }
    }


    // ==========================================
    // RÉGÉNÉRER LE QR CODE D'UN ÉQUIPEMENT
    // ==========================================

    public Equipement regenererQRCode(Long id) {

        try {

            Equipement equipement =
                    findById(id);


            String qrCode =
                    qrCodeService.genererQRCode(id);


            equipement.setQrCode(qrCode);


            return equipementRepository.save(
                    equipement
            );


        } catch (Exception e) {

            throw new RuntimeException(
                    "Impossible de régénérer le QR Code "
                            + "de l'équipement " + id,
                    e
            );
        }
    }


    // ==========================================
    // RÉGÉNÉRER TOUS LES QR CODES
    // ==========================================

    public void regenererTousLesQRCodes() {

        List<Equipement> equipements =
                equipementRepository.findAll();


        for (Equipement equipement : equipements) {

            regenererQRCode(
                    equipement.getId()
            );
        }
    }


    // ==========================================
    // SUPPRIMER UN ÉQUIPEMENT
    // ==========================================

    @Transactional
    public void delete(Long id) {

        // Vérifier que l'équipement existe
        Equipement equipement =
                findById(id);


        /*
         * 1. Supprimer les maintenances
         * directement liées à l'équipement.
         */
        maintenanceRepository
                .deleteByEquipementId(id);


        /*
         * 2. Supprimer les interventions
         * liées aux pannes de cet équipement.
         *
         * Elles doivent être supprimées AVANT
         * les pannes à cause des clés étrangères.
         */
        interventionRepository
                .deleteByPanneEquipementId(id);


        /*
         * 3. Supprimer les pannes
         * liées à l'équipement.
         */
        panneRepository
                .deleteByEquipementId(id);


        /*
         * 4. Supprimer finalement
         * l'équipement lui-même.
         */
        equipementRepository
                .delete(equipement);
    }
}