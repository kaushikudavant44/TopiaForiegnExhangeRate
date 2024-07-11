# TopiaForiegnExhangeRate
Prepared for interview
I've included the table creation script inside java/main/resources/db/migration.
This application has 2 profiles one is for the local h2 database and the other one is for MySQL.
dev environment is running on h2 db and prod environment is running on MySQL.
Need to change profile in application.properties 
Dev = spring.profiles.active=dev
Prod = spring.profiles.active=prod
