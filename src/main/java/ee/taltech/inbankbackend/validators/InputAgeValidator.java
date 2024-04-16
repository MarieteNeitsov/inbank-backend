package ee.taltech.inbankbackend.validators;

import com.github.vladislavgoltjajev.personalcode.exception.PersonalCodeException;
import ee.taltech.inbankbackend.config.DecisionEngineConstants;
import ee.taltech.inbankbackend.exceptions.InvalidAgeException;

import java.time.LocalDate;
import java.time.Period;

public class InputAgeValidator {

    public Boolean isAgeSuitable(String personalCode, String countryCode) throws InvalidAgeException {
        Period age = getAge(personalCode);
        int ageInYears = age.getYears();
        if (ageInYears <18){
            return false;
        }

        return switch (countryCode) {
            case "EE" -> (82 - DecisionEngineConstants.maxLoanPeriodInYears) > ageInYears;
            case "LV"-> (74 - DecisionEngineConstants.maxLoanPeriodInYears) > ageInYears;
            case "LT" -> (72 - DecisionEngineConstants.maxLoanPeriodInYears) > ageInYears;
            default -> false;
        };
    }
    private static Period getAge(String personalCode) throws InvalidAgeException {
        int year = getYear(personalCode);
        int month = getMonth(personalCode);
        int day = getDay(personalCode);
        LocalDate birthDate = LocalDate.of(year, month, day);
        return Period.between(birthDate, LocalDate.now());
    }

    private static int getDay(String personalCode) {
        return Integer.parseInt(personalCode.substring(5,7));
    }

    private static int getMonth(String personalCode) {
        return Integer.parseInt(personalCode.substring(3,5));
    }

    private static int getYear(String personalCode) throws InvalidAgeException {
        String yearEndNumbers = personalCode.substring(1,3);
        String centuryAndGender = personalCode.substring(0,1);
        String yearStartNumbers;

        switch (centuryAndGender) {
            case "1", "2" -> yearStartNumbers = "18";
            case "3", "4" -> yearStartNumbers = "19";
            case "5", "6" -> yearStartNumbers = "20";
            default -> throw new InvalidAgeException("Birthdate is in the future");
        }

        return Integer.parseInt(yearStartNumbers + yearEndNumbers);
    }


}
