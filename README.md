# Game of three

## Overall
Game is running using five microservices:
* `zookeeper` - infrastructure, kafka's dependency  (third party)
* `kafka` - infrastructure (third party)
* `control-panel` - UI interface which allows:
   * Start game on `player1` or `player2`
   * View game moves using WebSockets
* `player1` - "player" instance #1
* `player1` - "player" instance #2

Two `kafka` topics used to route messages for player 1 and Player 2 

## Run all in docker (deploy)
1. Install and run `docker` and `docker-compose`
1. Go to project root directory
1. Create Spring Boot `fat-jars`  
   ```
   mvn clean package
   ```
   ![ui.png](/docs/mvn.png)
1. Pack Docker images
   ```
   docker build -t ikapinos/game-of-three-control-panel ./control-panel
   docker build -t ikapinos/game-of-three-player ./player
   ```
1. Start cluster
   ```
   docker-compose up -d
   ```
   ![cmd.png](/docs/cmd.png)
   
   ![cmd.png](/docs/docker.png)
   
1. Open `control-panel` web page
   ```
   http://localhost:8080/
   ```
   ![ui.png](/docs/ui.png)

## Run from source code (development)
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

## Fun fact
Check out [google search result](https://www.google.com/search?q=game+of+three+github). Do not worry, no cheat.
