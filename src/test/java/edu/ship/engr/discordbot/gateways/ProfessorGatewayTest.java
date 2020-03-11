package edu.ship.engr.discordbot.gateways;

import edu.ship.engr.discordbot.containers.Professor;
import edu.ship.engr.discordbot.utils.OptionsManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProfessorGatewayTest {

    /**
     * Make sure we are in testing mode.
     */
    @BeforeEach
    public void setup() {
        OptionsManager.getSingleton(true);
    }

    @Test
    void testCanGetByName() {
        ProfessorGateway gateway = new ProfessorGateway();
        List<Professor> results = gateway.getProfessorByNameOrEmail("Wellington");
        assertEquals(1, results.size());
        assertEquals("Dr. Carol Wellington", results.get(0).getName());
    }

    @Test
    void testCanGetByEmail() {
        ProfessorGateway gateway = new ProfessorGateway();
        List<Professor> results = gateway.getProfessorByNameOrEmail("merlin@engr.ship.edu");
        assertEquals(1, results.size());
        assertEquals("Dr. Carol Wellington", results.get(0).getName());
    }


}
