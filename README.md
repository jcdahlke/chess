# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)


My Phase 2 Architecture:
https://sequencediagram.org/index.html#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAHZxDAaTgALdvYoALIoAIyY9lAQAK7YMADEaMBUljAASij2SKoWckgQaAkA7r5IYGKIqKQAtAB85JQ0UABcMADaAAoA8mQAKgC6MAD00QZQADpoAN4ARKOUSQC2KDPNMzAzADQbuOpF0Byr61sbKIvASAhHGwC+mMJNMHWs7FyUrbPzUEsraxvbM12qn2UEOfxOMzOFyu4LubE43FgzweolaUEy2XKUAAFBksjlKBkAI7RNRgACU90aoiedXq8LeLRgAFEoFEoJgGYjabV6g93jAphtlupgIFVhtWezWnoODB0SSyes7vykXT4MhzK0ACxOJwwSZCmYi1Ri36StnQGXAOUK0k5ZVUkQqJ71WTyJQqdStQJgACqY2xXx+lPdimUalUrqMOmaADEkJwYAHKGGYDpUsHgMtMGHPZGeXzqSpWmgIMw0NEEAgnVQadV1VyBVLoJzXtyG7yGtQBUaTWaJTMW0zgAh0TbUvIANboR2qwsaqpgHVOADMBumwsjA9aQ8tI7HKAnMGns5gdzzEfUPO7dZLMEr1dr9bd2nz3pA4-KKZxXzDobfK8o2eYxmgUDg5R-MNnxdZ5Ly9VRmk-I9ygUaIwF8bFgHQ3x-1zQCEOjUDwLlNCMOglFYOeJsmTxTFCTUKssBom9VQ+OYxh+dZd3+DZsIw3oIBnNBuI2W5a0oG9Ki1GAACY9UNDiFmzFYYB4iF+N8QThNE447nQDhTEiGI4niaAAkMAAZCAskKeISjKCpNRqIseyZToegGYYDHUfI0EUrNlkHXjAX0YEDmCmYVUaSTqPbXslO+FTrghIEQTBPS2wRWLXLvFBWgQGzE2xazbOJe0KRgww4IIyMfRQf1A0ClAAI9ICiNjBMkyg7R00zTiVPwtrCM7XLUQfKsa0o6rG3iplhyyxkFzYwUt1FcVd2HVpoiSHDoCQAAvFBDnPCS1S7aSMBXUJDTW00NotaUYB2zT9qOk6L1q69O1vcavi0eQqujeC6pgEBfBQEApw6ML0uxNKDk2SY-20VrwxG+pQPgCGoZgJAADMYAR0EYBQElR1USYfwB4AXGJwyQe+5Fi3yk8oFJIGauGurkLkFAyMwzS8MZ4DMdjEiYAFiiWZvGjWlKxMMlUJjFo7Zm3PY5rdJCzTtLPdTorcqTnOXOSFOmRKuLUsSNJwvWROtzKDKMqJYgSSIUHQGBSpiZh7NKcpMEu5h1aaVo2mkZlLOZXpmUGIYfNUPzJl1oT0DO6M5ZgVPhNVnLfvvQr7F9kqbN98qyUpabga+xCYCaAnLAFrC7bTtA0ffUWY1aPxIanPHCZzr3skmRMTwhmMhvRgtRoL1n8YpsRq9nrOFpY2eVr7bcHr3J6Xr2qBDuOucYvO+pg+u27jW381d6tZ7dowt7j9OkXWJZ1oLA55fX2570jBQNwVCOEW4CTbh3dqIFYwyEAQ1QwQ80CczitlJkPt0JKxVuvUOvZDaPB+hfM2ThDT6U4C7EyCR0RykspiGAABxFSUZ-aOSDibaMK02i0JjvHewKkU6t1zvOZBjJWgILzkiMahdMQlUxBXHIVcZZc2nv-Bu+Mm4gIQRAjG3dsZ9wHtnfhw9KZoDHhhQwxgp6d2Wh-GAC8EAGCQV2Ve+4xFWI1qta+61b5bQfq9Q+70T5G1ngQ3UN1NwePul4-c21H6+Gfh9CxkDQ7jS-kvBRv8lF12QDkehIpsSaJnmLVoGQLCoBoFGUcCAYA8JFA4l4KD5YyMYggZic135uKNNUtQEo2izE6QASWkBKUIslVzagBA5b8A0gprC2JMDYOgECgCnD+LiMyASdIAHIqRmTcfouDYrPAIfJIhhQOkMO6b0lSAyhkjLGRsCZKAVnJTWXMmYCyllPOmTMCEmztnfN2SQwyERXamWwNEKA2BuDwC-IYHJhgmGB2Dmw0+4duh9G4bwmJ9tDS-OWPs8RdThH6LAcJbYUxcUoDhHNJJ95eblGxHAGFsjKrL1mvUlkzisFdk3ndHcj176ynlGTCqAS8HqmCXqDcW9PGDm8YKu0SpTo-xkLXVoKi1EYVAVpcBCStFY17rjAmxLtXCTxkYkxE9zFvw3tY2x9jWWOLmq0NerSbXtN5Tvbx+8n5+JfvihcErQnSoibKqJPiD5H3idamlrMUmc3SZ3VodKUBwuxJ04WtcOpJphdnB8KAih0MGg6wliIe5MqaS0lBbSw7uP6YM1owzRlKtPsbJcrRjlzLOcsa5DbbmnWdsC8h8RLCAMKgWgAUhAMecKEjvKhiwpcyK3GdD9F5IYnS+EkvQIaSFwAR1QDgBAQqUBth1v9UI0txr7Zkt3fuw9x7T1XOkFSqtMbWgACsp1oGxJOxWwrK4OPpE6jl7IXFuprcGvld8mTyv-Q6ZtgTxUmxXPqK+-ZPVhtg4qeDuCXwqr-nXdVzcNG6oKdog1-cjUILNaPQopjJ7Ru5baxegGS3Nk5a69UPLwlQa9TEuJoqDkXWQzAEJaGb6hr3vx31UbM0-Wmp-dmqTnTVQTUBeqYBU11vyd9QpQqSkoBoDAGwkMCaoDlJ00wjG54+hUtLFTNcCPNFiADFNKkgxjDDEjQot7KD3ugDpruWNf2FB-CeCAhacyMYkazXzsBRzjg4JOYAAiFFsqJS619XGUXuPQ5Ep6CWjxJZPCls856kNttE2uKVHr8v30K8eU8DtcNUSA+ykLGDmlgewUyKY5Xz4iY7dMQFZC3bxEiHujUR54vYEhYQPIBRigBycounr4dI7R1jvHcxgi2vCLAzFpN3A8B5PjfhjJR3punetXpxA03s7ViML1fGURFiT2LVnO7eBOuVqWvJnLfWM74MG+bQHzsgA

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
