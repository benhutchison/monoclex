package monoclex

import cats._
import cats.data._
import cats.implicits._

import monocle._
import monocle.function._
import monocle.function.all._

trait ELens[F[_], S, A] {

  def get(s: S): F[A]

  def set(a: A)(s: S): F[S]

  def compose[B](lab: ELens[F, A, B])(implicit M: Monad[F]): ELens[F, S, B] = ELens.compose(this, lab)

}

object ELens {

  def compose[F[_]: Monad, S, A, B](l1: ELens[F, S, A], l2: ELens[F, A, B]): ELens[F, S, B] = new ELens[F, S, B] {

    def get(s: S): F[B] = for {
      a <- l1.get(s)
      b <- l2.get(a)
    } yield b

    def set(b: B)(s: S): F[S] = for {
      a <- l1.get(s)
      a2 <- l2.set(b)(a)
      s2 <- l1.set(a2)(s)
    } yield s2

  }

}


trait LensSyntax {
  implicit final def lensSyntaxMonoclex[S, A](l: Lens[S, A]): LensOps[S, A] = new LensOps(l)
}

final class LensOps[S, A](val l: Lens[S, A]) extends AnyVal {

  def toELens: ELens[Id, S, A] = new ELens[Id, S, A] {

    def get(s: S): Id[A] = l.get(s)

    def set(a: A)(s: S): Id[S] = l.set(a)(s)

  }

}
