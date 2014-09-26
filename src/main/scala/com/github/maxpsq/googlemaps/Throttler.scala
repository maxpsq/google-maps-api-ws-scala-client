package com.github.maxpsq.googlemaps

import scala.concurrent.duration.Duration
import scala.collection.mutable


case class UsageLimits(calls: Int, duration: Duration) {
  val rate: Long = ( duration / calls.toDouble ).toMillis
}

/**
  * Ensures to honour the usage limits rules 
  * by simply sleeping the application for a
  * given amount of time.
  */
class Throttler(limits: UsageLimits) {

  private val cache: mutable.Queue[Long] = mutable.Queue()
  cache.enqueue(now)
  
  def obeyLimits[T](block: => T): T = {
    if ( doWait ) Thread.sleep(limits.rate)
    record(now + limits.rate)
    block
  }
  
  private def doWait: Boolean = {
    cache.apply(0) > now 
  }
  
  private def record(t: Long): Unit = {
    cache.dequeue
    cache.enqueue(t)
  }

  private def now: Long = System.currentTimeMillis()
  
}