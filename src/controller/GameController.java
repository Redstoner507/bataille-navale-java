package controller;

import model.*;
import model.weapon.Weapon;
import model.player.Player;
import model.weapon.WeaponFactory;
import model.weapon.WeaponType;
import view.*;


public class GameController {
    //Variables
    private static final int CELL_SIZE = 50;
    private final ConfigController _configController;
    private final PlacementController _placementController;
    private GameState _gameState;
    private Player _currentPlayer;
    private Player _player;
    private Player _bot;
    private Weapon _selectedWeapon;
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
        _player = new Player(config.getGridSize(), CELL_SIZE, "Player");
        _bot = new Player(config.getGridSize(), CELL_SIZE, "Computer");
        PlacementScreen placement = new PlacementScreen(this, config.getGridSize(), CELL_SIZE, _placementController, _configController);
        _frame.addScreen(PLACEMENT, placement);
        _frame.showScreen(PLACEMENT);
    }

    public void showGameScreen() {
        GameConfig config = _configController.getGameConfig();
        _gameView = new GameView(_player.getGridView(), _bot.getGridView(), _placementController, _bot.getGrid(), _configController, this);
        _frame.addScreen(GAME, _gameView);
        _frame.showScreen(GAME);
        this._gameState = GameState.HUMAN_TURN;
        this._currentPlayer = _player;
        _gameView.setStatus("It's your turn !");
    }

    public void handlePlayerAttack(int row, int col, Weapon selectedWeapon) {
        if (_gameState != GameState.HUMAN_TURN && _gameState != GameState.COMPUTER_TURN) return;
        Player defender = _currentPlayer == _player ? _player : _bot;

        Attack attack = new Attack(_currentPlayer, defender, selectedWeapon, row, col);

        AttackResult result = defender.receiveAttack(attack);

        updateGameView(result, defender.getGrid(), row, col); // Afficher le résultat du coup

        if (checkGameOver()) {
            _gameView.setStatus("Game Over !");
            return;
        }
        changeTurn();
    }

    private void updateGameView(AttackResult result, Grid targetGrid, int row, int col) {
        // 1. Mettre à jour la grille d'attaque du joueur (visuelle)
        // Nécessite une méthode dans GridView pour mettre à jour l'icône de la case
        _gameView.getComputerGridView().updateCellDisplay(row, col, result.getStatus());

        // Mettre à jour le statut
        _gameView.setStatus(_player.getName() + " shot at coordinates " + (char)('A' + col) + (row + 1) + ". Result : " + result.getMessage());

        // Mettre à jour les statistiques (E3)
    }

    private void changeTurn() {
        if (_currentPlayer == _player) {
            // Passer à l'ordinateur
            _currentPlayer = _bot;
            _gameState = GameState.COMPUTER_TURN;
            _gameView.getComputerGridView().setLocked(true); // Verrouiller la grille d'attaque

            // Simuler le tour de l'ordinateur après un petit délai
            _gameView.setStatus(_bot.getName() + "'s turn...");
            _bot.play();
        } else {
            // Repasser à l'humain
            _currentPlayer = _player;
            _gameState = GameState.HUMAN_TURN;
            _gameView.getComputerGridView().setLocked(false);
            _turnNumber++;
            _gameView.setStatus("It's your turn !");
            _player.play();
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
