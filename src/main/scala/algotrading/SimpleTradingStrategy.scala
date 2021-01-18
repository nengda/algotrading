package algotrading

import java.io.{BufferedWriter, FileWriter}

import scala.util.{Failure, Try}

object SimpleTradingStrategy {

  def movingAverage(
     n: Int,
     m: Int,
     observations: List[Observation],
     commission: BigDecimal => BigDecimal = pnl => pnl - (pnl * 0.001).abs): List[Order] = {
    if (observations.size < n || m >= n || m <= 0 || n <= 0) List.empty
    else {
      val mAverage = movingAveragePrices(m, observations).drop(n - m)
      val nAverage = movingAveragePrices(n, observations)
      val directions = mAverage zip nAverage map {case (mAvg, nAvg) =>
        if (mAvg > nAvg) Buy else Sell
      }
      val candidates = removeConsectiveDuplicates(observations.drop(n - 1) zip directions)
      val orders = candidates.grouped(2) flatMap {
        case List((bObservation, bDirection), (sObservation, sDirection)) => {
          val bPrice = bObservation.price
          val sPrice = sObservation.price
          val bOrder = Order(bObservation.timestamp, bDirection, bPrice)
          val sOrder = Order(
            sObservation.timestamp,
            sDirection,
            sPrice,
            Some(commission(bOrder.quantity * (sPrice - bPrice))),
            bOrder.quantity * sPrice)

          List(bOrder, sOrder)
        }
        case List((observation, direction)) => {
          val order = Order(observation.timestamp, direction, observation.price)
          List(order)
        }
      }
      orders.toList
    }
  }

  private def movingAveragePrices(minutes: Int, observations: List[Observation]) : List[BigDecimal] =
    observations.map(_.price).sliding(minutes).map(_.sum).map(_ / minutes).toList


  private def removeConsectiveDuplicates(candidates : List[Tuple2[Observation, Direction]]) = {
    val tail = candidates
      .tail
      .sliding(2)
      .collect{ case List(first, second) if (first._2 != second._2) => second}.toList
    if (candidates.head._2 == Buy) candidates.head :: tail else tail
  }

}

object SimpleStrategyReport extends App {

  val usage = """
      |Usage: SimpleStrategyReport --lastN num --lastM num --marketDataFile filename --orderResultFile filename
      |""".stripMargin
  // trade time - exact the same time as the price, does it mean milisecond in market data is also needed here?
  val header = List("Number", "Time", "Side", "Quantity", "Price", "PnL", "Net PnL")

  if (args.length == 0) printUsageAndQuit

  val argsMap = listToArgsMap(Map(), args.toList)
  if (argsMap.get("lastN").isEmpty
    || argsMap.get("lastM").isEmpty
    || argsMap.get("orderResultFile").isEmpty
    || argsMap.get("marketDataFile").isEmpty
  ) printUsageAndQuit

  // args exception handling?
  val marketData = io.Source.fromFile(argsMap("marketDataFile"))
    .getLines().toList.tail
  val orders = startStrategy(argsMap("lastN").toInt, argsMap("lastM").toInt, marketData)
  writeCSVFile(header :: orders, argsMap("orderResultFile"))

  def printUsageAndQuit = {
    println(usage)
    sys.exit(1)
  }

  def listToArgsMap(map: Map[String, String], argList: List[String]) : Map[String, String] = {
    argList match {
      case Nil => map
      case "--lastN" :: value :: tail =>
        listToArgsMap(map ++ Map("lastN" -> value), tail)
      case "--lastM" :: value :: tail =>
        listToArgsMap(map ++ Map("lastM" -> value), tail)
      case "--marketDataFile" :: value :: tail =>
        listToArgsMap(map ++ Map("marketDataFile" -> value), tail)
      case "--orderResultFile" :: value :: tail =>
        listToArgsMap(map ++ Map("orderResultFile" -> value), tail)
      case arg :: tail => {
        println("Unknown argument "+arg)
        printUsageAndQuit
      }
    }
  }

  // how should we format the net pnl?
  def startStrategy(n: Int, m: Int, marketData: List[String]): List[List[String]] = {
    val observations = marketData.flatMap(s => Observation(s));
    val orders = SimpleTradingStrategy.movingAverage(n, m, observations)
    val reportElements = orders.scanLeft(0, BigDecimal.valueOf(0.0)){case ((number, netPnl), order) => {
      val nextNumber = if (order.direction == Sell) number + 1 else number
      (nextNumber, netPnl + order.pnl.getOrElse(BigDecimal.valueOf(0.0)))
    }}
    val reports = reportElements.tail zip orders map { case ((number, netPnl), order) => {
      (number.toString :: order.asString) :+ netPnl.doubleValue().toString
    }}
    reports
  }

  def writeCSVFile(lines: List[List[String]], fileName: String): Try[Unit] =
    Try(new BufferedWriter(new FileWriter(fileName))).flatMap( writer => {
      Try{
        lines.foreach(l => {
          writer.write(l.mkString(",") + "\n")
          writer.flush
        })
        writer.close
      } match {
        case f @ Failure(exception) => Try(writer.close).recoverWith{ case _ => f }
        case success => success
      }
    })

}
