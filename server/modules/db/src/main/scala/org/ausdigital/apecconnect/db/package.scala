package org.ausdigital.apecconnect.db

import slick.dbio.DBIO

import scala.concurrent.ExecutionContext
import scalaz.{ Functor, Monad }

/**
 * Additional implicit convertors and combinators for [[DBIO]] instances.
 */
object DbioOps {

  import scalaz._

  implicit def dbioMonad(implicit executionContext: ExecutionContext): Functor[DBIO] with Monad[DBIO] = new DBIOMonad

  def dbioSomeT[A](dbio: DBIO[A])(implicit executionContext: ExecutionContext): OptionT[DBIO, A] = OptionT[DBIO, A](dbio.map(Option.apply))

}

/**
 * Defines [[Monad]] for [[DBIO]].
 */
class DBIOMonad(implicit ec: ExecutionContext) extends Functor[DBIO] with Monad[DBIO] {
  def point[A](a: => A): DBIO[A] = DBIO.successful(a)

  def bind[A, B](fa: DBIO[A])(f: A => DBIO[B]): DBIO[B] = fa.flatMap(f)
}