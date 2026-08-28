package com.abmaintenance.controller;

import com.abmaintenance.service.AIService;
import com.abmaintenance.service.BaseConnaissanceService;
import com.abmaintenance.service.FiltreQuestionService;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/assistant-ia")
public class AssistantIAController {

    private final AIService aiService;
    private final BaseConnaissanceService baseConnaissanceService;
    private final FiltreQuestionService filtreQuestionService;


    public AssistantIAController(
            AIService aiService,
            BaseConnaissanceService baseConnaissanceService,
            FiltreQuestionService filtreQuestionService) {

        this.aiService = aiService;
        this.baseConnaissanceService = baseConnaissanceService;
        this.filtreQuestionService = filtreQuestionService;
    }


    @PostMapping
    public Map<String, String> poserQuestion(
            @RequestBody Map<String, String> body) {

        Map<String, String> resultat =
                new HashMap<>();

        try {

            String question =
                    body.get("question");


            // ==========================================
            // VERIFICATION DE LA QUESTION
            // ==========================================

            if (question == null
                    || question.trim().isEmpty()) {

                resultat.put(
                        "reponse",
                        "Veuillez saisir une question."
                );

                return resultat;
            }


            // ==========================================
            // FILTRE DES QUESTIONS HORS MAINTENANCE
            // ==========================================

            if (!filtreQuestionService
                    .estQuestionMaintenance(question)) {

                resultat.put(
                        "reponse",
                        """
                        Cette question ne concerne pas la maintenance industrielle.

                        Je suis l'assistant de diagnostic AB Maintenance.

                        Je peux vous aider concernant :
                        - une panne d'équipement ;
                        - des bruits ou vibrations ;
                        - une fuite ;
                        - un problème de pression ;
                        - une surchauffe ;
                        - un problème mécanique, électrique,
                          hydraulique ou pneumatique.
                        """
                );

                return resultat;
            }


            // ==========================================
            // RECUPERATION DE LA BASE DE CONNAISSANCES
            // ==========================================

            String connaissances =
                    baseConnaissanceService
                            .construireContexte();


            // ==========================================
            // CONSTRUCTION DU PROMPT
            // ==========================================

            String prompt = """
                    Tu es l'assistant intelligent de diagnostic
                    de l'application AB Maintenance.

                    Tu assistes un technicien de maintenance industrielle.

                    Voici la base de connaissances de l'entreprise :

                    %s

                    INSTRUCTIONS IMPORTANTES :

                    1. Analyse la question du technicien.

                    2. Identifie les symptômes correspondant à la
                       base de connaissances.

                    3. Utilise EN PRIORITE les causes et solutions
                       présentes dans cette base.

                    4. N'invente pas une cause comme diagnostic principal
                       si elle n'est pas cohérente avec les informations
                       fournies.

                    5. Si les informations sont insuffisantes,
                       demande au technicien des vérifications
                       complémentaires.

                    6. Ne présente jamais une hypothèse comme certaine.

                    7. Réponds en français.

                    8. Donne une réponse courte, claire et professionnelle.

                    Structure souhaitée :

                    Symptômes identifiés :
                    - ...

                    Causes possibles :
                    1. ...
                    2. ...

                    Vérifications recommandées :
                    - ...
                    - ...

                    QUESTION DU TECHNICIEN :

                    %s
                    """.formatted(
                            connaissances,
                            question
                    );


            // ==========================================
            // APPEL DU SERVICE IA
            // ==========================================

            String reponse =
                    aiService.genererExplication(
                            prompt
                    );


            resultat.put(
                    "reponse",
                    reponse
            );


        } catch (Exception e) {

            e.printStackTrace();

            resultat.put(
                    "reponse",
                    "Erreur lors de la communication avec l'IA : "
                            + e.getMessage()
            );
        }


        return resultat;
    }
}