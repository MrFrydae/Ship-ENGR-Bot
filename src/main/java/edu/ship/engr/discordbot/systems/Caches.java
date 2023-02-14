package edu.ship.engr.discordbot.systems;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import edu.ship.engr.discordbot.containers.Course;
import edu.ship.engr.discordbot.gateways.CourseGateway;

import java.util.List;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("Guava")
public final class Caches {
    private static final Supplier<List<Course>> courseSupplier = Suppliers.memoizeWithExpiration(() -> new CourseGateway().getAllOfferedCourses(), 1, TimeUnit.MINUTES);

    public static List<Course> getAllOfferedCourses() {
        return courseSupplier.get();
    }
}
