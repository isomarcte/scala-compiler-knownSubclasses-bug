import scala.reflect.runtime.universe.{TypeTag => TT}
import scala.reflect.macros.blackbox.Context
import scala.language.experimental.macros

/** A fake typeclass which doesn't do anything. */
trait FakeTypeClassCallsKnownDirectSubclasses[T]

/** Provides instances of [[FakeTypeClassCallsKnownDirectSubclasses]]. */
object FakeTypeClassCallsKnownDirectSubclasses {

  /** This macro creates trivial instances of
    * [[FakeTypeClassCallsKnownDirectSubclasses]] for any given type.
    *
    * Notable, it invokes `knownDirectSubclasses`
    * (https://github.com/scala/scala/blob/v2.12.3/src/reflect/scala/reflect/internal/Symbols.scala#L126). If
    * this is done for some type `T`, before all the subclasses for `T` have
    * been registered with the compiler, then a `globalError` is raised here
    * https://github.com/scala/scala/blob/v2.12.3/src/reflect/scala/reflect/internal/Symbols.scala#L3371
    */
  def instanceImpl[T : c.WeakTypeTag](
    c: Context
  ): c.Expr[FakeTypeClassCallsKnownDirectSubclasses[T]] = {
    import c.universe._
    val wtt: c.WeakTypeTag[T] = implicitly[c.WeakTypeTag[T]]

    /* When this is invoked it will show you any subclasses which ''have''
     * been registered.
     */
    if (wtt.tpe.typeSymbol.isClass) {
      println(s"Known Subclasses for ${wtt.tpe.toString} ${wtt.tpe.typeSymbol.asClass.knownDirectSubclasses.toString}")
    }

    reify(new FakeTypeClassCallsKnownDirectSubclasses[T]{})
  }

  /** Create a trivial instance of [[FakeTypeClassCallsKnownDirectSubclasses]]
    * for any type `T`
    */
  implicit def instance[T]: FakeTypeClassCallsKnownDirectSubclasses[T] = macro instanceImpl[T]
}
