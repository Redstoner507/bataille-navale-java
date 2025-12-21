package model.player;

import model.GameConfig;

public class PlayerStrategyFactory {

    public PlayerStrategy createHumanStrategy(GameConfig config) {
        return new HumanStrategy(config);
    }

    public PlayerStrategy createComputerVeryEasyStrategy(GameConfig config) {
        return new ComputerVeryEasyStrategy(config);
    }

    public PlayerStrategy createComputerEasyStrategy(GameConfig config) {
        return new ComputerEasyStrategy(config);
    }

    public PlayerStrategy createComputerMediumStrategy(GameConfig config) {
        return new ComputerMediumStrategy(config);
    }

    public PlayerStrategy createComputerHardStrategy(GameConfig config) {
        return new ComputerEasyStrategy(config);
        //return new ComputerHardStrategy(); // Not implemented yet
    }

    public PlayerStrategy createComputerVeryHardStrategy(GameConfig config) {
        return new ComputerEasyStrategy(config); 
        //return new ComputerVeryHardStrategy(); // Not implemented yet
    }
}
