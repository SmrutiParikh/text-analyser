# text-analyser

## Need following
- Git
- JDK 15
- Gradle 6.8.1 

## How to run?
1. Clone project
	````
	git clone https://github.com/SmrutiParikh/text-analyser.git
	````
2. Build project
	````
	gradle build
	````
3. [Optional] Run unit tests
	````
	gradle test --tests "com.example.*"   
	````
4. Run project
	````
	sh run.sh   
	````

## APIs

CRUDL
1. Create
	Curl
	````
	curl --location --request POST 'http://localhost:8888/dictionaries/create' \
	--header 'Content-Type: application/json' \
	--data-raw '{
			"is_case_sensitive": false,
			"entries": [
					"Smruti",
					"Parikh"
			]
	}'
	````

	Response
	````
	{
			"id": "2ADrCsvk4amix95YrKqqEillKkP",
			"entries": [
					"Smruti",
					"Parikh"
			],
			"is_case_sensitive": false,
			"is_deleted": false
	}
	````

2. Update
	
	Curl
	````
	curl --location --request PUT 'http://localhost:8888/dictionaries/update' \
	--header 'Content-Type: application/json' \
	--data-raw '{
    "id": "2ADrCsvk4amix95YrKqqEillKkP",
    "entries": [
        "Smruti",
        "N",
        "Parikh"
    ],
    "is_case_sensitive": true
	}'
	````
	
	Response
	````
	{
    "id": "2ADrCsvk4amix95YrKqqEillKkP",
    "entries": [
        "Smruti",
        "N",
        "Parikh"
    ],
    "is_case_sensitive": true,
    "is_deleted": false
	}
	````
	
3. Read

	Curl
	````
	curl --location --request POST 'http://localhost:8888/dictionaries/read' \
	--header 'Content-Type: application/json' \
	--data-raw '{
			"id": "2ADrCsvk4amix95YrKqqEillKkP"
	}'
	````
	
	Response
	````
	[
    "Smruti",
		"N",
    "Parikh"
	]
	````

4. List
	Curl
	````
	curl --location --request GET 'http://localhost:8888/dictionaries/list'
	````
	
	Response
	````
	[
    "2ADrCsvk4amix95YrKqqEillKkP"
	]
	````

5. Delete
	Curl
	````
	curl --location --request DELETE 'http://localhost:8888/dictionaries/delete' \
	--header 'Content-Type: application/json' \
	--data-raw '{
			"id": "2ADrCsvk4amix95YrKqqEillKkP"
	}'
	````
	
	Response
	````
	{
    "id": "2ADrCsvk4amix95YrKqqEillKkP",
    "entries": [
        "Smruti",
        "N",
        "Parikh"
    ],
    "is_case_sensitive": true,
    "is_deleted": true
	}
	````

Query
1. Analyse Text

	Curl
	````
	curl --location --request POST 'http://localhost:8888/analyse/text' \
	--header 'Content-Type: application/json' \
	--data-raw '{
			"dictionary_id": "2ADrCsvk4amix95YrKqqEillKkP",
			"target": "My name is Smruti"
	}'
	````
	
	Response
	````
	[
    {
        "text": "Smruti",
        "start_index": 11,
        "end_index": 16
    }
	]
	````
