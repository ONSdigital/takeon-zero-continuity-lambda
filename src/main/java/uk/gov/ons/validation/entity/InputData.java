package uk.gov.ons.validation.entity;

public class InputData {

    public String value = "";
    public String comparisonValue = "";
    public String threshold = "";
    public Object metaData = "{}";

    public InputData() {
    }

    public InputData(String value, String comparisonValue, String threshold) {
        this.value = value;
        this.comparisonValue = comparisonValue;
        this.threshold = threshold;
    }

}
