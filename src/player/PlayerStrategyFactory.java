package player;

public class PlayerStrategyFactory {

    public PlayerStrategy createHumanStrategy() {
        return new HumanStrategy();
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
}
