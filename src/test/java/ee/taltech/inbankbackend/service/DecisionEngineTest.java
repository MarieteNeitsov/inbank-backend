package ee.taltech.inbankbackend.service;

import ee.taltech.inbankbackend.config.DecisionEngineConstants;
import ee.taltech.inbankbackend.dataobjects.Decision;
import ee.taltech.inbankbackend.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class DecisionEngineTest {

    @InjectMocks
    private DecisionEngine decisionEngine;

    private String debtorPersonalCode;
    private String segment1PersonalCode;
    private String segment2PersonalCode;
    private String segment3PersonalCode;
    private String countryCodeEE;
    private String countryCodeLV;
    private String countryCodeLT;
    private String personalCodeWithInvalidAge;
    private String personalCodeAgeinFuture;
    private String personalCodeUnderage;

    @BeforeEach
    void setUp() {
        debtorPersonalCode = "37605030299";
        segment1PersonalCode = "50307172740";
        segment2PersonalCode = "38411266610";
        segment3PersonalCode = "35006069515";
        countryCodeEE = "EE";
        countryCodeLV = "LV";
        countryCodeLT = "LT";
        personalCodeWithInvalidAge = "33103236020";
        personalCodeAgeinFuture = "67304273199";
        personalCodeUnderage = "61308269999";
    }

    @Test
    void testDebtorPersonalCode() {
        assertThrows(NoValidLoanException.class,
                () -> decisionEngine.calculateApprovedLoan(debtorPersonalCode, 4000L, 12,countryCodeEE));
    }

    @Test
    void testSegment1PersonalCode() throws InvalidLoanPeriodException, NoValidLoanException,
            InvalidPersonalCodeException, InvalidLoanAmountException, InvalidAgeException {
        Decision decision = decisionEngine.calculateApprovedLoan(segment1PersonalCode, 4000L, 12,countryCodeEE);
        assertEquals(2000, decision.getLoanAmount());
        assertEquals(20, decision.getLoanPeriod());
    }

    @Test
    void testSegment2PersonalCode() throws InvalidLoanPeriodException, NoValidLoanException,
            InvalidPersonalCodeException, InvalidLoanAmountException, InvalidAgeException {
        Decision decision = decisionEngine.calculateApprovedLoan(segment2PersonalCode, 4000L, 12,countryCodeEE);
        assertEquals(3600, decision.getLoanAmount());
        assertEquals(12, decision.getLoanPeriod());
    }

    @Test
    void testSegment3PersonalCode() throws InvalidLoanPeriodException, NoValidLoanException,
            InvalidPersonalCodeException, InvalidLoanAmountException, InvalidAgeException {
        Decision decision = decisionEngine.calculateApprovedLoan(segment3PersonalCode, 4000L, 12,countryCodeEE);
        assertEquals(10000, decision.getLoanAmount());
        assertEquals(12, decision.getLoanPeriod());
    }

    @Test
    void testInvalidPersonalCode() {
        String invalidPersonalCode = "12345678901";
        assertThrows(InvalidPersonalCodeException.class,
                () -> decisionEngine.calculateApprovedLoan(invalidPersonalCode, 4000L, 12,countryCodeEE));
    }

    @Test
    void testInvalidLoanAmount() {
        Long tooLowLoanAmount = DecisionEngineConstants.MINIMUM_LOAN_AMOUNT - 1L;
        Long tooHighLoanAmount = DecisionEngineConstants.MAXIMUM_LOAN_AMOUNT + 1L;

        assertThrows(InvalidLoanAmountException.class,
                () -> decisionEngine.calculateApprovedLoan(segment1PersonalCode, tooLowLoanAmount, 12,countryCodeEE));

        assertThrows(InvalidLoanAmountException.class,
                () -> decisionEngine.calculateApprovedLoan(segment1PersonalCode, tooHighLoanAmount, 12,countryCodeEE));
    }

    @Test
    void testInvalidLoanPeriod() {
        int tooShortLoanPeriod = DecisionEngineConstants.MINIMUM_LOAN_PERIOD - 1;
        int tooLongLoanPeriod = DecisionEngineConstants.MAXIMUM_LOAN_PERIOD + 1;

        assertThrows(InvalidLoanPeriodException.class,
                () -> decisionEngine.calculateApprovedLoan(segment1PersonalCode, 4000L, tooShortLoanPeriod,countryCodeEE));

        assertThrows(InvalidLoanPeriodException.class,
                () -> decisionEngine.calculateApprovedLoan(segment1PersonalCode, 4000L, tooLongLoanPeriod,countryCodeEE));
    }

    @Test
    void testFindSuitableLoanPeriod() throws InvalidLoanPeriodException, NoValidLoanException,
            InvalidPersonalCodeException, InvalidLoanAmountException, InvalidAgeException {
        Decision decision = decisionEngine.calculateApprovedLoan(segment2PersonalCode, 2000L, 12,countryCodeEE);
        assertEquals(3600, decision.getLoanAmount());
        assertEquals(12, decision.getLoanPeriod());
    }

    @Test
    void testNoValidLoanFound() {
        assertThrows(NoValidLoanException.class,
                () -> decisionEngine.calculateApprovedLoan(debtorPersonalCode, 10000L, 60,countryCodeEE));
    }

    @Test
    void testInvalidAgeEE() throws InvalidAgeException, InvalidLoanPeriodException, NoValidLoanException, InvalidPersonalCodeException, InvalidLoanAmountException {
        Decision decision = decisionEngine.calculateApprovedLoan(personalCodeWithInvalidAge, 4000L, 12,countryCodeEE);
        assertEquals("Age is not valid to apply for a loan!", decision.getErrorMessage());
    }
    @Test
    void testInvalidAgeLV() throws InvalidAgeException, InvalidLoanPeriodException, NoValidLoanException, InvalidPersonalCodeException, InvalidLoanAmountException {
        Decision decision = decisionEngine.calculateApprovedLoan(personalCodeWithInvalidAge, 4000L, 12,countryCodeLV);
        assertEquals("Age is not valid to apply for a loan!", decision.getErrorMessage());
    }
    @Test
    void testInvalidAgeLT() throws InvalidAgeException, InvalidLoanPeriodException, NoValidLoanException, InvalidPersonalCodeException, InvalidLoanAmountException {
        Decision decision = decisionEngine.calculateApprovedLoan(personalCodeWithInvalidAge, 4000L, 12,countryCodeLT);
        assertEquals("Age is not valid to apply for a loan!", decision.getErrorMessage());
    }
    @Test
    void testAgeInFuture() throws InvalidAgeException, InvalidLoanPeriodException, NoValidLoanException, InvalidPersonalCodeException, InvalidLoanAmountException {
        Decision decision = decisionEngine.calculateApprovedLoan(personalCodeAgeinFuture, 4000L, 12,countryCodeEE);
        assertEquals("Birthdate is in the future", decision.getErrorMessage());
    }
    @Test
    void testUnderage() throws InvalidAgeException, InvalidLoanPeriodException, NoValidLoanException, InvalidPersonalCodeException, InvalidLoanAmountException {
        Decision decision = decisionEngine.calculateApprovedLoan(personalCodeUnderage, 4000L, 12,countryCodeEE);
        assertEquals("Age is not valid to apply for a loan!", decision.getErrorMessage());
    }


}

