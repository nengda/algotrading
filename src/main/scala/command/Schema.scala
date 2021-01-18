package command

/**
 *  Represents a type of CommandParam which can be used to
 *  define the schema of CommandTarget
 */
trait Schema {
  type Prepend[P <: CommandParam] <: Schema
  type Param <: CommandParam
  type Handler <: CommandHandler
  type Builder <: CommandBuilder
}

/**
 *  Syntax sugar to help composing Schema. For example
 *  A :: B :: C :: Nil = Schemas[A, Schemas[B, Schemas[C, Nil]]]
 */
object Schema {
  type ::[H <: CommandParam, T <:Schema] = T#Prepend[H]
}

/**
 *  Represents on empty schema
 */
trait Nil extends Schema {
  type Prepend[P <: CommandParam] = Schemas[P, Nil]
  type Param = CommandParam
  type Handler = CommandHandler
  type Builder = CommandBuilder
}

/**
 *  Composition of schemas
 */
trait Schemas[H <: CommandParam, T <: Schema] extends Schema {
  type Prepend[P <: CommandParam] = Schemas[P, Schemas[H, T]]
  type Param = H with T#Param
  type Handler = H#Handler with T#Handler
  type Builder = H#Builder with T#Builder
}