package ZeroContinuity;

import junit.framework.TestCase;
import uk.gov.ons.validation.entity.InputData;
import uk.gov.ons.validation.service.RuleZeroContinuity;

public class RuleZeroContinuityTest extends TestCase {

    public void testGivenValuesProvideValueFormula() {
        InputData sourceData = new InputData("6000", "0", "2000");
        String expectedFormula = "{ [ abs(6000 > 0) AND 0 = 0 ] OR [ abs(6000 = 0) AND 0 > 0 ] } AND abs(6000 - 0 ) > 2000";
        RuleZeroContinuity validation = new RuleZeroContinuity(sourceData);
        assertEquals(expectedFormula, validation.getValueFormula());
    }

    public void testGivenBlankVariablesProvideValueFormula() {
        InputData sourceData = new InputData();
        String expectedFormula = "{ [ abs(0 > 0) AND 0 = 0 ] OR [ abs(0 = 0) AND 0 > 0 ] } AND abs(0 - 0 ) > 0";
        RuleZeroContinuity validation = new RuleZeroContinuity(sourceData);
        assertEquals(expectedFormula, validation.getValueFormula());
    }

    public void testGivenBlankValueDoNotTriggerValidation() {
        InputData sourceData = new InputData("", "", "");
        RuleZeroContinuity validation = new RuleZeroContinuity(sourceData);
        assertFalse(validation.run());
    }

    public void testGivenNullValuesDoNotTriggerValidation() {
        InputData sourceData = new InputData(null, null, null);
        RuleZeroContinuity validation = new RuleZeroContinuity(sourceData);
        assertFalse(validation.run());
    }

    public void testGivenZeroToLargerValueTriggerValidation() {
        InputData sourceData = new InputData("0", "500.0002", "500");
        RuleZeroContinuity validation = new RuleZeroContinuity(sourceData);
        assertTrue(validation.run());
    }

    public void testGivenZeroToLargerValueExactThresholdDoesNotTriggerValidation() {
        InputData sourceData = new InputData("0", "500.0001", "500.0001");
        RuleZeroContinuity validation = new RuleZeroContinuity(sourceData);
        assertFalse(validation.run());
    }

    public void testGivenZeroToVeryLargeValueTriggerValidation() {
        InputData sourceData = new InputData("0", "-12345678901234567890.0001", "100000000000000000");
        RuleZeroContinuity validation = new RuleZeroContinuity(sourceData);
        assertTrue(validation.run());
    }

    public void testGivenBothZeroDoesNotTriggerValidation() {
        InputData sourceData = new InputData("0", "0", "0");
        RuleZeroContinuity validation = new RuleZeroContinuity(sourceData);
        assertFalse(validation.run());
    }

    public void testGivenBothNonZeroDoesNotTriggerValidation() {
        InputData sourceData = new InputData("1000", "-100", "500");
        RuleZeroContinuity validation = new RuleZeroContinuity(sourceData);
        assertFalse(validation.run());
    }
}
