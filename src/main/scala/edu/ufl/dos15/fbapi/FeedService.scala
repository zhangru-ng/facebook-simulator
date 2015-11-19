package edu.ufl.dos15.fbapi

import scala.concurrent.duration.Duration
import spray.routing.Route
import spray.routing.directives.CachingDirectives._
import spray.http.MediaTypes
import spray.http.HttpResponse
import spray.routing.HttpService

import UserService.User
import PageService.Page

object FeedService {
    case class Feed (
        id: String,            // The post ID
        caption: String,       // The caption of a link in the post (appears beneath the name).
        created_time: String,  // The time the post was initially published.
        description: String,   // A description of a link in the post (appears beneath the caption).
        from: User,            // Information about the profile that posted the message.
        icon: String,          // A link to an icon representing the type of this post.
        is_hidden: Boolean,    // If this post is marked as hidden (applies to Pages only).
        link: String,          // The link attached to this post.
        message: String,       // The status message in the post.
        name: String,          // The name of the link.
        object_id: String,     // The ID of any uploaded photo or video attached to the post.
        picture: String,       // The picture scraped from any link included with the post.
        place: Page,           // Any location information attached to the post.
        privacy: String,       // The privacy settings of the post.
        source: String,        // A URL to any Flash movie or video file attached to the post.
        status_type: StatusType.Value,  // Description of the type of a status update.
        story: String,         // Text from stories not intentionally generated by users
        targeting: Any,        // Object that limited the audience for this content.
        to: Array[User],       // Profiles mentioned or targeted in this post.
        feed_type: FeedType.Value,    // A string indicating the object type of this post.
        updated_time: String,    // The time of the last change to this post, or the comments on it.
        with_tags: Array[User])  // Profiles tagged as being 'with' the publisher of the post.

    object StatusType extends Enumeration {
        type StatusType = Value
        val MOBILE_STATUS_UPDATE,
            CREATED_NOTE,
            ADDED_PHOTOS,
            ADDED_VIDEO,
            SHARED_STORY,
            CREATED_GROUP,
            CREATED_EVENT,
            WALL_POST,
            APP_CREATED_STORY,
            PUBLISHED_STORY,
            TAGGED_IN_PHOTO,
            APPROVED_FRIEND = Value
    }

    object FeedType extends Enumeration {
         type FeedType = Value
         val LINK,
             STATUS,
             PHOTO,
             VIDEO,
             OFFER,
             EVENT = Value
    }
}

trait FeedService extends HttpService {

    val feedCache = routeCache(maxCapacity = 1000, timeToIdle = Duration("30 min"))

    val feedRoute: Route = respondWithMediaType(MediaTypes.`application/json`) {
        pathPrefix("photo") {
          get {
            cache(feedCache) {
              complete("Get")
              // TODO Get Request
            }
          } ~
          post {
            complete("Post")
            // TODO Post Request
          } ~
          delete {
            complete("Deleted")
            // TODO Delete Request
          }
        }
    }
}
