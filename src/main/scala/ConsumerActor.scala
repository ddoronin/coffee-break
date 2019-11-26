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

object ConsumerActor {
    sealed trait Command
    //case class Message(topic: String, key: String, message: String) extends Command
    case class Subscribe(topic: String) extends Command

    def apply(config: Map[String, String]): Behavior[Command] = {
        val kafka = new KafkaConsumer[String, String](KafkaHelper.toProperties(config))
        Behaviors.receive{ (context, message) => 
            message match {
                case Subscribe(topic) => {
                    println(s"subscribe: $topic")
                    kafka.subscribe(java.util.Arrays.asList(topic))
                    while (true) {
                        val record = kafka.poll(1000).asScala
                        for (data <- record.iterator) {
                            println(s"data $topic ${data.key} ${data.value()}")
                            //replyTo ! Message(topic, data.key(), data.value())
                        }
                    }
                    Behaviors.same
                }
                case _ => ???
            }
        }
    }
}