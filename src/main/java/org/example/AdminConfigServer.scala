package org.example

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.config.server.EnableConfigServer
import de.codecentric.boot.admin.server.config.EnableAdminServer

@EnableAdminServer
@EnableConfigServer
@SpringBootApplication
class AdminConfigServer {

}

object AdminConfigServer extends App {
  SpringApplication.run(classOf[AdminConfigServer], args: _*)

  val demoInstance = new Demo()
  demoInstance.demo()

  class Demo {
    def demo() = {
      val result = calculateFactorial(200)
      println(result)
    }

    def calculateFactorial(value: Int): BigInt = {
      if (value == 0) 1 else value * calculateFactorial(value - 1)
    }
  }
}