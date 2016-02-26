Bank Account Server : mvn + Java 8 + Spring MVC + Spring core + Spring cache + Logback

Look for any web.xml file or logback.xml... This is a ZERO config webapp.

To start the server, use the command `mvn jetty:run`

Here is a scenario that you can try with this server (without sudo) :

```bash
curl -X'GET' http://localhost:8080/api/
curl -X'POST' http://localhost:8080/api/account/AL47212110090000000235698741
curl -X'PUT' http://localhost:8080/api/account/AL47212110090000000235698741/details -H'Content-Type:application/json' -d '{"swiftCode":"ABCOFRPP","title":"MR","firstName":"Olivier","lastName":"Martin","address":"10 boulevard de sebastopol","zipCode":"75001","city":"Paris","state":"Ile De France","country":"FR"}'
curl -X'POST' http://localhost:8080/api/account/AL47212110090000000235698741/deposit -d '{"amount":1000.0}' -H 'Content-Type:application/json'
curl -X'POST' http://localhost:8080/api/account/AL47212110090000000235698741/withdraw -d '{"amount":500}' -H 'Content-Type:application/json'
curl -X'POST' http://localhost:8080/api/account/AL47212110090000000235698741/transfer -d '{"amount":70,"recipient":"RATP"}' -H 'Content-Type:application/json'
curl -X'GET' http://localhost:8080/api/account/AL47212110090000000235698741/balance
curl -X'GET' http://localhost:8080/api/account/AL47212110090000000235698741/history
curl -X'GET' http://localhost:8080/api/account/AL47212110090000000235698741/details

```
