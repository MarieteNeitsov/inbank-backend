package ee.taltech.inbankbackend.validators;

import ee.taltech.inbankbackend.exceptions.InvalidAgeException;
import ee.taltech.inbankbackend.exceptions.InvalidLoanAmountException;
import ee.taltech.inbankbackend.exceptions.InvalidLoanPeriodException;
import ee.taltech.inbankbackend.exceptions.InvalidPersonalCodeException;

public interface InputValidator {
    void verifyInputs(String personalCode, Long loanAmount, int loanPeriod, String countryCode)
            throws InvalidPersonalCodeException, InvalidLoanAmountException, InvalidLoanPeriodException, InvalidAgeException;
}
