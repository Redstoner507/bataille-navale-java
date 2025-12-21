package model;

/**
 * Defines the different results possible of an attack.
 */
public enum AttackStatus {
    MISS,               // Tirs manqués dans l'eau
    ISLAND_MISS,        // Tirs manqués sur l'île
    HIT,                // Bateau touché mais pas coulé
    SUNK,               // Bateau coulé
    BLACKHOLE,          // Trou Noir activé
    TORNADO,            // Tornade activée
    BOMB_FOUND,         // Bombe trouvée sur l'île
    SONAR_FOUND,        // Sonar trouvé sur l'île
    SONAR_RESULT,       // Résultat d'une analyse Sonar (ne cause pas de dégâts)
    ALREADY_VISITED     // Case déjà visée (utile pour l'ordinateur)
}