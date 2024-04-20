package ee.taltech.inbankbackend.validators;

import com.github.vladislavgoltjajev.personalcode.locale.estonia.EstonianPersonalCodeValidator;
import ee.taltech.inbankbackend.config.DecisionEngineConstants;
import ee.taltech.inbankbackend.exceptions.InvalidAgeException;
import ee.taltech.inbankbackend.exceptions.InvalidLoanAmountException;
import ee.taltech.inbankbackend.exceptions.InvalidLoanPeriodException;
import ee.taltech.inbankbackend.exceptions.InvalidPersonalCodeException;
import org.springframework.stereotype.Service;

@Service
public class DefaultInputValidator implements InputValidator{

    private final static EstonianPersonalCodeValidator personalCodeValidator = new EstonianPersonalCodeValidator();

    /**
     * Verify that all inputs are valid according to business rules.
     * If inputs are invalid, then throws corresponding exceptions.
     *
     * @param personalCode Provided personal ID code
     * @param loanAmount Requested loan amount
     * @param loanPeriod Requested loan period
     * @throws InvalidPersonalCodeException If the provided personal ID code is invalid
     * @throws InvalidLoanAmountException If the requested loan amount is invalid
     * @throws InvalidLoanPeriodException If the requested loan period is invalid
     */
    public void verifyInputs(String personalCode, Long loanAmount, int loanPeriod, String countryCode)
            throws InvalidPersonalCodeException, InvalidLoanAmountException, InvalidLoanPeriodException, InvalidAgeException {

        if (!personalCodeValidator.isValid(personalCode)) {
            throw new InvalidPersonalCodeException("Invalid personal ID code!");
        }
        if (!AgeValidator.isAgeValid(personalCode,countryCode)) {
            throw new InvalidAgeException("Age is not valid to apply for a loan!");
        }

        if (isAmountSuitable(loanAmount)) {
            throw new InvalidLoanAmountException("Invalid loan amount!");
        }
        if (isPeriodSuitable(loanPeriod)) {
            throw new InvalidLoanPeriodException("Invalid loan period!");
        }

    }
    private static boolean isAmountSuitable(Long loanAmount){
        return !(DecisionEngineConstants.MINIMUM_LOAN_AMOUNT <= loanAmount)
                || !(loanAmount <= DecisionEngineConstants.MAXIMUM_LOAN_AMOUNT);
    }
    private static boolean isPeriodSuitable(int loanPeriod){
        return !(DecisionEngineConstants.MINIMUM_LOAN_PERIOD <= loanPeriod)
                || !(loanPeriod <= DecisionEngineConstants.MAXIMUM_LOAN_PERIOD);
    }
}
