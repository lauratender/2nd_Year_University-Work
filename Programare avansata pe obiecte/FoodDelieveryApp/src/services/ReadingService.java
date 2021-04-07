package services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ReadingService {
    final private OrderServiceImpl orderService;
    final private RestaurantServiceImpl restaurantService;
    final private UserServiceImpl userService;
    final private RatingServiceImpl ratingService;
    private static ReadingService instance = null;

    private ReadingService(){
        orderService = OrderServiceImpl.getInstance();
        restaurantService = RestaurantServiceImpl.getInstance();
        userService = UserServiceImpl.getInstance();
        ratingService = RatingServiceImpl.getInstance();
        insertData();
    }

    public static ReadingService getInstance(){
        if (instance == null)
            instance = new ReadingService();
        return instance;
    }
    private void insertData(){
        // adaugare restaurante
        restaurantService.addRestaurant("Springtime", "Academiei 3-5");
        restaurantService.addRestaurant("Pizza Hut", "Dorobanti 5");
        restaurantService.addRestaurant("McDonald's", "Unirii 1");
        restaurantService.addRestaurant("KFC", "Regina Elisabeta 23");

        // adaugare meniu
        List <String> ings1 = new ArrayList<>();
        ings1.add("Piept de pui");
        ings1.add("Carne de vita");
        ings1.add("Castraveti");
        restaurantService.addFood("Springtime", "Shaorma", "pe plita", 19, 415, ings1);

        List <String> ings3 = new ArrayList<>();
        ings3.add("Mozzarella");
        ings3.add("Sunca");
        ings3.add("Ciuperci");
        ings3.add("Masline");
        restaurantService.addFood("Pizza Hut", "PizzaRoma", "Blat italian", 22, 210, ings3);

        List <String> ings2 = new ArrayList<>();
        ings2.add("Chifla");
        ings2.add("Carne de vita");
        ings2.add("Salata");
        restaurantService.addFood("McDonald's", "BigMac", "Burger", 30, 400, ings2);

        List <String> ings4 = new ArrayList<>();
        ings4.add("Piept de pui");
        restaurantService.addFood("KFC", "CrispyStrips", "8", 28, 240, ings4);

        restaurantService.addBeverage("McDonald's", "Cola", "Zero", 5, 500);
        restaurantService.addBeverage("Springtime", "Fanta", "Portocale", 5, 500);
        restaurantService.addBeverage("Pizza Hut", "Sprite", "Castraveti", 5, 500);
        restaurantService.addBeverage("KFC", "Pepsi", "Max", 5, 500);

        // adaugare useri
        userService.addUser("Laura", "lau.tender@gmail.com", "123", "0755555555", "gr19");

        // adaugare soferi
        orderService.addDriver("George Popescu", "george@gmail.com", "0744444444", 1500, "Renault", "B100AAA");
        orderService.addDriver("Alex Popa", "alex@gmail.com", "0733333333", 1500, "Renault", "B100AAA");
        orderService.addDriver("Stefan Georgescu", "stefan@gmail.com", "0722222222", 1500, "Renault", "B100AAA");
        orderService.addDriver("Andrei Ionescu", "andrei@gmail.com", "0711111111", 1500, "Renault", "B100AAA");
        orderService.addDriver("Ioan Vasile", "ioan@gmail.com", "0700000000", 1500, "Renault", "B100AAA");
    }

    private void watchMenue(String option, String email){
        if (option.equals("1"))
            restaurantService.seeProducts();
        else{
            Scanner scanner = new Scanner(System.in);
            System.out.println("Introduceti numele restaurantului");
            String restaurant = scanner.nextLine();
            restaurantService.seeProducts(restaurant);
        }
    }

    private void order(String email){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Introduceti numele restarantului");
        String restName = scanner.nextLine();
        System.out.println("Introduceti numele produselor");
        String[] productsList = scanner.nextLine().split(" ");
        orderService.Order(restName, email, Arrays.asList(productsList.clone()));
    }

    private void rate(String email){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Introduceti numele restarantului");
        String restName = scanner.nextLine();
        System.out.println("Scrieti ratingul de la 1 la 5");
        int value = scanner.nextInt();
        ratingService.giveRating(email, restName, value);
    }

    private void seeRating(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Introduceti numele restarantului");
        String restName = scanner.nextLine();
        ratingService.seeRating(restName);
    }

    private String Register(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Introduceti numele, emailul, parola, numarul de telefon si adresa");

        String name, email, password, phoneNumber, userAddress;
        name = scanner.nextLine();
        email = scanner.nextLine();
        password = scanner.nextLine();
        phoneNumber = scanner.nextLine();
        userAddress = scanner.nextLine();
        userService.addUser(name, email, password, phoneNumber, userAddress);
        return email;
    }

    private String Login(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Introduceti emailul");
        String email = scanner.nextLine();
        email = userService.login(email);
        return email;
    }

    private void User(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Pentru login apasati 1, iar pentru register 2");
        String option = scanner.nextLine();
        String email = null;

        if(option.equals("1"))
            email = Login();
        if(option.equals("2"))
            email = Register();

        if(email == null){
            System.out.println("Adresa de email nu este exista sau optiunea selectata nu este corecta.");
            return;
        }

        while(true) {
            System.out.println("Pentru a iesi din modul utilizator scrieti \"exit\"");
            System.out.println("Alegeti optiunea dorita:");
            System.out.println("1 pentru a vedea tot meniul;");
            System.out.println("2 pentru a vedea meniul unui restaurant;");
            System.out.println("3 pentru a vedea ratingul unui restaurant;");
            System.out.println("4 pentru a da comanda;");
            System.out.println("5 pentru a da rating unui restaurant;");
            System.out.println("6 pentru a vedea istoricul comenzilor.");

            option = scanner.nextLine();
            if(option.equals("exit"))
                return;
            if (option.equals("1") || option.equals("2"))
                watchMenue(option, email);
            if (option.equals("3"))
                seeRating();
            if (option.equals("4"))
                order(email);
            if (option.equals("5"))
                rate(email);
            if (option.equals("6"))
                orderService.printUserOrders(email);
        }
    }

    private void Owner(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Introducetui numele restaurantului");
        String restName = scanner.nextLine();

        while(true) {
            System.out.println("Pentru a iesi din modul owner scrieti \"exit\"");
            System.out.println("Alegeti optiunea dorita:");
            System.out.println("1 pentru a adauga un nou fel de mancare;");
            System.out.println("2 pentru a adauga un nou tip de bautura.");

            String option = scanner.nextLine();
            if(option.equals("exit"))
                return;
            System.out.println("Introduceti numele produsului");
            String prodName = scanner.nextLine();
            System.out.println("Introduceti descrierea produsului");
            String prodDesc = scanner.nextLine();
            System.out.println("Introduceti pretul produsului");
            int price = scanner.nextInt();

            if(option.equals("1")){
                System.out.println("Introduceti gramajul produsului");
                int g = scanner.nextInt();
                scanner.nextLine();
                System.out.println("Introduceti ingredientele produsului");
                String ing1 = scanner.nextLine();
                List <String> ing = Arrays.asList(ing1.split(" ").clone());
                restaurantService.addFood(restName, prodName, prodDesc, price, g, ing);
            }

            if(option.equals("2")){
                System.out.println("Introduceti ml produsului");
                int ml = scanner.nextInt();
                restaurantService.addBeverage(restName, prodName, prodDesc, price, ml);
            }
        }
    }

    private void addDriver(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Introduceti numele soferului");
        String name = scanner.nextLine();
        System.out.println("Introduceti emailul soferului");
        String email = scanner.nextLine();
        System.out.println("Introduceti telefonul soferului");
        String telefon = scanner.nextLine();
        System.out.println("Introduceti salariul soferului");
        Double salary  = scanner.nextDouble();
        scanner.nextLine();
        System.out.println("Introduceti numele masinii soferului");
        String carName = scanner.nextLine();
        System.out.println("Introduceti numarul masinii soferului");
        String carNumber = scanner.nextLine();
        orderService.addDriver(name, email, telefon, salary, carName, carNumber);
    }

    private void Admin(){
        Scanner scanner = new Scanner(System.in);
        while(true) {
            System.out.println("Pentru a iesi din modul administrator scrieti \"exit\"");
            System.out.println("Alegeti optiunea dorita:");
            System.out.println("1 pentru a vedea comenzile in progres;");
            System.out.println("2 pentru a adauga un nou sofer.");

            String option = scanner.nextLine();
            if (option.equals("exit"))
                return;
            if (option.equals("1"))
                orderService.printOrdersInProgress();
            if(option.equals("2"))
                addDriver();
        }
    }

    public void Run(){
        Scanner scanner = new Scanner(System.in);
        while(true){
            System.out.println("Pentru a iesi din aplicatie scrieti \"exit\"");
            System.out.println("Alegeti rolul:\na)Utilizator b)Proprietar c)Administrator");
            String inputString = scanner.nextLine();
            if (inputString.equals("exit"))
                break;
            if (inputString.equals("a"))
                User();
            if (inputString.equals("b"))
                Owner();
            if (inputString.equals("c"))
                Admin();
        }
    }
}
