package edu.ship.engr.discordbot.commands;

import edu.ship.engr.discordbot.commands.core.CommandContexts;
import edu.ship.engr.discordbot.commands.core.CommandExecutionContext;
import edu.ship.engr.discordbot.commands.core.CommandManager;
import edu.ship.engr.discordbot.commands.core.IllegalCommandException;
import edu.ship.engr.discordbot.testing.annotations.CommandTest;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@CommandTest
public class CommandContextsTest {
    @Test
    public void testStructure() throws IllegalCommandException {
        CommandContexts<CommandExecutionContext> commandContexts = CommandManager.getCommandContexts();

        commandContexts.registerContext(TestStructure.class, c -> new TestStructure(c.getMapping().getAsString()));

        OptionMapping mapping = mock(OptionMapping.class);
        when(mapping.getAsString()).thenReturn("fish");

        CommandExecutionContext context = new CommandExecutionContext(null, null, mapping, null);

        Object resolve = commandContexts.getResolver(TestStructure.class).resolve(context);

        assertTrue(resolve instanceof TestStructure);

        TestStructure structure = (TestStructure) resolve;
        assertEquals(structure.getElement(), "fish");
    }

    @Test
    public void testInteger() throws IllegalCommandException {
        CommandContexts<CommandExecutionContext> commandContexts = CommandManager.getCommandContexts();

        OptionMapping mapping = mock(OptionMapping.class);
        when(mapping.getAsInt()).thenReturn(12);

        CommandExecutionContext context = new CommandExecutionContext(null, null, mapping, null);

        Object resolve = commandContexts.getResolver(Integer.class).resolve(context);

        assertTrue(resolve instanceof Integer);

        Integer i = (Integer) resolve;
        assertEquals(i, 12);
    }


    private static class TestStructure {
        private final String element;

        public TestStructure(String element) {
            this.element = element;
        }

        public String getElement() {
            return element;
        }
    }
}
