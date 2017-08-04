/** A trivial type used to demonstrate the `knownDirectSubclasses` bug. */
sealed trait ADT

/** Contains all constructors for the type `ADT`. */
object ADT {

  /** A trivial constructor. */
  final case class ADTConstructor() extends ADT

  // Note, if you define an instance for
  // FakeTypeClassCallsKnownDirectSubclasses here, then the bug will not
  // occur. This is not always the case, see the Scodec/Shapeless example for
  // an instance where the bug is raised even if there is a instance of the
  // typeclass on the companion object of the given type.
  //
  // implicit def fakeInstance: FakeTypeClassCallsKnownDirectSubclasses[T] = null
}
