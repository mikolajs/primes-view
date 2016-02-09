package pl.edu.osp

import akka.actor.{Actor, ActorSystem, Props}
import akka.util.Timeout
import akka.pattern.ask

import scala.concurrent.Future
import scala.concurrent.duration._

import scalafx.Includes._
import scalafx.geometry.Insets
import scalafx.scene.canvas.Canvas
import scalafx.scene.control.{Separator, Button}
import scalafx.scene.layout.{FlowPane, VBox, HBox}
import scalafx.scene.paint.Color
import scalafx.scene.paint.{Stops, LinearGradient}
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.paint.Color._
import scalafx.scene.text.Text


object Boot extends JFXApp {
  val system = ActorSystem("MySystem")
  val supervisor = system.actorOf(Props[PrimeMainActor], name = "primeMActor")


  implicit val timeout = Timeout(2.second)
  implicit val ec = system.dispatcher
  val f: Future[Any] = supervisor ? "start"
  f.onSuccess {
    case l:List[Int] => println(s"Will pay $l cents for a cappuccino")
  }

  val buttonCount = new Button {
    text = "Licz!"
    style = "-fx-font-size: 24pt"
    minHeight = 30
    minWidth = 100
    onAction = handle {countPrimes()}
  }
  val canvas = new Canvas(750, 400)
  val gc = canvas.graphicsContext2D
  gc.fill = Color.DARKKHAKI
  gc.fillRect(0, 0, canvas.width.get, canvas.height.get)

  println("Start Application")
  stage = new PrimaryStage {
    title.value = "Primary View"
    width = 800
    height = 600
    scene = new Scene {
      fill = Color.LIGHTGREEN
      content = new VBox {
        fill = Color.WHITE
        padding = Insets(10)
        children = new VBox {
          fill = Color.WHITE
          padding = Insets(10)
          children = Seq(
            new FlowPane {
              children = List(
                new Text {
                  text = "Liczby pierwsze"
                  style = "-fx-font-size: 24pt"
                  padding = Insets(10)
                  fill = new LinearGradient(
                    endX = 0,
                    stops = Stops(SEAGREEN, DARKGREEN))
                },
                new Separator {
                  minWidth = 100
                },
                buttonCount)
              minHeight = 100
              prefHeight = 100
              minWidth = 400
              prefWidth = 400
            },
            canvas
          )
        }
      }
    }
  }
  def countPrimes():Unit = {
    println("===================== Button clicked!!!!")
    gc.fill = Color.RED
    gc.fillRect(0, canvas.height.get - 250, 50, 250)
    gc.fillPath()
  }
}


 




