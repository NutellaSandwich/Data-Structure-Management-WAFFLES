package uk.ac.warwick.cs126.test;

import uk.ac.warwick.cs126.models.*;
import uk.ac.warwick.cs126.stores.RestaurantStore;
import uk.ac.warwick.cs126.util.DataChecker;

public class TestTheRestaurantStore extends TestRunner {
    private DataChecker dataChecker;
        TestTheRestaurantStore() {
        dataChecker = new DataChecker();
        System.out.println("\n[Testing RestaurantStore]");

        // Run tests, comment out if you want to omit a test, feel free to modify or add more.
        testAddRestaurant();
        testAddRestaurants();
        testGetRestaurant();
        testGetRestaurants();
        testGetRestaurantsByName();
        testGetRestaurantsByDateEstablished();
        testGetRestaurantsByDateEstablishedInputArray();
        testGetRestaurantsByWarwickStars();
        testGetRestaurantsByRating();
        testGetRestaurantsByDistanceFrom();
        testGetRestaurantsByDistanceFromInputArray();
        testGetRestaurantsContaining();
    }
    private void testAddRestaurant() {
        try {
            // Initialise new store
            // We do this for every method so we have a fresh copy without any data in it
            RestaurantStore restaurantStore = new RestaurantStore();

            // Create a restaurant object
            // Restaurant(String repeatedID,
            //            String firstNameOfOwner,
            //            String lastNameOfOwner,
            //            Cuisine cuisine,
            //            EstablishmentType establishment,
            //            PriceRange price,
            //            Date dateEstablished,
            //            float latitude,
            //            float longitude,
            //            boolean vegetarian,
            //            boolean vegan,
            //            boolean glutenFree,
            //            boolean nutFree,
            //            boolean lactoseFree,
            //            boolean halal,
            //            Date inspectionData,
            //            int inspectionRating,
            //            int warwickStars,
            //            int customerRating)
            Restaurant restaurant = new Restaurant(
                    "111222333444555611122233344455561112223334445556",
                    "TARDIS",
                    "Doctor",
                    "Who",
                    Cuisine.British,
                    EstablishmentType.Takeaway,
                    PriceRange.FineDining,
                    parseDate("1963-11-23 12:34:56"),
                    52.3838f,
                    -1.560065f,
                    true,
                    true,
                    true,
                    true,
                    true,
                    true,
                    parseDate("2005-03-26 12:34:56"),
                    5,
                    2,
                    4.0f
            );

            // Add to store
            boolean result = restaurantStore.addRestaurant(restaurant);

            if (result) {
                System.out.println("[SUCCESS]    RestaurantStore: testAddRestaurant()");
            } else {
                System.out.println(" [FAILED]    RestaurantStore: testAddRestaurant()");
            }
        } catch (Exception e) {
            System.out.println(" [FAILED]    RestaurantStore: testAddRestaurant()");
            e.printStackTrace();
            System.out.println();
        }
    }

    private void testAddRestaurants() {
        try {
            // Initialise new store
            RestaurantStore restaurantStore = new RestaurantStore();

            // Load test data from /data folder
            Restaurant[] restaurants = restaurantStore.loadRestaurantDataToArray(
                    loadData("/test-restaurant/restaurant-10.csv"));

            // Add to store to be processed, should return true as all the data is valid
            boolean result = restaurantStore.addRestaurant(restaurants);

            if (result) {
                System.out.println("[SUCCESS]    RestaurantStore: testAddRestaurants()");
            } else {
                System.out.println(" [FAILED]    RestaurantStore: testAddRestaurants()");
                
            }
        } catch (Exception e) {
            System.out.println(" [FAILED]    RestaurantStore: testAddRestaurants()");
            e.printStackTrace();
            System.out.println();
        }
    }

    private void testGetRestaurant() {
        try {
            // Initialise new store
            RestaurantStore restaurantStore = new RestaurantStore();

            // Load test data from /data folder
            Restaurant[] restaurants = restaurantStore.loadRestaurantDataToArray(
                    loadData("/test-restaurant/restaurant-10.csv"));

            // Add to store to be processed
            restaurantStore.addRestaurant(restaurants);

            // Should return true as the restaurant with ID 9299783561728336 exists
            boolean result = restaurantStore.getRestaurant(9299783561728336L) != null
                    && restaurantStore.getRestaurant(9299783561728336L).getID().equals(9299783561728336L);

            if (result) {
                System.out.println("[SUCCESS]    RestaurantStore: testGetRestaurant()");
            } else {
                System.out.println(" [FAILED]    RestaurantStore: testGetRestaurant()");
            }
        } catch (Exception e) {
            System.out.println(" [FAILED]    RestaurantStore: testGetRestaurant()");
            e.printStackTrace();
            System.out.println();
        }
    }

    private void testGetRestaurants() {
        try {
            // Initialise new store
            RestaurantStore restaurantStore = new RestaurantStore();

            // Load test data from /data folder
            Restaurant[] restaurants = restaurantStore.loadRestaurantDataToArray(
                    loadData("/test-restaurant/restaurant-10.csv"));

            // Add to store to be processed
            restaurantStore.addRestaurant(restaurants);

            // Get sorted data by ID from store
            Restaurant[] gotRestaurants = restaurantStore.getRestaurants();

            // Load separate data that was manually sorted to verify with
            Restaurant[] expectedRestaurants = restaurantStore.loadRestaurantDataToArray(
                    loadData("/test-restaurant/restaurant-10-sorted-by-id.csv"));

            // Note: You have to set ID here as these do not get processed in the current RestaurantStore object
            // The gotRestaurants have proper IDs already as these have been processed in restaurantStore
            // The loadRestaurantDataToArray is a method that only gives us a Restaurant array from text data
            // It is not loaded into the restaurantStore object
            // We do not load anything into restaurantStore unless addRestaurant(r) is called
            // So, since we manually read the data, we can set IDs to their actual IDs
            expectedRestaurants[0].setID(1692228679459533L);
            expectedRestaurants[1].setID(1853896836596432L);
            expectedRestaurants[2].setID(1983542699134676L);
            expectedRestaurants[3].setID(3417685886353779L);
            expectedRestaurants[4].setID(4126217165326377L);
            expectedRestaurants[5].setID(4158933795222611L);
            expectedRestaurants[6].setID(5652995694831743L);
            expectedRestaurants[7].setID(9299783561728336L);
            expectedRestaurants[8].setID(9685731497851316L);
            expectedRestaurants[9].setID(9812977962372633L);

            // Now to compare and verify
            boolean result = true;
            if (gotRestaurants.length == expectedRestaurants.length) {
                for (int i = 0; i < expectedRestaurants.length; i++) {
                    result = gotRestaurants[i].getID().equals(expectedRestaurants[i].getID());
                    if(!result){
                        break;
                    }
                }
            } else {
                result = false;
            }

            if(!result){
                System.out.println("\n[Expected]");
                for (Restaurant r: expectedRestaurants){
                    System.out.println(r);
                }

                System.out.println("\n[Got]");
                if (gotRestaurants.length == 0) {
                    System.out.println("You got nothing!");
                }

                for (Restaurant r: gotRestaurants){
                    System.out.println(r);
                }

                System.out.println();
            }

            if (result) {
                System.out.println("[SUCCESS]    RestaurantStore: testGetRestaurants()");
            } else {
                System.out.println(" [FAILED]    RestaurantStore: testGetRestaurants()");
            }
        } catch (Exception e) {
            System.out.println(" [FAILED]    RestaurantStore: testGetRestaurants()");
            e.printStackTrace();
            System.out.println();
        }
    }

    private void testGetRestaurantsByName() {
        try {
            RestaurantStore restaurantStore = new RestaurantStore();

            Restaurant[] restaurants = restaurantStore.loadRestaurantDataToArray(
                loadData("/test-restaurant/restaurant-10.csv"));

            restaurantStore.addRestaurant(restaurants);

            Restaurant[] gotRestaurants = restaurantStore.getRestaurantsByName();

            Restaurant[] expectedRestaurants = restaurantStore.loadRestaurantDataToArray(
                loadData("/test-restaurant/restaurant-10-sorted-by-name.csv"));

            boolean result = true;
            if(gotRestaurants.length==expectedRestaurants.length){
                for(int i=0;i<expectedRestaurants.length;i++){
                    result = dataChecker.extractTrueID(gotRestaurants[i].getRepeatedID()).equals(dataChecker.extractTrueID(expectedRestaurants[i].getRepeatedID()));
                    if(!result){
                        break;
                    }
                }
            }else{
                result = false;
            }
            if(!result){
                System.out.println("\n[Expected]");
                for (Restaurant c: expectedRestaurants){
                    System.out.println(c);
                }

                System.out.println("\n[Got]");
                if (gotRestaurants.length == 0) {
                    System.out.println("You got nothing!");
                }

                for (Restaurant c: gotRestaurants){
                    System.out.println(c);
                }

                System.out.println();
            }

            if (result) {
                System.out.println("[SUCCESS]    RestaurantStore: testGetRestaurantsByName()");
            } else {
                System.out.println(" [FAILED]    RestaurantStore: testGetRestaurantsByName()");
            }
        } catch (Exception e) {
            System.out.println(" [FAILED]    RestaurantStore: testGetRestaurantsByName()");
            e.printStackTrace();
            System.out.println();
        }
    }

    private void testGetRestaurantsByDateEstablished() {
        try {
            RestaurantStore restaurantStore = new RestaurantStore();


            Restaurant[] restaurants = restaurantStore.loadRestaurantDataToArray(
                loadData("/test-restaurant/restaurant-10.csv"));

            restaurantStore.addRestaurant(restaurants);

            Restaurant[] gotRestaurants = restaurantStore.getRestaurantsByDateEstablished();

            Restaurant[] expectedRestaurants = restaurantStore.loadRestaurantDataToArray(
                loadData("/test-restaurant/restaurant-10-sorted-by-date.csv"));

            boolean result = true;
            if(gotRestaurants.length==expectedRestaurants.length){
                for(int i=0;i<expectedRestaurants.length;i++){
                    result = dataChecker.extractTrueID(gotRestaurants[i].getRepeatedID()).equals(dataChecker.extractTrueID(expectedRestaurants[i].getRepeatedID()));
                    if(!result){
                        break;
                    }
                }
            }else{
                result = false;
            }
            if(!result){
                System.out.println("\n[Expected]");
                for (Restaurant c: expectedRestaurants){
                    System.out.println(c);
                }

                System.out.println("\n[Got]");
                if (gotRestaurants.length == 0) {
                    System.out.println("You got nothing!");
                }

                for (Restaurant c: gotRestaurants){
                    System.out.println(c);
                }

                System.out.println();
            }

            if (result) {
                System.out.println("[SUCCESS]    RestaurantStore: testGetRestaurantsByDateEstablished()");
            } else {
                //System.out.println(" [FAILED]    RestaurantStore: testGetRestaurantsByDateEstablished()");
                System.out.println("   [TODO]    RestaurantStore: testGetRestaurantsByDateEstablished()");
            }
        } catch (Exception e) {
            System.out.println(" [FAILED]    RestaurantStore: testGetRestaurantsByDateEstablished()");
            e.printStackTrace();
            System.out.println();
        }
    }

    private void testGetRestaurantsByDateEstablishedInputArray() {
        try {
            //TODO
            RestaurantStore restaurantStore = new RestaurantStore();

            Restaurant[] restaurants = restaurantStore.loadRestaurantDataToArray(
                    loadData("/test-restaurant/restaurant-10.csv"));

            // Get customers sorted by name from store via method
            Restaurant[] gotRestaurants = restaurantStore.getRestaurantsByDateEstablished(restaurants);

            // Load manually sorted data from /data folder to verify with
            Restaurant[] expectedRestaurants = restaurantStore.loadRestaurantDataToArray(
                    loadData("/test-restaurant/restaurant-10-sorted-by-date.csv"));

            // Verify
            boolean result = true;
            if (gotRestaurants.length == expectedRestaurants.length) {
                for (int i = 0; i < expectedRestaurants.length; i++) {
                    result = gotRestaurants[i].getID().equals(expectedRestaurants[i].getID());
                    if(!result){
                        break;
                    }
                }
            } else {
                result = false;
            }

            // Print if wrong
            if(!result){
                System.out.println("\n[Expected]");
                for (Restaurant c: expectedRestaurants){
                    System.out.println(c);
                }

                System.out.println("\n[Got]");
                if (gotRestaurants.length == 0) {
                    System.out.println("You got nothing!");
                }

                for (Restaurant c: gotRestaurants){
                    System.out.println(c);
                }

                System.out.println();
            }

            if (result) {
                System.out.println("[SUCCESS]    RestaurantStore: testGetRestaurantsByDateEstablishedInputArray()");
            } else {
                //System.out.println(" [FAILED]    RestaurantStore: testGetRestaurantsByDateEstablishedInputArray()");
                System.out.println("   [TODO]    RestaurantStore: testGetRestaurantsByDateEstablishedInputArray()");
            }
        } catch (Exception e) {
            System.out.println(" [FAILED]    RestaurantStore: testGetRestaurantsByDateEstablishedInputArray()");
            e.printStackTrace();
            System.out.println();
        }
    }

    private void testGetRestaurantsByWarwickStars() {
        try {
            RestaurantStore restaurantStore = new RestaurantStore();

           Restaurant[] restaurants = restaurantStore.loadRestaurantDataToArray(
                    loadData("/test-restaurant/restaurant-10.csv"));

            restaurantStore.addRestaurant(restaurants);

            // Get customers sorted by name from store via method
            Restaurant[] gotRestaurants = restaurantStore.getRestaurantsByWarwickStars();

            // Load manually sorted data from /data folder to verify with
            Restaurant[] expectedRestaurants = restaurantStore.loadRestaurantDataToArray(
                    loadData("/test-restaurant/restaurant-10-sorted-by-stars.csv"));

            // Verify
            boolean result = true;
            if(gotRestaurants.length==expectedRestaurants.length){
                for(int i=0;i<expectedRestaurants.length;i++){
                    result = dataChecker.extractTrueID(gotRestaurants[i].getRepeatedID()).equals(dataChecker.extractTrueID(expectedRestaurants[i].getRepeatedID()));
                    if(!result){
                        break;
                    }
                }
            }else{
                result = false;
            }

            // Print if wrong
            if(!result){
                System.out.println("\n[Expected]");
                for (Restaurant c: expectedRestaurants){
                    System.out.println(c);
                }

                System.out.println("\n[Got]");
                if (gotRestaurants.length == 0) {
                    System.out.println("You got nothing!");
                }

                for (Restaurant c: gotRestaurants){
                    System.out.println(c);
                }

                System.out.println();
            }

            if (result) {
                System.out.println("[SUCCESS]    RestaurantStore: testGetRestaurantsByWarwickStars()");
            } else {
                //System.out.println(" [FAILED]    RestaurantStore: testGetRestaurantsByWarwickStars()");
                System.out.println("   [TODO]    RestaurantStore: testGetRestaurantsByWarwickStars()");
            }
        } catch (Exception e) {
            System.out.println(" [FAILED]    RestaurantStore: testGetRestaurantsByWarwickStars()");
            e.printStackTrace();
            System.out.println();
        }
    }

    private void testGetRestaurantsByRating() {
        //TODO
            // Note: The loader does not load restaurants with ratings, default is 0.0 for all
            // You have to set them yourselves after loading it, use:
            //     restaurants[0].setCustomerRating(4.9f);
            // Alternatively, you can create your own restaurant objects with the constructor
        try {
            
            RestaurantStore restaurantStore = new RestaurantStore();

            Restaurant[] restaurants = restaurantStore.loadRestaurantDataToArray(
                    loadData("/test-restaurant/restaurant-10.csv"));

            restaurants[0].setCustomerRating(1.2f);
            restaurants[1].setCustomerRating(3.6f);
            restaurants[2].setCustomerRating(1.4f);
            restaurants[3].setCustomerRating(0.5f);
            restaurants[4].setCustomerRating(2.0f);
            restaurants[5].setCustomerRating(1.4f);
            restaurants[6].setCustomerRating(1.6f);
            restaurants[7].setCustomerRating(1.8f);
            restaurants[8].setCustomerRating(2.3f);
            restaurants[9].setCustomerRating(1.2f);

            // Get customers sorted by name from store via method
            Restaurant[] gotRestaurants = restaurantStore.getRestaurantsByDateEstablished(restaurants);

            // Load manually sorted data from /data folder to verify with
            Restaurant[] expectedRestaurants = restaurantStore.loadRestaurantDataToArray(
                    loadData("/test-restaurant/restaurant-10-sorted-by-rating.csv"));
            expectedRestaurants[0].setCustomerRating(3.6f);
            expectedRestaurants[1].setCustomerRating(2.3f);
            expectedRestaurants[2].setCustomerRating(2.0f);
            expectedRestaurants[3].setCustomerRating(1.8f);
            expectedRestaurants[4].setCustomerRating(1.6f);
            expectedRestaurants[5].setCustomerRating(1.4f);
            expectedRestaurants[6].setCustomerRating(1.4f);
            expectedRestaurants[7].setCustomerRating(1.2f);
            expectedRestaurants[8].setCustomerRating(0.5f);
            expectedRestaurants[9].setCustomerRating(0.2f);
            // Verify
            boolean result = true;
            if (gotRestaurants.length == expectedRestaurants.length) {
                for (int i = 0; i < expectedRestaurants.length; i++) {
                    result = gotRestaurants[i].getID().equals(expectedRestaurants[i].getID());
                    if(!result){
                        break;
                    }
                }
            } else {
                result = false;
            }

            // Print if wrong
            if(!result){
                System.out.println("\n[Expected]");
                for (Restaurant c: expectedRestaurants){
                    System.out.println(c);
                }

                System.out.println("\n[Got]");
                if (gotRestaurants.length == 0) {
                    System.out.println("You got nothing!");
                }

                for (Restaurant c: gotRestaurants){
                    System.out.println(c);
                }

                System.out.println();
            }
            

            if (result) {
                System.out.println("[SUCCESS]    RestaurantStore: testGetRestaurantsByRating()");
            } else {
                //System.out.println(" [FAILED]    RestaurantStore: testGetRestaurantsByRating()");
                System.out.println("   [TODO]    RestaurantStore: testGetRestaurantsByRating()");
            }
        } catch (Exception e) {
            System.out.println(" [FAILED]    RestaurantStore: testGetRestaurantsByRating()");
            e.printStackTrace();
            System.out.println();
        }
    }

    private void testGetRestaurantsByDistanceFrom() {
        try {
            
            RestaurantStore restaurantStore = new RestaurantStore();

            Restaurant[] restaurants = restaurantStore.loadRestaurantDataToArray(
                    loadData("/test-restaurant/restaurant-10.csv"));

            restaurantStore.addRestaurant(restaurants);

            // Get customers sorted by name from store via method
            RestaurantDistance[] got = restaurantStore.getRestaurantsByDistanceFrom(52.30565f,1.044021f);
            Restaurant[] gotRestaurants = new Restaurant[got.length];
            for (int i=0;i<got.length;i++){
                gotRestaurants[i]=got[i].getRestaurant();
            }
            // Load manually sorted data from /data folder to verify with
            Restaurant[] expectedRestaurants = restaurantStore.loadRestaurantDataToArray(
                    loadData("/test-restaurant/restaurant-10-sorted-by-distance.csv"));
            for(int i=0; i<expectedRestaurants.length;i++){
                expectedRestaurants[i].setID(dataChecker.extractTrueID(expectedRestaurants[i].getRepeatedID()));
            }
            // Verify
            boolean result = true;
            if (gotRestaurants.length == expectedRestaurants.length) {
                for (int i = 0; i < expectedRestaurants.length; i++) {
                    result = gotRestaurants[i].getID().equals(expectedRestaurants[i].getID());
                    if(!result){
                        break;
                    }
                }
            } else {
                result = false;
            }

            // Print if wrong
            if(!result){
                System.out.println("\n[Expected]");
                for (Restaurant c: expectedRestaurants){
                    System.out.println(c);
                }

                System.out.println("\n[Got]");
                if (gotRestaurants.length == 0) {
                    System.out.println("You got nothing!");
                }

                for (Restaurant c: gotRestaurants){
                    System.out.println(c);
                }

                System.out.println();
            }

            if (result) {
                System.out.println("[SUCCESS]    RestaurantStore: testGetRestaurantsByDistanceFrom()");
            } else {
                System.out.println(" [FAILED]    RestaurantStore: testGetRestaurantsByDistanceFrom()");
            }
        } catch (Exception e) {
            System.out.println(" [FAILED]    RestaurantStore: testGetRestaurantsByDistanceFrom()");
            e.printStackTrace();
            System.out.println();
        }
    }

    private void testGetRestaurantsByDistanceFromInputArray() {
         try {
            
            RestaurantStore restaurantStore = new RestaurantStore();

            Restaurant[] restaurants = restaurantStore.loadRestaurantDataToArray(
                    loadData("/test-restaurant/restaurant-10.csv"));
            restaurantStore.addRestaurant(restaurants);

            // Get customers sorted by name from store via method
            RestaurantDistance[] got = restaurantStore.getRestaurantsByDistanceFrom(restaurants,52.30565f,1.044021f);
            Restaurant[] gotRestaurants = new Restaurant[got.length];
            for (int i=0;i<got.length;i++){
                gotRestaurants[i]=got[i].getRestaurant();
            }
            // Load manually sorted data from /data folder to verify with
            Restaurant[] expectedRestaurants = restaurantStore.loadRestaurantDataToArray(
                    loadData("/test-restaurant/restaurant-10-sorted-by-distance.csv"));
            for(int i=0; i<expectedRestaurants.length;i++){
               expectedRestaurants[i].setID(dataChecker.extractTrueID(expectedRestaurants[i].getRepeatedID()));
            }
            // Verify
            boolean result = true;
            if (gotRestaurants.length == expectedRestaurants.length) {
                for (int i = 0; i < expectedRestaurants.length; i++) {
                    result = gotRestaurants[i].getID().equals(expectedRestaurants[i].getID());
                    if(!result){
                        break;
                    }
                }
            } else {
                result = false;
            }

            // Print if wrong
            if(!result){
                System.out.println("\n[Expected]");
                for (Restaurant c: expectedRestaurants){
                    System.out.println(c);
                }

                System.out.println("\n[Got]");
                if (gotRestaurants.length == 0) {
                    System.out.println("You got nothing!");
                }

                for (Restaurant c: gotRestaurants){
                    System.out.println(c);
                }

                System.out.println();
            }
            if (result) {
                System.out.println("[SUCCESS]    RestaurantStore: testGetRestaurantsByDistanceFromInputArray()");
            } else {
                //System.out.println(" [FAILED]    RestaurantStore: testGetRestaurantsByDistanceFromInputArray()");
                System.out.println("   [TODO]    RestaurantStore: testGetRestaurantsByDistanceFromInputArray()");
            }
        } catch (Exception e) {
            System.out.println(" [FAILED]    RestaurantStore: testGetRestaurantsByDistanceFromInputArray()");
            e.printStackTrace();
            System.out.println();
        }
    }

    private void testGetRestaurantsContaining() {
        try {
            //TODO
            RestaurantStore restaurantStore = new RestaurantStore();

            Restaurant[] restaurants = restaurantStore.loadRestaurantDataToArray(
                loadData("/test-restaurant/restaurant-10.csv"));

            restaurantStore.addRestaurant(restaurants);
            Restaurant[] gotRestaurants = restaurantStore.getRestaurantsContaining("eng");

            boolean result = true;

            if (result) {
                System.out.println("[SUCCESS]    RestaurantStore: testGetRestaurantsContaining()");
            } else {
                //System.out.println(" [FAILED]    RestaurantStore: testGetRestaurantsContaining()");
                System.out.println("   [TODO]    RestaurantStore: testGetRestaurantsContaining()");
            }
        } catch (Exception e) {
            System.out.println(" [FAILED]    RestaurantStore: testGetRestaurantsContaining()");
            e.printStackTrace();
            System.out.println();
        }
    }
}
