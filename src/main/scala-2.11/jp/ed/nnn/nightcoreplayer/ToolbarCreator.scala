package jp.ed.nnn.nightcoreplayer

import java.lang
import javafx.beans.value.{ChangeListener, ObservableValue}
import javafx.event.{ActionEvent, EventHandler}
import javafx.geometry.Pos
import javafx.scene.control.{Label, TableView}
import javafx.scene.layout.HBox
import javafx.scene.media.MediaView
import javafx.scene.Scene
import javafx.stage.Stage
import javafx.util.Duration

import jp.ed.nnn.nightcoreplayer.SizeConstants._
import jp.ed.nnn.nightcoreplayer.ToolbarButtonCreator.createButton


object ToolbarCreator {

  def create(mediaView: MediaView, tableView: TableView[Movie], timeLabel: Label, scene: Scene, primaryStage: Stage): HBox = {

    val toolBar = new HBox()
    toolBar.setMinHeight(toolBarMinHeight)
    toolBar.setAlignment(Pos.CENTER)
    toolBar.setStyle("-fx-background-color: Black")

    // firstButton
    val firstButton = createButton("first.png", new EventHandler[ActionEvent]() {
      override def handle(event: ActionEvent): Unit = {
        if (mediaView.getMediaPlayer != null) {
          MoviePlayer.playPre(tableView, mediaView, timeLabel)
        }
      }
    })


    // back button
    val backButton = createButton("back.png", new EventHandler[ActionEvent]() {
      override def handle(event: ActionEvent): Unit =
        if (mediaView.getMediaPlayer != null) {
          mediaView.getMediaPlayer.seek(
            mediaView.getMediaPlayer.getCurrentTime.subtract(new Duration(10000)))
        }
    })

    // play button
    /*
    画像のインスタンスを作成し、皿にその後ボタンのインスタンスを作成した後、setGraphicで
    ボタンと画像を関連付ける。それ以下はボタンのスタイルを設定している
     */
    val playButton = createButton("play.png", new EventHandler[ActionEvent]() {
      override def handle(event: ActionEvent): Unit = {
        // MediaPlayerのインスタンスが既にMediaView内にあり、選択が空でないときのみ、playメソッドが実行される
        val selectionModel = tableView.getSelectionModel
        if (mediaView.getMediaPlayer != null && !selectionModel.isEmpty) {
          mediaView.getMediaPlayer.play()
        }
      }
    })

    // pause button
    val pauseButton = createButton("pause.png", new EventHandler[ActionEvent]() {
      override def handle(event: ActionEvent): Unit = {
        // MediaPlayerのインスタンスがある時のみ、playerを停止する
        if (mediaView.getMediaPlayer != null) mediaView.getMediaPlayer.pause()
      }
    })

    // forward button
    val forwardButton = createButton("forward.png", new EventHandler[ActionEvent]() {
      override def handle(event: ActionEvent): Unit =
        if (mediaView.getMediaPlayer != null) {
          /*
          mediaView.getMediaPlayer.seek(
            mediaView.getMediaPlayer.getCurrentTime.add(new Duration(10000)))
          */
          val player = mediaView.getMediaPlayer
          if (player.getTotalDuration.greaterThan(player.getCurrentTime.add(new Duration(10000)))) {
            player.seek(player.getCurrentTime.add(new Duration(10000)))
          }
        }
    })

    // lastButton
    val lastButton = createButton("last.png", new EventHandler[ActionEvent]() {
      override def handle(event: ActionEvent): Unit = {
        if (mediaView.getMediaPlayer != null) {
          MoviePlayer.playNext(tableView, mediaView, timeLabel)
        }
      }
    })

    // fullscreenButton
    val fullscreenButton = createButton("fullscreen.png", new EventHandler[ActionEvent]() {
      override def handle(event: ActionEvent): Unit = {
        primaryStage.setFullScreen(true)
        mediaView.fitHeightProperty().unbind()
        mediaView.fitHeightProperty().bind(scene.heightProperty())
        mediaView.fitWidthProperty().unbind()
        mediaView.fitWidthProperty().bind(scene.widthProperty())
      }
    })

    primaryStage.fullScreenProperty().addListener(new ChangeListener[lang.Boolean] {
      override def changed(observable: ObservableValue[_ <: lang.Boolean], oldValue: lang.Boolean, newValue: lang.Boolean): Unit =
        if (!newValue) {
          mediaView.fitHeightProperty().unbind()
          mediaView.fitWidthProperty().unbind()
          mediaView.fitHeightProperty().bind(scene.heightProperty().subtract(toolBarMinHeight))
          mediaView.fitWidthProperty().bind(scene.widthProperty().subtract(tableMinWidth))
        }
    })

    /*
    toolBar.getChildren.addAll(
      firstButton, backButton, playButton, pauseButton, forwardButton, lastButton, fullscreenButton, timeLabel)
    */
    toolBar.getChildren.addAll(
      firstButton, backButton, playButton, pauseButton, forwardButton, lastButton, fullscreenButton)

    toolBar
  }


}