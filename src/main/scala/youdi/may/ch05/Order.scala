package youdi.may.ch05

case class Order(
                  id: String,
                  userId: Int,
                  money: Double,
                  createTime: Long,
                ) extends Serializable
