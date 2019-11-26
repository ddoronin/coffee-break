package chat

import akka.actor.typed.Behavior
import akka.actor.typed.javadsl.Behaviors
import java.net.URLEncoder
import akka.actor.typed.ActorRef
import chat.Api.PostMessage

final case class Notify(token: String, message: String) extends Api.RoomCommand

object ChatRoom {
    def apply(sessions: List[(String, ActorRef[Api.SessionCommand])] = Nil): Behavior[Api.RoomCommand] = {
        Behaviors.receive{(context, message) => {
            message match {
                case Api.GetSession(token, client) => {
                    val session = context.spawn(Session(token, context.getSelf), URLEncoder.encode(token))
                    client ! Api.SessionGranted(handle = session)
                    apply((token, session)::sessions)
                }
                case Notify(token, message) => {
                    sessions.foreach{
                        case (_, session) => session ! Api.ReceivedMessage(token, message)
                    }
                    Behaviors.same
                }
            }
        }}
    }
}

object Session {
    def apply(token: String, chatRoom: ActorRef[Api.RoomCommand]): Behavior[Api.SessionCommand] = {
        Behaviors.receiveMessage {
            case PostMessage(message) => {
                println(s"$token > $message")
                chatRoom ! Notify(token, message)
                Behaviors.same
            }
            case Api.ReceivedMessage(somebody, message) => {
                println(s"$token <<< $somebody: $message")
                Behaviors.same
            }
            case _ => ???
        }
    }
}