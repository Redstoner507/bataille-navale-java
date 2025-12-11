package model.player;

public class PlayerStrategyFactory {

    public PlayerStrategy createHumanStrategy() {
        return new HumanStrategy();
    }

    public PlayerStrategy createComputerVeryEasyStrategy() {
        return new ComputerVeryEasyStrategy();
    }

    public PlayerStrategy createComputerEasyStrategy() {
        return new ComputerEasyStrategy();
    }

    public PlayerStrategy createComputerMediumStrategy() {
        return new ComputerEasyStrategy();
        //return new ComputerMediumStrategy(); // Not implemented yet
    }

    public PlayerStrategy createComputerHardStrategy() {
        return new ComputerEasyStrategy();
        //return new ComputerHardStrategy(); // Not implemented yet
    }

    public PlayerStrategy createComputerVeryHardStrategy() {
        return new ComputerEasyStrategy(); 
        //return new ComputerVeryHardStrategy(); // Not implemented yet
    }
}
