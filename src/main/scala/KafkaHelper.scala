import java.util.Properties
import scala.collection.JavaConverters._

object KafkaHelper {
    lazy val producerConfig = Map[String, String](
        "bootstrap.servers" -> "localhost:9092",
        "key.serializer" -> "org.apache.kafka.common.serialization.StringSerializer",
        "value.serializer" -> "org.apache.kafka.common.serialization.StringSerializer"
    )

    lazy val consumerConfig = Map[String, String](
        "bootstrap.servers"-> "localhost:9092",
        "key.deserializer"-> "org.apache.kafka.common.serialization.StringDeserializer",
        "value.deserializer"-> "org.apache.kafka.common.serialization.StringDeserializer",
        "auto.offset.reset"-> "latest",
        "group.id"-> "consumer-group"
    )

    def toProperties(config: Map[String, String]): Properties = {
        val props = new Properties()
        config.foreach{
          case (key, value) => props.put(key, value)
        }
        props
    }
}