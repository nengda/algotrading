package command

import java.io.{StringWriter, Writer}

import org.junit.runner.RunWith
import org.scalatest.{FunSuite, Matchers}
import org.scalatestplus.junit.JUnitRunner

import scala.util.Success

@RunWith(classOf[JUnitRunner])
class CommandHandlerTest extends FunSuite with Matchers {

//  test("should create new canvas") {
//    val writer = new StringWriter()
//    val handler = getCanvasHandler(writer)
//    handler.create(CreateParam(Size(2, 3))) shouldBe Success()
//    writer.toString shouldBe
//      """----
//        ||  |
//        ||  |
//        ||  |
//        |----
//        |""".stripMargin
//
//    handler.create(CreateParam(Size(-1, 0))).isFailure shouldBe true
//    handler.create(CreateParam(Size(0, -1))).isFailure shouldBe true
//    handler.create(CreateParam(Size(0, 0))).isFailure shouldBe true
//
//    writer.toString shouldBe
//      """----
//        ||  |
//        ||  |
//        ||  |
//        |----
//        |""".stripMargin
//  }
//
//  test("should draw line") {
//    val writer = new StringWriter()
//    val handler = getCanvasHandler(writer)
//    handler.create(CreateParam(Size(1, 4))) shouldBe Success()
//    handler.line(LineParam(TurplePointInput(Point(1, 1), Point(1, 3), CharDot('x')))) shouldBe Success()
//    writer.toString shouldBe
//      """---
//        || |
//        || |
//        || |
//        || |
//        |---
//        |---
//        ||x|
//        ||x|
//        ||x|
//        || |
//        |---
//        |""".stripMargin
//
//    handler.create(CreateParam(Size(4, 1))) shouldBe Success()
//    handler.line(LineParam(TurplePointInput(Point(1, 1), Point(4, 1), CharDot('x')))) shouldBe Success()
//    writer.toString shouldBe
//      """---
//        || |
//        || |
//        || |
//        || |
//        |---
//        |---
//        ||x|
//        ||x|
//        ||x|
//        || |
//        |---
//        |------
//        ||    |
//        |------
//        |------
//        ||xxxx|
//        |------
//        |""".stripMargin
//
//  }
//
//  test("should fail if line is out of range") {
//    val writer = new StringWriter()
//    val handler = getCanvasHandler(writer)
//    handler.create(CreateParam(Size(1, 4))) shouldBe Success()
//    handler.line(LineParam(TurplePointInput(Point(1, 1), Point(1, 5), CharDot('x')))).isFailure shouldBe true
//    handler.line(LineParam(TurplePointInput(Point(1, 0), Point(1, 3), CharDot('x')))).isFailure shouldBe true
//    handler.line(LineParam(TurplePointInput(Point(1, 0), Point(1, 3), CharDot('x')))).failed.get.getMessage shouldBe "Input is out of bound"
//  }
//
//  test("should fail if canvas is not created") {
//    val writer = new StringWriter()
//    val handler = getCanvasHandler(writer)
//    handler.line(LineParam(TurplePointInput(Point(1, 1), Point(1, 5), CharDot('x')))).isFailure shouldBe true
//  }
//
//  test("should fail if neither vertical or horizontal") {
//    val writer = new StringWriter()
//    val handler = getCanvasHandler(writer)
//    handler.create(CreateParam(Size(1, 4))) shouldBe Success()
//    handler.line(LineParam(TurplePointInput(Point(2, 1), Point(1, 3), CharDot('x')))).isFailure shouldBe true
//  }
//
//  test("should draw rectangle") {
//    val writer = new StringWriter()
//    val handler = getCanvasHandler(writer)
//    handler.create(CreateParam(Size(4, 4))) shouldBe Success()
//    handler.rectangle(RectangleParam(TurplePointInput(Point(2, 1), Point(4, 3), CharDot('x')))) shouldBe Success()
//    writer.toString shouldBe
//      """------
//        ||    |
//        ||    |
//        ||    |
//        ||    |
//        |------
//        |------
//        || xxx|
//        || x x|
//        || xxx|
//        ||    |
//        |------
//        |""".stripMargin
//  }
//
//  test("should draw reverse rectangle") {
//    val writer = new StringWriter()
//    val handler = getCanvasHandler(writer)
//    handler.create(CreateParam(Size(4, 4))) shouldBe Success()
//    handler.rectangle(RectangleParam(TurplePointInput(Point(4, 3), Point(2, 1), CharDot('x')))) shouldBe Success()
//    writer.toString shouldBe
//      """------
//        ||    |
//        ||    |
//        ||    |
//        ||    |
//        |------
//        |------
//        || xxx|
//        || x x|
//        || xxx|
//        ||    |
//        |------
//        |""".stripMargin
//  }
//
//  test("should draw rectangle as line") {
//    val writer = new StringWriter()
//    val handler = getCanvasHandler(writer)
//    handler.create(CreateParam(Size(4, 4))) shouldBe Success()
//    handler.rectangle(RectangleParam(TurplePointInput(Point(4, 3), Point(2, 3), CharDot('x')))) shouldBe Success()
//    writer.toString shouldBe
//      """------
//        ||    |
//        ||    |
//        ||    |
//        ||    |
//        |------
//        |------
//        ||    |
//        ||    |
//        || xxx|
//        ||    |
//        |------
//        |""".stripMargin
//  }
//
//  test("should fail if rectangle is out of bound") {
//    val writer = new StringWriter()
//    val handler = getCanvasHandler(writer)
//    handler.create(CreateParam(Size(4, 4))) shouldBe Success()
//    handler.rectangle(RectangleParam(TurplePointInput(Point(5, 5), Point(2, 1), CharDot('x')))).isFailure shouldBe true
//    handler.rectangle(RectangleParam(TurplePointInput(Point(3, 4), Point(0, 1), CharDot('x')))).isFailure shouldBe true
//  }
//
//  test("should bucket fill") {
//    val writer = new StringWriter()
//    val handler = getCanvasHandler(writer)
//    handler.create(CreateParam(Size(4, 4))) shouldBe Success()
//    handler.bucketFill(BucketFillParam(PointInput(Point(4, 3), CharColor('o')))) shouldBe Success()
//    writer.toString shouldBe
//      """------
//        ||    |
//        ||    |
//        ||    |
//        ||    |
//        |------
//        |------
//        ||oooo|
//        ||oooo|
//        ||oooo|
//        ||oooo|
//        |------
//        |""".stripMargin
//  }
//
//  test("should bucket fill with rectangle") {
//    val writer = new StringWriter()
//    val handler = getCanvasHandler(writer)
//    handler.create(CreateParam(Size(8, 8))) shouldBe Success()
//    handler.rectangle(RectangleParam(TurplePointInput(Point(3, 3), Point(7, 7), CharDot('x')))) shouldBe Success()
//    handler.bucketFill(BucketFillParam(PointInput(Point(4, 4), CharColor('o')))) shouldBe Success()
//    writer.toString shouldBe
//      """----------
//        ||        |
//        ||        |
//        ||        |
//        ||        |
//        ||        |
//        ||        |
//        ||        |
//        ||        |
//        |----------
//        |----------
//        ||        |
//        ||        |
//        ||  xxxxx |
//        ||  x   x |
//        ||  x   x |
//        ||  x   x |
//        ||  xxxxx |
//        ||        |
//        |----------
//        |----------
//        ||        |
//        ||        |
//        ||  xxxxx |
//        ||  xooox |
//        ||  xooox |
//        ||  xooox |
//        ||  xxxxx |
//        ||        |
//        |----------
//        |""".stripMargin
//  }
//
//  test("should bucket fill with line") {
//    val writer = new StringWriter()
//    val handler = getCanvasHandler(writer)
//    handler.create(CreateParam(Size(8, 8))) shouldBe Success()
//    handler.line(LineParam(TurplePointInput(Point(3, 3), Point(3, 7), CharDot('x')))) shouldBe Success()
//    handler.line(LineParam(TurplePointInput(Point(3, 3), Point(7, 3), CharDot('x')))) shouldBe Success()
//    handler.line(LineParam(TurplePointInput(Point(5, 3), Point(5, 7), CharDot('x')))) shouldBe Success()
//    handler.bucketFill(BucketFillParam(PointInput(Point(4, 4), CharColor('o')))) shouldBe Success()
//    writer.toString shouldBe
//      """----------
//        ||        |
//        ||        |
//        ||        |
//        ||        |
//        ||        |
//        ||        |
//        ||        |
//        ||        |
//        |----------
//        |----------
//        ||        |
//        ||        |
//        ||  x     |
//        ||  x     |
//        ||  x     |
//        ||  x     |
//        ||  x     |
//        ||        |
//        |----------
//        |----------
//        ||        |
//        ||        |
//        ||  xxxxx |
//        ||  x     |
//        ||  x     |
//        ||  x     |
//        ||  x     |
//        ||        |
//        |----------
//        |----------
//        ||        |
//        ||        |
//        ||  xxxxx |
//        ||  x x   |
//        ||  x x   |
//        ||  x x   |
//        ||  x x   |
//        ||        |
//        |----------
//        |----------
//        ||oooooooo|
//        ||oooooooo|
//        ||ooxxxxxo|
//        ||ooxoxooo|
//        ||ooxoxooo|
//        ||ooxoxooo|
//        ||ooxoxooo|
//        ||oooooooo|
//        |----------
//        |""".stripMargin
//  }
//
//  test("should fill no bucket if already set") {
//    val writer = new StringWriter()
//    val handler = getCanvasHandler(writer)
//    handler.create(CreateParam(Size(8, 8))) shouldBe Success()
//    handler.rectangle(RectangleParam(TurplePointInput(Point(3, 3), Point(7, 7), CharDot('x')))) shouldBe Success()
//    handler.bucketFill(BucketFillParam(PointInput(Point(3, 4), CharColor('o')))) shouldBe Success()
//    writer.toString shouldBe
//      """----------
//        ||        |
//        ||        |
//        ||        |
//        ||        |
//        ||        |
//        ||        |
//        ||        |
//        ||        |
//        |----------
//        |----------
//        ||        |
//        ||        |
//        ||  xxxxx |
//        ||  x   x |
//        ||  x   x |
//        ||  x   x |
//        ||  xxxxx |
//        ||        |
//        |----------
//        |----------
//        ||        |
//        ||        |
//        ||  xxxxx |
//        ||  x   x |
//        ||  x   x |
//        ||  x   x |
//        ||  xxxxx |
//        ||        |
//        |----------
//        |""".stripMargin
//  }
//
//  test("should fail if bucket is out of bound") {
//    val writer = new StringWriter()
//    val handler = getCanvasHandler(writer)
//    handler.create(CreateParam(Size(8, 8))) shouldBe Success()
//    handler.bucketFill(BucketFillParam(PointInput(Point(9, 4), CharColor('o')))).isFailure shouldBe true
//  }
//
//  test("should fill bucket with builder") {
//    val writer = new StringWriter()
//    val builder = getCanvasBuilder(writer)
//    builder.create(Size(20, 4))
//      .line.from(Point(1, 2)).to(Point(6, 2)).draw(CharDot('x'))
//      .line.from(Point(6, 3)).to(Point(6, 4)).draw(CharDot('x'))
//      .retangle.from(Point(14, 1)).to(Point(18, 3)).draw(CharDot('x'))
//      .bucketFill(PointInput(Point(10, 3), CharColor('o'))).execute shouldBe List.fill(5)(Success())
//    writer.toString shouldBe
//      """----------------------
//        ||                    |
//        ||                    |
//        ||                    |
//        ||                    |
//        |----------------------
//        |----------------------
//        ||                    |
//        ||xxxxxx              |
//        ||                    |
//        ||                    |
//        |----------------------
//        |----------------------
//        ||                    |
//        ||xxxxxx              |
//        ||     x              |
//        ||     x              |
//        |----------------------
//        |----------------------
//        ||             xxxxx  |
//        ||xxxxxx       x   x  |
//        ||     x       xxxxx  |
//        ||     x              |
//        |----------------------
//        |----------------------
//        ||oooooooooooooxxxxxoo|
//        ||xxxxxxooooooox   xoo|
//        ||     xoooooooxxxxxoo|
//        ||     xoooooooooooooo|
//        |----------------------
//        |""".stripMargin  }

  private def getCanvasHandler(writer: Writer) = new CanvasHandler(new ArrayCanvas[Char](writer))
  private def getCanvasBuilder(writer: Writer) = CommandAPI.canvas(getCanvasHandler(writer))
}
