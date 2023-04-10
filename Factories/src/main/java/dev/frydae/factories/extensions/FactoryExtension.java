package dev.frydae.factories.extensions;

import dev.frydae.factories.FactoryManager;
import dev.frydae.factories.annotations.InjectFactory;
import lombok.SneakyThrows;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;

import java.lang.reflect.Field;

public class FactoryExtension implements BeforeEachCallback, BeforeAllCallback {
    /**
     * Callback that is invoked <em>before</em> an individual test and any
     * user-defined setup methods for that test have been executed.
     *
     * @param context the current extension context; never {@code null}
     */
    @Override
    @SneakyThrows(IllegalAccessException.class)
    public void beforeEach(ExtensionContext context) {
        System.out.println("Test class is empty: " + context.getTestClass().isEmpty());
        System.out.println("Test instance is empty: " + context.getTestInstance().isEmpty());

        if (context.getTestClass().isEmpty() || context.getTestInstance().isEmpty()) {
            System.out.println("Failed to inject factories: " + context.getDisplayName());
            return;
        }

        Class<?> clazz = context.getTestClass().get();

        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(InjectFactory.class)) {
                field.setAccessible(true);
                field.set(context.getTestInstance().get(), FactoryManager.resolveFactory(field.getType()));
            }
        }
    }


    /**
     * Callback that is invoked once <em>before</em> all tests in the current
     * container.
     *
     * @param context the current extension context; never {@code null}
     */
    @Override
    public void beforeAll(ExtensionContext context) {
        String packageStr = context.getRoot().getStore(Namespace.create("factories")).get("package", String.class);

        if (packageStr != null) {
            FactoryManager.getSingleton().register(packageStr);
        }
    }
}
