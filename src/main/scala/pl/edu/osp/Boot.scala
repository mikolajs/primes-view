package pl.edu.osp


import akka.actor.{ ActorSystem, Props}
import akka.util.Timeout
import akka.pattern.ask

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

import scalafx.Includes._
import scalafx.geometry.Insets
import scalafx.scene.canvas.Canvas
import scalafx.scene.control.{Label, Separator, Button, Spinner}
import scalafx.scene.layout.{FlowPane, VBox}
import scalafx.scene.paint.Color
import scalafx.scene.paint.{Stops, LinearGradient}
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.paint.Color._
import scalafx.scene.text.{TextAlignment, Font, Text, FontWeight}


object Boot extends JFXApp {

  val buttonCount = new Button {
    text = "Licz!"
    style = "-fx-font-size: 24pt"
    minHeight = 30
    minWidth = 100
    onAction = handle {countPrimes()}
  }

  val canvas = new Canvas(750, 400)
  val gc = canvas.graphicsContext2D
  gc.fill = Color.DarkKhaki
  gc.fillRect(0, 0, canvas.width.get, canvas.height.get)

  val spinner = new Spinner[Int](1000, 50000, 1000, 1000) {
    styleClass += Spinner.StyleClassArrowsOnRightHorizontal
    minWidth = 200
    prefHeight = 30
  }

  val descript = (0 to 14 ).toList.map(x => {
    new Label("") {
      style = "-fx-font-size: 10pt"
      minWidth = 50
      textAlignment = TextAlignment.Center
    }
  })
  val piks = (0 to 14 ).toList.map(x => {
    new Label("") {
      minWidth = 50
      style = "-fx-font-size: 10pt"
      textAlignment = TextAlignment.Center
    }
  })

  println("Start Application")
  stage = new PrimaryStage {
    title.value = "Primary View"
    width = 800
    height = 620
    scene = new Scene {
      fill = Color.LightGreen
      content = new VBox {
        fill = Color.White
        padding = Insets(10)
        children = new VBox {
          fill = Color.White
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
                    stops = Stops(SeaGreen, DarkGreen))
                },
                new Separator {
                  minWidth = 50
                },
                spinner,
                new Separator {
                  minWidth = 50
                },
                buttonCount)
              minHeight = 100
              prefHeight = 100
              minWidth = 400
              prefWidth = 400
            },
            new FlowPane {
              children = piks
            },
            canvas,
            new FlowPane {
              children = descript
            }
          )
        }
      }
    }
  }
  def countPrimes():Unit = {

    val system = ActorSystem("MySystem")
    val many = spinner.value.value
    val mainActor = system.actorOf(Props(classOf[PrimeMainActor], many ), name = "primeMActor")
    var j = 3
    val max = many*15
    while (j < max){
      mainActor ! j
      j += 2
    }
    mainActor ! 999983
    implicit val timeout = Timeout(20 seconds)
    val future: Future[Array[Int]] = ask(mainActor, Result).mapTo[Array[Int]]
    val result = Await.result(future, 10 second)
    var k = 1
    descript.foreach(d => {
      d.text = (many*k).toString
      k += 1
    })
      k = 0
    piks.foreach(d => {
      d.text = result(k).toString
      k += 1
    })
    println("Boot receive data: " + result.mkString(", "))
    val colors = Array(Color.Red, Color.Blue, Color.Silver, Color.Fuchsia, Color.Green,
    Color.RoyalBlue, Color.Chocolate, Color.Cyan, Color.Wheat, Color.Tan,
    Color.BlueViolet, Color.Yellow, Color.Brown, Color.HotPink, Color.ForestGreen)
    var i = 0
    val maxi = result.max.toDouble
    println("MAXI = " + maxi)
    for(r <- result) {
      gc.fill = colors(i)
      val h = 400.0*r/maxi
      gc.fillRect(i*50, canvas.height.get - h, 50, h)
      gc.fillPath()

      i += 1
    }
  }
}


 




