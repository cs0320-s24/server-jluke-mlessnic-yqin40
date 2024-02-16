> **GETTING STARTED:** You must start from some combination of the CSV Sprint code that you and your partner ended up with. Please move your code directly into this repository so that the `pom.xml`, `/src` folder, etc, are all at this base directory.

> **IMPORTANT NOTE**: In order to run the server, run `mvn package` in your terminal then `./run` (using Git Bash for Windows users). This will be the same as the first Sprint. Take notice when transferring this run sprint to your Sprint 2 implementation that the path of your Server class matches the path specified in the run script. Currently, it is set to execute Server at `edu/brown/cs/student/main/server/Server`. Running through terminal will save a lot of computer resources (IntelliJ is pretty intensive!) in future sprints.

# LoadCSV and View CSV
_**Design Choices**_

This program is designed to load and view CSV-formatted census data through API endpoints. It primarily consists of two handler classes: LoadCSVHandler and ViewCSVHandler.

LoadCSVHandler is responsible for loading a CSV file from a given file path and storing the parsed data in a static variable on the server for further use.

ViewCSVHandler reads this data from the server's static variable and returns it to the client in JSON format for viewing at an API endpoint.
Relationships Between Classes/Interfaces

Both LoadCSVHandler and ViewCSVHandler implement Spark's Route interface, allowing them to handle HTTP requests.

The CSVParser class is used to parse the CSV file, converting CSV rows into Census objects.

The Census class encapsulates fields related to census data, such as city, household income, etc.

_**Data Structures & High-Level Explanations**_

A List Census is used to store the parsed census data.

A Map<String, Object> maps the fields of Census objects to key-value pairs for easy JSON serialization.

_**Runtime/Space Optimizations**_

Stream processing is used when handling CSV files and generating JSON responses to optimize memory usage and response times.

_**Errors/Bugs**_

     * @throws FileNotFoundException if File Path is incorrect
     * @throws Exception if Wrong File provided by end user.
     * @throws NoFilePathResponse if file path is empty.
     * @throws BadJSONResponse if Query parameter 'filepath' is missing or incorrect.
     * @throws NOCSVDataResponse if no CSV file did not load before.
_**Checkstyle Error Explanations**_

The program should adhere to standard Java coding conventions, with no intentional Checkstyle errors left.
Tests

_**Testing Suites**_

LoadCSVHandlerTest: Tests the CSV file loading functionality, ensuring data is correctly parsed and stored.

ViewCSVHandlerTest: Tests the JSON serialization and response functionality, ensuring data is correctly returned.
Each test case is designed to verify that a specific part of the program works as expected, covering various normal and edge-case scenarios.


# Errors/Bugs

# Tests

# How to
