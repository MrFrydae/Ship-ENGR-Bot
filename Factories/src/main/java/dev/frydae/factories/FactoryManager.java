package dev.frydae.factories;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.SneakyThrows;
import org.javatuples.Tuple;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

@SuppressWarnings({"unchecked", "rawtypes"})
public class FactoryManager {
    private final HashMap<Class<?>, Class<? extends Factory>> factories = Maps.newHashMap();
    private final ListMultimap<Class<?>, Tuple> dataProviders = ArrayListMultimap.create();
    private static FactoryManager singleton;
    private final AtomicBoolean registered = new AtomicBoolean(false);

    public static FactoryManager getSingleton() {
        if (singleton == null) {
            singleton = new FactoryManager();
        }

        return singleton;
    }

    private FactoryManager() {}

    public void register(@NotNull String packageStr) {
        if (!registered.get()) {
            Reflections reflections = new Reflections(packageStr, SubTypesScanner.class);

            Set<Class<? extends Factory>> factories = reflections.getSubTypesOf(Factory.class);

            registerFactories(factories);
            registerDataProviders(factories);

            registered.set(true);
        }
    }

    @SneakyThrows(NoSuchMethodException.class)
    private void registerFactories(Set<Class<? extends Factory>> factories) {
        for (Class<? extends Factory> factory : factories) {
            Method method = factory.getMethod("factory", List.class);

            this.factories.put(method.getReturnType(), factory);
        }
    }

    @SneakyThrows(ReflectiveOperationException.class)
    private void registerDataProviders(Set<Class<? extends Factory>> factories) {
        for (Class<? extends Factory> test : factories) {
            Factory factory = test.getDeclaredConstructor().newInstance();

            this.dataProviders.putAll(factory.getFactoryData());
        }
    }

    @SneakyThrows(IllegalAccessException.class)
    public static <T> T resolveFactory(Class<T> type) {
        T instance = factory(type);

        for (Field field : type.getDeclaredFields()) {
            if (getSingleton().factories.containsKey(field.getType())) {
                field.setAccessible(true);
                field.set(instance, resolveFactory(field.getType()));
            }
        }

        return instance;
    }

    @NotNull
    @SneakyThrows(ReflectiveOperationException.class)
    public static <T> T factory(Class<T> clazz) {
        Class<? extends Factory> factoryClass = getSingleton().factories.get(clazz);
        Factory<T> factory = factoryClass.getConstructor().newInstance();

        return factory.factory(getSingleton().dataProviders.get(clazz));
    }
}
