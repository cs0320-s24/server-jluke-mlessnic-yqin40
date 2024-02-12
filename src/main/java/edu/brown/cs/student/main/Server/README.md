This package contains the files specific to the Server project. 

Functionality must include:

User Story 1: user can make api requests using various endpoints
* loadcsv
  * NOTE: a csv will be provided on the backend (rhode island town and income dataset)
* viewcsv
* searchcsv
  * must also accept nested boolean queries
  
User Story 2: developer calling our server can make api requests specific broadband information from ACS api
* broadband
  * retrieves percentage of households with broadband access for a target location from target state and county
  * must include date and time data was retreived
  * must include state and county names received by our server
  * must also include a set of variables from official list
    * these can be arbitrary values
    * error handling very important

User Story 3: backend developer will use our server in their own server
* control over caching of ACS request-responses
  * especially when entries are removed
  * cache can also be omitted
  * can perform other configurations specified by developer
  * NOTE: ACS data source should be represented by its own class
  * NOTE: use proxy class as intermediary between our server's handler and ACS data source
    * assists in managing cached data and eviction policies
    * 