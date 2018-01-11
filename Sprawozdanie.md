##Temat zadania: Zdalna konsola typu telnet

**1. Opis protokołu komunikacyjnego**  
Komunikacja pomiędzy serwerem a klientami odbywa się poprzez połączenie sieciowe. Serwer po połączeniu uruchamia dedykowany dla klienta proces  powłoki bash. Klient po połączeniu z serwerem rozpoczyna wysyłanie komend oraz wątek odczytu i prezentowania odpowiedzi na obiekcie typu TextArea.

**2. Opis implementacji**  
Klient jest zaimplementowany w języku Java, serwer w języku C. Serwer po uruchomieniu inicjalizuje gniazdo, następnie je tworzy oraz wykonuje dowiązanie adresu i numeru portu do gniazda. Następnie ustawia kolejkę dla klientów. Po czym rozpoczyna się główna pętla programu, w niej serwer oczekuje na nowych klientów. Po podłączeniu klienta serwer tworzy dla niego dedykowany proces operujący na pseudoterminalu. Następnie duplikuje deskryptor klienta na deskryptory wejścia, wyjścia i wyjścia błędów. Po czym zamyka gniazdo klienta oraz uruchamia proces powłoki bash, który pracuje do momentu zamknięcia go przez klienta. 
Aplikacja po podłączeniu rozpoczyna wątek odczytu danych i umożliwia klientowi wprowadzenie komendy, którą na jego polecenie przesyła do serwera. Wątek odpowiedzialny za odczyt czeka na odpowiedź serwera, którą po odczycie prezentuje na obiekcie typu TextArea.
Pliki źródłowe:
* Connection.java – klasa implementująca połączenie
* Controller.java – klasa kontrolera dla aplikacji w fxml
* Main.java – główna klasa programu
* App.fxml – plik fxml aplikacji
* Server.c – plik w języku C implementujący serwer
* Makefile – make file

**3. Sposób kompilacji, uruchomienia i obsługi programów projektu**
* **Kompilacja serwera:**
``gcc -Wall server.c -o s –Lutol`` lub uruchamiając plik Makefile
* **Kompilacja klienta:**

**Obsługa programów:**
* **Serwer:** Serwer uruchamiamy z parametrem oznaczającym port, na którym ma pracować. Jeżeli zostanie uruchomiony bezparametrowo będzie pracował na porcie 1236. Po uruchomieniu serwer nie wymaga uwagi
* **Klient:** Po uruchomieniu aplikacji, należy wprowadzić adres serwera oraz port, a następnie kliknąć Connect. Aplikacja połączy się z serwerem. Komendy należy wprowadzać w polu obok przycisku Send command. Komenda może zostać zatwierdzona przyciskiem Send command bądź enterem po jej wprowadzeniu. Zakończenie pracy z serwerem jest możliwe poprzez przycisk Disconnect bądź wydanie komendy exit.
