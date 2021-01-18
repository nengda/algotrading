package command

import scala.util.{Failure, Success, Try}
import TryUtil._

/**
 *  Base trait for all command handlers, implementing the business logic of each command.
 */
sealed trait CommandHandler

private[command] trait BucketFillActionHandler[ParamType] extends CommandHandler {
  def bucketFill(param: BucketFillParam[ParamType]): Try[Unit]
}

private[command] trait RectangleActionHandler[ParamType] extends CommandHandler {
  def rectangle(param: RectangleParam[ParamType]): Try[Unit]
}

private[command] trait LineActionHandler[ParamType] extends CommandHandler {
  def line(param: LineParam[ParamType]): Try[Unit]
}

private[command] trait CreateActionHandler[ParamType] extends CommandHandler {
  def create(param: CreateParam[ParamType]): Try[Unit]
}

private[command] trait QuitActionHandler extends CommandHandler {
  def quit(param: QuitParam.type): Try[Unit]
}

/**
 *  Actual implementation of business logic for all Canvas commands
 */
class CanvasHandler(canvas: Canvas[Char]) extends CommandTargetable[CanvasCommand.type]
  with CreateParam[Size]#Handler
  with LineParam[TurplePointInput[CharDot]]#Handler
  with RectangleParam[TurplePointInput[CharDot]]#Handler
  with BucketFillParam[PointInput[Color]]#Handler
  with QuitParam.type#Handler {

  override def line(param: LineParam[TurplePointInput[CharDot]]): Try[Unit] =
    canvasInited andThen
    pointInRange(param.value.from) andThen
    pointInRange(param.value.to) andThen flush {
      Seq((param.value.from, param.value.to, param.value.paint.value))
    }

  override def rectangle(param: RectangleParam[TurplePointInput[CharDot]]): Try[Unit] =
    canvasInited andThen
    pointInRange(param.value.from) andThen
    pointInRange(param.value.to) andThen flush {
      val value = param.value
      Seq(
        (value.from, Point(value.to.x, value.from.y), value.paint.value),
        (Point(value.from.x, value.to.y), value.to, value.paint.value),
        (value.from, Point(value.from.x, value.to.y), value.paint.value),
        (Point(value.to.x, value.from.y), value.to, value.paint.value)
      )
    }

  override def bucketFill(param: BucketFillParam[PointInput[Color]]): Try[Unit] =
    canvasInited andThen pointInRange(param.value.point) andThen
      Try(checkAndFill(param.value.point, param.value.paint.toChar)) andThen Try(canvas.flush)

  // recursive implementation of bucket fill. Might have stack issue when Canvas gets bigger.
  // Possibly change to a @tailrec version
  private def checkAndFill(p: Point, v: Char): Unit = {
    if (pointInRange(p).isSuccess && canvas.getOption(p, DEFAULT_PAINT).isEmpty) {
      canvas.set(p, v)
      checkAndFill(Point(p.x - 1, p.y), v)
      checkAndFill(Point(p.x + 1, p.y), v)
      checkAndFill(Point(p.x, p.y - 1), v)
      checkAndFill(Point(p.x, p.y + 1), v)
    }
  }

  override def create(param: CreateParam[Size]): Try[Unit] =
    canvas.initialize(param.value, DEFAULT_PAINT) andThen flush {
      val value = param.value
      val from = Point(0, 0)
      val to = Point(value.width + 1, value.height + 1)
      Seq(
        (from, Point(from.x, to.y), '|'),
        (Point(to.x, from.y), to, '|'),
        (from, Point(to.x, from.y), '-'),
        (Point(from.x, to.y), to, '-')
      )
    }


  override def quit(param: QuitParam.type): Try[Unit] = Try {
    System.exit(0)
  }

  private def flush(lines: => Seq[(Point, Point, Char)]) = Try {
    lines.foreach{case (from, to, char) => {
      if (from.x == to.x) {
        (to.y to from.y) foreach (n => canvas.set(Point(from.x, n), char))
        (from.y to to.y) foreach (n => canvas.set(Point(from.x, n), char))
      } else if (from.y == to.y) {
        (from.x to to.x) foreach (n => canvas.set(Point(n, from.y), char))
        (to.x to from.x) foreach (n => canvas.set(Point(n, from.y), char))
      } else
        failUnsupportedLineAction
    }}
    canvas.flush
  }
  private def canvasInited = if (canvas.initialized) Success() else failUnitializedCanvas

  private def pointInRange(p: Point) = if (canvas.inRange(p)) Success() else failOutOfRange

  private val DEFAULT_PAINT = ' '
  private val failUnitializedCanvas = Failure(new IllegalStateException("Canvas is not initialized"))
  private val failOutOfRange = Failure(new IllegalArgumentException("Input is out of bound"))
  private def failUnsupportedLineAction = throw new IllegalArgumentException("Only horizontal or vertical line is supported")
}

/**
 *  Syntax suger chaining a sequence of Trys, which would
 *  only continue if previous ones are successful
 */

object TryUtil {
  implicit class ChainedTry[T](t: Try[T]) {
    def andThen(default: => Try[T]) = if (t.isSuccess) default else t
  }
}