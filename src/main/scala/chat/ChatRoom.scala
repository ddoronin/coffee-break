package chat

import akka.actor.typed.Behavior
import akka.actor.typed.javadsl.Behaviors
import java.net.URLEncoder
import akka.actor.typed.ActorRef
import chat.Api.PostMessage

object ChatRoom {
    def apply(sessions: List[ActorRef[Api.SessionCommand]] = Nil): Behavior[Api.RoomCommand] = {
        Behaviors.receive{(context, message) => {
            message match {
                case Api.GetSession(token, client) => {
                    val session = context.spawn(Session(token), URLEncoder.encode(token))
                    client ! Api.SessionGranted(handle = session)
                    apply(session::sessions)
                }
            }
        }}
    }
}

object Session {
    def apply(token: String): Behavior[Api.SessionCommand] = {
        Behaviors.receive{(context, message) => {
            message match {
                case PostMessage(message) => {
                    println(s"$token > $message")
                    Behaviors.same
                }
                case _ => ???
            }
        }}
    }
}