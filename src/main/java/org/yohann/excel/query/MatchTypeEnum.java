package org.yohann.excel.query;

import java.util.Date;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * An enumeration of the types of matching that can be performed by the Matcher class.
 * Each enumeration contains a Match object that implements the matching logic for that type,
 * and a Predicate that specifies the types of objects that are supported by that type of matching.
 */
public enum MatchTypeEnum {
    /**
     * Performs an exact match between the original and target objects.
     */
    EQUALS(Object::equals, (o -> true)),

    /**
     * Performs a partial match between the original and target objects, where the original object contains the target object as a substring.
     * Only supports matching against String objects.
     */
    LIKE((original, target) -> ((String) original).contains((String) target), (o -> o instanceof String)),

    /**
     * Performs a less-than comparison between the original and target objects.
     * Supports matching against Number and Date objects.
     */
    LESS((original, target) -> {
        if (original instanceof Number) {
            return ((Number) original).doubleValue() < ((Number) target).doubleValue();
        }
        return ((Date) original).compareTo((Date) target) < 0;
    }, (o -> o instanceof Number || o instanceof Date)),

    /**
     * Performs a less-than-or-equal-to comparison between the original and target objects.
     * Supports matching against Number and Date objects.
     */
    LESS_EQUALS((original, target) -> {
        if (original instanceof Number) {
            return ((Number) original).doubleValue() <= ((Number) target).doubleValue();
        }
        return ((Date) original).compareTo((Date) target) <= 0;
    }, (o -> o instanceof Number || o instanceof Date)),

    /**
     * Performs a greater-than comparison between the original and target objects.
     * Supports matching against Number and Date objects.
     */
    GREATER((original, target) -> {
        if (original instanceof Number) {
            return ((Number) original).doubleValue() > ((Number) target).doubleValue();
        }
        return ((Date) original).compareTo((Date) target) > 0;
    }, (o -> o instanceof Number || o instanceof Date)),

    /**
     * Performs a greater-than-or-equal-to comparison between the original and target objects.
     * Supports matching against Number and Date objects.
     */
    GREATER_EQUALS((original, target) -> {
        if (original instanceof Number) {
            return ((Number) original).doubleValue() >= ((Number) target).doubleValue();
        }
        return ((Date) original).compareTo((Date) target) >= 0;
    }, (o -> o instanceof Number || o instanceof Date)),

    /**
     * Matches objects that are null.
     * Supports matching against any object type.
     */
    NULL(((original, target) -> Objects.isNull(original)), (o -> true)),

    /**
     * Matches objects that are not null.
     * Supports matching against any object type.
     */
    NOT_NULL(((original, target) -> Objects.nonNull(original)), (o -> true)),
    ;

    private Match match;
    private Predicate<Object> support;

    /**
     * Constructs a new MatchTypeEnum object with the given Match object and Predicate.
     *
     * @param match   the Match object that implements the matching logic for this type
     * @param support the Predicate that specifies the types of objects that are supported by this type of matching
     */
    MatchTypeEnum(Match match, Predicate<Object> support) {
        this.match = match;
        this.support = support;
    }

    /**
     * Returns the Match object that implements the matching logic for this type.
     *
     * @return the Match object that implements the matching logic for this type
     */
    protected Match getMatch() {
        return match;
    }

    /**
     * Returns the Predicate that specifies the types of objects that are supported by this type of matching.
     *
     * @return the Predicate that specifies the types of objects that are supported by this type of matching
     */
    protected Predicate<Object> getSupport() {
        return support;
    }
}
