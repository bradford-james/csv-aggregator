# csv-aggregator

## Quick Start
JDK Version 11  
Download Maven

Build: `./run.sh build`  
Execute: `./run.sh execute`
## Background
The purpose of this application is to conflate multiple csv files into a single csv file, according to a defined schema

## Design


## Future Plans
* This project could be modified to be executed from your PATH in a target directory containing source csv files
* This project could be adapted to run in an automated job.  One concept would be to have the client files arrive in an S3 bucket,
  then run a lambda function at a set cadence (daily, weekly, etc)
* There are libraries that would abstract away some details.  For the purpose of this demonstration, such libraries have been avoided (recommended: OpenCSV)
* This application has been designed to be extensible