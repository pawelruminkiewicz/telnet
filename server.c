#include <sys/types.h>
#include <sys/wait.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <netdb.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <pty.h>
#include <utmp.h>

#define SERVER_PORT 1236//port serwera
#define QUEUE_SIZE 5 //ilosc dostepnych miejsc dla uzytkownikow na serwerze
#define commForServer(...) fprintf(alternative_stream_for_server, __VA_ARGS__) //makro do wyrzucania informacji przez serwer

//struktura potrzebna do polaczenia
struct data_t {
    int cfd;
    struct sockaddr_in caddr;
};

void childend(int signo)
{
   pid_t pid;
   pid = wait(NULL);
   printf("\t[Client %d disconnected]\n", pid);
}


int main(int argc, char *argv[]) {
    printf("Server start..\n");
    int server_socket_descriptor;
    int bind_result;
    int listen_result;
    char reuse_addr_val = 1;
    struct sockaddr_in server_address;

	signal(SIGCHLD, childend);
	   
    //inicjalizacja gniazda serwera
    memset(&server_address, 0, sizeof(struct sockaddr));
    server_address.sin_family = PF_INET;
    server_address.sin_addr.s_addr = htonl(INADDR_ANY);
    server_address.sin_port = htons(SERVER_PORT);

    //tworzenie gniazda serwera
    server_socket_descriptor = socket(PF_INET, SOCK_STREAM, 0);
    if (server_socket_descriptor < 0) {
        printf("x:%d\n", server_socket_descriptor);
        fprintf(stderr, "1%s: Błąd przy próbie utworzenia gniazda..\n", argv[0]);
        exit(1);
    }

    //dowiazanie adresu i numeru portu do gniazda
    setsockopt(server_socket_descriptor, SOL_SOCKET, SO_REUSEADDR, (char *) &reuse_addr_val, sizeof(reuse_addr_val));
    bind_result = bind(server_socket_descriptor, (struct sockaddr *) &server_address, sizeof(server_address));
    if (bind_result < 0) {
        fprintf(stderr, "2%s: Błąd przy próbie dowiązania adresu IP i numeru portu do gniazda.\n", argv[0]);
        exit(1);
    }

    listen_result = listen(server_socket_descriptor, QUEUE_SIZE);
    if (listen_result < 0) {
        fprintf(stderr, "3%s: Błąd przy próbie ustawienia wielkości kolejki.\n", argv[0]);
        exit(1);
    }

//deklaracja zmiennych   
    struct data_t *t_data = malloc(sizeof(struct data_t));
    socklen_t
    slt = (socklen_t)
    sizeof(t_data->caddr); //rozmiar struktury sockaddr_in
    int new_socket;
    int pid;
    int new_fd;


    FILE *alternative_stream_for_server;
    new_fd = dup(STDERR_FILENO);
    alternative_stream_for_server = fdopen(new_fd, "w");
    setbuf(alternative_stream_for_server, NULL);

    //glowna petla programu
    while (1) {
		new_socket = accept(server_socket_descriptor, (struct sockaddr *) &t_data->caddr, &slt);
        char ip[15];
        memcpy(ip, inet_ntoa((struct in_addr) t_data->caddr.sin_addr),
               15); //kopiowanie adresu ip uzytkownika do tablicy char
        printf("[New client connected: %s]\n", ip);

		pid=forkpty(&new_fd, NULL, NULL, NULL);
		if (pid==0)
       {
            //duplikacja deskryptorow
             if (dup2(new_socket, STDIN_FILENO) == -1) {
                commForServer("--Error with duplicate output desc..\n");
            }
            if (dup2(new_socket, STDOUT_FILENO) == -1) {
                commForServer("--Error with duplicate output desc..\n");
            }
            if (dup2(new_socket, STDERR_FILENO) == -1) {
                commForServer("--Error with duplicate error desc..\n");
            }
			close(new_socket);
            execl("/bin/bash", "/bin/bash","-i", (char *)0);
            exit(0);
        }
    }
    fclose(alternative_stream_for_server);//zamykanie deskryptora dla informacji serwera
    close(server_socket_descriptor); //zamykanie deskryptora socketu serwera
    return (0);
}
