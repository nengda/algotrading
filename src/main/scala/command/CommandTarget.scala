package command

import Schema._

/**
 *  This trait serves as a linkage between the command actions and target application.
 *  It binds all actions target application supports and their corresponding
 *  parameter types. Only ActionParam is added in this base trait, but it can extend
 *  to other generic parameter if application requires in the future. For example, a FilterParam
 *  type to represent the runtime filtering logic applied before performing command action.
 */
trait CommandTarget {
  type ActionParam <: Schema
}

/**
 *  The base class for targeted handlers. The self typing gunrentees the mixed-in of right
 *  set of handlers mixed-in.
 */
abstract class CommandTargetable[T <: CommandTarget] {
  self: T#ActionParam#Handler =>
}

/**
 *  The actual binding for CanvasCommand and its actions
 */
case object CanvasCommand extends CommandTarget {
  type ActionParam = CreateParam[Size] ::
    LineParam[TurplePointInput[CharDot]] ::
    RectangleParam[TurplePointInput[CharDot]] ::
    BucketFillParam[PointInput[Color]] :: QuitParam.type :: Nil
}
