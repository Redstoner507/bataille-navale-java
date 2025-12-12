package model.player;

public class PlayerStrategyFactory {

    public PlayerStrategy createHumanStrategy(int gridSize) {
        return new HumanStrategy(gridSize);
    }

    public PlayerStrategy createComputerVeryEasyStrategy(int gridSize) {
        return new ComputerVeryEasyStrategy(gridSize);
    }

    public PlayerStrategy createComputerEasyStrategy(int gridSize) {
        return new ComputerEasyStrategy(gridSize);
    }

    public PlayerStrategy createComputerMediumStrategy(int gridSize) {
        return new ComputerMediumStrategy(gridSize);
    }

    public PlayerStrategy createComputerHardStrategy(int gridSize) {
        return new ComputerEasyStrategy(gridSize);
        //return new ComputerHardStrategy(); // Not implemented yet
    }

    public PlayerStrategy createComputerVeryHardStrategy(int gridSize) {
        return new ComputerEasyStrategy(gridSize); 
        //return new ComputerVeryHardStrategy(); // Not implemented yet
    }
}
