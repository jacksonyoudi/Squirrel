package org.tutorial.time

case class MyEvent(
                    createTime: Long,
                    userId: String,
                    desc: String,
                    amount: Int

                  ) {
  def add(other: MyEvent): MyEvent = {
    this.amount += other.amount
    this
  }
}
