#include <stdio.h>
#include <stdlib.h>
#include <string.h>

// Argument prototypes
void handle_arguments(int arg_count, char const *argv[]);
int is_arg(char const *argv[], int arg_num, char *arg);

// Logic prototypes
int screen_exists();
void start();
void stop();
void join();
void compile();
void update();
void clone();
void refresh();
void show_help();

int main(int argc, char const *argv[]) {
  argc = argc - 1; argv = argv + 1;
  handle_arguments(argc, argv);
  return 0;
}

void handle_arguments(int arg_count, char const *argv[]) {
  if (arg_count > 0) {
    if (strcmp(argv[0], "compile") == 0) {
      compile();
    } else if (strcmp(argv[0], "start") == 0) {
      start();
    } else if (strcmp(argv[0], "stop") == 0) {
      stop();
    } else if (strcmp(argv[0], "join") == 0) {
      join();
    } else if (strcmp(argv[0], "update") == 0) {
      update();
    } else if (strcmp(argv[0], "clone") == 0) {
      clone();
    } else if (strcmp(argv[0], "refresh") == 0) {
      refresh();
    }
  } else {
    show_help();
  }
}

void show_help() {
  printf("List of commands:\n");
  printf("join\t- Joins the bot's console if it is running\n");
  printf("start\t- Starts the bot and join it's running console\n");
  printf("stop\t- Kill the bot and exit the screen\n");
  printf("update\t- Recompile and restart the bot\n");
  printf("compile\t- Compile the source code\n");
  printf("clone\t- Clone the project files\n");
  printf("refresh\t- Pull any changes from the repository\n");
}

int screen_exists() {
  int exists = system("screen -S DiscordBot -X select .; echo $? > /dev/null");
  return exists == 0;
}

void join() {
  if (screen_exists()) {
    system("screen -drS DiscordBot");
  }
}

void start() {
  system("screen -S DiscordBot java -jar DiscordBot.jar");
}

void stop() {
  system("screen -S DiscordBot -X stuff \"stop\n\"");
}

void update() {
  if (screen_exists()) {
    stop();
  }
  compile();
  start();
}

void compile() {
  system("(cd ../ && gradle shadowJar)");
  system("cp \"../build/DiscordBot.jar\" ../stage/");
}

void clone() {
  system("cd ../");
  system("git init && git remote add origin git@gitlab.engr.ship.edu:Merlin/Ship-ENGR-Bot");
  system("git pull -f origin master");
  system("cd stage");
}

void refresh() {
  system("cd ../");
  system("git stash && git pull -f && git stash pop");
  system("cd stage");
}
