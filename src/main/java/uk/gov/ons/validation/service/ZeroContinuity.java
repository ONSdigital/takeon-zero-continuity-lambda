package uk.gov.ons.validation.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import uk.gov.ons.validation.entity.InputData;
import uk.gov.ons.validation.entity.OutputData;

public class ZeroContinuity {

    private static final Logger LOG = LogManager.getLogger(ZeroContinuity.class);

    public OutputData apply(String input) {
        LOG.info("received: {}", input);
        return parseAndRunValidationRule(input);
    }

    // Take any required source JSON, parse it into our data class, run the validation rule and then produce the output
    private OutputData parseAndRunValidationRule(String inputJson) {
        OutputData outputData;
        try {
            InputData inputData = new ObjectMapper().readValue(inputJson, InputData.class);
            outputData = runValidationRule(inputData);
            LOG.info("output: {}", outputData.toString());
        } catch (JsonProcessingException e) {
            LOG.error("json error: {}", e.getMessage());
            outputData = new OutputData(null, null, null, "Error parsing source JSON: " + inputJson);
        } catch (Exception e) {
            LOG.error("json error: {}", e.getMessage());
            outputData = new OutputData(null, null, null, "Miscellaneous error parsing JSON input parameters: " + inputJson);
        }
        return outputData;
    }

    // Take the given data and invoke the validation rule
    private OutputData runValidationRule(InputData inputData) {
        RuleZeroContinuity rule = new RuleZeroContinuity(inputData);
        return new OutputData(rule.getValueFormula(), rule.run(), inputData.metaData, null);
    }
}
