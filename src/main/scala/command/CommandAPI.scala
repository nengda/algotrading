package command

import scala.collection.mutable
import scala.util.Try

/**
 *  The utility factory to construct command-based applications.
 */
object CommandAPI {
  def canvas(handler: CanvasCommand.type#ActionParam#Handler) = new CommandParamBuilder[CanvasCommand.type](handler, CanvasCommand)
    with CreateActionBuilder[Size]
    with LineActionBuilder[TurplePointInput[CharDot]]
    with RectangleActionBuilder[TurplePointInput[CharDot]]
    with BucketFillActionBuilder[PointInput[Color]]
    with QuitActionBuilder
}

/**
 *  This class connects the dots between handlers and builders. The self-typing
 *  ensures the correct implementation of both are mixed-in. This class also defines
 *  the runtime execution behavior of each command. It assumes FIFO and
 *  sequential execution but can extend to parallel or concurrent version when required.
 */
abstract class CommandParamBuilder[T <: CommandTarget](val actionHandler: T#ActionParam#Handler, val target: T) extends CommandBuilder {
  self:T#ActionParam#Builder =>

  type Builder  = CommandParamBuilder[T] with T#ActionParam#Builder

  private val queue = mutable.Queue.empty[CommandParam]
  private val history  = mutable.MutableList.empty[CommandParam]

  def getHistory = history.toList
  def getQueue = queue.toList

  override protected def add(c: CommandParam): Builder = {
    queue += c
    this
  }

  def execute: Seq[Try[Unit]] = {
    val result = queue.map(executeFor)
    history ++= queue
    queue.clear()
    result.toList
  }

  private def executeFor[T <: CommandParam](param: T) = param match {
    case c: CreateParam[_] => asActionHandlerFor(c).create(c)
    case l: LineParam[_] => asActionHandlerFor(l).line(l)
    case r: RectangleParam[_] => asActionHandlerFor(r).rectangle(r)
    case bf: BucketFillParam[_] => asActionHandlerFor(bf).bucketFill(bf)
    case q: QuitParam.type => asActionHandlerFor(q).quit(q)
  }

  private def asActionHandlerFor[T <: CommandParam](param: T) = actionHandler.asInstanceOf[T#Handler]
}