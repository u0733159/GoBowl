# Test Plan

**Author**: 6300Summer16Team49

| Version | Description     |
| --------|:---------------:|
| V3      | 3rd version of test plan with all test case details |


## 1 Testing Strategy

### 1.1 Overall strategy

The overall test strategy is based mainly on proposing and analyzing test cases. The unit tests were performed during the code was developed. The integration test on each class were also performed during implementation. Once the app was fully implemented and tested on unit and integrated, the auto and manual tests was performed to adequately cover the desired functions of the app.


### 1.2 Test Selection

The test was implemented in a combined manner:
* Black box testing - According to the assumptions and scenarios described in other documents, we used integration testing and system testing to inspect the behaviors of the application.
* White box testing - We have various tests written by developers who implemented the classes. They therefore know the behavior and implementation details of the classes and methods. We use unit testing and regression tests. 


### 1.3 Adequacy Criterion

* We used junit for unit tests to cover all the code
* We used Espresso to create some auto tests to test on "Add customer"
* We used the use case documentation to ensure that black box testing covers all use cases.

### 1.4 Bug Tracking

We used the github Issues tag to do bug tracking and to track feature requests.

### 1.5 Technology

* We used Junit for unit and regression tests
* We used Espresso tools for GUI testing.
* We used app-user interactions to test all desired app functions 


## 2 Test Cases

*This section is the core of this document and list the set of tests that we perform to assess the quality of the system. Normally, for system tests, this section consists of a table of test cases, one per row. For each test case, the table provided: its purpose, steps necessary to perform, the expected result and the actual result.

|Test name|Purpose| Steps| Expected Result | Pass/Fail | Additional info |
|---------|-------|------|-----------------|-----------|-----------------|
| TestAddCustomer| Tests that we can add a customer to the system correctly|Using dummy data, call addCustomer(), then retrieve the created customer instance and verifiy all the fields.|Customer object exists and new customers could be added to the list|   Pass    |                 |
| TestDuplicateCustomer| Test whether the customer is already in the file |add a duplicate customer will return an error message"customer already in the file|duplicate detected and save is denied|   Pass    |                 |
| TestUpperLowerCaseCustomer| Test the same customer name and email address with both upper case and lower case|type in customer names and emails with both upper case and lower case at different positions|upper case or lower case does not matter|   Pass    |                 |
| TestEditCustomer|Test that the manager can modify customer information|Find the customer who requests to modify information in the system and modify it|Customer information modified and new customer card printed|     Pass      |                 |
| TestPrintCard|Test that the manager can print customer cards|Find the customer by his email|Customer information founded and new customer card printed|     Pass      |                 |
| TestShowCustomers|Test that the manager can check all customer information|Press the "show customers" button|Show all customer information in order|     Pass      |                 |
| TestRequestLane|Test the request lane functionality. Makes sure the customers are validated and then assigned a lane| Run through the request lane process. Including scanning customer cards and entering number of players.|The customers assigned lane should be displayed|Pass||
| TestCheckout | Test that customer stops playing bowling and the bowling lane becomes available. |  Customer presses checkout button on the UI, and entered the bowling lane number |  A correct bill was generated successfully|Pass|assume play time less than one hour to be one hour|
| TestSaveScores | Test each player's score could be saved during the check out |  Customer select saving scores or not and then select each score account |each score saved accordingly|Pass||
| TestPaymentPortal | Test the bill could be divided and the credit cards could be processed regardless the card holder during the check out |  enter how many people want to divide the bill and then swipe each card one by one |got the bill divided correctly and process credits successfully|Pass||
| TestHistoricScore | Test the customer could check his score history |  scan the customer card |got the corresponding score history|Pass||
:
