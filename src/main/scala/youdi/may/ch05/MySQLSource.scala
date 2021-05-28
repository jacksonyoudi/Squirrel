package youdi.may.ch05

import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.source.{RichParallelSourceFunction, SourceFunction}

import java.sql.{Connection, DriverManager, PreparedStatement, ResultSet, Time}

object MySQLSource extends RichParallelSourceFunction[Student] {
  private var flag: Boolean = true
  private var conn: Connection = null
  private var statement: PreparedStatement = null

  // 开启资源， 只是执行一次
  override def open(parameters: Configuration): Unit = {
    conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bigdata", "root", "root")
    val sql: String = "select id,name,age from student"
    statement = conn.prepareStatement(sql)
  }


  override def run(ctx: SourceFunction.SourceContext[Student]): Unit = {
    while (flag) {
      val rs: ResultSet = statement.executeQuery()
      while (rs.next()) {
        val id: Int = rs.getInt("id")
        val name: String = rs.getString("name")
        val age: Int = rs.getInt("age")

        ctx.collect(Student(id, name, age))
      }
      Thread.sleep(5000)
    }
  }

  // 取消
  override def cancel(): Unit = {
    flag = false

  }


  // 需要处理异常
  override def close(): Unit = {
    if (statement != null) {
      statement.close()
    }

    if (statement != null) {
      conn.close()
    }
  }
}
