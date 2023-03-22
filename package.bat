mvn javafx:jlink && ^
jpackage --name cardGenerate --type app-image -m com.yang.javafx/com.yang.javafx.MainApplication --runtime-image ./target/app/  --icon ./doc/img/cardGenerate.ico && ^
rmdir /s/q target