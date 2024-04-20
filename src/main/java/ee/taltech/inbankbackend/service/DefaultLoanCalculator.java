package ee.taltech.inbankbackend.service;

import ee.taltech.inbankbackend.config.DecisionEngineConstants;
import ee.taltech.inbankbackend.dataobjects.Decision;
import ee.taltech.inbankbackend.exceptions.*;

import org.springframework.stereotype.Service;

@Service
public class DefaultLoanCalculator implements LoanCalculator {

    @Override
    public Decision calculateApprovedLoan(int creditModifier, Long loanAmount, int loanPeriod)
            throws NoValidLoanException {

        int outputLoanAmount;

        if (creditModifier == 0) {
            throw new NoValidLoanException("No valid loan found!");
        }

        while (highestValidLoanAmount(creditModifier, loanPeriod) < DecisionEngineConstants.MINIMUM_LOAN_AMOUNT) {
            loanPeriod++;
        }

        if (loanPeriod <= DecisionEngineConstants.MAXIMUM_LOAN_PERIOD) {
            outputLoanAmount = Math.min(DecisionEngineConstants.MAXIMUM_LOAN_AMOUNT, highestValidLoanAmount(creditModifier, loanPeriod));
        } else {
            throw new NoValidLoanException("No valid loan found!");
        }

        return new Decision(outputLoanAmount, loanPeriod, null);
    }
    /**
     * Calculates the largest valid loan for the current credit modifier and loan period.
     *
     * @return Largest valid loan amount
     */
    private int highestValidLoanAmount(int creditModifier, int loanPeriod) {
        return creditModifier * loanPeriod;
    }
}
