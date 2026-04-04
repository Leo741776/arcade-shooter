// Separate entry point required because JavaFX applications packaged as a JAR
// will fail to launch if the main class directly extends Application — the JVM
// needs a non-JavaFX class as the entry point.

package core;

public class Launcher {

    public static void main(String[] args) {
        Main.main(args);
    }
}