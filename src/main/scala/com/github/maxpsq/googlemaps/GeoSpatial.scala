package com.github.maxpsq.googlemaps

object GeoSpatials {
  
  abstract class Coordinate(val coord: Double) {
    val Min: Double 
    val Max: Double 
    def isValid: Boolean = ( Max > coord && Min < coord )
  }
  
  class Latitude(override val coord: Double) extends Coordinate(coord) {
    override val Min: Double = -90.0
    override val Max: Double =  90.0
  }

  class Longitude(override val coord: Double) extends Coordinate(coord) {
    override val Min: Double = -180.0
    override val Max: Double =  180.0
  }

  class GeoPoint(lat: Latitude, lng: Longitude) {
    
    def this( lat: Double, lng: Double) = this(new Latitude(lat), new Longitude(lng))
    
    def isValid: Boolean = lat.isValid && lng.isValid
      
    override def toString = "%s,%s".format(lat.coord, lng.coord)
  }
  
}