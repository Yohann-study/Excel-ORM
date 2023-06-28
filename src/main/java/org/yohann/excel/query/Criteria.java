package org.yohann.excel.query;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * A class representing a set of criteria used to match objects based on the values of their fields.
 * A Criteria object contains a list of Matcher objects, each of which specifies a field to match and the criteria for that matching.
 */
public class Criteria {

    /**
     * The list of Matcher objects that define the criteria for matching.
     */
    private final List<Matcher> matchers = new ArrayList<>();

    /**
     * Constructs a new empty Criteria object.
     */
    public Criteria() {

    }

    /**
     * Constructs a new Criteria object with a single Matcher that matches the given field name and value exactly.
     *
     * @param fieldName the name of the field to match
     * @param value     the value to match against
     */
    public Criteria(String fieldName, Object value) {
        matchers.add(new Matcher(fieldName, value));
    }

    /**
     * Adds a Matcher to the Criteria object that matches the given field name and value using a partial matching algorithm.
     * Only supports matching against String objects.
     *
     * @param fieldName the name of the field to match
     * @param value     the value to match against
     * @return the Criteria object itself, for method chaining
     */
    public Criteria like(String fieldName, Object value) {
        addMatcher(MatchTypeEnum.LIKE, value, fieldName);
        return this;
    }

    /**
     * Adds a Matcher to the Criteria object that matches the given field name and value using a less-than comparison.
     * Only supports matching against Number and Date objects.
     *
     * @param fieldName the name of the field to match
     * @param value     the value to match against
     * @return the Criteria object itself, for method chaining
     */
    public Criteria less(String fieldName, Object value) {
        addMatcher(MatchTypeEnum.LESS, value, fieldName);
        return this;
    }

    /**
     * Adds a Matcher to the Criteria object that matches the given field name and value using a less-than-or-equal-to comparison.
     * Only supports matching against Number and Date objects.
     *
     * @param fieldName the name of the field to match
     * @param value     the value to match against
     * @return the Criteria object itself, for method chaining
     */
    public Criteria lessEquals(String fieldName, Object value) {
        addMatcher(MatchTypeEnum.LESS_EQUALS, value, fieldName);
        return this;
    }

    /**
     * Adds a Matcher to the Criteria object that matches the given field name and value using a greater-than comparison.
     * Only supports matching against Number and Date objects.
     *
     * @param fieldName the name of the field to match
     * @param value     the value to match against
     * @return the Criteria object itself, for method chaining
     */
    public Criteria greater(String fieldName, Object value) {
        addMatcher(MatchTypeEnum.GREATER, value, fieldName);
        return this;
    }

    /**
     * Adds a Matcher to the Criteria object that matches the given field name and value using a greater-than-or-equal-to comparison.
     * Only supports matching against Number and Date objects.
     *
     * @param fieldName the name of the field to match
     * @param value     the value to match against
     * @return the Criteria object itself, for method chaining
     */
    public Criteria greaterEquals(String fieldName, Object value) {
        addMatcher(MatchTypeEnum.GREATER_EQUALS, value, fieldName);
        return this;
    }

    /**
     * Adds a Matcher to the Criteria object that matches fields that are null.
     * Supports matching against any object type.
     *
     * @param fieldName the name of the field to match
     * @param value     the value to match against (ignored)
     * @return the Criteria object itself, for method chaining
     */
    public Criteria isNUll(String fieldName, Object value) {
        addMatcher(MatchTypeEnum.NULL, value, fieldName);
        return this;
    }

    /**
     * Adds a Matcher to the Criteria object that matches fields that are not null.
     * Supports matching against any object type.
     *
     * @param fieldName the name of the field to match
     * @param value     the value to match against (ignored)
     * @return the Criteria object itself, for method chaining
     */
    public Criteria notNull(String fieldName, Object value) {
        addMatcher(MatchTypeEnum.NOT_NULL, value, fieldName);
        return this;
    }

    /**
     * Adds a new Matcher object to the Criteria object with the given match type, value, and field name.
     * Throws a RuntimeException if the given value is not supported by the given match type.
     *
     * @param matchType the type of matching to perform
     * @param value     the value to match against
     * @param fieldName the name of the field to match
     */
    private void addMatcher(MatchTypeEnum matchType, Object value, String fieldName) {
        boolean support = matchType.getSupport().test(value);
        if (!support) {
            throw new RuntimeException(matchType + " not supported type:" + value.getClass().getName());
        }
        matchers.add(new Matcher(fieldName, value, matchType));
    }

    /**
     * Determines whether the given object matches all the criteria specified by the Criteria object.
     * <p>
     * For each Matcher in the Criteria object, this method retrieves the field with the matching field name from the given object.
     * If the field value is null and the Matcher's match type is not MatchTypeEnum.NULL, this method returns false.
     * Otherwise, this method calls the Matcher's match method with the retrieved field value and the Matcher's match value.
     * If any Matcher returns false, this method returns false. Otherwise, it returns true.
     *
     * @param value the object to match against
     * @return true if the object matches all the criteria, false otherwise
     */
    public boolean isMatch(Object value) {
        // Get the class of the given object
        Class<?> clazz = value.getClass();

        // Check if all the Matchers in the Criteria object match the given object
        return matchers.stream()
                .allMatch(matcher -> {
                    // Get the field name, match value, and match type of the Matcher
                    String fieldName = matcher.getFieldName();
                    Object matchValue = matcher.getMatchValue();
                    MatchTypeEnum matchType = matcher.getMatchType();

                    // Get the field with the matching field name from the given object
                    Field field;
                    try {
                        field = clazz.getDeclaredField(fieldName);
                    } catch (NoSuchFieldException e) {
                        throw new RuntimeException(e.getMessage(), e);
                    }
                    field.setAccessible(true);

                    // Get the value of the field from the given object
                    Object fieldValue = null;
                    try {
                        fieldValue = field.get(value);
                    } catch (IllegalAccessException ignored) {
                    }

                    // If the field value is null and the Matcher's match type is not MatchTypeEnum.NULL, return false
                    if (fieldValue == null && matchType != MatchTypeEnum.NULL) {
                        return false;
                    }

                    // Otherwise, call the Matcher's match method with the retrieved field value and the Matcher's match value
                    return matchType.getMatch().match(fieldValue, matchValue);
                });
    }

}