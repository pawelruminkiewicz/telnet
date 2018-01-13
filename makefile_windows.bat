cd src
javac put\sk\Connection.java put\sk\Controller.java put\sk\Main.java  -d ..\out
copy put\sk\app.fxml ..\out\put\sk
copy META-INF ..\out
cd ..\out
jar cmvf MANIFEST.MF telnet_client.jar  put\sk\Main.class put\sk\Main$1.class put\sk\Connection.class put\sk\Controller.class put\sk\app.fxml
java -jar telnet_client.jar