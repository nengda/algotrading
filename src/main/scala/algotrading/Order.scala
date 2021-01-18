package algotrading

import java.text.DecimalFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Collections

import scala.math.BigDecimal.RoundingMode
import scala.math.BigDecimal.RoundingMode.RoundingMode
import scala.util.Try

sealed trait Direction
case object Buy extends Direction {
  override def toString = "Buy"
}
case object Sell extends Direction {
  override def toString = "Sell"
}

case class Order(
    time: LocalDateTime,
    direction: Direction,
    price: BigDecimal,
    pnl: Option[BigDecimal] = None,
    notional : BigDecimal = 20.0,
    precision: Int = 4,
    roundingMode: RoundingMode = RoundingMode.DOWN) {

  private val format = new DecimalFormat("#." + List.fill(precision)("#").mkString(""))

  def quantity = adjustScale(notional / price)
  def asString : List[String] = List(
    time.format(Observation.dateTimeFormatter),
    direction.toString,
    adjustAsString(quantity),
    adjustAsString(price),
    pnl.map(p => adjustAsString(p)).getOrElse("0"))

  private def adjustScale(decimal: BigDecimal) = decimal.setScale(precision, roundingMode)
  private def adjustAsString(decimal: BigDecimal) = format.format(adjustScale(decimal))
}


case class Observation(
    price: BigDecimal,
    timestamp: LocalDateTime,
    volumn: Double) {
}

object Observation {
  val CSVPattern = "(.*),(.*),(.*),(.*)".r
  val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

  private def isValidDouble(s: String) = Try{ s.toDouble }.toOption.collect{case v if v > 0.0 => v}.isDefined
  private def isValidDateTime(s: String) = Try{ dateTimeFormatter.parse(s) }.toOption.isDefined

  def apply(s: String): Option[Observation] = s match {
    case CSVPattern(timestamp, _, price, volumn) if
    isValidDouble(price)
      && isValidDouble(volumn)
      && isValidDateTime(timestamp) =>
        Some(Observation(
          BigDecimal(price),
          LocalDateTime.from(dateTimeFormatter.parse(timestamp)),
          volumn.toDouble))
    case _ => None
  }
}
