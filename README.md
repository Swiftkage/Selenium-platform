
# Selenium project
This guide is for the technical aspects of the Selenium project. For usability of the site, there is a separate "About" section under 192.168.0.219:8080/Selenium/about.

## Basic information

Selenium is accessed at 192.168.0.219:8080/Selenium

192.168.0.219 - username: SeleniumServer, password: Cde*****

The /selenium folder is essential for the server to proceed with the operations. Currently on the server (192.168.0.219), the folder could be found under C:/Users/SeleniumServer/Desktop/selenium.

Selenium is coded in Java 8, packaged using maven 3.3.9 and deployed using Tomcat 8.5.3. 
Spring framework 4.3.0 is used as the programming model. The database used is MySQL 6.3. 

More information about version information could be found in pom.xml.

## Changing the source code

Source code is located at https://192.168.0.11/yuxi/Selenium

The Selenium project could be build locally, and then the war file could be transferred and deployed to the SeleniumServer at 192.168.0.219.

If there is a need to modify the source code, follow the steps:

1) Download an IDE. It is recommended to use Intellj IDEA as the IDE platform for modifying the code. Open the project.

2) ***In application properties, ensure that the database connection settings, file locations are exactly the same on the local machine. If in doubt, dump the database from the server and copy the entire folder C:/Users/SeleniumServer/Desktop/selenium to the local machine. File locations in the database should match with the local machine. Ensure that the all the setting are correct, and the site is up and running before continuing.

3) Html error codes are handled by tomcat. For developmental purposes, remove the error-code under /conf/web.xml.

3) Modify.

## Deployment

Use the command "mvn package" to create the war file, which would be created at /selenium/target. Transfer the war to /webapps in Tomcat folder, and startup the service at /bin.


## Updating

There are a few important items that should be noted.

1) Update selenese-runner.jar file periodically at https://github.com/vmi/selenese-runner-java/releases. The current version used is 2.8.0.

2) Update the items found under pom.xml.

3) Selenium is dependent on the browser that would run the test cases. It should be noted that after browser updates, it could be possible that Selenium might encounter errors and some changes might be needed.