import java.util.Properties
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.scaladsl.LoggerOps
import akka.actor.typed.{ ActorRef, ActorSystem, Behavior }
import akka.actor.typed.Terminated
import akka.actor.typed.scaladsl.Routers
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import akka.actor.typed.PostStop
import org.apache.kafka.clients.consumer.KafkaConsumer
import scala.collection.JavaConverters._

object ProducerActor {
    sealed trait Command
    case class Message(topic: String, key: String, message: String) extends Command

    def apply(config: Map[String, String]): Behavior[Command] = {
        val kafka = new KafkaProducer[String, String](KafkaHelper.toProperties(config))
        Behaviors.receive{ (context, message) => 
            message match {
                case Message(topic, key, message) => {
                    Thread.sleep(1000)
                    println(s"producer: $topic > $key > $message")
                    kafka.send(new ProducerRecord[String, String](topic, key, message))
                    Behaviors.same
                }
                case _ => ???
            }
        }
    }
}