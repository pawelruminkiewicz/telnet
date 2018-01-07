#include <sys/types.h>
#include <sys/wait.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <netdb.h>
#include <signal.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include <pthread.h>
#include <fcntl.h>

#define SERVER_PORT 1236
#define QUEUE_SIZE 5

struct data_t
{
int cfd;
struct sockaddr_in caddr;
};


int main(int argc, char* argv[])
{
	printf("Server start..\n");

   int server_socket_descriptor;
   int connection_socket_descriptor;
   int bind_result;
   int listen_result;
   char reuse_addr_val = 1;
   struct sockaddr_in server_address;

   //inicjalizacja gniazda serwera
   memset(&server_address, 0, sizeof(struct sockaddr));
   server_address.sin_family = PF_INET;
   server_address.sin_addr.s_addr = htonl(INADDR_ANY);
   server_address.sin_port = htons(SERVER_PORT);
   
  
   server_socket_descriptor = socket(PF_INET, SOCK_STREAM, 0);
   
   if (server_socket_descriptor < 0)
   {	
		printf("x:%d\n",server_socket_descriptor);
       fprintf(stderr, "1%s: Błąd przy próbie utworzenia gniazda..\n", argv[0]);
       exit(1);
   }
   setsockopt(server_socket_descriptor, SOL_SOCKET, SO_REUSEADDR, (char*)&reuse_addr_val, sizeof(reuse_addr_val));
   bind_result = bind(server_socket_descriptor, (struct sockaddr*)&server_address, sizeof(server_address));
   if (bind_result < 0)
   {
       fprintf(stderr, "2%s: Błąd przy próbie dowiązania adresu IP i numeru portu do gniazda.\n", argv[0]);
       exit(1);
   }
   listen_result = listen(server_socket_descriptor, QUEUE_SIZE);
   if (listen_result < 0) {
       fprintf(stderr, "3%s: Błąd przy próbie ustawienia wielkości kolejki.\n", argv[0]);
       exit(1);
   }
   struct data_t* t_data=malloc(sizeof(struct data_t));
	int slt = sizeof(t_data->caddr);
	int new_socket;
   while(new_socket = accept(server_socket_descriptor, (struct sockaddr*)&t_data->caddr,&slt))
   {
	   char ip[15];
	   memcpy(ip,inet_ntoa((struct in_addr)t_data->caddr.sin_addr),15);
		printf("++New client connected:%s\n",ip);
		
   }

   close(server_socket_descriptor);
   return(0);
}
