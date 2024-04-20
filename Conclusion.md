## Conclusion for TICKET 101

### What the intern did well

#### Backend:
- Created a class for constants, so they can be used anywhere in the project.
- Used specific exceptions for different types of validation errors in the loan application process.These specific exceptions make error handling more precise and informative, improving the robustness and maintainability of the code.
- Provided unit tests and integration tests.
- Used DTOs for request and response to improve data handling.
- Implemented a better algorithm for finding the maximum valid loan amount by multiplying loan period with credit modifier. This works because using the maximum amount in this formula credit score = (credit modifier / loan amount )* loan period results always in 1. This is much more efficient than checking the credit score for each possible loan amount to find the maximum amount with the provided formula.

#### Frontend:
- Separated the national id field from the loan form. This allows for better modularity of the form component.
- Implemented a clear and user-friendly UI that provides immediate feedback to the user. The form validation of national id length and sliders that have minimum and maximum values that correspond to the requirements ensures that the user inputs pass the first check before submitting the form.

### Places for improvement

#### Backend:
- Decision engine service violates the Single Responsibility Principle because it is responsible for validating inputs, calculating the maximum loan amount and credit modifier. In addition, the verifyInputs method is not very easily readable because of the long if statements regarding loan period and amount.
- The calculateApprovedLoan method directly calls the verifyInputs and getCreditModifier methods. If the way inputs are verified or the credit modifier is calculated needs to change, the source code of calculateApprovedLoan would need to be modified. This is a violation of the Open-Closed Principle
- Data objects and DTOs are not in their separate packages which makes the project structure more difficult to understand and maintain.
- The while loop in calculateApprovedLoan method increases the loan period until the minimum loan amount is met but this is inefficient since the loan period can go over the maximum loan period.
- Instead of handling exceptions within the controller method, we could use @ControllerAdvice annotations to handle exceptions globally. This would make controller methods cleaner and more focused on their main responsibilities.
- In this implementation instance variable response can be shared between requests in the endpoint, which can lead to unexpected behavior if multiple requests are being handled at the same time. This can be solved by creating a new response object for each request and removing the @Component annotation from the DecisionResponse class.
- It would be better for the exception classes to extend Exception instead of Throwable. This is because Throwable is a superclass of all errors and exceptions, so it can be confusing to use it for exceptions.

#### Frontend:
- The label for minimum loan period is 6 months but should be 12 like the smallest possible value for the input slider
- The requirements mentioned that a valid maximum loan amount should be shown even if the requested amount is smaller than the maximum. The implementation right now does not show the maximum valid loan amount when the user requests for a smaller amount.
- The UI code in the build method of LoanForm is quite long and could be broken up into smaller more manageable and reusable widgets.
- Loan period slider uses 40 divisions but it would be more convenient for the user if the slider had 48 divisions so the loan period could be selectable in increments of 1 month.
- The _submitForm method could be refactored to improve readability and maintainability. Currently, this method is responsible for validating the form, making the API request, and updating the state. These responsibilities could be separated into different methods.

## Most important shortcoming

The most important shortcoming is that the DecisionEngine class violates the Single Responsibility Principle. Therefore, I created an InputValidator to separate the input validation and loan amount calculation. I also extracted methods for better readability and responsibility separation for some if statements int the verifyInputs method. The requirements state that in real life the solution should compose a comprehensive user profile to calculate credit modifiers, so I created a new service CreditModifierService that could be updated in the future. I created a LoanCalculator interface and a DefaultLoanCalculator so that there can be different loan calculations in the future without having to change the DecisionEngine class. To make the project more maintainable I created packages for DTOs and data objects used for business logic. To follow the Open-Closed Principle I made interfaces for calaculating credit modifier and input validation.