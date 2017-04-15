/**
 * Runs the LOOK command <code>LOOK_PER_SECOND</code>number of times
 * per second.
 */
public class HumanClientLookThread implements Runnable {

    static final private int LOOK_PER_SECOND = 1;
    private HumanClientController controller;

    /**
     * Constructor.
     *
     * @param controller    the controller.
     */
    public HumanClientLookThread(HumanClientController controller) {
        this.controller = controller;
    }

    /**
     * Executed when it is started in a new Thread. Loops while the
     * game is not over, running <code>processLookThreadAction()</code>
     * in the controller, before delaying.
     */
    public void run() {
        while (!controller.getGameOver()) {
            controller.processLookThreadAction();
            try {
                Thread.sleep(1000/LOOK_PER_SECOND);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
