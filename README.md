# log-state

You can use this module for logging the state of Objects (in a file or in console), in Json format. For logging to a file, provide the file fullpath with the system property *'StateLogger.outputFile'*.

1. Using the StateLogger.log(x,y,z...), where x,y,z... object instances you can log the state of objects while debugging/running a Java Application 
1. Using the StateLogger.getStateAsJson(x,y,z...),  where x,y,z... object instances you can log the state of objects (Logging Breakpoints in Intellij- https://www.baeldung.com/intellij-debugging-tricks)
1. The only requirement is to put the following dependency in your code:

        <dependency>
            <groupId>org.log.state</groupId>
            <artifactId>log-state</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>



