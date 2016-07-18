# Design Document

**Author**:  6300Summer16Team49

| Version | Description     |
| --------|:---------------:|
| V4      | 4th version of design document with every interface |


## 1 Design Considerations

### 1.1 Assumptions ##
 - Background: 
 - Dependencies: Based on the project description, we assume all the hardware and related libraries are provided for now.
 - Use: Installed on a fixed device in the bowling alley.
 - Operational Environment: Android mobile device 
 - Significant project issues: Not yet.

### 1.2 Constraints

 - The app can only be used on android system.
 - Must be a fixed device accessible by all customers of the bowling alley.

### 1.3 System Environment

 Hardware: Android mobile device
 
 - VideoCamera: Scan Customer Card when the customer requests a bowling lane.
 - CustomerCard Printer: It prints out customer card for the manager when a new customer wants to play or a customer changes their name or email.
 - Credit Card Scanner: Scan customers' credit cards to finish payment process.
 
 Software: Android operating system. Platform 15 and above

## 2 Architectural Design

### 2.1 Component Diagram

The system consists of a single android application and this is therefore the only components in the system. All data is stored locally so there is no need for external components. This makes the system simple and keeps cost low and reduces the requirements for the customer (No need for them to have a mobile phone on hand).

### 2.2 Deployment Diagram

There will be an android terminal in the bowling alley. The system only needs to be installed to this single terminal which is really just an Android tabled enclosed in fixed hardware.


## 3 Low-Level Design

### 3.1 Class Diagram
![Alt text](design-team.PNG?raw=true "Title")

### 3.2 Other Diagrams

For now, we don't have any other diagrams, we may add later.

## 4 User Interface Design
We added several main UI designs here, but we do not go to the implementation in detail.
- The main user interface
![Alt text](FrontSceen.png?raw-true "Title")


- The interface for customer
![Alt text](CustomerPage.png?raw-true "Title")

- The Request Lane interface 
![Alt text](RequestLane.png?raw-true "Title")

- The Request Lane Done interface 
![Alt text](RequestLaneDone.png?raw-true "Title")

- The CheckOut interface 
![Alt text](CheckOut.png?raw-true "Title")

- The Pay Bill interface 
![Alt text](PayBill.png?raw-true "Title")

- The Record score interface 
![Alt text](RecordScore.png?raw-true "Title")

- The Score history interface 
![Alt text](ScoreHistory.png?raw-true "Title")


- The interface for the manager
![Alt text](ManagerPage.png?raw-true "Title")

- The Add Customer interface
![Alt text](AddCustomer.png?raw-true "Title")

- The Print Card interface
![Alt text](PrintCard.png?raw-true "Title")

- The Edit Customer interface
![Alt text](EditCustomer.png?raw-true "Title")

- The Show Customer interface
![Alt text](ShowCustomer.png?raw-true "Title")



