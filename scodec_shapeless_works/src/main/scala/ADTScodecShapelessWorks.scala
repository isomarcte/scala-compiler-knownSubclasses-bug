import scodec.Codec

/** A trivial type used to demonstrate the `knownDirectSubclasses` bug. */
sealed trait ADTScodecShapelessWorks

/** Contains all constructors for the type `ADT`. */
object ADTScodecShapelessWorks {

  /** A trivial constructor. */
  final case class ADTScodecShapelessWorksConstructor() extends ADTScodecShapelessWorks

  /** Unlike the example defined in `uses_macro`, this manifestation of the
    * bug will trigger even if there is an instance for the typeclass in
    * question defined on the type.
    *
    * For some reason, the Scodec/Shapeless based example ''always'' performs
    * implicit search and then macro expansion of the type, even if there is
    * an instance defined on the type's companion object. I am unsure why this
    * is the case.
    */
  implicit def codec: Codec[ADTScodecShapelessWorks] = {
    println("DERP!")
    null
  }
}
