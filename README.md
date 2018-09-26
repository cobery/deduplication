# deduplication
Remove duplicates from a list of contacts

# To build
```
./gradlew clean build
```

# To run
```
./gradlew bootRun
```
then put that command in the background (control-z and bg), then run to upload a file
```
curl -F file=@./src/main/resources/csv/normal.csv  http://localhost:8181/api/v1/contacts -X POST -v
```

