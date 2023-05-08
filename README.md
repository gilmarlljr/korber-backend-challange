# **Getting Started**

### **What this is**

This is a simple template to facilitate the development startup of our Backend Challenge.
Provided in this project is a stripped down Springboot application, with a Postgres database, orchestrated with Docker.

### **Requirements**

 - Maven
 - Java 17
 - Docker

### **Start-up**

Run the following commands in the command line:

    - mvn clean install
    - docker build -t challenge-1.0.0.jar .
    - docker-compose up

### **Instructions**

Please do not code on this repo, just fork-it or build a new one based on this

## **Challange conclusion**

### **Endpoints**
Here I will provide all the endpoints used, and a curl to use them.

#### DataLoad - DO IT TO POPULATE THE DATABASE
To list all yellows tou need to pass `POST /api/v1/dataload`
this endpoint will donwload a file and save the infromation on the database.

    it only accepts `.csv` for the `ZONES` and for `GREEN` and `YELLOW` it aceepts only `.parquet`

it needs a json body formed by a list of `"filesToDownload"` :
- `url`: the adress of the file, the file need to end with `.csv` or `.parquet`
- `dataType`: the type of the documents, can only be `YELLOW`,`GREEN`,`ZONE`
  
Request example:
```
curl --location 'http://localhost:8081/api/v1/dataload' \
--header 'Content-Type: application/json' \
--data '{
"filesToDownload":[{
"url": "https://d37ci6vzurychx.cloudfront.net/trip-data/yellow_tripdata_2018-01.parquet",
"dataType": "YELLOW"
},
{
"url": "https://d37ci6vzurychx.cloudfront.net/trip-data/green_tripdata_2018-01.parquet",
"dataType": "GREEN"
},
{
"url": "https://d37ci6vzurychx.cloudfront.net/misc/taxi+_zone_lookup.csv",
"dataType": "ZONE"
}]
}'
```

it Returns just an 202 Aceepted, because only trigger the process, and can take a while, since
it will handle large files.

*PS: I decided to hardcode a limit of interactions on the save process, to avoid long runs just for this test. 
if you want you can just remove the limit of the batched tha will be executed in the [DataLoadTask.java#L68](https://github.com/gilmarlljr/korber-backend-challange/blob/e2015517cb00251880edb2d7803e5210822d293b/src/main/java/com/challenge/challenge/services/dataload/DataLoadTask.java#L68)*


#### List all Yellows
To list all yellows tou need to pass `GET /api/v1/list-yellow`
it has the following params:
- `page`: it informs the page that you want;
- `pageSize`: it informs the number of the items by page
- `sortBy`: it sort by a column - can be `asc` or `desc`
- `sortDir`: it choose a sort direction

Request example:
```
curl --location 'http://localhost:8081/api/v1/list-yellow?page=0&pageSize=10&sortBy=id&sortDir=asc'
```
Response example:
```json
{
    "yellowTripPage": {
        "content": [
            {
                "id": 1,
                "pickupDatetime": "2018-01-02T08:08:54",
                "dropoffDatetime": "2018-01-02T08:15:43",
                "pulocationID": 249,
                "dolocationID": 107
            }
        ],
        "pageable": {
            "sort": {
                "empty": false,
                "sorted": true,
                "unsorted": false
            },
            "offset": 0,
            "pageNumber": 0,
            "pageSize": 1,
            "paged": true,
            "unpaged": false
        },
        "totalPages": 300000,
        "totalElements": 300000,
        "last": false,
        "size": 1,
        "number": 0,
        "sort": {
            "empty": false,
            "sorted": true,
            "unsorted": false
        },
        "numberOfElements": 1,
        "first": true,
        "empty": false
    }
}

```

#### List Zone trips by date
To zone trips by date to pass `GET /api/v1/zone-trips`
it has the following params:
- `zone`: id of a zone 
- `date`: date in format `yyyy-MM-dd` (ISO_DATE)


Request example:
```
curl --location 'http://localhost:8081/api/v1/zone-trips?zone=1&date=2018-01-12'
```
Response example:
```json
{
    "zoneName": "\"Newark Airport\"",
    "date": "2018-01-12",
    "pickupCount": 0,
    "dropoffCount": 14
}
```

#### List Top Five Zones - NOT WORKING ....
To list the top five zones you need to pass `/api/v1/top-zones`
it has the following params:
- `order`: order of the top, can be only `PICKUPS` or `DROPOFFS`;

Request example:

```
curl --location 'http://localhost:8081/api/v1/top-zones?order=PICKUPS'
```
### **Knowing problems**

#### Maven Wrapper not working
if you try to use `./mvnw clean install` it not works so i recomend to it by the IDE or using the installed maven.
I not sure why it not works, and I tried to fix but didn't work up.

#### It takes a long time to process large parquet files like `yellow_tripdata_2018-01.parquet`

In the [ParquetFileParser.java](src%2Fmain%2Fjava%2Fcom%2Fchallenge%2Fchallenge%2Fservices%2Fdataload%2Ffileparser%2Fparquet%2FParquetFileParser.java) I though to implement a concurrent read of the file, reading the file by batches and saving it by parts. But I didnt find a proper and easyer why to do that with the apache lib, and it took me a long time searching that I could to do other partes of the challange.

And the save could be improved to be more efficient, because it can took a very long time with the full `yellow_tripdata_2018-01.parquet` thats why I decided to limit to save only 10 batchs of 30000 rows.

#### Improve the top five zones
I did not finish this step, because the query was taking ao long to execute, and it sure can be improved.
I think if I put some indexes on the database it will result.

#### better error handling
The code leaks of error handling. and it for sure needs to be improved.

And for sure the code will break if it was not following the "good scenario"

#### Tests
The code have almost any test. I only tested with UNIT test the Parses. but was just a green scenario test.

#### More splited commits
I started to doing that, but I haved an error with git and lost the history.
I miss committed the `yellow_tripdata_2018-01.parquet` and github only accept 100MB files. so I reverted all the commits and made only one.

#### Map CSV for YELLOW and GREEN
I think it could be easy to map with the [CsvFileParser.java](src%2Fmain%2Fjava%2Fcom%2Fchallenge%2Fchallenge%2Fservices%2Fdataload%2Ffileparser%2Fcsv%2FCsvFileParser.java), but I didn't have the time.
