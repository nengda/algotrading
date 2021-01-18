package algotrading

import org.junit.runner.RunWith
import org.scalatest.{FunSuite, Matchers}
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class SimpleTradingStrategyTest extends FunSuite with Matchers {

  test("should generate order with buy only") {
    generateOrders(2, 1, List(
      "2020-06-27 16:52,1593276720000,2.0,4328.29",
      "2020-06-27 16:53,1593276780000,3.0,6737.97",
      "2020-06-27 16:54,1593276840000,4.0,5078.15",
      "2020-06-27 16:55,1593276900000,5.0,2018.64",
    )) shouldBe List(
      List("0", "2020-06-27 16:53", "Buy", "6.6666", "3", "0", "0.0"))
  }

  test("should generate order with sell only") {
    generateOrders(2, 1, List(
      "2020-06-27 16:52,1593276720000,5.0,4328.29",
      "2020-06-27 16:53,1593276780000,3.0,6737.97",
      "2020-06-27 16:54,1593276840000,2.0,5078.15",
      "2020-06-27 16:55,1593276900000,1.0,2018.64",
    )) shouldBe List.empty
  }

  test("should generate orders with buy and sell") {
    generateOrders(3, 2, List(
      "2020-06-27 16:52,1593276720000,2.0,4328.29",
      "2020-06-27 16:53,1593276780000,3.0,6737.97",
      "2020-06-27 16:54,1593276840000,4.0,5078.15", //3.5 Buy@4;
      "2020-06-27 16:55,1593276900000,5.0,2018.64", // 4.5, can't buy
      "2020-06-27 16:56,1593276960000,6.0,4378.82", // 5.5, can't buy
      "2020-06-27 16:57,1593277020000,5.0,16759.23", //5.5, can't buy
      "2020-06-27 16:58,1593277080000,5.1,6997.16", // 5.05, sell
      "2020-06-27 16:59,1593277140000,6.0,3344.05", // 5.5, Buy@6;
      "2020-06-27 17:00,1593277200000,3.0,7788.77", // 4.5, Sell@3, Total Pnl
    )) shouldBe List(
      List("0","2020-06-27 16:54", "Buy", "5", "4", "0", "0.0"),
      List("1", "2020-06-27 16:58", "Sell", "5", "5.1", "5.4945", "5.4945"),
      List("1", "2020-06-27 16:59", "Buy", "3.3333", "6", "0", "5.4945"),
      List("2", "2020-06-27 17:00", "Sell", "3.3333", "3", "-10.0098", "-4.5153999"),
    )
  }

  test("should skip orders with invalid market data") {
    generateOrders(2, 1, List(
      "2020-06-27 16:52,1593276720000,2.0,4328.29",
      "2020-06-27 16:53,1593276780000,-3.0,6737.97", // skip
      "2020-06-27 16:54,1593276780000,3.0,-6737.97", // skip
      "2020-06-27 16:55,1593276780000,foo,6737.97", // skip
      "foo,1593276780000,3.0,6737.97", // skip
      "2020-06-27 16:56,1593276840000,4.0,5078.15", // 3 buy @4.0 can't buy
    )) shouldBe List(
      List("0", "2020-06-27 16:56", "Buy", "5", "4", "0", "0.0"),
    )
  }

  test("should only generate order with enough market data") {
    generateOrders(3, 2, List(
      "2020-06-27 16:52,1593276720000,4.4972,4328.29",
    )) shouldBe List.empty
    generateOrders(3, 2, List.empty) shouldBe List.empty
  }

  test("should not generate order with invalid n/m minutes") {
    generateOrders(-1, 2, List(
      "2020-06-27 16:52,1593276720000,2.0,4328.29",
      "2020-06-27 16:53,1593276780000,3.0,6737.97",
      "2020-06-27 16:54,1593276840000,4.0,5078.15",
      "2020-06-27 16:55,1593276900000,5.0,2018.64",
    )) shouldBe List.empty
    generateOrders(2, 0, List(
      "2020-06-27 16:52,1593276720000,2.0,4328.29",
      "2020-06-27 16:53,1593276780000,3.0,6737.97",
      "2020-06-27 16:54,1593276840000,4.0,5078.15",
      "2020-06-27 16:55,1593276900000,5.0,2018.64",
    )) shouldBe List.empty
    generateOrders(2, 2, List(
      "2020-06-27 16:52,1593276720000,2.0,4328.29",
      "2020-06-27 16:53,1593276780000,3.0,6737.97",
      "2020-06-27 16:54,1593276840000,4.0,5078.15",
      "2020-06-27 16:55,1593276900000,5.0,2018.64",
    )) shouldBe List.empty
    generateOrders(2, 3, List(
      "2020-06-27 16:52,1593276720000,2.0,4328.29",
      "2020-06-27 16:53,1593276780000,3.0,6737.97",
      "2020-06-27 16:54,1593276840000,4.0,5078.15",
      "2020-06-27 16:55,1593276900000,5.0,2018.64",
    )) shouldBe List.empty

  }

  private def generateOrders(n: Int, m: Int, marketData : List[String]) = SimpleStrategyReport.startStrategy(n, m, marketData)

}
