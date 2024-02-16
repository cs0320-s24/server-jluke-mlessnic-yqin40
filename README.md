# Project Details
This project creates CSVParse, of which CSVParse.java is the main file. CSVParse supports search keywords, also accepts reader object, but also to create an object per line.
# Story 1: 
Refer Story1Test.java how to use Parse to search for keywords.
First we need to create a CSVParse instance:

`CSVParse csvp1 = new CSVParse("dol_ri_earnings_disparity.csv");
`

Once the CSVParse instance has been created, the CSV file is loaded. In CSVParse Class, I use String[ ][ ] to store the content in the CSV file. One of the String[ ] is a line of data in the CSV file.

Next you can call the searchData method to search for keywords. In searchData I used the Overload method for different input parameters. SearchData can be used to search either by entering a value or by entering a column identifier.

`csvp1.searchData("White") `  
`csvp1.searchData("Black", "Data Type")`

There are error messages that pop up to tell you what's wrong when dealing with a file that doesn't exist or a keyword that doesn't exist.
# Story 2:
Story 2 has been changed mainly for various reader objects as input parameters. CSVParse can receive different reader object. the specific execution code is as follows.

`FileReader fileReader = new FileReader("./data/census/dol_ri_earnings_disparity.csv");`  
`CSVParse csvp2 = new CSVParse(fileReader);`

First we have to create the FileReader object and then pass the FileReader object as a parameter to the CSVParse constructor. At this point our constructor can already load the CSV content as String[ ][ ].

`csvp2.searchData("White")`   
`csvp2.searchData("Black", "Data Type")`

Next we can search for keywords in the same way as Story 1.

# Story 3:
Story3 In order to convert each line into an instance, we first have to define our Class. for example, under objectCreator I wrote myself two Classes, Race and Star. their code result is almost the same. First we need to take the first line of the CSV file as a constructor parameter and then write the method to modify them.
Users can edit the class they want in a similar way.

Next we're going to use the GenericCreator to create the instance. The code is as follows.

    GenericCreator<Race> raceCreator =
        new GenericCreator<>(
            row -> new Race(),
            Arrays.asList(
                Race::setState,
                Race::setDataType,
                Race::setAverageWeeklyEarnings,
                Race::setNumberOfWorkers,
                Race::setEarningsDisparity,
                Race::setEmployedPercent));
The way to do this is to let the GenericCreator know how to modify the parameters of the Class.     
The next step is to construct our constructor just like Story2, except with the extra pass to raceCreator.

`FileReader fileReader2 = new FileReader("./data/census/dol_ri_earnings_disparity.csv"); `    
`CSVParse csv4 = new CSVParse(fileReader2, raceCreator);`

Finally we can print out the chained table composed of instances using print.

`csv4.printDataList();
`
# Story 5
Story 5 mainly tests the search under different logic, the detailed test code is as follows.

public void testQueries() throws FileNotFoundException {
FileReader fileReader3 = new FileReader("./data/stars/ten-star.csv");
CSVParse csv5 = new CSVParse(fileReader3);
String[][] csvData = csv5.getCsvData();

    BasicQuery starIdQuery = new BasicQuery(0, "1", false);

    BasicQuery properNameQuery = new BasicQuery(1, "", true);

    AndQuery combinedQuery = new AndQuery(starIdQuery, properNameQuery);

    boolean isMatch = combinedQuery.matches(csvData[1]);
    boolean isNotMatch = combinedQuery.matches(csvData[2]);

    System.out.println("Row 1 matches: " + isMatch);
    System.out.println("Row 2 matches: " + isNotMatch); //should be True but the rsult is false.

# Design Choices
The regular expression I use when matching each word. My regex is used to match comma-separated values, but ignores those commas surrounded by double quotes. The effect of this regular expression is to match commas only if the number of double quotes following the comma is an even number (including zero).
# Errors/Bugs
Story 5 yields erroneous results in some environments. This is evidenced by the fact that and logic searches will treat otherwise correct results as incorrect. This is not yet fixed.
# Tests
All test information is in the test folder.

# How to...
Regarding the folder architecture: the CSVParse constructor is in the CSVParse folder. The ObjectCreator folder is mainly about the Story3 constructor List. The files under query files are about Story5.
