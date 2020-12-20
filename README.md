# Game of three

## Run from sources (development)
1. Install `maven`, `zookeeper` and `kafka` 
1. Run `zookeeper` and `kafka` with default parameters
1. Go to project root directory
1. Run `player1` app
   ```
   mvn -pl player -am spring-boot:run -Dspring-boot.run.profiles=player1
   ```
1. Run `player2` app
   ```
   mvn -pl player -am spring-boot:run -Dspring-boot.run.profiles=player2
   ```   
1. Run `control-panel` app
   ```
   mvn -pl control-panel -am spring-boot:run
   ```
1. Open `control-panel` web page
   ```
   http://localhost:8080
   ```

## Run from cluster (deploy)
1. Install and run `docker` and `docker-compose`
1. Go to project root directory
1. Create Spring Boot `fat-jars`  
   ```
   mvn clean package
   ```
1. Pack Docker images
   ```
   docker build -t ikapinos/game-of-three-control-panel ./control-panel
   docker build -t ikapinos/game-of-three-player ./player
   ```
1. Start cluster
	```
	docker-compose up -d
	```
1. Open `control-panel` web page
   ```
   http://localhost:8080/
   ```

## Fun fact
Check out [google search result](https://www.google.com/search?q=game+of+three+github). Do not worry, no cheat.
