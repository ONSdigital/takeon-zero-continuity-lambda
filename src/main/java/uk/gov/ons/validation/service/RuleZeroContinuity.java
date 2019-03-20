package uk.gov.ons.validation.service;

import uk.gov.ons.validation.entity.InputData;

import java.math.BigDecimal;

/**
 * Validation rule for Zero continuity.
 * <p>
 * Compare 2 values. Trigger if one of the values is ZERO, the other is greater than 0 and the
 * difference between them is greater than the given threshold
 * <p>
 * Missing/blank/invalid values are treated as ZERO
 *
 * formula: abs(formulaVariable - comparisonVariable) > threshold AND
 * [ abs(formulaVariable > 0) AND comparisonVariable = 0 ]
 * OR
 * [ abs(formulaVariable = 0) AND comparisonVariable > 0 ]
 */
public class RuleZeroContinuity {

    private BigDecimal statisticalValue;
    private BigDecimal comparisonValue;
    private BigDecimal threshold;

    /**
     * Assumes a pre-populated copy of the standard Validation data class InputData is provided
     * However, the rule will handle null/missing data without error
     */
    public RuleZeroContinuity(InputData sourceInputData) {
        statisticalValue = safeDefineDecimal(sourceInputData.value);
        comparisonValue = safeDefineDecimal(sourceInputData.comparisonValue);
        threshold = safeDefineDecimal(sourceInputData.threshold);
    }

    // Ensure we end up with 0 if no (or invalid) values are passed through to this validation rule
    private BigDecimal safeDefineDecimal(String value) {
        BigDecimal safeDecimal;
        try {
            safeDecimal = new BigDecimal(value);
        } catch (NumberFormatException | NullPointerException e) {
            safeDecimal = new BigDecimal(0);
        }
        return safeDecimal;
    }

    /**
     * Give the value based formula. i.e. the formula used at runtime and so uses the given
     * statistical values and threshold value
     *
     * @return String
     */
    public String getValueFormula() {
        return getFormula(statisticalValue.toString(), comparisonValue.toString(), threshold.toString());
    }

    // Shared formula function so we can use the same formula definition for the statistical
    // variables and the values (i.e. at definition and at runtime)
    private String getFormula(String variable, String comparisonVariable, String threshold) {
        return "{ [ abs(" + variable + " > 0) AND " + comparisonVariable + " = 0 ] OR" +
                " [ abs(" + variable + " = 0) AND " + comparisonVariable + " > 0 ] } AND" +
                " abs(" + variable + " - " + comparisonVariable + " ) > " + threshold;
    }

    /**
     * For the given values and threshold return true if rule is triggered, false otherwise
     *
     * @return boolean
     */
    public boolean run() {
        BigDecimal difference = statisticalValue.subtract(comparisonValue).abs();
        return (oneValueOnlyIsZero(statisticalValue, comparisonValue)) && difference.compareTo(threshold) > 0;
    }

    // Extracted out to provide simpler utility function that also simplifies the validation formula
    private static boolean oneValueOnlyIsZero(BigDecimal value1, BigDecimal value2) {
        return ((value1.compareTo(BigDecimal.ZERO) != 0) && (value2.compareTo(BigDecimal.ZERO) == 0)) ||
                ((value2.compareTo(BigDecimal.ZERO) != 0) && (value1.compareTo(BigDecimal.ZERO) == 0));
    }

}
