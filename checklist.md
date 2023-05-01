# Spring Tests

#### Remember about naming of test methods.
There are a lot of ways to name your test methods. The main point is that 
they should have informative names and be consistent along with other developers in your team. 
For this task use such convention: `<methodUnderTest>_<state>_<expectedBehavior>`. 
For example, if we are testing the method `register` with a `null` user's age 
the test method name should be `register_nullAge_notOk`. `notOk` is because 
the test expects the register method to throw an exception.

#### You can use assertThrows() to test exceptions (JUnit 5).
More info - https://howtodoinjava.com/junit5/expected-exception-example/

#### Don't leave comments in your code. The code must be clear without them.

#### Don't ignore checkstyle rules. 
Remember about empty lines, variable naming, etc.
