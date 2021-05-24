package services;

import data.Reader;
import data.Writer;
import database.CRUD;

import java.util.*;

public class ReadingService {
    final private OrderServiceImpl orderService;
    final private RestaurantServiceImpl restaurantService;
    final private UserServiceImpl userService;
    final private RatingServiceImpl ratingService;
    final private AuditService auditService;
    private static ReadingService instance = null;

    private ReadingService(){
        orderService = OrderServiceImpl.getInstance();
        restaurantService = RestaurantServiceImpl.getInstance();
        userService = UserServiceImpl.getInstance();
        ratingService = RatingServiceImpl.getInstance();
        auditService = AuditService.getInstance();
        insertData();
    }

    public static ReadingService getInstance(){
        if (instance == null)
            instance = new ReadingService();
        return instance;
    }

    private void insertData() {
        // adaugare restaurante
        List<List<String>> restaurants = CRUD.getRestaurants();
        //System.out.println(restaurants);
        restaurantService.addRestaurants(restaurants);

        //adaugare produse
        List<List<String>> foods = CRUD.getFoods();
        restaurantService.addFoods(foods);

        List<List<String>> beverages = CRUD.getBeverages();
        restaurantService.addBeverages(beverages);

        // adaugare useri
        List<List<String>> users = CRUD.getUsers();
        userService.addUsers(users);

        // adaugare soferi
        List<List<String>> drivers = CRUD.getDrivers();
        orderService.addDrivers(drivers);
    }

    private void watchMenue(String option, String email){
        if (option.equals("1")) {
            restaurantService.seeProducts();
            CRUD.writeAction("watched menue");
        }
        else{
            Scanner scanner = new Scanner(System.in);
            System.out.println("Introduceti numele restaurantului");
            String restaurant = scanner.nextLine();
            restaurantService.seeProducts(restaurant);
            CRUD.writeAction("watched restaurant's menue");
        }

    }

    private void order(String email){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Introduceti numele restarantului");
        String restName = scanner.nextLine();

        System.out.println("Introduceti numarul produselor");
        int nrProduse = scanner.nextInt();
        scanner.nextLine();

        List<String> produse = new ArrayList<>();
        System.out.println("Introduceti numele produselor pe cate o linie");
        while (nrProduse > 0){
            String produs = scanner.nextLine();
            produse.add(produs);
            nrProduse -= 1;
        }
        orderService.Order(restName, email, produse);
        CRUD.writeAction("ordered");
    }

    private void rate(String email){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Introduceti numele restarantului");
        String restName = scanner.nextLine();
        System.out.println("Scrieti ratingul de la 1 la 5");
        int value = scanner.nextInt();
        ratingService.giveRating(email, restName, value);
        CRUD.writeAction("rated");
    }

    private void seeRating(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Introduceti numele restarantului");
        String restName = scanner.nextLine();
        ratingService.seeRating(restName);
        CRUD.writeAction("watched rating");
    }

    private String Register(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Introduceti numele, emailul, parola, numarul de telefon si adresa pe cate o linie");

        String name, email, password, phoneNumber, userAddress;
        name = scanner.nextLine();
        email = scanner.nextLine();
        password = scanner.nextLine();
        phoneNumber = scanner.nextLine();
        userAddress = scanner.nextLine();
        userService.addUser(name, email, password, phoneNumber, userAddress);
        boolean status = CRUD.addUser(name, email, password, phoneNumber, userAddress);
        if (status)
            return email;
        return null;
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
            if(option.equals("1")) {
                CRUD.writeAction("logged in");
            }
            if(option.equals("2")) {
                CRUD.writeAction("registered");
            }
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
            System.out.println("7 pentru a schimba parola.");
            System.out.println("8 pentru a schimba adresa.");
            System.out.println("9 pentru a schimba telefonul.");

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
            if (option.equals("6")) {
                orderService.printUserOrders(email);
                CRUD.writeAction("watched orders");
            }
            if (option.equals("7")) {
                System.out.println("Introduceti noua parola");
                String newPassword = scanner.nextLine();
                CRUD.changePassword(email, newPassword);
                CRUD.writeAction("changed password");
            }
            if (option.equals("8")) {
                System.out.println("Introduceti noua adresa");
                String newAddress = scanner.nextLine();
                CRUD.changeAddress(email, newAddress);
                CRUD.writeAction("changed address");
            }
            if (option.equals("9")) {
                System.out.println("Introduceti noul numar de telefon");
                String newPhone = scanner.nextLine();
                CRUD.changePhone(email, newPhone);
                CRUD.writeAction("chaged phone number");
            }
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
            System.out.println("3 pentru a creste preturile cu 5%.");
            System.out.println("4 pentru a sterge un tip de mancare.");
            System.out.println("5 pentru a sterge un tip de bautura.");

            String option = scanner.nextLine();
            if(option.equals("exit"))
                return;
            if (!(option.equals("1") || option.equals("2") || option.equals("3") || option.equals("4") || option.equals("5"))){
                System.out.println("Optiune incorecta " + option);
                break;
            }
            if (option.equals("1") || option.equals("2")){
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

                    System.out.println("Introduceti numarul ingredientelor");
                    int nrIngrediente = scanner.nextInt();
                    scanner.nextLine();

                    ArrayList<String> ing = new ArrayList<>();
                    System.out.println("Introduceti numele produselor pe cate o linie");
                    while (nrIngrediente > 0){
                        String ingredient = scanner.nextLine();
                        ing.add(ingredient);
                        nrIngrediente -= 1;
                    }

                    restaurantService.addFood(restName, prodName, prodDesc, price, g, ing);
                    String ingredientsStr = String.join(";", ing);
                    // scriu in food
                    CRUD.addFood(restName, prodName, prodDesc, price, g, ingredientsStr);
                    CRUD.writeAction("added food");
                }

                if(option.equals("2")){
                    System.out.println("Introduceti ml produsului");
                    int ml = scanner.nextInt();
                    scanner.nextLine();
                    restaurantService.addBeverage(restName, prodName, prodDesc, price, ml);
                    // scriu in beverage
                    CRUD.addBeverage(restName, prodName, prodDesc, price, ml);
                    CRUD.writeAction("added beverage");
                }
            }

            if(option.equals("3")){
                CRUD.updatePrices(restName);
                CRUD.writeAction("updated prices");
            }
            if(option.equals("4")){
                System.out.println("Introduceti numele felului de mancare.");
                String foodName = scanner.nextLine();
                CRUD.deleteFood("food", restName, foodName);
                CRUD.writeAction("deleted food");
            }
            if(option.equals("5")){
                System.out.println("Introduceti numele bauturii.");
                String beverageName = scanner.nextLine();
                CRUD.deleteFood("beverage", restName, beverageName);
                CRUD.writeAction("deleted beverage");
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

        CRUD.addDriver(name, email, telefon, salary, carName, carNumber);
        auditService.writeAction("added driver");
        CRUD.writeAction("added driver");
    }

    private void Admin(){
        Scanner scanner = new Scanner(System.in);
        while(true) {
            System.out.println("Pentru a iesi din modul administrator scrieti \"exit\"");
            System.out.println("Alegeti optiunea dorita:");
            System.out.println("1 pentru a vedea comenzile in progres;");
            System.out.println("2 pentru a adauga un nou sofer.");
            System.out.println("3 pentru a schimba masina unui sofer");
            System.out.println("4 pentru a sterge un restaurant");
            System.out.println("5 pentru a sterge un sofer");
            System.out.println("6 pentru a adauga un restaurant");

            String option = scanner.nextLine();
            if (option.equals("exit"))
                return;
            if (option.equals("1")) {
                orderService.printOrdersInProgress();
                CRUD.writeAction("watched orders in progress");
            }
            if(option.equals("2"))
                addDriver();
            if(option.equals("3")){
                System.out.println("Introduceti numele soferului");
                String driverName = scanner.nextLine();
                System.out.println("Introduceti tipui noii masini");
                String driverCar = scanner.nextLine();
                System.out.println("Introduceti numarul noii masini");
                String driverCarNumber= scanner.nextLine();
                CRUD.updateCar(driverName, driverCar, driverCarNumber);
                CRUD.writeAction("updated driver's car");
            }
            if(option.equals("4")){
                System.out.println("Introduceti numele restaurantului");
                String restName = scanner.nextLine();
                CRUD.deleteRestaurant(restName);
                CRUD.writeAction("deleted restaurant");
            }
            if(option.equals("5")){
                System.out.println("Introduceti numele soferului");
                String driverName = scanner.nextLine();
                CRUD.deleteDriver(driverName);
                CRUD.writeAction("deleted driver");
            }
            if(option.equals("6")){
                System.out.println("Introduceti numele restaurantului");
                String restName = scanner.nextLine();
                System.out.println("Introduceti adresa restaurantului");
                String address = scanner.nextLine();
                CRUD.addRestaurant(restName, address);
                CRUD.writeAction("added restaurant");
            }
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
