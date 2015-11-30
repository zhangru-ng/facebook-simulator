package edu.ufl.dos15.fbapi

import scala.concurrent.duration.Duration
import spray.routing.Route
import spray.routing.directives.CachingDirectives._
import spray.routing.HttpService
import spray.http.StatusCodes

object PageService {
  case class Page (
    id: Option[String] = None,           // Page ID. No access token is required to access this field
    category: Option[String] = None,     // The Page's category. e.g. Product/Service, Computers/Technology
    description: Option[String] = None,  // The description of the Page
    link: Option[String] = None,         // The Page's Facebook URL
    name: Option[String] = None,         // The name of the Page
    username: Option[String] = None,     // The alias of the Page
    likes: Option[Int] = None,           // The number of users who like the Page
    location: Option[Location] = None,   // The location of this place. Applicable to all Places
    parent_page: Option[Page] = None)    // Parent Page for this Page

  case class Location (
    city: Option[String] = None,
    country: Option[String] = None,
    latitude: Option[Float] = None,
    longitude: Option[Float] = None,
    name: Option[String] = None,
    region: Option[String] = None,
    state: Option[String] = None,
    street: Option[String] = None,
    zip: Option[String] = None)
}

trait PageService extends HttpService with PerRequestFactory with Json4sProtocol {
  import PageService._

  val pageCache = routeCache(maxCapacity = 1000, timeToIdle = Duration("30 min"))

  val pageRoute: Route = {
    (path("page") & get) {
      complete(StatusCodes.OK)
    } ~
    (path("page") & post) {  // creates a page
      entity(as[Page]) { page =>
        ctx => handleRequest(ctx, Post(page))
      }
    } ~
    pathPrefix("page" / Segment) { id => // gets infomation about a page
      get {
        parameter('fields.?) { fields =>
          ctx => handleRequest(ctx, Get(id, fields))
        }
      }~
      put { // update a page
        entity(as[Page]) { values =>
          ctx => handleRequest(ctx, Put(id, values))
        }
      } ~
      delete { // delete a page
        ctx => handleRequest(ctx, Delete(id))
      }
    }
  }
}
