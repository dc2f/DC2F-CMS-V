# DC2F CMS

UI based on vaadin


## Setup Development Environment

This project was developed with Eclipse, therefore this is the prefered development platform.

Things you need on top of Eclipse Luna:
* m2e-wtp - Maven Integration for Eclipse WTP)
* lombok - [install it from their project page](http://projectlombok.org/download.html)
* vaadin - Vaadin Plugin for Eclipse

Please set your workspace encoding to UTF-8 if you are on windows.

With "Import" > "Existing Maven Projects" import the following projects from the checked out project
* dc2f
* dc2f-dstore
* dc2f-vaadin

### Tomcat 8 Target Runtime
If you don't have an existing tomcat 8 target runtime please add it:
1. In Eclipse open Window > Preferences
2. Select Server > Runtime Environments
3. Click add
    * If you don't see tomcat as one of your options [follow this guide to add support for the tomcat version 8](http://www.joe0.com/2014/03/24/how-to-fix-apache-tomcat-v7-0-not-showing-in-eclipse-server-runtime-environments-kepler-instructions/)
4. Select tomcat 8
5. In the next dialog point it to the correct installation directory or install a new tomcat 8
6. Finish adding the target runtime by closing the dialogs with "OK"


### Tomcat 7 Server
* Open the server view with Window > Show View > Other ...
** Select Server > Server
* Create a new Tomcat 8 server

