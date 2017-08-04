import scodec.Codec

/** Defines a method `callMacro` to trigger
  * https://github.com/scala/scala/blob/v2.12.3/src/reflect/scala/reflect/internal/Symbols.scala#L3371 .
  *
  * @note In this example, the error is ''not'' generated if the type `T` has
  *       another implicit instance which ranks higher than the macro
  *       generated one defined on `FakeTypeClassCallsKnownDirectSubclasses`.
  *
  * @note This error is dependent on the lexicographical ordering of the
  *       files. Both this file and the type in question, `ADT` in our case,
  *       must be compiled together ''and'' the file which ''calls''
  *       `FakeTypeClassCallsKnownDirectSubclasses#instance` must be
  *       ordered ''before'' the type in question.
  *       In our case, the type is `ADT` in file `ADT.scala` and it is used in
  *       `UsesMacro` in `AAA_UsesMacro.scala`. The `AAA` prefix is
  *       intentional to have it be ordered before `ADT.scala`. If you rename
  *       the file to `UsesMacro.scala` then you will not get the
  *       `globalError` defined at
  *       https://github.com/scala/scala/blob/v2.12.3/src/reflect/scala/reflect/internal/Symbols.scala#L3371
  *       .
  */
object UsesMacro {

  /** Do nothing, but do ensure that `T` has an implicit instance of
    * `FakeTypeClassCallsKnownDirectSubclasses`.
    */
  def callMacro[T : Codec]: Unit = ()

  /* Invoke [[AAA_UsesMacro#callMacro]] */
  def derp(): Unit = {
    callMacro[ADTScodecShapelessWorks]
  }
}
