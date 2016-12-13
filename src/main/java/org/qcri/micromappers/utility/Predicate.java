package org.qcri.micromappers.utility;

/**
 * This class needs to be replaced with java.util.function.Predicate<T>
 * which is introduced in Java 8
 *
 */
public interface Predicate<T> {

	static String filterName = null;
    boolean test(T t);

    String getFilterName();
}
