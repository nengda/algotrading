package command

/**
 *  Base trait of all kinds of command parameters. For example
 *  a parameter used for creation of something, drawing a line somewhere or
 *  rotate some object.
 */
trait CommandParam {
  type Handler <: CommandHandler
  type Builder <: CommandBuilder
}

final case class CreateParam[ParamType](value: ParamType) extends CommandParam {
  type Handler = CreateActionHandler[ParamType]
  type Builder = CreateActionBuilder[ParamType]
}

final case class LineParam[ParamType](value: ParamType) extends CommandParam {
  type Handler = LineActionHandler[ParamType]
  type Builder = LineActionBuilder[ParamType]
}

final case class RectangleParam[ParamType](value: ParamType) extends CommandParam {
  type Handler = RectangleActionHandler[ParamType]
  type Builder = RectangleActionBuilder[ParamType]
}

final case class BucketFillParam[ParamType](value: ParamType) extends CommandParam {
  type Handler = BucketFillActionHandler[ParamType]
  type Builder = BucketFillActionBuilder[ParamType]
}

final case object QuitParam extends CommandParam {
  type Handler = QuitActionHandler
  type Builder = QuitActionBuilder
}
