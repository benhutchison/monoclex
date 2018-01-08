import commondeps.Dependencies._

val vAll = Versions(versions, libraries, scalacPlugins)


lazy val monoclex = crossProject.crossType(CrossType.Pure).in(file("shared"))
  .settings(name := "monoclex", organization := "com.github.benhutchison")
  .settings(scalaVersion := vAll.vers("scalac"))
  .settings(addCompilerPlugins(vAll, "kind-projector"))
  .settings(addLibs(vAll,
    "cats-core", "monocle-core", "monocle-generic", "monocle-macro"
  ))
  .settings(scalacOptions ++= scalacAllOptions)

lazy val projJS = monoclex.js
lazy val projJVM = monoclex.jvm

lazy val monoclexRoot = project.in(file(".")).
  aggregate(projJS, projJVM).
  settings(noPublishSettings)