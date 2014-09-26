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
  cache.enqueue(0L) // Epoch time January 1, 1970
  
  def obeyLimits[T](block: => T): T = {
    if ( doWait ) Thread.sleep(limits.rate)
    record(now + limits.rate)
    block
  }
  
  private def doWait: Boolean = {
    cache.apply(0) > now 
  }
  
  private def record(t: Long): Unit = {
    // .dequeue will raise an Exception on an empty Queue.
    // This is not expected to happen being the Queue initialized
    // with 1 element.
    // If that happens, there is a problem somewhere.
    cache.dequeue 
    cache.enqueue(t)
  }

  private def now: Long = System.currentTimeMillis()
  
}