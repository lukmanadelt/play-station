# Play Station

This is the example project for making an User Registration REST API in Play Framework

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

- Make sure you have the Java 8 JDK (also known as 1.8)
- [Scala 2.12.8+](https://scala-lang.org/download/)
- [SBT 1.2.8+](https://www.scala-sbt.org/download.html)
- [PostgreSQL 11.1+](https://formulae.brew.sh/formula/postgresql)

### Installing

1. You need to create database called ```users```.
2. Clone this repository.
```
git clone https://github.com/lukmanadelt/play-station.git
```
3. Go to the cloned directory (e.g. `cd play-station`).
4. Open file ```application.conf``` inside ```conf``` directory.
```
slick.dbs.default.db.url="jdbc:postgresql://localhost:5432/users"
slick.dbs.default.db.user="postgres"
slick.dbs.default.db.password=""
```
You can see the block code and you can first configure your database port, user, and password or you can use the same configuration as mine.

5. If you have finished configuring the database, then return to the root project (e.g. `cd play-station`).
6. Once you have sbt installed, the following at the command prompt will start up Play in development mode :
```bash
sbt run
```
Play will start up on the HTTP port at <http://localhost:9000/> and table ```users``` will be automatically created by [Play Evolutions](https://www.playframework.com/documentation/2.7.x/Evolutions).

## Running the tests

Makes a basic GET request to the specifed URI to get users who have registered.
```
curl http://localhost:9000/v1/users
```

Likewise, you can also send a POST directly to create a user.
```
curl --data "user_full_name=Lukman&user_email=lukman@email.com&user_password=12345678&user_address=Jakarta&user_phone=0817652348910" http://localhost:9000/v1/users
```

## Built With

* [Scala](https://scala-lang.org/) - The Programming language
* [Play Framework](https://www.playframework.com/) - The High Velocity Web Framework For Java and Scala
* [Slick](http://slick.lightbend.com/) - Functional Relational Mapping for Scala
* [Play Evolutions](https://www.playframework.com/documentation/2.7.x/Evolutions) - Database Migration

## Authors

* **Lukman Adel Taufiqurahman** - *Initial work*

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details
