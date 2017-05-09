package org.ausdigital.apecconnect.common.query

import play.api.libs.json._

/**
  * ng-admin compatible Query.
  * @param page the page of results that have been requested.
  * @param perPage the number of results per page that should be returned.
  * @param sortField the field to use to sort the results.
  * @param sortOrder the direction in which the sort should be applied.
  * @param filters the filters to use when querying.
  */
final case class Query(page: Option[Int], perPage: Option[Int], sortField: Option[String], sortOrder: Option[String], filters: JsObject)

/**
  * Result of executing a Query.
  *
  * @param results the results returned by the execution.
  * @param total the total of number of results.
  * @tparam A the type of results.
  */
final case class QueryResults[A](results: Seq[A], total: Int)

object QueryResults {
  implicit def writes[A](implicit dataWrites: Writes[A]): Writes[QueryResults[A]] = new Writes[QueryResults[A]] {
    override def writes(o: QueryResults[A]): JsValue = {
      JsArray(o.results.map(dataWrites.writes))
    }
  }
}