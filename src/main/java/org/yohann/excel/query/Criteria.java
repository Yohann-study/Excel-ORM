package org.yohann.excel.query;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Criteria object used to filter Excel data based on specific conditions.
 */
public class Criteria {

    // Number of rows to skip
    private Integer skip = 0;

    // Maximum number of rows to read
    private Integer limit = -1;

    // List of Matchers used to filter the data
    private final List<Matcher> matchers = new ArrayList<>();

    /**
     * Constructs a new Criteria object with default values.
     */
    public Criteria() {

    }

    /**
     * Gets the number of rows to skip.
     *
     * @return the number of rows to skip
     */
    public Integer getSkip() {
        return skip;
    }

    /**
     * Sets the number of rows to skip.
     *
     * @param skip the number of rows to skip
     * @return this Criteria object
     */
    public Criteria setSkip(Integer skip) {
        this.skip = skip;
        return this;
    }

    /**
     * Gets the maximum number of rows to read.
     *
     * @return the maximum number of rows to read
     */
    public Integer getLimit() {
        return limit;
    }

    /**
     * Sets the maximum number of rows to read.
     *
     * @param limit the maximum number of rows to read
     * @return this Criteria object
     */
    public Criteria setLimit(Integer limit) {
        this.limit = limit;
        return this;
    }

    /**
     * Constructs a new Criteria object with a single Matcher.
     *
     * @param fieldName the name of the field to match
     * @param value     the value to match
     */
    public Criteria(String fieldName, Object value) {
        matchers.add(new Matcher(fieldName, value));
    }

    /**
     * Adds an EQUALS Matcher to the Criteria object.
     *
     * @param fieldName the name of the field to match
     * @param value     the value to match
     * @return this Criteria object
     */
    public Criteria equals(String fieldName, Object value) {
        addMatcher(MatchTypeEnum.EQUALS, value, fieldName);
        return this;
    }

    /**
     * Adds a NOT_EQUALS Matcher to the Criteria object.
     *
     * @param fieldName the name of the field to match
     * @param value     the value to match
     * @return this Criteria object
     */
    public Criteria notEquals(String fieldName, Object value) {
        addMatcher(MatchTypeEnum.NOT_EQUALS, value, fieldName);
        return this;
    }

    /**
     * Adds a LIKE Matcher to the Criteria object.
     *
     * @param fieldName the name of the field to match
     * @param value     the value to match
     * @return this Criteria object
     */
    public Criteria like(String fieldName, Object value) {
        addMatcher(MatchTypeEnum.LIKE, value, fieldName);
        return this;
    }

    /**
     * Adds a LESS Matcher to the Criteria object.
     *
     * @param fieldName the name of the field to match
     * @param value     the value to match
     * @return this Criteria object
     */
    public Criteria less(String fieldName, Object value) {
        addMatcher(MatchTypeEnum.LESS, value, fieldName);
        return this;
    }

    /**
     * Adds a LESS_EQUALS Matcher to the Criteria object.
     *
     * @param fieldName the name of the field to match
     * @param value     the value to match
     * @return this Criteria object
     */
    public Criteria lessEquals(String fieldName, Object value) {
        addMatcher(MatchTypeEnum.LESS_EQUALS, value, fieldName);
        return this;
    }

    /**
     * Adds a GREATER Matcher to the Criteria object.
     *
     * @param fieldName the name of the field to match
     * @param value     the value to match
     * @return this Criteria object
     */
    public Criteria greater(String fieldName, Object value) {
        addMatcher(MatchTypeEnum.GREATER, value, fieldName);
        return this;
    }

    /**
     * Adds a GREATER_EQUALS Matcher to the Criteria object.
     *
     * @param fieldName the name of the field to match
     * @param value     the value to match
     * @return this Criteria object
     */
    public Criteria greaterEquals(String fieldName, Object value) {
        addMatcher(MatchTypeEnum.GREATER_EQUALS, value, fieldName);
        return this;
    }

    /**
     * Adds a NULL Matcher to the Criteria object.
     *
     * @param fieldName the name of the field to match
     * @param value     the value to match
     * @return this Criteria object
     */
    public Criteria isNUll(String fieldName, Object value) {
        addMatcher(MatchTypeEnum.NULL, value, fieldName);
        return this;
    }

    /**
     * Adds a NOT_NULL Matcher to the Criteria object.
     *
     * @param fieldName the name of the field to match
     * @param value     the value to match
     * @return this Criteria object
     */
    public Criteria notNull(String fieldName, Object value) {
        addMatcher(MatchTypeEnum.NOT_NULL, value, fieldName);
        return this;
    }

    /**
     * Adds a new Matcher to the Criteria object.
     *
     * @param matchType the type of Matcher to add
     * @param value     the value to match
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
     * Checks if the given object matches all the Matchers in this Criteria object.
     *
     * @param value the object to match
     * @return true if the object matches all the Matchers in this Criteria object, false otherwise
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