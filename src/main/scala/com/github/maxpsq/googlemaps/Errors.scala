package com.github.maxpsq.googlemaps


sealed trait Error
case object ZeroResults    extends Error
case object OverQuotaLimit extends Error
case object Denied         extends Error
case object InvalidRequest extends Error