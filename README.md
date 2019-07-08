# HelloChange


Small project on producing changes given certain set of bills with different bill types.

Use ```mvn package``` to build the project.

Then use ```java -cp target/Rocketmiles.jar  com.rocketmiles.hellochange.HelloChange ($20 bills) ($10 bills) ($5 bills) ($2 bills) ($1 bills)``` to initialize the start up amount of changes that the change machine has from the beginning and start up the application.

Type ```put ($20 bills) ($10 bills) ($5 bills) ($2 bills) ($1 bills)``` to add additional amount of the bill entered from the command line to the change machine.
 
Type ```take ($20 bills) ($10 bills) ($5 bills) ($2 bills) ($1 bills)``` to remove amount of the bill entered from the command line to reduce the bills from the change machine.

Type ```show``` to show the current state of the change machine which includes the total dollar amount in the change machine and the amount of each bills in the change machine.

Type ```change (dollar amount)``` to get changes of smaller bills of the given dollar amount.

Type ```quit``` to exit out of the HelloChange program.
