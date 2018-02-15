lazy val commonSettings = Seq(
  /*
  セマンティックバージョニング = メジャー.マイナー.パッチ
  API の変更に互換性のない場合にはメジャーバージョンを、
  後方互換性があり機能性を追加した場合はマイナーバージョンを、
  後方互換性を伴うバグ修正をした場合はパッチバージョンを上げます。
  */
  version := "1.0.0",
  organization := "jp.ed.nnn",
  scalaVersion := "2.11.8",
  test in assembly := {}
)

lazy val app = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    mainClass in assembly := Some("jp.ed.nnn.nightcoreplayer.Main"),
    assemblyJarName in assembly := "nightcoreplayer.jar",
    unmanagedJars in Compile += {
      val ps = new sys.SystemProperties
      val jh = ps("java.home")
      Attributed.blank(file(jh) / "lib/ext/jfxrt.jar")
    }
  )
