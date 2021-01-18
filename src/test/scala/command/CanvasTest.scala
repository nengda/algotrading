package command

import java.io.{StringWriter, Writer}

import org.junit.runner.RunWith
import org.scalatest.{FunSuite, Matchers}
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class CanvasTest extends FunSuite with Matchers {

//  test("should initialize canvas") {
//    val writer = new StringWriter()
//    val canvas = getCanvas(writer)
//    canvas.initialized shouldBe false
//    canvas.initialize(Size(10, 10), ' ')
//    canvas.initialized shouldBe true
//    canvas.initialize(Size(-1, -1), ' ').failed.get.getMessage shouldBe "Canvas' height and width must be larger than 0"
//  }
//
//  test("should check in range") {
//    val writer = new StringWriter()
//    val canvas = getCanvas(writer)
//    canvas.initialize(Size(10, 10), ' ')
//    canvas.inRange(Point(-1, -2)) shouldBe false
//    canvas.inRange(Point(11, 8)) shouldBe false
//    canvas.inRange(Point(8, 11)) shouldBe false
//    canvas.inRange(Point(11, 11)) shouldBe false
//    canvas.inRange(Point(10, 10)) shouldBe true
//    canvas.inRange(Point(9, 9)) shouldBe true
//  }
//
//  test("should get point") {
//    val writer = new StringWriter()
//    val canvas = getCanvas(writer)
//    canvas.initialize(Size(2, 2), '-')
//    canvas.set(Point(1, 1), 'x')
//    canvas.getOption(Point(1, 1), '-') shouldBe Some('x')
//    canvas.getOption(Point(100, 1), '-') shouldBe None
//    canvas.getOption(Point(2, 1), '-') shouldBe None
//    canvas.set(Point(2, 1), 'x')
//    canvas.getOption(Point(2, 1), '-') shouldBe Some('x')
//  }
//
//  test("should flush nothing when uninitialized") {
//    val writer = new StringWriter()
//    val canvas = getCanvas(writer)
//    canvas.flush
//    writer.toString shouldBe ""
//  }
//
//  test("should set point") {
//    val writer = new StringWriter()
//    val canvas = getCanvas(writer)
//    canvas.initialize(Size(2, 2), '-')
//    canvas.set(Point(1, 1), 'x')
//    canvas.flush
//    writer.toString shouldBe
//      """----
//        |-x--
//        |----
//        |----
//        |""".stripMargin
//
//    canvas.set(Point(-1, 1), 'x')
//    canvas.flush
//    writer.toString shouldBe
//      """----
//        |-x--
//        |----
//        |----
//        |----
//        |-x--
//        |----
//        |----
//        |""".stripMargin
//
//    canvas.set(Point(2, 1), 'x')
//    canvas.flush
//    writer.toString shouldBe
//      """----
//        |-x--
//        |----
//        |----
//        |----
//        |-x--
//        |----
//        |----
//        |----
//        |-xx-
//        |----
//        |----
//        |""".stripMargin
//  }

  private def getCanvas(writer: Writer) = new ArrayCanvas[Char](writer)

}