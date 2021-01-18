package command

import org.junit.runner.RunWith
import org.scalatest.{FunSuite, Matchers}
import org.scalatestplus.junit.JUnitRunner

import scala.util.{Success, Try}

@RunWith(classOf[JUnitRunner])
class CommandBuilderTest extends FunSuite with Matchers {
//  type TChar = TurplePointInput[CharDot]
//  val canvasCommandHandler = new CommandTargetable[CanvasCommand.type]
//    with CreateParam[Size]#Handler
//    with QuitParam.type#Handler
//    with LineParam[TChar]#Handler
//    with RectangleParam[TChar]#Handler
//    with BucketFillParam[PointInput[Color]]#Handler {
//
//    override def line(param: LineParam[TChar]) = Success()
//    override def create(param: CreateParam[Size]): Try[Unit] = Success()
//    override def rectangle(param: RectangleParam[TChar]): Try[Unit] = Success()
//    override def bucketFill(param: BucketFillParam[PointInput[Color]]): Try[Unit] = Success()
//
//    override def quit(param: QuitParam.type): Try[Unit] = Success()
//  }
//
//  val lineDot = CharDot('c')
//  val createInput = Size(50, 50)
//  val lineInput = TurplePointInput(Point(0, 0), Point(0, 3), lineDot)
//  val rectangleInput = TurplePointInput(Point(4, 0), Point(8, 8), lineDot)
//  val bucketFillInput = PointInput(Point(6, 7), Blue)
//
//
//  test("should execute for empty queque") {
//    val builder = CommandAPI.canvas(canvasCommandHandler)
//    executeAndVerify(builder, List.empty)
//  }
//
//  test("should quit ") {
//    val builder = CommandAPI.canvas(canvasCommandHandler).quit
//    executeAndVerify(builder, List(QuitParam))
//  }
//
//  test("should draw lines") {
//    val builder = CommandAPI.canvas(canvasCommandHandler)
//        .line.from(lineInput.from).to(lineInput.to).draw(lineDot)
//    executeAndVerify(builder, List(LineParam(lineInput)))
//
//    val lineDot2 = CharDot(' ')
//    val lineInput2 = TurplePointInput(Point(10, 5), Point(15, 15), lineDot2)
//    builder.line.from(lineInput2.from).to(lineInput2.to).draw(lineDot2)
//    executeAndVerify(builder, List(LineParam(lineInput2)))
//  }
//
//  test("should draw rectangle") {
//    val builder = CommandAPI.canvas(canvasCommandHandler)
//      .retangle.from(rectangleInput.from).to(rectangleInput.to).draw(lineDot)
//    executeAndVerify(builder, List(RectangleParam(rectangleInput)))
//
//    val lineDot2 = CharDot(' ')
//    val rectangleInput2 = TurplePointInput(Point(5, 60), Point(4, 45), lineDot2)
//    builder.retangle.from(rectangleInput2.from).to(rectangleInput2.to).draw(lineDot2)
//    executeAndVerify(builder, List(RectangleParam(rectangleInput2)))
//  }
//
//  test("should bucket fill with color") {
//    val builder = CommandAPI.canvas(canvasCommandHandler)
//      .bucketFill(bucketFillInput)
//    executeAndVerify(builder, List(BucketFillParam(bucketFillInput)))
//
//    val bucketFillInput2 = PointInput(Point(15, 15), Red)
//    builder.bucketFill(bucketFillInput2)
//    executeAndVerify(builder, List(BucketFillParam(bucketFillInput2)))
//  }
//
//  test("should draw line, retangle and bucket fill") {
//    val builder = CommandAPI.canvas(canvasCommandHandler)
//      .create(createInput)
//      .line.from(lineInput.from).to(lineInput.to).draw(lineDot)
//      .retangle.from(rectangleInput.from).to(rectangleInput.to).draw(lineDot)
//      .bucketFill(bucketFillInput)
//
//    builder.getHistory shouldBe empty
//    executeAndVerify(builder, List(
//      CreateParam(createInput),
//      LineParam(lineInput),
//      RectangleParam(rectangleInput),
//      BucketFillParam(bucketFillInput)))
//  }
//
//
//  private def executeAndVerify(builder: CommandParamBuilder[_], params: List[CommandParam]) = {
//    builder.getQueue shouldBe params
//    builder.execute shouldBe List.fill(params.size)(Success())
//    builder.getQueue shouldBe empty
//    builder.getHistory.takeRight(params.size) shouldBe params
//  }

}

