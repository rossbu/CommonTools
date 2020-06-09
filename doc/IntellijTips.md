# Intellij Tips

## Global Setting vs Project Settings ( A pain in this regard as of this release 02.2020)
Many requests made to jetbrain team to support global settings and Project settings, it's on the track
https://youtrack.jetbrains.com/issue/IDEA-221422?_ga=2.64261551.1206491663.1591033081-1682341316.1567179937

so that we do NOT have to copy/paste all setting across 30+ projects that you workig on: here is the minimum set of settings:
1. Inspections
1. Code Style
1. Dictionary (the one that new words get added to via the inspection)
1. Scopes and Copyright
1. Default Run/Debug Configurations
1. Live Templates

## Incrementally compile Java classes in IDEA ? Not supported
IntelliJ IDEA has its own incremental compilation system which tracks the dependencies between files being compiled and recompiles the minimum set of classes for every set of changes.
External compilation with tools like Maven or Gradle does not update IntelliJ IDEA's incremental compilation database.  
Because of that, IntelliJ IDEA cannot recognize the fact that classes have been already compiled with an external tool, and will recompile.
So when Tomcat run/Debug, the Run or Debug function needs to ensure that your classes are up to date by Recompiling all ( minimum set of files )

## Delegate build and run actions to Maven vs native IntelliJ IDEA builder
Go to setting - search maven - runner - delete build/action run actions to maven
- By default, IntelliJ IDEA uses the native IntelliJ IDEA builder to build a Maven project.  It might be helpful if you have a pure Java or a Kotlin project since IntelliJ IDEA supports the incremental build which significantly speeds up the building process.
- However, if you have a configuration that changes the compilation on the fly, or your build generates an artifact with a custom layout, then Maven would be preferable for the building process.
- https://www.jetbrains.com/help/idea/delegate-build-and-run-actions-to-maven.html (Jetbrain Reference)

##  Writing classes when compiling is suddenly very, very slow
1. Go to Setting -> Compiler
2. Set compile Head Size to 2000 ( was 700)

##  Java internal compiler error ( intellij )
1. On Intellij IDEA Ctrl + Alt + S to open settings.
1. Build, Execution, Deployment -> Compiler -> Java Compiler
1. choose your java version from Project bytecode version
1. Uncheck ***Use compiler from module target JDK when possible***
1. click apply and ok.

## Java version 13, 14 issues --release invalid flag  with maven

- Issue 1:
when built by intellij maven , it shows errors **--release** invalid flag, while command line may or may NOT have this problem
causes: it can be multiple reasons , java_home , path which points to different versions of jdk, even you set all settings in global settings and project settings in intellij,
but it still causes problem because OS java_home is different, so change all java_home, path to use same jdk13 or jdk14, and it will work after restart of intellij

- Issue 2:
error shows: It appears from this bug ticket that --release cannot be used in combination with --add-exports, --add-reads, and --patch-module
so maven compiler plugin, comment out  add-exports and java.base setting , release can't work with them together.
						<arg>-verbose</arg>
						<arg>-g</arg>
<!--						<arg>&#45;&#45;add-exports</arg>-->
<!--						<arg>java.base/sun.security.provider=ALL-UNNAMED</arg>-->

- Issue 3:
sometimes you need to set below to run the program independently in the project like main method, set below in compiler settings
in intellij setting compiler, add this to the module -parameters -verbose -g --add-exports java.base/sun.security.provider=ALL-UNNAMED

## JDK13 Settings
    open intellij ctrl+alt+s setting , and go to
    File | Settings | Build, Execution, Deployment | Compiler | Java Compiler
    Override compiler parameters Per module, add below to your module
    **--add-exports java.base/sun.security.x509=ALL-UNNAMED --add-exports java.base/sun.security.tools.keytool=ALL-UNNAMED**