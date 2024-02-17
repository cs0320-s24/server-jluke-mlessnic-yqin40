> **GETTING STARTED:** You must start from some combination of the CSV Sprint code that you and your
> partner ended up with. Please move your code directly into this repository so that
> the `pom.xml`, `/src` folder, etc, are all at this base directory.

> **IMPORTANT NOTE**: In order to run the server, run `mvn package` in your terminal then `./run` (
> using Git Bash for Windows users). This will be the same as the first Sprint. Take notice when
> transferring this run sprint to your Sprint 2 implementation that the path of your Server class
> matches the path specified in the run script. Currently, it is set to execute Server
> at `edu/brown/cs/student/main/server/Server`. Running through terminal will save a lot of computer
> resources (IntelliJ is pretty intensive!) in future sprints.

# Overview

This project is the implementation of a local server that is able to take in user-specified inputs
to find data.
The main class Server instantiates the localhost server at port 3233. Each of the endpoints
are handled through the use of handler classes that can take in a request and output the response
back to the user.
The separation of handler classes allows for the easy addition of other endpoints and
functionalities, and also ensures that
various functions do not interfere with each other.

This project draws from two data sources.

The first is locally stored csv files.
The user can load a csv file from a filepath, view that csv file as a json object, search for within
that csv files that contain a certain value.
Our implementation also allows for and/or/not queries, including nested queries.

The second datasource collects data from the ACS API. The user can specify the state and county, as
well as other variables, and receive the corresponding broadband data.
Additionally, in order to reduce API calls, we have implemented caching using google's Guava
library. In terms of high level design,
the Server sends its request to a ProxyACS datasource. Then, the ProxyACS datasource is able to
determine whether the api response is already stored in the cache,
which takes requests as the key and location data taken from the ACS api as the values. If it is
not,
The APIProxy will ask the APICaller to make a call to the ACS api and then save that result in the
cache. If a request is already found in the cache,
we do not need to make the API call. We wrapped the guava cache object inside our own ProxyCache
object. Both the ProxyCache and the APICaller are implementations of the LocationDataFinder
Interface,
which ensures that they both are able to output the data in the same way, because the data that the
ProxyAPI sends to the BroadbandHandler is agnostic to whether that data came from the cache or the
api server.

# LoadCSV and View CSV

_**Design Choices**_

This program is designed to load and view CSV-formatted census data through API endpoints. It
primarily consists of two handler classes: LoadCSVHandler and ViewCSVHandler.

LoadCSVHandler is responsible for loading a CSV file from a given file path and storing the parsed
data in a static variable on the server for further use.

ViewCSVHandler reads this data from the server's static variable and returns it to the client in
JSON format for viewing at an API endpoint.
Relationships Between Classes/Interfaces

Both LoadCSVHandler and ViewCSVHandler implement Spark's Route interface, allowing them to handle
HTTP requests.

The CSVParser class is used to parse the CSV file, converting CSV rows into Census objects.

The Census class encapsulates fields related to census data, such as city, household income, etc.

# Broadband

This component is designed to gather data from the ACS api and display it to a user endpoint. It
supports the ability to gather an arbitrary number of data points at once through the optional “get”
parameter. The user must always provide a state and county, and the county must include the word
county or city, depending on what appears in the ACS api. Additionally, the broadband endpoint will
always return the broadband data regardless of what parameters are given in the “get” parameter.
Internally, the broadband handler just passes requests to and from the broadband endpoint. Requests
are sent from the endpoint to the ACS proxy class. Doing so provides us with caching, as a cache
object is able to live within this class and cache previously queries responses. If a request is
found within the cache, then the ACS proxy will simply return the associated value. If no key
matches that request, however, the proxy will then call another class, API caller, which deals
exclusively with real get requests to the ACS api. API caller takes in a parsed request, needing
state, county, and any optional parameters, and follows a 4-step process. First, it finds the
matching state and county code associated with the real state and county. Next, it finds the
broadband data for that location. After that, it separately finds the data associated with the other
parameters. Finally, it combines the broadband and optional data into a new location object and
returns that up to the proxy, which then hands it back to the handler and finally displays it on the
endpoint.

_**Data Structures & High-Level Explanations**_

A List Census is used to store the parsed census data.

A Map<String, Object> maps the fields of Census objects to key-value pairs for easy JSON
serialization.

_**Runtime/Space Optimizations**_

Stream processing is used when handling CSV files and generating JSON responses to optimize memory
usage and response times.

_**Errors/Bugs**_

     * @throws FileNotFoundException if File Path is incorrect
     * @throws Exception if Wrong File provided by end user.
     * @throws NoFilePathResponse if file path is empty.
     * @throws BadJSONResponse if Query parameter 'filepath' is missing or incorrect.
     * @throws NOCSVDataResponse if no CSV file did not load before.

_**Checkstyle Error Explanations**_

The program should adhere to standard Java coding conventions, with no intentional Checkstyle errors
left.
Tests

_**Testing Suites**_

LoadCSVHandlerTest: Tests the CSV file loading functionality, ensuring data is correctly parsed and
stored.

ViewCSVHandlerTest: Tests the JSON serialization and response functionality, ensuring data is
correctly returned.
Each test case is designed to verify that a specific part of the program works as expected, covering
various normal and edge-case scenarios.

# SearchCSV

_**Design Choices**_

The SearchCSVHandler utilizes the Searcher class from the CSV Library.
The SearchCSVHandler itself parses through the parameters specified by the user url. It only
accepts "find" and "query"
as possible fields. Find does a simple search of the csvfile for a value and query allows for the
and/or/not queries.
A successful search creates the success response, regardless of whether the item was found.
Other scenarios, including incorrect queries, no file found, and json, are all handled through
specific error codes.

_**Data Structures & High-Level Explanations**_
This library supports nested queries through a recursive chain of QueryBuilders, such that a Query (
built by the root QueryBuilder) is able to call upon a QueryBuilder to create new Queries.
There are two types of Queries: the and/or/not query classes are only able to take in other queries
as parameters. The column (which allows the specification of a column by name or index) and basic
queries are the only queries that are able to be evaluated.

_**Runtime/Space Optimizations**_

_**Errors/Bugs**_
When parsing a query, the parser may struggle if a value contains a comma or a space and therefore
make an incorrect or unbalanced query.

_**Testing Suites**_
There are two testing suites.

The QuerySearchTesting Suite just tests the search functionality.
The SearchCSVHandleTest suite tests the search functionality from the server.

# Broadband API

# Errors/Bugs

# Tests

# How to



