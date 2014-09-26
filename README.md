# Google Maps API client on Scala [![Build Status](https://secure.travis-ci.org/thenewmotion/scala-geocode.png)](https://travis-ci.org/maxpsq/google-maps-api-ws-scala-client.svg?branch=master)

Google [Maps API Web Services](https://developers.google.com/maps/documentation/webservices/) client written on Scala for geocoding and time zone

## Supports

[GEOCODING](https://developers.google.com/maps/documentation/geocoding/)
* Getting responses for an address (geocoding)
* Getting responses for a GPS coordinate (reverse geocoding)

[TIME ZONE](https://developers.google.com/maps/documentation/timezone/)
* Getting responses for a GPS coordinate and timestamp 

NEED MORE?
* Any contribution will be very welcome! Please feel free to add comments, suggestions and of course to fork this repo and sending pull requests. Take a look at what is needed in the [issues page](https://github.com/maxpsq/google-maps-api-ws-scala-client/issues/)

## Example

```scala

   import scala.concurrent.ExecutionContext.Implicits.global
   import scala.concurrent.duration._
   import com.github.maxpsq.googlemaps.GoogleParameters._
   import com.github.maxpsq.googlemaps.geocoding.Parameters._
   import com.github.maxpsq.googlemaps.geocoding._
   import com.github.maxpsq.googlemaps.timezone.Parameters._
   import com.github.maxpsq.googlemaps.timezone._

   object GeocodeObj extends GeocodeCalls {

      implicit val geocodeClient = new GeocodeClient()

      def ?(location: LocationParam) = callGeocode(location, Duration(3, SECONDS))

      //  Geocoding
      ?(AddressParam("Via Montenapoleone, 4, Milan, Italy")) match {
         case Right(results) => results.foreach(r => 
            println(r.formatted_address + " -> " + r.geometry.location)
         )
         case Left(error) => println(error)
      }

/* <STDOUT>
  Via Montenapoleone, 20121 Milan, Italy -> Point(45.4678198,9.1958378)
*/

      // Reverse Geocoding
      ?(LatLngParam(51.498685, -0.12967)) match {
            case Right(results) => results.foreach(r => println(r.formatted_address))
            case Left(error) => println(error)
      }

/* <STDOUT>
  4B Deans Yd, Westminster, London, Greater London SW1P 3NP, UK
  Westminster Abbey (Stop W), Westminster, City of Westminster, London SW1P, UK
  Westminster, Westminster Abbey (S-bound), Westminster, City of Westminster, London SW1P, UK
  Westminster, Westminster Abbey (Stop X), Westminster, City of Westminster, London SW1P, UK
  Westminster Abbey (R), Westminster, City of Westminster, London SW1H, UK
  Westminster, City of Westminster, London SW1P 3NY, UK
  London, Greater London SW1P, UK
  Westminster, London, UK
  City of Westminster, Greater London, UK
  London, UK
  London, UK
  Greater London, UK
  England, UK
  United Kingdom
*/
   }


   object TimezoneObj extends TimezoneCalls {

      implicit val timezoneClient = new TimezoneClient()

      def ?(loc: LocationParam, epoch: Long) = callTimezone(loc, epoch, Duration(3, SECONDS))

      val epoch = 198264918L
      
      ?(LocationParam(50.516196, 30.466651), epoch) match {
         case Right(results) => results.foreach(r => 
            println(t.timeZoneName + " raw offset was " + r.rawOffset + " sec")
         )
         case Left(error) => println(error)
      }
   }
   
/* <STDOUT>   
  Europe/Rome raw offset was 3600 sec
*/
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
        <version>0.2-SNAPSHOT</version>
    </dependency>
  ```

### SBT

Add this repository and dependency to your build.sbt:
```
resolvers += "Sonatype repository" at "https://oss.sonatype.org/service/local/staging/deploy/maven2"

libraryDependencies ++= Seq(
    "com.github.maxpsq" % "maxpsq-gmapsclient_2.11" % "0.2-SNAPSHOT"
)
```
