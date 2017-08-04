/** A trivial type used to demonstrate the `knownDirectSubclasses` bug. */
sealed trait ADTWorks

/** Contains all constructors for the type `ADT`. */
object ADTWorks {

  /** A trivial constructor. */
  final case class ADTWorksConstructor() extends ADTWorks
}
