package edu.ship.engr.discordbot.gateways;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.ship.engr.discordbot.containers.Professor;
import edu.ship.engr.discordbot.utils.OptionsManager;

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
        List<Professor> results = gateway.getProfessorByNameOrEmail("merlin@cs.ship.edu");
        assertEquals(1, results.size());
        assertEquals("Dr. Carol Wellington", results.get(0).getName());
    }


}
