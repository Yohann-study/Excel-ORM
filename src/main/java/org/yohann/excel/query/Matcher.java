package org.yohann.excel.query;

/**
 * A class representing a matcher that can be used to match objects based on the value of a specific field.
 * A Matcher object contains the name of the field to be matched, the value to match, and the type of matching to perform.
 */
public class Matcher {

    /**
     * The name of the field to be matched.
     */
    private final String fieldName;

    /**
     * The value to match against.
     */
    private final Object matchValue;

    /**
     * The type of matching to perform.
     * Defaults to MatchTypeEnum. EQUALS if not specified.
     */
    private MatchTypeEnum matchType = MatchTypeEnum.EQUALS;

    /**
     * Constructs a new Matcher object with the given field name and match value.
     *
     * @param fieldName  the name of the field to be matched
     * @param matchValue the value to match against
     */
    protected Matcher(String fieldName, Object matchValue) {
        this.fieldName = fieldName;
        this.matchValue = matchValue;
    }

    /**
     * Constructs a new Matcher object with the given field name, match value, and match type.
     *
     * @param fieldName  the name of the field to be matched
     * @param matchValue the value to match against
     * @param matchType  the type of matching to perform
     */
    protected Matcher(String fieldName, Object matchValue, MatchTypeEnum matchType) {
        this.fieldName = fieldName;
        this.matchValue = matchValue;
        this.matchType = matchType;
    }

    /**
     * Returns the name of the field to be matched.
     *
     * @return the name of the field to be matched
     */
    protected String getFieldName() {
        return fieldName;
    }

    /**
     * Returns the value to match against.
     *
     * @return the value to match against
     */
    protected Object getMatchValue() {
        return matchValue;
    }

    /**
     * Returns the type of matching to perform.
     *
     * @return the type of matching to perform
     */
    protected MatchTypeEnum getMatchType() {
        return matchType;
    }
}
