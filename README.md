# immobilienscout24
immobilienscout24 Technical Challenge

### Technologies used
* Java 8
* Library
  * Jsoup (HTML Parser)
  * Guava (Util library)
  * Apache commons-validator (Util library)
* Spring-boot (Application)
* Thymeleaf (Template engine)
* Bootstrap (CSS)

### Run the Application

You can package and run the backend application by executing the following command:

```
#!bash
mvn package spring-boot:run
```

This will start an instance running on the default port `8080`.

You can also build a single executable JAR file that contains all the necessary dependencies, classes, and resources, and run that.
The JAR file will be create by executing the command `mvn package`, and the JAR file will be located under target directory.

Then you can run the JAR file:
```
java -jar target/HtmlProcessor-1.0-SNAPSHOT.jar
```

After successfully running the application, you can now open your browser with the following url:
 ```
 http://localhost:8080/htmlinfo
 ```

 Now you can start analyzing the url you want. For example: `https://www.github.com`.
