# car dealership

## Prerequisites
- gradle build tool >=2.6 http://gradle.org/
- ElasticSearch running locally

## Clone, build and start the API
In a work directory do the steps below

```bash
git clone https://github.com/droptableusers/cardealership.git
cd cardealership/
gradle clean build shadowJar
```
The config file comes with sane defaults, but you may need to change it to reflect local configuration.
(e.g. if the clustername is not "elasticsearch", if you prefer a different node name, indexname other than "cardealership", etc.)
```
vim src/main/resources/config.yml
```
Finally, start the API
```bash
java -jar build/libs/cardealership-1.0-all.jar server src/main/resources/config.yml
```

### A quick test
```bash
curl --user adam:secret 'http://localhost:14321/cars/' 
``` 
should return 
```json
{"totalHits":0,"hits":[]}
```
```bash
curl 'http://localhost:14321/cars/'
```
should return `Credentials are required to access this resource.`

An index is created at this point, that can be checked via:
```
curl 'http://localhost:9200/cardealership/'
```

## Clone and copy the GUI
Go back to the work directory, clone the gui files and place them to a folder where the web server serves it.
```
cd ..
git clone https://github.com/droptableusers/cardealership_gui.git
sudo cp -r cardealership_gui /var/www/cardealership_gui
or
cp -r cardealership_gui/ ~/Sites/cardealership_gui
```

## Authentication and Authorization
The API uses HTTP basic access authentication. All the endpoints are protected. The username can be anything, it only checks for the password to be "secret". 
The authentication credentials are hardcoded into the GUI, so they don't need to be entered manually.

## Try the GUI
Navigate the browser to http://localhost/cardealership_gui/ 
An empty resultset is visible with two buttons at the top: Add New Car and Batch upload car json. 
Choose the later, on the next screen select a file with "Choose file" button. 
It automatically uploads the file and navigates back you to the main screen. 
You may need to refresh the page, as ES doesn't make the results instantly available.
Now there are many items on the screen, so the other required features are testable too.

> A note on batch update: 
> There is no feedback to the user about the status of the uploaded batch json file. This may be upgraded later with some kind of notification, based on the HTTP response from the API.

### SCRUD operations: 
- Search with the Search field in the top right. Notice that Brand and Model fields are boosted, e.g. if you type "au" there then "Audi"s will be above the cars with "Aut." gears.
- Create a car with the Add New Car button at the top
- Update a car by clicking on the View button in its row and then Edit button at the bottom of the view page
- Delete a car by clicking on the Delete button in its row
- Additionally, pagination is implemented too, try that with the bar at the bottom

## Try the API 
Everything works directly via API too.
Batch upload:
```
curl -i --user adam:secret -XPOST 'http://localhost:14321/cars/batch' -F file=@src/test/resources/fixtures/vehicles-bulk.json
```
Single car create:
```
curl -v --user adam:secret -XPOST 'http://localhost:14321/cars' -H 'Content-Type: application/json' -d @src/test/resources/fixtures/car.json
```
Single car view:
here you can reuse the Location header that was returned by the previous call:
```
curl -v --user adam:secret -XPOST 'http://localhost:14321/cars' -H 'Content-Type: application/json' -d @src/test/resources/fixtures/car.json
...
* upload completely sent off: 414 out of 414 bytes
< HTTP/1.1 201 Created
< Date: Tue, 22 Sep 2015 08:37:00 GMT
< Location: http://localhost:14321/cars/e1c266e9-8b03-451f-aca6-dc3b4c0c527e
< Content-Length: 0
...

curl -i --user adam:secret http://localhost:14321/cars/e1c266e9-8b03-451f-aca6-dc3b4c0c527e
```
Single car update:
```
curl -i --user adam:secret -XPUT http://localhost:14321/cars/e1c266e9-8b03-451f-aca6-dc3b4c0c527e -H 'Content-Type: application/json'  -d @src/test/resources/fixtures/car.json
```

