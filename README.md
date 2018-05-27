Interpreter Pattern
===================

This project is just an exercise to implement a simple Interpreter Pattern.

# Syntax
**Functions**

- TO_DATE(String dateRepresentation, String fromFormat, String toFormat)
- CONCATENATE(String...parameters)

**Navigator**
- property.nested_property.nested_property
(Arrays not implemented yet.)

# Examples
 Given a Json:

    {
        "name": "Ragnar",
        "age": 52,
        "address": {
            "street": "Vivaldi"
            "since": "2017/05/19"
        }
    }

Calling the Interpreter:

    Interpreter.of("address.street").eval(json);
= Vivaldi

    Interpreter.of("TO_DATE(address.since,'yyyy/MM/dd','dd.MM.yyyy')").eval(json);
First evals the address.since to 2017/05/19, then applies the to date:
= 19.05.2017

    Interpreter.of("CONCATENATE('Living in this address since: ', TO_DATE(address.since,'yyyy/MM/dd','dd.MM.yyyy'))").eval(json);
= Living in this address since: 19.05.2017


> Attention:
This code deserves major refactorings.
One of the main points is that to add a new functionality, several places must to be touched.One of the main points is that to add a new functionality, several places must to be touched.