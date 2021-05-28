package youdi.may.ch05.sink

import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.sink.{RichSinkFunction, SinkFunction}
import youdi.may.ch05.MySQLSource.{conn, statement}
import youdi.may.ch05.Student

import java.sql.{Connection, DriverManager, PreparedStatement}


object MySqlSink extends RichSinkFunction[Student] {
  private var flag: Boolean = true
  private var conn: Connection = null
  private var statement: PreparedStatement = null

  override def invoke(value: Student, context: SinkFunction.Context): Unit = {
    statement.setInt(1, value.id)
    statement.setString(2, value.name)
    statement.setInt(3, value.age)

    statement.execute()

  }

  override def open(parameters: Configuration): Unit = {
    conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bigdata", "root", "root")
    val sql: String = "insert into `student` (`id`,`name`,`age`) values (?,?,?);"
    statement = conn.prepareStatement(sql)

  }

  override def close(): Unit = {
    if (statement != null) {
      statement.close()
    }

    if (statement != null) {
      conn.close()
    }
  }
}
