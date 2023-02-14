package edu.ship.engr.discordbot.testing.extensions;

import edu.ship.engr.discordbot.commands.core.CommandManager;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class CommandTestExtension implements BeforeEachCallback {
    /**
     * Callback that is invoked <em>before</em> each test is invoked.
     *
     * @param context the current extension context; never {@code null}
     */
    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        CommandManager.resetSingleton();
    }
}
