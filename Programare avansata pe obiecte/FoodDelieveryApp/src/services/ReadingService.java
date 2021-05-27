package services;

import data.Reader;
import data.Writer;
import database.*;

import java.util.*;

public class ReadingService {
    final private OrderServiceImpl orderService;
    final private RestaurantServiceImpl restaurantService;
    final private UserServiceImpl userService;
    final private RatingServiceImpl ratingService;
    final private AuditService auditService;
    private static ReadingService instance = null;
    final private Scanner scanner;

    private ReadingService(){
        orderService = OrderServiceImpl.getInstance();
        restaurantService = RestaurantServiceImpl.getInstance();
        userService = UserServiceImpl.getInstance();
        ratingService = RatingServiceImpl.getInstance();
        auditService = AuditService.getInstance();
        scanner = new Scanner(System.in);
        insertData();
    }

    public static ReadingService getInstance(){
        if (instance == null)
            instance = new ReadingService();
        return instance;
    }

    private void insertData() {
        // adaugare restaurante
        List<List<String>> restaurants = RestaurantCRUD.getRestaurants();
        restaurantService.addRestaurants(restaurants);

        //adaugare produse
        List<List<String>> foods = FoodCRUD.getFoods();
        restaurantService.addFoods(foods);

        List<List<String>> beverages = FoodCRUD.getBeverages();
        restaurantService.addBeverages(beverages);

        // adaugare useri
        List<List<String>> users = UserCRUD.getUsers();
        userService.addUsers(users);

        // adaugare soferi
        List<List<String>> drivers = DriverCRUD.getDrivers();
        orderService.addDrivers(drivers);
    }

    private void watchMenue(String option, String email){
        if (option.equals("1")) {
            restaurantService.seeProducts();
            CRUD.writeAction("watched menue");
        }
        else{
            System.out.println("Introduceti numele restaurantului");
            String restaurant = scanner.nextLine();
            restaurantService.seeProducts(restaurant);
            CRUD.writeAction("watched restaurant's menue");
        }

    }

    private void order(String email){
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
        System.out.println("Introduceti numele restarantului");
        String restName = scanner.nextLine();
        System.out.println("Scrieti ratingul de la 1 la 5");
        int value = scanner.nextInt();
        ratingService.giveRating(email, restName, value);
        CRUD.writeAction("rated");
    }

    private void seeRating(){
        System.out.println("Introduceti numele restarantului");
        String restName = scanner.nextLine();
        ratingService.seeRating(restName);
        CRUD.writeAction("watched rating");
    }

    private String Register(){
        System.out.println("Introduceti numele, emailul, parola, numarul de telefon si adresa pe cate o linie");

        String name, email, password, phoneNumber, userAddress;
        name = scanner.nextLine();
        email = scanner.nextLine();
        password = scanner.nextLine();
        phoneNumber = scanner.nextLine();
        userAddress = scanner.nextLine();
        userService.addUser(name, email, password, phoneNumber, userAddress);
        boolean status = UserCRUD.addUser(name, email, password, phoneNumber, userAddress);
        if (status)
            return email;
        return null;
    }

    private String Login(){
        System.out.println("Introduceti emailul");
        String email = scanner.nextLine();
        email = userService.login(email);
        return email;
    }

    private void User(){
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
            switch(option){
                case("exit"):{
                    return;
                }
                case("1"):{
                    watchMenue(option, email);
                    break;
                }
                case("2"):{
                    watchMenue(option, email);
                    break;
                }
                case("3"):{
                    seeRating();
                    break;
                }
                case("4"):{
                    order(email);
                    break;
                }
                case("5"):{
                    rate(email);
                    break;
                }
                case("6"):{
                    orderService.printUserOrders(email);
                    CRUD.writeAction("watched orders");
                    break;
                }
                case("7"):{
                    System.out.println("Introduceti noua parola");
                    String newPassword = scanner.nextLine();
                    UserCRUD.changePassword(email, newPassword);
                    CRUD.writeAction("changed password");
                    break;
                }
                case("8"):{
                    System.out.println("Introduceti noua adresa");
                    String newAddress = scanner.nextLine();
                    UserCRUD.changeAddress(email, newAddress);
                    CRUD.writeAction("changed address");
                }
                case("9"):{
                    System.out.println("Introduceti noul numar de telefon");
                    String newPhone = scanner.nextLine();
                    UserCRUD.changePhone(email, newPhone);
                    CRUD.writeAction("chaged phone number");
                }
                default: {
                    System.out.println("Optiune invalida");
                }
            }
        }
    }

    private void addFood(String restName){
        System.out.println("Introduceti numele produsului");
        String prodName = scanner.nextLine();
        System.out.println("Introduceti descrierea produsului");
        String prodDesc = scanner.nextLine();
        System.out.println("Introduceti pretul produsului");
        int price = scanner.nextInt();
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
        FoodCRUD.addFood(restName, prodName, prodDesc, price, g, ingredientsStr);
        CRUD.writeAction("added food");
    }

    private void addBeverage(String restName){
        System.out.println("Introduceti numele produsului");
        String prodName = scanner.nextLine();
        System.out.println("Introduceti descrierea produsului");
        String prodDesc = scanner.nextLine();
        System.out.println("Introduceti pretul produsului");
        int price = scanner.nextInt();
        System.out.println("Introduceti ml produsului");
        int ml = scanner.nextInt();
        scanner.nextLine();
        restaurantService.addBeverage(restName, prodName, prodDesc, price, ml);
        FoodCRUD.addBeverage(restName, prodName, prodDesc, price, ml);
        CRUD.writeAction("added beverage");
    }

    private void Owner(){
        boolean restaurantExists = false;
        String restName = "";

        while (!restaurantExists) {
            System.out.println("Introducetui numele restaurantului");
            restName = scanner.nextLine();
            restaurantExists = restaurantService.findRestaurant(restName);
            if (!restaurantExists)
                System.out.println("Acest restaurant nu exista");
        }

        while(true) {
            System.out.println("Pentru a iesi din modul owner scrieti \"exit\"");
            System.out.println("Alegeti optiunea dorita:");
            System.out.println("1 pentru a adauga un nou fel de mancare;");
            System.out.println("2 pentru a adauga un nou tip de bautura.");
            System.out.println("3 pentru a creste preturile cu 5%.");
            System.out.println("4 pentru a sterge un tip de mancare.");
            System.out.println("5 pentru a sterge un tip de bautura.");

            String option = scanner.nextLine();
            switch (option){
                case("exit"):{
                    return;
                }
                case("1"):{
                    addFood(restName);
                    break;
                }
                case("2"):{
                    addBeverage(restName);
                    break;
                }
                case("3"):{
                    FoodCRUD.updatePrices(restName);
                    CRUD.writeAction("updated prices");
                    break;
                }
                case("4"):{
                    System.out.println("Introduceti numele felului de mancare.");
                    String foodName = scanner.nextLine();
                    FoodCRUD.deleteFood("food", restName, foodName);
                    CRUD.writeAction("deleted food");
                    break;
                }
                case("5"):{
                    System.out.println("Introduceti numele bauturii.");
                    String beverageName = scanner.nextLine();
                    FoodCRUD.deleteFood("beverage", restName, beverageName);
                    CRUD.writeAction("deleted beverage");
                    break;
                }
                default:{
                    System.out.println("Optiune invalida");
                }
            }
        }
    }

    private void addDriver(){
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

        DriverCRUD.addDriver(name, email, telefon, salary, carName, carNumber);
        auditService.writeAction("added driver");
        CRUD.writeAction("added driver");
    }

    private void Admin(){
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
                DriverCRUD.updateCar(driverName, driverCar, driverCarNumber);
                CRUD.writeAction("updated driver's car");
            }
            if(option.equals("4")){
                System.out.println("Introduceti numele restaurantului");
                String restName = scanner.nextLine();
                RestaurantCRUD.deleteRestaurant(restName);
                CRUD.writeAction("deleted restaurant");
            }
            if(option.equals("5")){
                System.out.println("Introduceti numele soferului");
                String driverName = scanner.nextLine();
                DriverCRUD.deleteDriver(driverName);
                CRUD.writeAction("deleted driver");
            }
            if(option.equals("6")){
                System.out.println("Introduceti numele restaurantului");
                String restName = scanner.nextLine();
                System.out.println("Introduceti adresa restaurantului");
                String address = scanner.nextLine();
                RestaurantCRUD.addRestaurant(restName, address);
                CRUD.writeAction("added restaurant");
            }
        }
    }

    public void Run(){
        while(true){
            System.out.println("Pentru a iesi din aplicatie scrieti \"exit\"");
            System.out.println("Alegeti rolul:\na)Utilizator b)Proprietar c)Administrator");
            String inputString = scanner.nextLine();
            switch (inputString){
                case("exit"):{
                    MySqlConn.getInstance().closeConnection();
                    return;
                }
                case("a"):{
                    User();
                    break;
                }
                case("b"):{
                    Owner();
                    break;
                }
                case("c"):{
                    Admin();
                    break;
                }
                default:{
                    System.out.println("Optiune invalida");
                }
            }
        }
    }
}
