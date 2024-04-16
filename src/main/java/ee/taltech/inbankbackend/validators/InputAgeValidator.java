package ee.taltech.inbankbackend.validators;
import ee.taltech.inbankbackend.config.DecisionEngineConstants;
import ee.taltech.inbankbackend.exceptions.InvalidAgeException;

import java.time.LocalDate;
import java.time.Period;

public class InputAgeValidator {

    public Boolean isAgeSuitable(String personalCode, String countryCode) throws InvalidAgeException {

        int age = getAge(personalCode);
        LocalDate birthDate = getBirthDate(personalCode);
        if (age <18 ){
            return false;
        }
        if(birthDate.isAfter(LocalDate.now())){
            throw new InvalidAgeException("Birthdate is in the future");
        }

        return switch (countryCode) {
            case "EE" -> (DecisionEngineConstants.LIFE_EXPECTANCY_EST - DecisionEngineConstants.MAX_LOAN_PERIOD_IN_YEARS) > age;
            case "LV"-> (DecisionEngineConstants.LIFE_EXPECTANCY_LV - DecisionEngineConstants.MAX_LOAN_PERIOD_IN_YEARS) > age;
            case "LT" -> (DecisionEngineConstants.LIFE_EXPECTANCY_LT - DecisionEngineConstants.MAX_LOAN_PERIOD_IN_YEARS) > age;
            default -> false;
        };
    }
    private static int getAge(String personalCode) throws InvalidAgeException {
        return Period.between(getBirthDate(personalCode), LocalDate.now()).getYears();
    }
    private static LocalDate getBirthDate(String personalCode) throws InvalidAgeException {
        int year = getYear(personalCode);
        int month = getMonth(personalCode);
        int day = getDay(personalCode);
        return LocalDate.of(year, month, day);
    }

    private static int getDay(String personalCode) {
        return Integer.parseInt(personalCode.substring(5,7));
    }

    private static int getMonth(String personalCode) {
        return Integer.parseInt(personalCode.substring(3,5));
    }

    private static int getYear(String personalCode){
        String yearEndNumbers = personalCode.substring(1,3);
        String centuryAndGender = personalCode.substring(0,1);
        String yearStartNumbers;

        switch (centuryAndGender) {
            case "1", "2" -> yearStartNumbers = "18";
            case "3", "4" -> yearStartNumbers = "19";
            case "5", "6" -> yearStartNumbers = "20";
            default -> yearStartNumbers = "21";
        }

        return Integer.parseInt(yearStartNumbers + yearEndNumbers);
    }


}
