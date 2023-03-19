# Getting started
## Local development
1. Modify application-dev.properties file to set your local database connection.
2. If you don't have MySQL instance, you can use docker container
<pre>
docker run \
   --name=demo_mysql \
   --publish 3306:3306 \
   -e MYSQL_ROOT_PASSWORD=demopassword \
   -e MYSQL_ROOT_HOST="%" \
   -d \
   mysql/mysql-server:8.0.25
</pre>

## Build
1. Run `mvn clean package` to build the project.
2. run `java -jar target/phoneRentManager.jar` to start the application.

## Docker compose
1. Run `mvn -P prod clean package` to build the project.
1. Run `docker-compose up --force-recreate -d` to start the application.
