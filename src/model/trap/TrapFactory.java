package model.trap;

public class TrapFactory {

    public Trap createBlackHole() {
        return new BlackHole();
    }

    public Trap createTornado() {
        return new Tornado();
    }
}
