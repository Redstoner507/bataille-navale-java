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
        return new ComputerMediumStrategy();
    }

    public PlayerStrategy createComputerHardStrategy() {
        return new ComputerHardStrategy();
    }

    public PlayerStrategy createComputerVeryHardStrategy() {
        return new ComputerVeryHardStrategy();
    }
}
