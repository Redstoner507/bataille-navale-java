package controller;

import model.*;
import model.player.PlayerStrategyFactory;
import model.weapon.Weapon;
import model.player.Player;
import model.weapon.WeaponFactory;
import model.weapon.WeaponType;
import view.*;

import java.awt.*;
import java.util.ArrayList;


public class GameController {
    //Variables
    private static final int CELL_SIZE = 50;
    private final ConfigController _configController;
    private final PlacementController _placementController;
    private GameState _gameState;
    private Player _player;
    private Player _bot;
    private Weapon _selectedWeapon;
    private ArrayList<AttackResult> _previousBotResults = new ArrayList<>();
    private int _turnNumber = 0;
    //Vues
    private final MainFrame _frame;
    private GameView _gameView;
    private static final String START = "start";
    private static final String CONFIG = "config";
    private static final String PLACEMENT = "placement";
    private static final String GAME = "game";

    public GameController() {
        _configController = new ConfigController();
        _placementController = new PlacementController();
        _player = null;
        _bot = null;

        _frame = new MainFrame();

        _frame.addScreen(START, new StartScreen(this));
        _frame.addScreen(CONFIG, new ConfigScreen(this, _configController));

        _frame.setVisible(true);
        showStartScreen();
        setSelectedWeapon(WeaponType.MISSILE);
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
        _player.setStrategy(playerStrategyFactory.createHumanStrategy());

        switch (config.getPlayerStrategy()) {
            case COMPUTER_VERY_EASY ->
                _bot.setStrategy(playerStrategyFactory.createComputerVeryEasyStrategy());
            case COMPUTER_EASY ->
                _bot.setStrategy(playerStrategyFactory.createComputerEasyStrategy());
            case COMPUTER_MEDIUM ->
                _bot.setStrategy(playerStrategyFactory.createComputerMediumStrategy());
            case COMPUTER_HARD ->
                _bot.setStrategy(playerStrategyFactory.createComputerHardStrategy());
            case COMPUTER_VERY_HARD ->
                _bot.setStrategy(playerStrategyFactory.createComputerVeryHardStrategy());
            case HUMAN -> {
                // Default to easy if human is selected for bot (à modifier si multi-joueur ou autre)
                _bot.setStrategy(playerStrategyFactory.createComputerEasyStrategy());
            }
        }
        PlacementScreen placement = new PlacementScreen(this, config.getGridSize(), CELL_SIZE, _placementController, _configController);
        _frame.addScreen(PLACEMENT, placement);
        _frame.showScreen(PLACEMENT);
    }

    public void showGameScreen() {
        _gameView = new GameView(_player.getGridView(), _bot.getGridView(), _placementController, _bot.getGrid(), _configController, this);
        _frame.addScreen(GAME, _gameView);
        _frame.showScreen(GAME);
        this._gameState = GameState.HUMAN_TURN;
        _gameView.setStatus("It's your turn !");
    }

    public void handleHumanAttack(int row, int col) {
        if (_gameState != GameState.HUMAN_TURN) return;

        Attack attack = new Attack(_player, _bot, _selectedWeapon, row, col);
        ArrayList<AttackResult> results = _bot.receiveAttack(attack);

        if (!results.isEmpty() && results.getFirst().getStatus() == AttackStatus.ALREADY_VISITED) return; //Annuler l'attaque si elle a déjà été faite
        endTurn(results, attack);
    }

    public void handleComputerAttack() {
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

    public void endTurn(ArrayList<AttackResult> results, Attack attack) {
        // Afficher le résultat du coup
        String playerName = attack.getAttackingPlayer().getName();
        GridView playerGridView = attack.getTargetPlayer().getGridView();
        for (AttackResult result : results) {
            Point hitCoordinates = result.getHitCoordinates();
            updateGameView(result, playerName, playerGridView, hitCoordinates.x, hitCoordinates.y);
        }

        if (checkGameOver()) {
            _gameView.setStatus("Game Over !");
            return;
        }
        changeTurn();
    }

    private void updateGameView(AttackResult result, String playerName, GridView targetGridView, int row, int col) {
        // 1. Mettre à jour la grille visuelle
        Point hitCoordinates = result.getHitCoordinates();
        targetGridView.updateCellDisplay(result, hitCoordinates.x, hitCoordinates.y);

        // Mettre à jour le statut
        _gameView.setStatus(playerName + " shot at coordinates " + (char)('A' + col) + (row + 1) + ". Result : " + result.getMessage());

        // Mettre à jour les statistiques (E3)
    }

    private void changeTurn() {
        if (_gameState == GameState.HUMAN_TURN) {
            // Passer à l'ordinateur
            _gameState = GameState.COMPUTER_TURN;
            _gameView.getComputerGridView().setLocked(true); // Verrouiller la grille d'attaque

            // Simuler le tour de l'ordinateur après un petit délai
            //_gameView.setStatus(_bot.getName() + "'s turn...");
            handleComputerAttack();
        }
        else if (_gameState == GameState.COMPUTER_TURN) {
            // Repasser à l'humain
            _gameState = GameState.HUMAN_TURN;
            _gameView.getComputerGridView().setLocked(false);
            _turnNumber++;
            //_gameView.setStatus("It's your turn !");
        }
    }

    private boolean checkGameOver() {
        // Nécessite la méthode allShipsSunk() dans la classe Player
        if (_player.allShipsSunk() || _bot.allShipsSunk()) {
            _gameState = GameState.GAME_OVER;
            // Gérer l'affichage de l'écran de fin (E4)
            return true;
        }
        return false;
    }

    public Grid getPlayerGrid() {
        return _player.getGrid();
    }

    public Grid getBotGrid() {
        return _bot.getGrid();
    }

    public GridView getPlayerGridView() {
        return _player.getGridView();
    }
    
    public void quit() {
        _frame.dispose();
    }

    public Weapon getSelectedWeapon() {
        return this._selectedWeapon;
    }

    public void setSelectedWeapon(WeaponType weaponType) {
        WeaponFactory weaponFactory = new WeaponFactory();
        switch (weaponType) {
            case MISSILE -> this._selectedWeapon = weaponFactory.createMissile();
            case BOMB -> this._selectedWeapon = weaponFactory.createBomb();
            case SONAR -> this._selectedWeapon = weaponFactory.createSonar();
        }
    }
}
