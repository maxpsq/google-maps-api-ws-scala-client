# Google Maps API client on Scala [![Build Status](https://secure.travis-ci.org/thenewmotion/scala-geocode.png)](http://travis-ci.org/maxpsq/google-maps-api-ws-scala-client)

Google [Maps API Web Services](https://developers.google.com/maps/documentation/webservices/) client written on Scala

## Supports

* Getting geocode responses for an address (geocoding)
* Getting geocode responses for a GPS coordinates (reverse geocoding)
* Getting time zone responses for a GPS coordinate 

## Example

```scala
      val geocode = new Geocode()

      def ?(location: Location) = Await.result(geocode ? location, Duration(3, SECONDS))

      //  Geocoding
      ?(Address("Via Montenapoleone, 4, Milan, Italy")) match {
            case Right(results) => results.foreach(r => println(r.formatted_address + " -> " + r.geometry.location))
            case Left(error) => println(error)
      }

//    Via Montenapoleone, 20121 Milan, Italy -> Point(45.4678198,9.1958378)


      // Reverse Geocoding
      ?(GeoPoint(51.498685, -0.12967)) match {
            case Right(results) => results.foreach(r => println(r.formatted_address))
            case Left(error) => println(error)
      }

//      4B Deans Yd, Westminster, London, Greater London SW1P 3NP, UK
//      Westminster Abbey (Stop W), Westminster, City of Westminster, London SW1P, UK
//      Westminster, Westminster Abbey (S-bound), Westminster, City of Westminster, London SW1P, UK
//      Westminster, Westminster Abbey (Stop X), Westminster, City of Westminster, London SW1P, UK
//      Westminster Abbey (R), Westminster, City of Westminster, London SW1H, UK
//      Westminster, City of Westminster, London SW1P 3NY, UK
//      London, Greater London SW1P, UK
//      Westminster, London, UK
//      City of Westminster, Greater London, UK
//      London, UK
//      London, UK
//      Greater London, UK
//      England, UK
//      United Kingdom
```


## Setup

### Maven

1. Add this repository to your pom.xml:
```xml
    <repository>
        <id>maxpsq</id>
        <name>maxpsq Repository</name>
        <url>https://oss.sonatype.org/service/local/staging/deploy/maven2</url>
    </repository>
```

2. Add dependency to your pom.xml:
```xml
    <dependency>
        <groupId>com.github.maxpsq</groupId>
        <artifactId>maxpsq-gmapsclient_2.11</artifactId>
        <version>0.2</version>
    </dependency>
```

### SBT

1. Add this repository to your build.sbt:
```
resolvers += "Sonatype repository" at "https://oss.sonatype.org/service/local/staging/deploy/maven2"
```

2. Add dependency to your build.sbt:
```
libraryDependencies ++= Seq(
    "com.github.maxpsq" % "maxpsq-gmapsclient_2.11" % "0.2"
)
```
