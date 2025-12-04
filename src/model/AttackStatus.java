package model;

/**
 * Defines the different results possible of an attack.
 */
public enum AttackStatus {
    MISS,               // Tirs manqués dans l'eau
    ISLAND_MISS,        // Tirs manqués sur l'île
    HIT,                // Bateau touché mais pas coulé
    SUNK,               // Bateau coulé
    TRAP_ACTIVATED,     // Piège (Trou Noir/Tornade) touché et activé
    ITEM_FOUND,         // Arme/Piège trouvé sur l'Île
    SONAR_RESULT,       // Résultat d'une analyse Sonar (ne cause pas de dégâts)
    ALREADY_VISITED     // Case déjà visée (utile pour l'ordinateur)
}