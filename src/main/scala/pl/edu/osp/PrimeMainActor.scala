package pl.edu.osp

import akka.actor.{Props, ActorRef, Actor, OneForOneStrategy}
import akka.actor.SupervisorStrategy._
import scala.concurrent.duration._

case class Repeat(n:Int)
case class Next()
case class Finish(l:List[Int])

class PrimeMainActor extends Actor  {
  var j = 3
  val ref:Array[ActorRef] = new Array[ActorRef](15)
  val primesCont = new Array[Int](15)
  for(i <- 0 to 14) ref(i) = context.actorOf(Props(classOf[PrimeActor], i, i*5000 + 5000))
  var last = 0
    def receive = {
      case (n:Int, id:Int) => {
        if(n / 1000 > last  )  {
          last = (n / 1000L).toInt
          sendNext(1000)
        }
        ref(id +1) ! n
      }
      case Counted(n, id) => {
        primesCont(id) = n
        println(s"Size of $id is $n")
      }
    }
  override def postStop(): Unit = {

    println("Supervisor zatrzymany")
  }
  def sendNext(num:Int): Unit = {
    for(i <- 1 to num) {
      if(!(j > 14*5000 + 5000)) {
        ref(0) ! j
        j += 2
      }
      else mkStop()
    }
  }

  def mkStop(): Unit = {
    println("Terminate actors system")
    context.parent ! List(1,2,3,4,5)
    Thread.sleep(10000)
    ref.foreach(r => context.stop(r))
    context.system.terminate()
    context.stop(self)
  }
}
