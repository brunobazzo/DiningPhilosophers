package com.company;

import java.sql.Timestamp;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;

public class DiningPhilosophers {

  static int philosophersNumber = 5, qtdThinking = 5, qtdEating = 0;
  static Philosopher philosophers[] = new Philosopher[philosophersNumber];
  static Fork forks[] = new Fork[philosophersNumber];

  static class Fork {

    public Semaphore mutex = new Semaphore(1);

    void grab() {
      try {
        mutex.acquire();
      }
      catch (Exception e) {
        e.printStackTrace(System.out);
      }
    }

    void release() {
      mutex.release();
    }

    boolean isFree() {
      return mutex.availablePermits() > 0;
    }

  }

  static class Philosopher extends Thread {

    public int number;
    public Fork leftFork;
    public Fork rightFork;

    Philosopher(int num, Fork left, Fork right) {
      number = num;
      leftFork = left;
      rightFork = right;
    }

    public void run(){
      System.out.println("Hi! I'm philosopher #" + number);

      while (true) {
        leftFork.grab();
        rightFork.grab();
        if(qtdEating<philosophersNumber){qtdEating+=1;}
        if(qtdThinking!=0){qtdThinking-=1;}
        eat();
        leftFork.release();
        rightFork.release();
        if(qtdThinking<philosophersNumber){qtdThinking+=1;}
        if(qtdEating!=0){qtdEating-=1;}
        think();
      }
    }

    void eat() {
      try {
        int sleepTime = ThreadLocalRandom.current().nextInt(0, 1000);
        System.out.println("Filosofo " + number + " COMENDO..." + new Timestamp(System.currentTimeMillis()));
        System.out.println("Filosofo " + number + " comeu por " + sleepTime + " " +  new Timestamp(System.currentTimeMillis()));
        Thread.sleep(sleepTime);
      }
      catch (Exception e) {
        e.printStackTrace(System.out);
      }
    }

    void think() {
      try {
        int sleepTime = ThreadLocalRandom.current().nextInt(0, 1000);
        System.out.println("Filosofo " + number + " PENSANDO... "+ new Timestamp(System.currentTimeMillis()));
        System.out.println("Filosofo " + number + " pensou por " + sleepTime + " " + new Timestamp(System.currentTimeMillis()));
        Thread.sleep(sleepTime);
      }
      catch (Exception e) {
        e.printStackTrace(System.out);
      }
    }

  }

  public static void Table(int Tthink, int Teat, int cycles){
    int rel = (Tthink/Teat);
    if((rel)==0){
      message(100,cycles);
    }
    if((rel)==4.5){
      message(90,cycles);
    }
    if((rel)==4){
      message(80,cycles);
    }
    if((rel)==3.5){
      message(70,cycles);
    }
    if((rel)==3){
      message(60,cycles);
    }
    if((rel)==2.5){
      message(50,cycles);
    }
    if((rel)==2){
      message(40,cycles);
    }
    if((rel)==1.5){
      message(30,cycles);
    }
    if((rel)==1){
      message(20,cycles);
    }
    if((rel)==0.5){
      message(10,cycles);
    }
  }

  public static void message(float per, int cycles){
    System.out.println("Porcentagem (TPensando/Tcomendo): "+per+"%; Ciclos: "+cycles);
  }

  public static void main(String[] args) {
	  
    for (int i = 0; i < philosophersNumber; i++) {
      forks[i] = new Fork();
    }

    for (int i = 0; i < philosophersNumber; i++) {
      philosophers[i] = new Philosopher(i, forks[i], forks[(i + 1) % philosophersNumber]);
      philosophers[i].start();
    }
    int cont = 0;

    while (true) {
      try {
        Thread.sleep(1000);

        System.out.println("Quantidade Pensando " + qtdThinking + " " + new Timestamp(System.currentTimeMillis()));
        System.out.println("Quantidade Comendo " + qtdEating + " " + new Timestamp(System.currentTimeMillis()));
        cont++;
        Table(qtdThinking,qtdEating,cont);
        System.out.println("Final do ciclo "+cont);

        boolean deadlock = true;
        for (Fork f : forks) {
          if (f.isFree()) {
            deadlock = false;
            break;
          }
        }
        if (deadlock) {
          Thread.sleep(1000);
          System.out.println("ocorreu deadlock");
          break;
        }
      }
      catch (Exception e) {
        e.printStackTrace(System.out);
      }
    }
    System.exit(0);
  }

}