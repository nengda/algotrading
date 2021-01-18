package command

/**
 *  Base trait for all command builders, defining the input parameter
 *  and how it's provided
 */
private[command] trait CommandBuilder {
  protected type Builder <: CommandBuilder
  protected def add(c: CommandParam): Builder
}

private[command] trait CreateActionBuilder[ParamType] extends CommandBuilder {
  def create(value: ParamType) = add(CreateParam(value))
}

private[command] trait LineActionBuilder[ParamType] extends CommandBuilder {
  def line = new FromInput[Builder, CharDot](d => add(LineParam(d)))
}

private[command] trait RectangleActionBuilder[ParamType] extends CommandBuilder {
  def retangle = new FromInput[Builder, CharDot](d => add(RectangleParam(d)))
}

private[command] trait BucketFillActionBuilder[ParamType] extends CommandBuilder {
  def bucketFill(value: ParamType) = add(BucketFillParam(value))
}

private[command] trait QuitActionBuilder extends CommandBuilder {
  def quit = add(QuitParam)
}

case class PointInput[+PaintType <: Paint](point: Point, paint: PaintType)
case class TurplePointInput[PaintType <: Paint](from: Point, to: Point, paint: PaintType)

private case class TurplePointHelper[PaintType <: Paint](from: Point = Point(0, 0), to: Point = Point(0, 0)) {
  private[command] def toInput(paint: PaintType) = TurplePointInput(from, to, paint)
}

private[command] class FromInput[Builder, PaintType <: Paint](commandParamCallback: TurplePointInput[PaintType] => Builder) {
  def from(value: Point) = new ToInput[Builder, PaintType](TurplePointHelper(from = value), commandParamCallback)
}

private[command] class ToInput[Builder, PaintType <: Paint](turple: TurplePointHelper[PaintType], commandParamCallback: TurplePointInput[PaintType] => Builder) {
  def to(value: Point) = new DrawInput[Builder, PaintType](turple.copy(to = value), commandParamCallback)
}

private[command] class DrawInput[Builder, PaintType<: Paint](turple: TurplePointHelper[PaintType], commandParamCallback: TurplePointInput[PaintType] => Builder) {
  def draw(value: PaintType) = commandParamCallback(turple.toInput(value))
}

sealed trait Paint
sealed trait Color extends Paint { def toChar: Char }
case object Red extends Color { override val toChar = 'r' }
case object Blue extends Color { override val toChar = 'b' }
case class CharColor(override val toChar:Char) extends Color
case class CharDot(value: Char) extends Paint

case class Size(width: Int, height: Int)
case class Point(x: Int, y: Int)
