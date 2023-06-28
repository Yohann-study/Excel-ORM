package org.yohann.excel.query;

/**
 * A functional interface that defines a method for matching two objects.
 * The match method takes in two objects - an original object and a target object - and returns a boolean value
 * indicating whether they match or not.
 */
@FunctionalInterface
public interface Match {

    /**
     * Matches two objects and returns a boolean value indicating whether they match or not.
     *
     * @param original the original object to be matched
     * @param target   the target object to be matched
     * @return true if the objects match, false otherwise
     */
    boolean match(Object original, Object target);

}
