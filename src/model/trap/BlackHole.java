package model.trap;

import model.Attack;
import model.AttackResult;
import model.AttackStatus;
import model.cell.CellState;

import java.awt.*;

public class BlackHole extends Trap {

    public BlackHole() {
        super(TrapType.BLACKHOLE, CellState.BLACKHOLE, AttackStatus.BLACKHOLE);
    }

    @Override
    public AttackResult activate(Attack attack, AttackResult originalResult) {
        // Coordonnées du coup qui a touché le Trou Noir (qui sont les coordonnées du contre-coup)
        Point counterCoords = originalResult.getHitCoordinates();

        // 1. Créer l'objet Attack à relancer (la Commande)
        Attack attackToRelaunch = new Attack(
                attack.getTargetPlayer(),
                attack.getAttackingPlayer(),
                attack.getWeapon(),
                counterCoords.x,
                counterCoords.y
        );

        // 2. Créer le résultat du Piège (le signal)
        AttackResult trapResult = new AttackResult(AttackStatus.BLACKHOLE, "You triggered a Black hole !");

        // 3. Passer la commande dans le résultat
        trapResult.setAttackToRelaunch(attackToRelaunch);

        return trapResult;
    }
}
