# csv-aggregator

## Quick Start
Prerequisites:
* JDK Version 11  
* Download Maven - [Start up guide](https://maven.apache.org/guides/getting-started/)

Build: `mvn clean install`  
Execute: `java -cp target/csv-aggregator-1.0.jar org.bwettig.App`
Test: `mvn clean test`
## Background
The purpose of this application is to conflate multiple csv files into a single csv file, according to a defined schema.  Some validation and translation is required where necessary to capture full transactions, otherwise missing or invalid data is reported to the terminal.

## Design
At the root there is a `resources` directory that contains four sub-directories `compiled`, `error`, `processed`, and `staging`.  They are leveraged as follows:
* Documents to be conflated are loaded into `staging`
* Upon running, sheets that are successfully processed (even in the case of excluded rows due to missing or invalid data) will be moved to `processed` at the end of the job
* If there is an error case that results in the document being excluded (missing header column, empty document), then these documents will end up in `error`
* The completed, compiled document can be found in `compiled` accordingly.  It is time stamped to help differentiate runs.

The class structure utilizes the Strategy pattern, as shown below.
![UML](/images/uml.PNG)

The intention is to create a project that is readily extensible to other kinds of jobs.  By extending the CSVStrategy, different schemas and validation/transformation rules can be considered.  Furthermore, additional Strategies can be implemented to handle other kinds of documents.

## Future Plans
This project could be useful as a tool used locally, but could also be decomposed to be fully automated. One concept would be to have the client files arrive in an S3 bucket,
  then run a lambda function at a set cadence (daily, weekly, etc) to bring the data into a Data Warehouse or Data Lake.  Schemas would be registered to an external DB ideally as well.

There are libraries that would abstract away some details, and assist in development.  For the purpose of this demonstration, such libraries have been avoided (recommended: OpenCSV)

One particular point of opportunity is to further develop the test cases to gain greater code coverage.  The `process` method of the Strategy class invokes many methods with are individually tested themselves, but the `process` method itself does not lend well to testing.  Additionally, further implementation of mocks/stubs/spies would further isolate tests and produce more meaningful records.