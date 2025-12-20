package controller;

import model.*;
import model.player.PlayerStrategyFactory;
import model.ship.Ship;
import model.weapon.Weapon;
import model.player.Player;
import model.weapon.WeaponType;
import view.*;

import java.awt.*;
import java.util.ArrayList;

public class GameController {
    //Variables
    private static final int CELL_SIZE = 50;
    private ConfigController _configController;
    private PlacementController _placementController;
    private StatsController _statsController;
    private GameState _gameState;
    private Player _player;
    private Player _bot;
    private Weapon _selectedWeapon;
    private ArrayList<AttackResult> _previousBotResults = new ArrayList<>();
    private int _turnNumber = 1;
    private final ArrayList<String> _playerAttackHistory = new ArrayList<>();
    private final ArrayList<String> _botAttackHistory = new ArrayList<>();
    //Vues
    private final MainFrame _frame;
    private GameView _gameView;
    private static final String START = "start";
    private static final String CONFIG = "config";
    private static final String PLACEMENT = "placement";
    private static final String GAME = "game";
    private static final String END = "end";

    public GameController() {
        _frame = new MainFrame();
        _frame.addScreen(START, new StartScreen(this));
        _frame.setVisible(true);
        resetGame();

        showStartScreen();
    }

    public void showStartScreen() {
        _frame.showScreen(START);
    }

    public void showConfigScreen() {
        _frame.showScreen(CONFIG);
    }

    public void showPlacementScreen() {
        _gameState = GameState.SETUP;
        GameConfig config = _configController.getGameConfig();
        _player = new Player(config.getGridSize(), CELL_SIZE, "Player", config.isIslandModeEnabled());
        _bot = new Player(config.getGridSize(), CELL_SIZE, "Computer", config.isIslandModeEnabled());

        PlayerStrategyFactory playerStrategyFactory = new PlayerStrategyFactory();
        _player.setStrategy(playerStrategyFactory.createHumanStrategy(config));

        switch (config.getComputerStrategy()) {
            case COMPUTER_VERY_EASY ->
                _bot.setStrategy(playerStrategyFactory.createComputerVeryEasyStrategy(config));
            case COMPUTER_EASY ->
                _bot.setStrategy(playerStrategyFactory.createComputerEasyStrategy(config));
            case COMPUTER_MEDIUM ->
                _bot.setStrategy(playerStrategyFactory.createComputerMediumStrategy(config));
            case COMPUTER_HARD ->
                _bot.setStrategy(playerStrategyFactory.createComputerHardStrategy(config));
            case COMPUTER_VERY_HARD ->
                _bot.setStrategy(playerStrategyFactory.createComputerVeryHardStrategy(config));
            case HUMAN ->
                // Default to easy if human is selected for bot (à modifier si multi-joueur ou autre)
                _bot.setStrategy(playerStrategyFactory.createComputerEasyStrategy(config));
        }
        PlacementScreen placement = new PlacementScreen(this, CELL_SIZE, _placementController, _configController);
        _frame.addScreen(PLACEMENT, placement);
        _frame.showScreen(PLACEMENT);
    }

    public void showGameScreen() {
        _gameView = new GameView(_player.getGridView(), _bot.getGridView(), _placementController, _bot.getGrid(), _configController, this);
        fillStatsController();
        _gameView.setTurnNumber(_turnNumber);
        _frame.addScreen(GAME, _gameView);
        _frame.showScreen(GAME);
        this._gameState = GameState.HUMAN_TURN;
        setSelectedWeapon(null);
    }

    public void showEndScreen(String endMessage) {
        _frame.addScreen(END, new EndScreen(this, endMessage, _gameView.getStatsView()));
        _frame.showScreen(END);
    }

    public void handleHumanAttack(int row, int col) {
        if (_gameState != GameState.HUMAN_TURN) return;

        setSelectedWeapon(_selectedWeapon.getWeaponType()); // Ensure weapon is valid
        Attack attack = new Attack(_player, _bot, _selectedWeapon, row, col);
        _selectedWeapon.use();
        ArrayList<AttackResult> results = _bot.receiveAttack(attack);

        if (!results.isEmpty() && results.getFirst().getStatus() == AttackStatus.ALREADY_VISITED) return; //Annuler l'attaque si elle a déjà été faite
        endTurn(results, attack);
    }

    private void handleComputerAttack() {
        if (_gameState != GameState.COMPUTER_TURN) return;

        Attack attack = null;
        AttackResult baseResult = new AttackResult(AttackStatus.ALREADY_VISITED, "");
        while (baseResult.getStatus() == AttackStatus.ALREADY_VISITED) {
            attack = _bot.play(_previousBotResults);
            attack.setAttackingPlayer(_bot);
            attack.setTargetPlayer(_player);
            _previousBotResults = _player.receiveAttack(attack);
            baseResult = _previousBotResults.getFirst();
        }

        if (attack == null) return;

        endTurn(_previousBotResults, attack);
    }

    /**
     * Handles the end of the turn after an attack was executed.
     * Manages and displays the effects of the attack.
     * * @param results ArrayList containing the impacts of the attack for each cell concerned.
     * @param attack The initial Attack
     */
    private void endTurn(ArrayList<AttackResult> results, Attack attack) {
        if (results.isEmpty()) {
            changeTurn();
            return;
        }

        // Traiter tous les impacts séquentiellement (Missile, ou Bombe sur bateaux/eau)
        for (AttackResult result : results) {
            processSingleImpact(result, attack);
            // Exécuter l'attaque miroir
            if (result.getStatus() == AttackStatus.BLACKHOLE) {
                Attack counterAttack = result.getAttackToRelaunch();
                executeCounterAttack(counterAttack);
            }
        }

        // Mettre à jour l'historique
        updateHistoryLog(results, attack);

        // Mettre à jour les statistiques
        _gameView.getStatsView().update(_statsController);

        // Vérifier la fin du jeu et changer de tour
        if (checkGameOver()) {
            // Récupérer le joueur qui a gagné
            Player p = attack.getAttackingPlayer();
            String endMessage;
            if (p.getName().equals("Player")) endMessage = "You won !";
            else endMessage = "You lost !";
            showEndScreen(endMessage);
            return;
        }

        changeTurn();
    }

    /**
     * Updates the game log.
     * @param results ArrayList of the real attack impacts.
     * @param attack The initial Attack.
     */
    private void updateHistoryLog(ArrayList<AttackResult> results, Attack attack) {
        // Déterminer la liste d'historique à mettre à jour (Humain ou Ordinateur)
        boolean isHumanAttack = attack.getAttackingPlayer() == _player;
        ArrayList<String> targetHistory = isHumanAttack ? _playerAttackHistory : _botAttackHistory;

        // Le résultat principal est le premier de la liste
        AttackResult mainResult = results.getFirst();

        // Créer la chaîne de base
        String entryBase = String.format("%s%d : %s",
                (char)('A' + attack.getRealCol()),
                attack.getRealRow() + 1,
                mainResult.getMessage());

        // Gérer les cas spéciaux et ajouter les entrées à la liste
        if (attack.getWeapon().getWeaponType() == WeaponType.BOMB && results.size() > 1) {
            // Cas Bombe : Ajout de la ligne principale et de tous les impacts secondaires
            targetHistory.add(entryBase);

            for (int i = 1; i < results.size(); i++) {
                AttackResult subResult = results.get(i);
                // Format : "E4 : Hit" (Utiliser l'indentation pour aligner dans la colonne)
                String subEntry = String.format("      %s%d : %s",
                        (char)('A' + subResult.getHitCoordinates().y),
                        subResult.getHitCoordinates().x + 1,
                        subResult.getMessage());
                targetHistory.add(subEntry);
            }
        }
        else if (attack.getWeapon().getWeaponType() == WeaponType.SONAR && mainResult.getSonarCount() != null) {
            // Cas Sonar : Affichage du compte des objets trouvés
            targetHistory.add(String.format("%s%d : Sonar -> %d",
                    (char)('A' + attack.getRealCol()),
                    attack.getRealRow() + 1,
                    mainResult.getSonarCount()));
        }
        else if (mainResult.getStatus() == AttackStatus.BLACKHOLE && mainResult.getAttackToRelaunch() != null) {
            // Cas Trou Noir : Affichage de l'activation + marqueur de contre-coup
            targetHistory.add(entryBase);
            targetHistory.add("      -> Attack sent back !");
        }
        else {
            // Cas Missile
            targetHistory.add(entryBase);
        }

        // Equilibrage des listes pour l'affichage en colonnes
        int maxLen = Math.max(_playerAttackHistory.size(), _botAttackHistory.size());

        // Remplir les listes avec des entrées vides (" ") pour maintenir l'alignement visuel
        while (_playerAttackHistory.size() < maxLen) { _playerAttackHistory.add(" "); }
        while (_botAttackHistory.size() < maxLen) { _botAttackHistory.add(" "); }

        // Mise à jour de la Vue
        // Cette méthode dans HistoryPanel rafraîchit les JTextArea pour les deux colonnes.
        _gameView.getHistoryView().updateHistory(_playerAttackHistory, _botAttackHistory);
    }

    /**
     * Handles the result of a single impact of an attack.
     * Manages the grid display.
     * @param result The result of a single impact.
     * @param originalAttack The original Attack.
     */
    private void processSingleImpact(AttackResult result, Attack originalAttack) {
        Point hitCoordinates = result.getHitCoordinates();
        GridView targetGridView = originalAttack.getTargetPlayer().getGridView();

        // Mise à jour de la grille et du statut de la victime
        updateGameView(result, targetGridView, hitCoordinates.x, hitCoordinates.y);
    }

    /**
     * Executes the attack returned by the BlackHole.
     * @param counterAttack The Attack Object prepared by the trap.
     */
    private void executeCounterAttack(Attack counterAttack) {
        Player targetOfCounterAttack = counterAttack.getTargetPlayer();
        GridView attackerGridView = targetOfCounterAttack.getGridView();

        // Exécuter la relance complète
        ArrayList<AttackResult> counterResults = targetOfCounterAttack.receiveAttack(counterAttack);

        // Afficher tous les impacts du contre-coup
        for (AttackResult finalHit : counterResults) {
            Point hitCoordinates = finalHit.getHitCoordinates();

            // Mise à jour de la grille du joueur qui s'est tiré dessus
            attackerGridView.updateCellDisplay(finalHit, hitCoordinates.x, hitCoordinates.y);
        }
    }

    private void updateGameView(AttackResult result, GridView targetGridView, int row, int col) {
        // Mettre à jour la grille visuelle
        targetGridView.updateCellDisplay(result, row, col);
    }

    private void changeTurn() {
        if (_gameState == GameState.HUMAN_TURN) {
            // Passer à l'ordinateur
            _gameState = GameState.COMPUTER_TURN;
            _gameView.getComputerGridView().setLocked(true); // Verrouiller la grille d'attaque

            // Simuler le tour de l'ordinateur après un petit délai
            handleComputerAttack();
        }
        else if (_gameState == GameState.COMPUTER_TURN) {
            // Repasser à l'humain
            _gameState = GameState.HUMAN_TURN;
            _gameView.getComputerGridView().setLocked(false);
            _turnNumber++;
            _gameView.setTurnNumber(_turnNumber);
        }
    }

    private boolean checkGameOver() {
        // Nécessite la méthode allShipsSunk() dans la classe Player
        if (_player.allShipsSunk() || _bot.allShipsSunk()) {
            _gameState = GameState.GAME_OVER;
            return true;
        }
        return false;
    }

    private void fillStatsController() {
        _statsController = _bot.getGrid().getStatsController();
        int shipsUntouched = 0;
        int totalShipCells = 0;
        int weaponsAvailable = 0;
        int islandCellsLeft = 0;

        ArrayList<Placeable> ships = _configController.getShipConfig();
        for (Placeable placeable : ships) {
            Ship ship = (Ship) placeable;
            int cellsRemaining = ship.getCellsRemaining();
            int totalCells = ship.getTotalCells();
            if (cellsRemaining == totalCells) {
                shipsUntouched++;
            }
            totalShipCells += totalCells;
        }

        if (_configController.getGameConfig().isIslandModeEnabled()) {
            int islandSize = getPlayerGrid().getIslandSize();
            islandCellsLeft = islandSize * islandSize;
        }
        else {
            weaponsAvailable = 2;
        }

        _statsController.setShipsUntouched(shipsUntouched);
        _statsController.setTotalShipCells(totalShipCells);
        _statsController.setWeaponsAvailable(weaponsAvailable);
        _statsController.setIslandCellsLeft(islandCellsLeft);
        _gameView.getStatsView().update(_statsController);
    }

    public Grid getPlayerGrid() {
        return _player.getGrid();
    }

    public GridView getPlayerGridView() {
        return _player.getGridView();
    }
    
    public void quit() {
        _frame.dispose();
    }

    public void setSelectedWeapon(WeaponType weaponType) {
        if (_player == null) return;

        Weapon weapon = _player.getWeaponFromInventory(weaponType);
        if (weapon != null && weapon.getUses() != 0) {
            this._selectedWeapon = weapon;
        }
        else {
            this._selectedWeapon = _player.getWeaponFromInventory(WeaponType.MISSILE); // Default Weapon
            _gameView.setWeaponSelection(WeaponType.MISSILE);
        }
    }

    public void resetGame() {
        _configController = new ConfigController();
        _frame.addScreen(CONFIG, new ConfigScreen(this, _configController));
        _placementController = new PlacementController();
        _statsController = null;
        _player = null;
        _bot = null;
        setSelectedWeapon(WeaponType.MISSILE);
        _playerAttackHistory.clear();
        _botAttackHistory.clear();
        _previousBotResults.clear();
    }
}
