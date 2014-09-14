package com.github.maxpsq.googlemaps.timezone

import com.github.maxpsq.googlemaps.Parameter

case class Location(latitude: Double, longitude: Double) extends Parameter {
  override def tuple = ("location", "%s,%s".format(latitude, longitude))
}
