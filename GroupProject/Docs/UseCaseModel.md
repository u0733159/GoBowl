# Use Case Model

**Author**: 6300Summer16Team49

| Version | Description     |
| --------|:---------------:|
| V2      | Describe detail use cases for system | 


## 1 Use Case Diagram
![Alt text](useCaseModel.png?raw=true "Title")

## 2 Use Case Descriptions

#### Select role 
- Requirements: The manager or customer can select his role correspondingly. 
- Pre-conditions: The manager or customer knows his role.
- Post-conditions: The manager or customer role is selected.
- Scenarios:

1. The manager selects a manager role
2. The customer selects a customer role
3. People not sure his role exits

Manager part:
#### Add customer 
- Requirements: The manager can add customers to the system and a membership cards can be printed. 
- Pre-conditions: The customer should not already exist on the system.
- Post-conditions: Customer is added to the system and is given a card.
- Scenarios:

1. Customer provides fullname and email.
2. Manager adds the customer to the system
3. Manager prints a membership card for the customer

#### Print card 
- Requirements: The manager can print any customer card. 
- Pre-conditions: The customer should already exist on the system.
- Post-conditions: Customer card is printed.
- Scenarios:

1. Customer provides his email to the manager
2. Manager applies the email to find the customer's profile
3. Manager can print a new membership card for the customer

#### Edit customer 
- Requirements: The manager can edit the customers profile information. 
- Pre-conditions: The customer should already exist on the system.
- Post-conditions: Customer's information is update on the system.
- Scenarios:

1. The manager find the customer profile by scanning the card or entering       customer ID
2. Customer provides full name or email to be changed
3. Manager applies the changes to the customer's profile

#### Show customers 
- Requirements: The manager can check all the customers profile information. 
- Pre-conditions: The customer profile should already exist on the system.
- Post-conditions: All the Customer's profiles are shown on the system.
- Scenarios:

1. The manager press the show customers button to check all the customers profile information.



Customer part:
#### Request Lane
- Requirements: Allows a user to request a lane from the bowling management system 
- Pre-conditions: All customer must have been added to the system and own a customer cards
- Post-conditions: Customers are assigned a lane. 
- Scenarios:

1. Customer presses *request lane* on user interface
2. Customer is asked to scan their membership card  
3. Customer is asked for number of players
4. For each other player they are asked to scan their card. Each customer is validated against the system.
5. A lane number is assigned to the customer and displayed on the interface.

#### Checkout 
- Requirements: Allows a customer to complete their bowling event including calculating their bill, saving their scores and paying their bill by credit cards. 
- Pre-conditions: Customer must have been assigned a lane number by the system
- Post-conditions: Customer transactions are finalized and bowling lane released to the system
- Scenarios:

1. Customer presses *checkout* on user interface.
2. Customer is asked their lane number.  
3. A bill will be generated with start time, end time and amount due based on different time rates.
4. Each Customer is asked if they would like to save their score and input his score.
5. Customer is asked how they want to split the bill.
6. Bill is calculated using users VIP status.
7. For the number of splits chosen, the customer is asked to swipe their credit card.
8. Once the bill is paid, that line will be released.


#### Historic scores 
- Requirements: The manager can check his score history. 
- Pre-conditions: The customer score should already exist on the system.
- Post-conditions: The customer score history is shown on the system.
- Scenarios:

1. The customer press the Historic scores button. 
2. The customer scan his customer card.
3. The customer score history is shown on the system.