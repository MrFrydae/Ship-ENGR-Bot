package edu.ship.engr.discordbot.gateways;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import dev.frydae.factories.Factory;
import dev.frydae.factories.annotations.InjectFactory;
import edu.ship.engr.discordbot.testing.annotations.BotTest;
import org.javatuples.Pair;
import org.javatuples.Tuple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test public methods in DiscordGateway.
 *
 * @author merlin
 */
@BotTest
public class DiscordGatewayTest implements Factory<DiscordGateway> {
    @InjectFactory private DiscordGateway gateway;

    private static final List<Pair<String, String>> users = Lists.newArrayList();

    // region Factory
    @AfterEach
    public void tearDown() {
        users.clear();
    }

    @Override
    public ListMultimap<Class<?>, ? extends Tuple> getFactoryData() {
        ListMultimap<Class<?>, Pair<String, String>> data = ArrayListMultimap.create();

        data.put(DiscordGateway.class, Pair.with("student1@ship.edu", "127389127389217837"));
        data.put(DiscordGateway.class, Pair.with("student2@ship.edu", "168125712404840449"));
        data.put(DiscordGateway.class, Pair.with("student3@ship.edu", "173980123782913782"));

        return data;
    }

    @Override
    public DiscordGateway factory(List<? extends Tuple> data) {
        DiscordGateway gateway = mock(DiscordGateway.class);

        if (users.isEmpty()) {
            data.stream().map(t -> (Pair<String, String>) t).forEach(users::add);
        }

        when(gateway.getAllDiscordUsers()).thenReturn(users);
        when(gateway.getEmailByDiscordId(anyString())).thenCallRealMethod();
        when(gateway.getDiscordIdByEmail(anyString())).thenCallRealMethod();
        when(gateway.isDiscordStored(anyString(), anyString())).thenCallRealMethod();
        when(gateway.getAllIds()).thenCallRealMethod();
        when(gateway.getAllEmails()).thenCallRealMethod();
        doAnswer(i -> {
            storeDiscordId(i.getArgument(0), i.getArgument(1));

            return null;
        }).when(gateway).storeDiscordId(anyString(), anyString());

        return gateway;
    }

    private void storeDiscordId(String discordId, String email) {
        users.add(Pair.with(email, discordId));
    }
    // endregion

    @Test
    public void getDiscordIdByEmail() {
        assertEquals("127389127389217837", gateway.getDiscordIdByEmail("student1@ship.edu"));
        assertEquals("168125712404840449", gateway.getDiscordIdByEmail("student2@ship.edu"));
        assertEquals("173980123782913782", gateway.getDiscordIdByEmail("student3@ship.edu"));
    }

    @Test
    public void getEmailByDiscordId() {
        assertEquals("student1@ship.edu", gateway.getEmailByDiscordId("127389127389217837"));
        assertEquals("student2@ship.edu", gateway.getEmailByDiscordId("168125712404840449"));
        assertEquals("student3@ship.edu", gateway.getEmailByDiscordId("173980123782913782"));
    }

    @Test
    public void isDiscordStored() {
        assertTrue(gateway.isDiscordStored("127389127389217837", "student1@ship.edu"));
        assertTrue(gateway.isDiscordStored("168125712404840449", "student2@ship.edu"));
        assertTrue(gateway.isDiscordStored("173980123782913782", "student3@ship.edu"));
    }

    @Test
    public void storeDiscordId() {
        gateway.storeDiscordId("12345678912345", "silly@ship.edu");

        assertTrue(gateway.isDiscordStored("12345678912345", "silly@ship.edu"));
    }

    @Test
    public void getAllEmails() {
        List<String> emails = getFactoryData().get(DiscordGateway.class).stream().map(t -> String.valueOf(t.getValue(0))).collect(Collectors.toList());

        assertEquals(emails, gateway.getAllEmails());
    }

    @Test
    public void getAllIds() {
        List<String> ids = getFactoryData().get(DiscordGateway.class).stream().map(t -> String.valueOf(t.getValue(1))).collect(Collectors.toList());

        assertEquals(ids, gateway.getAllIds());
    }
}
