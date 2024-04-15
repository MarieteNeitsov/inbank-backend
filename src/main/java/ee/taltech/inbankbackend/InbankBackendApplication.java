package ee.taltech.inbankbackend;

import com.github.vladislavgoltjajev.personalcode.locale.estonia.EstonianPersonalCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;
import java.time.Period;

@SpringBootApplication
public class InbankBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(InbankBackendApplication.class, args);
        EstonianPersonalCodeGenerator generator = new EstonianPersonalCodeGenerator();
        String personalCode = generator.generateRandomPersonalCode();
        System.out.println(personalCode);
        LocalDate dateOfBirth = LocalDate.of(2024, 4, 4);
        Period p =Period.between(dateOfBirth, LocalDate.now());
        System.out.println(p.getYears());
    }

}
