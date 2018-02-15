package jp.ed.nnn.nightcoreplayer

import javafx.application.Application
import javafx.collections.FXCollections
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.control._
import javafx.scene.input.MouseEvent
import javafx.scene.layout.BorderPane
import javafx.scene.media.MediaView
import javafx.scene.paint.Color
import javafx.stage.Stage
import javafx.util.Callback

import jp.ed.nnn.nightcoreplayer.SizeConstants._


object Main extends App {
  Application.launch(classOf[Main], args: _*)
}

class Main extends Application {

  override def start(primaryStage: Stage): Unit = {
    val mediaView = new MediaView()

    val timeLabel = new Label()
    timeLabel.setText("00:00:00/00:00:00")
    timeLabel.setTextFill(Color.WHITE)

    val tableView = new TableView[Movie]()
    tableView.setMinWidth(tableMinWidth)
    val movies = FXCollections.observableArrayList[Movie]()
    tableView.setItems(movies)
    tableView.setRowFactory(new Callback[TableView[Movie], TableRow[Movie]]() {
      override def call(param: TableView[Movie]): TableRow[Movie] = {
        val row = new TableRow[Movie]()
        row.setOnMouseClicked(new EventHandler[MouseEvent] {
          override def handle(event: MouseEvent): Unit = {
            if (event.getClickCount >= 1 && !row.isEmpty) {
              MoviePlayer.play(row.getItem, tableView, mediaView, timeLabel)
            }
          }
        })
        row
      }
    })


    val fileNameColumn = new TableColumn[Movie, String]("ファイル名")
    fileNameColumn.setCellValueFactory(new PropertyValueFactory("fileName"))
    fileNameColumn.setPrefWidth(160)
    val timeColumn = new TableColumn[Movie, String]("時間")
    timeColumn.setCellValueFactory(new PropertyValueFactory("time"))
    timeColumn.setPrefWidth(80)
    val deleteActionColumn = new TableColumn[Movie, Long]("削除")
    deleteActionColumn.setCellValueFactory(new PropertyValueFactory("id"))
    deleteActionColumn.setPrefWidth(60)
    deleteActionColumn.setCellFactory(new Callback[TableColumn[Movie, Long], TableCell[Movie, Long]]() {
      override def call(param: TableColumn[Movie, Long]): TableCell[Movie, Long] = {
        new DeleteCell(movies, mediaView, tableView)
      }
    })

    tableView.getColumns.setAll(fileNameColumn, timeColumn, deleteActionColumn)


    /*
リファクタリング
 - 外部から見たときの振る舞いを保ちつつ、理解や修正が簡単になるようにソフトウェアの内部構造を変化させること

なぜリファクタリングが必要か
 - 読みにくいプログラムは変更しにくい
 - ロジックが重複しているプログラムは変更しにくい
 - 機能追加に伴い、既存のコード修正が必要になるプログラムは修正しにくい
 - 複雑な条件分k位の多いプログラムは修正しにくい

リファクタリングの手法
 - メソッドの抽出
 - クラスの抽出
 - データの再構成
 - メソッド名の変更

まとめ
- コピー & ペーストを使えば簡単に機能追加できるが、そのせいでできてしまうコードの重複はメンテナンス性を悪くする
- リファクタリングは、外部から見たときの振る舞いを保ちつつ、理解や修正が簡単になるようにソフトウェアの内部構造を変化させること
- セマンティックバージョンを利用することで、バージョンだけでソフトウェアの変化を理解することができる


 */

    val baseBorderPane = new BorderPane()
    val scene = new Scene(baseBorderPane, mediaViewFitWidth + tableMinWidth, mediaViewFitHeight + toolBarMinHeight)
    val toolBar = ToolbarCreator.create(mediaView, tableView, timeLabel, scene, primaryStage)
    baseBorderPane.setStyle("-fx-background-color: Black")
    baseBorderPane.setCenter(mediaView)
    baseBorderPane.setBottom(toolBar)
    baseBorderPane.setRight(tableView)

    scene.setFill(Color.BLACK)
    mediaView.fitWidthProperty().bind(scene.widthProperty().subtract(tableMinWidth))
    mediaView.fitHeightProperty().bind(scene.heightProperty().subtract(toolBarMinHeight))


    scene.setOnDragOver(new MovieFileDragOverEventHandler(scene))
    scene.setOnDragDropped(new MovieFileDragDroppedEventHandler(movies))

    primaryStage.setTitle("mp4ファイルをドラッグ&ドロップしてください")

    primaryStage.setScene(scene)
    primaryStage.show()
  }
}