package ee.taltech.inbankbackend.service;

import ee.taltech.inbankbackend.dataobjects.Decision;
import ee.taltech.inbankbackend.exceptions.*;

public interface LoanCalculator {
    Decision calculateApprovedLoan(int creditModifier, Long loanAmount, int loanPeriod)
            throws NoValidLoanException;
}
