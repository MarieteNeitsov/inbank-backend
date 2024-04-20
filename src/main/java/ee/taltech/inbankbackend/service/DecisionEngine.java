package ee.taltech.inbankbackend.service;

import ee.taltech.inbankbackend.dataobjects.Decision;
import ee.taltech.inbankbackend.exceptions.*;
import ee.taltech.inbankbackend.validators.DefaultInputValidator;
import ee.taltech.inbankbackend.validators.InputValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * A service class that provides a method for calculating an approved loan amount and period for a customer.
 * The loan amount is calculated based on the customer's credit modifier,
 * which is determined by the last four digits of their ID code.
 */
@Service
public class DecisionEngine {

    private final LoanCalculator loanCalculator;
    private final CreditModifierCalculator creditModifierCalculator;
    private final InputValidator inputValidator;


    @Autowired
    public DecisionEngine(LoanCalculator loanCalculator, CreditModifierCalculator creditModifier, InputValidator inputValidator){
        this.loanCalculator = loanCalculator;
        this.creditModifierCalculator = creditModifier;
        this.inputValidator = inputValidator;
    }

    /**
     * Calculates the maximum loan amount and period for the customer based on their ID code,
     * the requested loan amount and the loan period.
     * The loan period must be between 12 and 60 months (inclusive).
     * The loan amount must be between 2000 and 10000€ months (inclusive).
     *
     * @param personalCode ID code of the customer that made the request.
     * @param loanAmount Requested loan amount
     * @param loanPeriod Requested loan period
     * @return A Decision object containing the approved loan amount and period, and an error message (if any)
     * @throws InvalidPersonalCodeException If the provided personal ID code is invalid
     * @throws InvalidLoanAmountException If the requested loan amount is invalid
     * @throws InvalidLoanPeriodException If the requested loan period is invalid
     * @throws NoValidLoanException If there is no valid loan found for the given ID code, loan amount and loan period
     */
    public Decision calculateApprovedLoan(String personalCode, Long loanAmount, int loanPeriod, String countryCode)
            throws InvalidPersonalCodeException, InvalidLoanAmountException, InvalidLoanPeriodException,
            NoValidLoanException, InvalidAgeException{
        try {
            inputValidator.verifyInputs(personalCode, loanAmount, loanPeriod,countryCode);
        } catch (Exception e) {
            return new Decision(null, null, e.getMessage());
        }

        int creditModifier = creditModifierCalculator.getCreditModifier(personalCode);

        return loanCalculator.calculateApprovedLoan(creditModifier, loanAmount, loanPeriod);
    }


}
