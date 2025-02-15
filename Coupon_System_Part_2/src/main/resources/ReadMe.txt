Welcome to the Coupon System Project - Part 2

What’s going on in here? 🤔

Click run and initialized the
database and filled it with some delightfully
random test data, it's magic.

Every time you
run the program, you’ll encounter a fresh new
set of results. Think of it as a mini surprise
party for your data! The system is designed to generate X
number of companies or customers for each run,
complete with data to demonstrate the system’s functionality and tests.

Setup is as Easy as Pie 🥧

No need to wrestle with the database address. Just run the program,
and it will automatically create the database schema and populate it with data on your local database.

Heads up idan!

If you’re rolling with a custom username and password,
you’ll need to update them in the application.properties file (spring.datasource).
Here’s the default configuration for your reference:

spring.datasource.url=jdbc:mysql://localhost:3306/couponSystem2?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=shlomi123

Exceptions? Let’s Keep it Colorful 🌈

Errors: Custom exceptions are printed in a bold red hue,
courtesy of our ConsoleColorsUtil class, to ensure you don’t miss them ->

Success Messages ->
For the good stuff, we’ve gone with a calming green,
making sure those moments of triumph stand out (because you deserve it).
And that’s it! Dive in, explore, and have fun playing with the system.

Who knew a coupon project could be this entertaining? 😎

--

Important : delete and update do not return void, why?
it is better once you delete or update to understand what you deleted or updated and get
the object you got updated or deleted - for example once we delete a customer
we get a jason message like this one (same for update):

{
    "code": 2047,
    "message": "Customer got deleted",
    "data": {
        "id": 1,
        "firstName": "Monika",
        "lastName": "Stiedemann",
        "email": "Monika.Stiedemann@yahoo.com",
        "password": "d4hkj7s5ry7a"
    }
}

If you tried to twice to insert
same company you will get this:

{
    "code": 1012,
    "message": "Company name already exists"
}
Voices using in this project isn't real.

'Introduction' to project and 'goodbye' message after testing -
using AI voices by:
https://play.ht/ (Introduction) &
https://elevenlabs.io/ (goodBye)
