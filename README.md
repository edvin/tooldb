# Cutting Tool Database

This is a demo application showing one way to use JDBC from a TornadoFX Application.
Important: This is not supposed to show the recommended way to interact with a database from
any Kotlin application. Using plain JDBC today is probably not the best idea, but it should give
you an overview of the steps involved and how to hook into the lifecycle to initialize
a datasource and shut it down correctly before app exit.

We are using HikariCP and MySQL in this example but the concepts should be the same
for any DataSource or RDBMS, in fact even for most abstractions on top of JDBC. 

Everything interesting is in the DB.kt file.

Also note that this demo project is not advocating best practices about how to use and organize SQL in any way. 