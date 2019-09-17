package edu.ship.engr.discordbot.utils;

import edu.ship.engr.discordbot.containers.Student;
import org.apache.commons.csv.CSVRecord;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class CSVUtilTest {

    @Before
    public void setup() {
        OptionsManager.getSingleton(true);
    }

    @Test
    public void testWeGetAGoodStudent() {
        CSVRecord studentRow = CSVUtil.getStudentClasses().getRecords().get(0);
        String name = Util.ucfirst(studentRow.get("PREF_FIRST_NAME") + " " + studentRow.get("PREF_LAST_NAME"));
        Student s = CSVUtil.getStudentFromRecord(studentRow);
        String email = studentRow.get("EMAIL");
        assertEquals(email, s.getEmail());
        assertEquals(name, s.getName());
        assertEquals(CSVUtil.getCrewByEmail(email), s.getCrew());
        assertEquals(studentRow.get("MAJOR"), s.getMajor());
        assertEquals(CSVUtil.getDiscordIdByEmail(email), s.getDiscordId());
    }
}
