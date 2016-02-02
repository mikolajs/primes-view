
name := "Hello SBT"
 
version := "1.0"
 
scalaVersion := "2.11.7"
 
libraryDependencies += "org.scalafx" %% "scalafx" % "1.0.0-R8"
 
unmanagedJars in Compile += Attributed.blank(file(System.getenv("JAVA_HOME") + "/jre/lib/jfxrt.jar"))

