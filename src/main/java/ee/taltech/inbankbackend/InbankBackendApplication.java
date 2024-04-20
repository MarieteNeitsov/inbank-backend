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
        for (int i = 0; i < 10; i++) {
            String personalCode = generator.generateRandomPersonalCode(); System.out.println(personalCode);
        }



    }

}
