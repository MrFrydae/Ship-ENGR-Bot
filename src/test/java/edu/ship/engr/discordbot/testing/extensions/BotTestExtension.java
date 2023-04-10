package edu.ship.engr.discordbot.testing.extensions;

import edu.ship.engr.discordbot.utils.OptionsManager;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class BotTestExtension implements BeforeAllCallback {
    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        OptionsManager.getSingleton().setTestMode(true);

        context.getRoot().getStore(ExtensionContext.Namespace.create("factories")).put("package", "edu.ship.engr.discordbot");
    }
}
