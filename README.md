# `knownDirectSubclasses` bug #

This project is a minimal example of a bug in the Scala compiler where [`knownDirectSubclasses`][knownDirectSubclasses] is being invoked before all the sub-classes for a given type have been registered.

# Background #

The general issue has been documented before as part of [SI-7046][SI-7046]. Although it was thought to have been _mostly_ resolved by [PR 5284][PR-5284]. This PR _does_ list a few known exceptional cases where the bug will still manifest, but the cases presented in this project do not appear to fall in line with the exceptions noted in that PR (at least to me).

# Overview #

Here is what is happening.

Consider some `sealed` type `T` that defines all sub-classes in the companion object for the type.

```scala
sealed trait T

object T {

  final case class TImpl() extends T
}
```

Consider some type-class `F[_]` which can derive instances for certain types, assuming certain requirements for `T` are met. The validation of these requirements and the derivation code is done using a macro. This macro invokes [`knownDirectSubclasses`][knownDirectSubclasses] at some point.

```scala
trait F[T]

object F {

  def instanceImpl[T : c.WeakTypeTag](
    c: Context
  ): c.Expr[F[T]] = {
    import c.universe._
    val wtt: c.WeakTypeTag[T] = implicitly[c.WeakTypeTag[T]]

    wtt.tpe.typeSymbol.asClass.knownDirectSubclasses.toString

    reify(new F[T]{})
  }

  implicit def instance[T]: F[T] = macro instanceImpl[T]
}
```

Finally, consider a function `foo` which uses `T` and requires that `T` have an instances of `F`.

```scala
object Foo {

    def foo[T : F]: Unit = ()

    foo[T]
}
```

If the `T` is defined in a file whose filename has a lexicographical ordering which places it _after_ the file in which `foo` is defined _and_ `T` highest ranking implicit instance for `F` is the macro derived instance, then the following will occur.

* The call to [`knownDirectSubclasses`][knownDirectSubclasses] in the macro will fail to see any sub-classes of `T`
* When the compiler type-checks `T` it will raise N [`globalError` invocations][globalError], where N is the exactly equal to the number of sub-classes for `T`.

If the file in which `T` is defined has a lexicographical ordering which places it _before_ the file in which `foo` is defined, everything works great.

# Example Projects #

* `macro` - This project provides a trivial type class for any type. Instances are derived with a macro which always invokes [`knownDirectSubclasses`][knownDirectSubclasses].
* `uses_macro` - This defines a type `ADT` and uses it in `AAA_UsesMacro.scala`. This project fails.
* `uses_macro_works` - This defines a type `ADTWorks` and uses it in `UsesMacro.scala`. This project works.
* `scodec_shapeless` - This defines a type `ADTScodecShapeless` and uses it in `AAA_UsesMacro.scala`. This is a more real world example and demonstrates on property of this bug which I was unable to reproduce in `uses_macro`. In `uses_macro`, no error will be raised if `ADT` defines an instance for `FakeTypeClassCallsKnownDirectSubclasses` on the companion object. However, in this `scodec_shapeless` the error is raised even if there is a `Codec` instance defined on the companion object for `ADTScodecShapeless`. It should be noted that the derived instance which is triggering the failure is not the one which is used if the filenames are ordered differently, as can be seen in `scodec_shapeless_works` bu running the `Main` class. It prints `"DERP!"` if the instance defined on `ADTScodecShapelessWorks` is the one which is used.
* `scodec_shapeless_works` - This renames `AAA_UsesMacro.scala` to `UsesMacro.scala` and works.

# Running The Example #

I wasn't sure how to get `sbt` to continue build other cross builds after one of them fails, so a file `build.sh` is provided which will build all these projects for Scala versions `2.11.9`-`2.13.0-M2`. Note, the bug still appears to be present _before_ `2.11.9`, but the code which raises the error was only added in `2.11.9`. Nevertheless, you can observe the bug by noticing the known sub-classes of printed when the macro is executed.

```bash
$ bash build.sh
```

[knownDirectSubclasses]: https://github.com/scala/scala/blob/v2.12.3/src/reflect/scala/reflect/internal/Symbols.scala#L126 "knownDirectSubclasses"

[SI-7046]: https://issues.scala-lang.org/browse/SI-7046 "SI-7046"

[PR-5284]: https://github.com/scala/scala/pull/5284 "Scala PR 5284"

[globalError]: https://github.com/scala/scala/blob/v2.12.3/src/reflect/scala/reflect/internal/Symbols.scala#L3371 "globalError"
