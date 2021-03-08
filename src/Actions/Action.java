package Actions;

import java.io.IOException;
import java.net.URISyntaxException;

public abstract class Action {
    abstract String action() throws URISyntaxException, IOException;//method that returns some output to be displayed
}
