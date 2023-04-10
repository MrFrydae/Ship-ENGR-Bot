package dev.frydae.factories;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import org.javatuples.Tuple;

import java.util.List;

public interface Factory<T> {
    T factory(List<? extends Tuple> data);

    default ListMultimap<Class<?>, ? extends Tuple> getFactoryData() {
        return ArrayListMultimap.create();
    }
}
