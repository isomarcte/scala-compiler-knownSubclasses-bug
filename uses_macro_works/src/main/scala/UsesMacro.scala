/** This version UsesMacro ''does not'' trigger the `globalError` on
  * https://github.com/scala/scala/blob/v2.12.3/src/reflect/scala/reflect/internal/Symbols.scala#L3371.
  *
  * The only difference between these two projects is the name of this file
  * and the name of the type `ADTWorks`. Only the name of the file is
  * significant to the bug. `ADT` was renamed so that it is easy to
  * distinguish which project is triggering the console message about the
  * observed sub-classes.
  */
object UsesMacro {

  /** Do nothing, but do ensure that `T` has an implicit instance of
    * `FakeTypeClassCallsKnownDirectSubclasses`.
    */
  def callMacro[T : FakeTypeClassCallsKnownDirectSubclasses]: Unit = ()

  /* Invoke [[AAA_UsesMacro#callMacro]] */
  callMacro[ADTWorks]
}
