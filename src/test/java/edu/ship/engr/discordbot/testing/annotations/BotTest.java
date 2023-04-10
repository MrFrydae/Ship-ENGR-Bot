package edu.ship.engr.discordbot.testing.annotations;

import dev.frydae.factories.extensions.FactoryExtension;
import edu.ship.engr.discordbot.testing.extensions.BotTestExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@ExtendWith({BotTestExtension.class, FactoryExtension.class})
public @interface BotTest {
}
