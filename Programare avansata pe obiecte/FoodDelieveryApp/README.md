# Proiect Programare Avansată pe Obiecte
## Food Delivery Application
### Cerințele primei etape
**1. Definirea aplicației**

- Zece acțiuni care se pot face în cadrul sistemului

În cadrul sistemului acțiunile pot fi făcute de utilizatori, proprietari de restaurant și administratori de aplicație

Utilizatorii pot:
1. înregistra
2. loga
3. pot vedea meniul
4. pot vedea meniul unui restaurant
5. pot vedea rating unui restaurant
6. pot comanda
7. pot da rating unui restaurant
8. pot vedea comenzile pe care le-au plasat

Proprietarii de restaurant pot:

9. adăuga un fel de mâncare în meniu
10. adăuga o nouă băutură în meniu

Administratorii de aplicație pot:

11. adăuga un șofer
12. vedea toate comenzile în procesare

- Cel puțin 8 tipuri de obiecte

1. Restaurant
2. Product
3. Food
4. Beverage
5. User
6. Rating
7. Order
8. Driver
9. Car


**2.	Implementare:**

Aplicația va conține:
- clase simple cu atribute private/protected și metode de acces
```java
public class Car {
    private String carName;
    private String carNumber;

    public Car(String carName, String carNumber) {
        this.carName = carName;
        this.carNumber = carNumber;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }
}
```
- cel puțin 2 colecții diferite capabile să gestioneze obiectele definite anterior (List, Set, Map, etc.) dintre care cel puțin una sa fie sortata
```java
// in services/OrderServiceImpl.java
  private static List<Driver> drivers;
  private static List <Driver> driversAvailable;
  private static PriorityQueue<Order> currentOrders;
  private static List <Order> ordersHistory;
```
```java
// in model/Order.java
  @Override
    public int compareTo(Order o) {
        return o.endTime.compareTo(endTime);
    }
```
- utilizare moștenire pentru crearea de clase adiționale și utilizarea lor în cadrul colecțiilor;
```java
public abstract class Product
public class Food extends Product
public class Beverage extends Product
```
```java
// in model/Restaurant.java
 private List<Product> products;
 ```
- cel puțin o clasa serviciu care sa expună operațiile: OrderServiceImpl, RatingServiceImpl, RestaurantServiceImpl, UserServiceImpl
- o clasa main din care sunt făcute apeluri către servicii
```java
public class Main {
    public static void main(String[] args) {
        ReadingService appR = ReadingService.getInstance();
        appR.Run();
    }
}
 ```
### Cerințele celei de-a doua etape
1.	Extindeți proiectul din prima etapa prin realizarea persistentei utilizând fișiere.
- se vor realiza fișiere de tip csv pentru cel puțin 4 dintre clasele definite in prima etapa.
- se vor realiza servicii singleton generice pentru scrierea și citirea din fișiere
- la pornirea programului se vor încărca datele din fișiere utilizând serviciile create
2. Realizarea unui serviciu de audit
- se va realiza un serviciu care sa scrie într-un fișier de tip CSV de fiecare data când este executata una dintre acțiunile descrise in prima etapa. Structura fișierului: nume_actiune, timestamp

### Cerințele celei de-a treia etape
Înlocuiți serviciile realizate în etapa a II-a cu servicii care sa asigure persistenta utilizând baza de date folosind JDBC.
-	sa se realizeze servicii care sa expună operații de tip create, read, update, delete pentru cel puțin 4 dintre clasele definite

