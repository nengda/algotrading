package command

import java.io.{PrintWriter, Writer}

import scala.io.StdIn
import scala.reflect.ClassTag
import scala.util.{Failure, Properties, Try}

/**
 *  Base trait representing the Canvas itself.
 */
sealed trait Canvas[E] {
  def initialize(size: Size, v: E) : Try[Unit]
  def initialized: Boolean
  def inRange(p: Point): Boolean
  def set(p: Point, v: E)
  def getOption(p: Point, default: E): Option[E]
  def flush
}

/**
 *  Canvas backed by mutable two-dimentional array. It is not thread safe
 *  and shouldn't be used in multi-threading environment.
 */
class ArrayCanvas[E:ClassTag](writer: Writer) extends Canvas[E] {
  private type BufferInternal = Array[Array[E]]
  private var maybeBuffer = Option.empty[BufferInternal]

  override def initialize(size: Size, v: E): Try[Unit] = Try {
    if (size.width < 1 || size.height < 1) throw new IllegalArgumentException("Canvas' height and width must be larger than 0")
    else maybeBuffer = Some(Array.fill[E](size.height + 2, size.width + 2)(v))
  }

  override def initialized: Boolean = maybeBuffer.isDefined

  override def inRange(p: Point): Boolean = maybeBuffer.map(buffer =>
    p.x > 0 && p.x < buffer(0).length - 1
    && p.y > 0 && p.y < buffer.length - 1).getOrElse(false)

  override def set(p: Point, v: E): Unit =
    if (inRangeWithCanvasEdge(p)) maybeBuffer.foreach(buffer => buffer(p.y).update(p.x, v))

  override def getOption(p: Point, default: E): Option[E] = if (!inRange(p)) None else maybeBuffer.flatMap(buffer => {
    val v = buffer(p.y)(p.x)
    if (v == default) None else Some(v)
  })

  override def flush: Unit = {
    maybeBuffer.foreach(buffer => buffer.foreach(b => writer.write(b.mkString("") + Properties.lineSeparator)))
    writer.flush
  }

  private def inRangeWithCanvasEdge(p: Point) = maybeBuffer.map(buffer =>
    p.x > -1 && p.x < buffer(0).length + 2 && p.y > -1 && p.y < buffer.length + 2).getOrElse(false)
}

object CanvasConsole extends App {

  val canvasCommands = CommandAPI.canvas(
    new CanvasHandler(
      new ArrayCanvas[Char](new PrintWriter(Console.out))))

  val CreatePattern = "(?i)^C (\\d+) (\\d+)$".r
  val LinePattern = "(?i)^L (\\d+) (\\d+) (\\d+) (\\d+)$".r
  val RectanglePattern = "(?i)^R (\\d+) (\\d+) (\\d+) (\\d+)$".r
  val BucketFillPattern = "(?i)^B (\\d+) (\\d+) ([a-zA-Z])$".r
  val QuitPattern = "(?i)^Q$".r

  printHelp

  while (true) {
    print("enter command: ")
    val rawInput = StdIn.readLine()
    val result = rawInput match {
      case CreatePattern(width, height) => canvasCommands.create(Size(width, height)).execute
      case LinePattern(x1, y1, x2, y2) => canvasCommands.line.from(Point(x1, y1)).to(Point(x2, y2)).draw(CharDot('x')).execute
      case RectanglePattern(x1, y1, x2, y2) => canvasCommands.retangle.from(Point(x1, y1)).to(Point(x2, y2)).draw(CharDot('x')).execute
      case BucketFillPattern(x1, y1, c) => canvasCommands.bucketFill(PointInput(Point(x1, y1), CharColor(c.head))).execute
      case QuitPattern() => canvasCommands.quit.execute
      case _ => Seq(Failure(new IllegalArgumentException(s"Command: <${rawInput}> is not supported")))
    }
    result.collect{case r: Failure[_] => r}.foreach( f => println(s"Failed to execute <${rawInput}>, reason: ${f.exception.getMessage}"))
  }

  implicit def string2Int(s: String): Int = augmentString(s).toInt
  def printHelp = println("""
                            |Command 		Description
                            |C w h           Should create a new canvas of width w and height h.
                            |L x1 y1 x2 y2   Should create a new line from (x1,y1) to (x2,y2). Currently only
                            |                horizontal or vertical lines are supported. Horizontal and vertical lines
                            |                will be drawn using the 'x' character.
                            |R x1 y1 x2 y2   Should create a new rectangle, whose upper left corner is (x1,y1) and
                            |                lower right corner is (x2,y2). Horizontal and vertical lines will be drawn
                            |                using the 'x' character.
                            |B x y c         Should fill the entire area connected to (x,y) with "colour" c. The
                            |                behavior of this is the same as that of the "bucket fill" tool in paint
                            |                programs.
                            |Q               Should quit the program.""".stripMargin)


}