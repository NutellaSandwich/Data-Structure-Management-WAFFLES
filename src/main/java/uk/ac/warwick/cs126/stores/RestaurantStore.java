package uk.ac.warwick.cs126.stores;

import uk.ac.warwick.cs126.interfaces.IRestaurantStore;
import uk.ac.warwick.cs126.models.Cuisine;
import uk.ac.warwick.cs126.models.EstablishmentType;
import uk.ac.warwick.cs126.models.Place;
import uk.ac.warwick.cs126.models.PriceRange;
import uk.ac.warwick.cs126.models.Restaurant;
import uk.ac.warwick.cs126.models.RestaurantDistance;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.commons.io.IOUtils;

import uk.ac.warwick.cs126.structures.MyArrayList;
import uk.ac.warwick.cs126.structures.MyHashMap;

import uk.ac.warwick.cs126.util.ConvertToPlace;
import uk.ac.warwick.cs126.util.HaversineDistanceCalculator;
import uk.ac.warwick.cs126.util.DataChecker;
import uk.ac.warwick.cs126.util.StringFormatter;


/** Class for implementing RestaurantStore and operations related. */
public class RestaurantStore implements IRestaurantStore {

    private MyArrayList<Restaurant> restaurantArray;
    private DataChecker dataChecker;
    private MyHashMap<Long,Long> blacklist;
    private static HaversineDistanceCalculator distance;
    private StringFormatter stringFormatter;
    private ConvertToPlace convertPlace;
    private MyHashMap<Long,Restaurant> ids;

    /** Initialises Variables. */
    public RestaurantStore() {
        restaurantArray = new MyArrayList<>();
        dataChecker = new DataChecker();
        blacklist = new MyHashMap<>();
        distance = new HaversineDistanceCalculator();
        stringFormatter = new StringFormatter();
        convertPlace = new ConvertToPlace();
        ids = new MyHashMap<>();
    }

    /** Loads the Restaurant data inputted into the restaurantArray. */
    public Restaurant[] loadRestaurantDataToArray(InputStream resource) {
        Restaurant[] restaurantArray = new Restaurant[0];

        try {
            byte[] inputStreamBytes = IOUtils.toByteArray(resource);
            BufferedReader lineReader = new BufferedReader(new InputStreamReader(
                    new ByteArrayInputStream(inputStreamBytes), StandardCharsets.UTF_8));

            int lineCount = 0;
            String line;
            while ((line = lineReader.readLine()) != null) {
                if (!("".equals(line))) {
                    lineCount++;
                }
            }
            lineReader.close();

            Restaurant[] loadedRestaurants = new Restaurant[lineCount - 1];

            BufferedReader csvReader = new BufferedReader(new InputStreamReader(
                    new ByteArrayInputStream(inputStreamBytes), StandardCharsets.UTF_8));

            String row;
            int restaurantCount = 0;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

            csvReader.readLine();
            while ((row = csvReader.readLine()) != null) {
                if (!("".equals(row))) {
                    String[] data = row.split(",");

                    Restaurant restaurant = new Restaurant(
                            data[0],
                            data[1],
                            data[2],
                            data[3],
                            Cuisine.valueOf(data[4]),
                            EstablishmentType.valueOf(data[5]),
                            PriceRange.valueOf(data[6]),
                            formatter.parse(data[7]),
                            Float.parseFloat(data[8]),
                            Float.parseFloat(data[9]),
                            Boolean.parseBoolean(data[10]),
                            Boolean.parseBoolean(data[11]),
                            Boolean.parseBoolean(data[12]),
                            Boolean.parseBoolean(data[13]),
                            Boolean.parseBoolean(data[14]),
                            Boolean.parseBoolean(data[15]),
                            formatter.parse(data[16]),
                            Integer.parseInt(data[17]),
                            Integer.parseInt(data[18]));

                    loadedRestaurants[restaurantCount++] = restaurant;
                }
            }
            csvReader.close();

            restaurantArray = loadedRestaurants;

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return restaurantArray;
    }

    /** Attempts to add a restaurant object to the storage.
     * @param restaurant                    The restaurant object to be added.
     * 
     * Only adds if the restaurant is Valid, checker with DataChecker.
     * ID for restaurant object is set using extractTrueID from DataChecker.
     * Adds restaurant both to store and HashMap of ID's so it can be searched for quickly.
     * 
     * @return True or False for if restaurant is added or not. 
    */
    public boolean addRestaurant(Restaurant restaurant) {
        Long trueID = dataChecker.extractTrueID(restaurant.getRepeatedID());
        if(trueID==null||dataChecker.isValid(trueID)==false){
            return false;
        }
        restaurant.setID(trueID);
        if(dataChecker.isValid(restaurant)==true){
            if(blacklist.find(trueID)){
                return false;
            }
            if(getRestaurant(trueID)!=null){
                restaurantArray.remove(getRestaurant(trueID));
                blacklist.add(trueID,trueID);
                return false;
            }else{
                restaurantArray.add(restaurant);
                ids.add(restaurant.getID(),restaurant);
                return true;
           }
        }else{
            return false;
        }
    }

    /** Attempts to add all restaurant objects from input array.
     * 
     * @param restaurants                   Array of restaurant objects to add.
     * 
     * Checks if restaurant element is null first to save time.
     * 
     * @return True or False for if all restaurants are added or not.
    */
    public boolean addRestaurant(Restaurant[] restaurants) {
        boolean flag = true;
        for(int i=0;i<restaurants.length;i++){
            if(restaurants[i]==null){
                flag = false;
            }
            if(addRestaurant(restaurants[i])==false){
                flag = false;
            }
        }
        return flag;
    }

    /** Attempts to find a Restaurant with given ID in the store.
     * 
     * @param id                    ID to find relating Restaurant object. 
     * 
     * Searches using HashMap. 
     * 
     * @return Restaurant object found or null if none is found. 
    */
    public Restaurant getRestaurant(Long id) {
        if(ids.find(id)){
            return(ids.get(id));
        }
        return null;
    }

    /** Splits up a Restaurant Array recursively to then be sorted and merged.
     * 
     * @param restaur                   Array of Restaurants to be split up. 
     * @param n                         Size of array restaur.
     * @param flag                      Indicates what the array needs to be sorted by when passed to sort.
     * 
     * Will split the array into two, then calling the same function recursively.
     * Until the resulting arrays are smaller than 2 elements.
     * Then calling the function which will sort the data. 
     * 
     * @return Sorted Restaurant array.
     *         Using the original array, both halves of the array, their end indexes and what to sort by.                         
    */
    public static Restaurant[] mergeSort(Restaurant[] restaur, int n, int flag){
        if (n<2){
            return restaur;
        }
        int mid = n/2;
        Restaurant[] l = new Restaurant[mid];
        Restaurant[] r = new Restaurant[n-mid];

        for (int i=0; i<mid; i++){
            l[i]=restaur[i];
        }
        for (int i=mid; i<n; i++){
            r[i-mid]=restaur[i];
        }
        mergeSort(l, mid,flag);
        mergeSort(r, n-mid,flag);
        return(sort(restaur,l,r,mid,n-mid,flag));

    }

    /** Sorts and merges the split data passed from mergeSort. 
     * 
     * @param rest                  Array of Restaurants to be sorted.
     * @param l                     Left half of Restaurant array to be merged.
     * @param r                     Right half of Restaurant array to be merged.
     * @param left                  Last index of left array.
     * @param right                 Last index of right array.
     * @param flag                  Indicates what the array needs to be sorted by.
     * 
     * Compares the elements of each array and will put the element that needs to be before into the array.
     * Once the end of an array is reached, the remaining elements can be added into the rest array.
     * 
     * When the flag is 1 it sorts by ID.
     * When it is 2, it is sorted by Name then ID.
     * When it is 3, it is sorted by Date, Name, ID. 
     * When it is 4, it is sorted by Stars, Name, ID. 
     * When it is 5, it is sorted by Rating, Name, ID.
     * 
     * @return Array sorted by whatever is needed. 
    */ 
    public static Restaurant[] sort(Restaurant[] rest,Restaurant[] l,Restaurant[] r,int left, int right, int flag){
        int i=0;
        int j=0;
        int k=0;

        if(flag == 1){
            while(i<left&&j<right){
                if(l[i].getID()<r[j].getID()){
                    rest[k++]=l[i++];
                }else{
                    rest[k++] = r[j++];
                }
            }
        }
        
        if(flag == 2){
            while(i<left&&j<right){
                if(l[i].getName().toUpperCase().compareTo(r[j].getName().toUpperCase())<0){
                    rest[k++]=l[i++];  
                }else if ((l[i].getName().toUpperCase().compareTo(r[j].getName().toUpperCase()))>0){
                    rest[k++]=r[j++];
                }else if (l[i].getID()<r[j].getID()){
                    rest[k++]=l[i++]; 
                }else{
                    rest[k++]=r[j++];
                }
            }
        }

        if (flag == 3){
            while(i<left&&j<right){
                if(l[i].getDateEstablished().compareTo(r[j].getDateEstablished())<0){
                    rest[k++]=l[i++];
                }else if((l[i].getDateEstablished().compareTo(r[j].getDateEstablished()))>0){
                    rest[k++]=r[j++];                    
                }else if((l[i].getName().toUpperCase().compareTo(r[j].getName().toUpperCase()))<0){
                    rest[k++]=l[i++];  
                }else if ((l[i].getName().toUpperCase().compareTo(r[j].getName().toUpperCase()))>0){
                    rest[k++]=r[j++];
                }else if (l[i].getID()<r[j].getID()){
                    rest[k++]=l[i++]; 
                }else{
                    rest[k++]=r[j++];
                }
            }
        }

        if (flag == 4){
            while(i<left&&j<right){
                if(l[i].getWarwickStars()>r[j].getWarwickStars()){
                    rest[k++]=l[i++];
                }else if(l[i].getWarwickStars()<r[j].getWarwickStars()){
                    rest[k++]=r[j++];                                                          
                }else if((l[i].getName().toUpperCase().compareTo(r[j].getName().toUpperCase()))<0){
                    rest[k++]=l[i++];  
                }else if ((l[i].getName().toUpperCase().compareTo(r[j].getName().toUpperCase()))>0){
                    rest[k++]=r[j++];
                }else if (l[i].getID()<r[j].getID()){
                    rest[k++]=l[i++]; 
                }else{
                    rest[k++]=r[j++];
                }
            }
        }

        if (flag == 5){
            while(i<left&&j<right){
                if(l[i].getCustomerRating()>r[j].getCustomerRating()){
                    rest[k++]=l[i++];
                }else if(l[i].getCustomerRating()<r[j].getCustomerRating()){
                    rest[k++]=r[j++];                                                          
                }else if((l[i].getName().toUpperCase().compareTo(r[j].getName().toUpperCase()))<0){
                    rest[k++]=l[i++];  
                }else if ((l[i].getName().toUpperCase().compareTo(r[j].getName().toUpperCase()))>0){
                    rest[k++]=r[j++];
                }else if (l[i].getID()<r[j].getID()){
                    rest[k++]=l[i++]; 
                }else{
                    rest[k++]=r[j++];
                }
            }
        }


        while(i<left){
                rest[k++]=l[i++];
            }
            while(j<right){
                rest[k++]=r[j++];
            }
            return rest;

    }   

    /** Gets all restaurants and sorts them by ID in ascending order.
     * 
     * Creates Array of all Restaurants and passes this to merge sort.
     * 
     * @return Array of restaurants sorted by ascending order of ID or null if the customer store is empty. 
    */ 
    public Restaurant[] getRestaurants() {
        Restaurant[] unsortedArr = new Restaurant[restaurantArray.size()];

        for(int i=0;i<restaurantArray.size();i++){
            unsortedArr[i] = restaurantArray.get(i);
        }
        Restaurant[] sortedArr = mergeSort(unsortedArr, unsortedArr.length, 1);
        return sortedArr;
    }

    /** Sorts array of Restaurant objects in ascending order of ID. 
     * 
     * @param restaurants                   Array of Restaurants. 
     * 
     * Checks if array is empty first to save time. 
     * Then passed to merge sort if not.
     * Merge sort flag for ID sorting.
    */ 
    public Restaurant[] getRestaurants(Restaurant[] restaurants) {
        Restaurant[]sorArr = mergeSort(restaurants, restaurants.length, 1);
        return sorArr;
    }

    /** Gets all restaurants and sorts them alphabetically by Name.
     * If they are the same, ascending order of ID.
     * 
     * Merge Sort flag for Name sorting.
     * 
     * @return Array sorted alphabetically by Name then ascending by ID. 
    */ 
    public Restaurant[] getRestaurantsByName() {
        Restaurant[] unsortedN = new Restaurant[restaurantArray.size()];

        for(int i=0;i<restaurantArray.size();i++){
            unsortedN[i] = restaurantArray.get(i);
        }
        Restaurant[] sortedN = mergeSort(unsortedN, unsortedN.length, 2);
        return sortedN;
    }

    /** Gets all restaurants and sorts them by Date.
     * If they are the same, Name. 
     * If they are the same, ascending order of ID.
     * 
     * Merge Sort flag for Date sorting.
     * 
     * @return Array sorted by Date, Name then ascending by ID. 
    */ 
    public Restaurant[] getRestaurantsByDateEstablished() {
        Restaurant[] unsortedD = new Restaurant[restaurantArray.size()];

        for(int i=0;i<restaurantArray.size();i++){
            unsortedD[i] = restaurantArray.get(i);
        }
        Restaurant[] sortedD = mergeSort(unsortedD, unsortedD.length, 3);
        return sortedD;
    }

    /** Sorts array of Restaurant objects by Date, Name,ID.
     * 
     * @param restaurants                   Array of Restaurants.
     * 
     * Checks if array is empty to save time.
     * Passed to Merge Sort for Date Sorting.
     * 
     * @return Array sorted by Date, Name, ID.
    */
    public Restaurant[] getRestaurantsByDateEstablished(Restaurant[] restaurants) {
        if(restaurants.length==0){
            return new Restaurant[0];
        }
        Restaurant[] sorD = mergeSort(restaurants, restaurants.length, 3);
        return sorD;
    }

    /** Gets all Restaurants with more than 0 stars and sorts them in descending order.
     * If they are the same, Name, then ID.
     * 
     * The array that is to be sorted excludes all Restaurants with 0 stars.
     * 
     * Merge sort flag for Stars sorting.
     * 
     * @return Array sorted in descending order by Stars, Name then ID. 
    */
    public Restaurant[] getRestaurantsByWarwickStars() {
        Restaurant[] unsortedS = new Restaurant[restaurantArray.size()];

        for(int i=0;i<restaurantArray.size();i++){
            unsortedS[i] = restaurantArray.get(i);
        }
        
        int x=0;
        for(int i=0;i<unsortedS.length;i++){
            if (unsortedS[i].getWarwickStars()==0){
                x++;
            }
        }
        Restaurant[] sortedS = mergeSort(unsortedS, unsortedS.length, 4);

        Restaurant[] finalS = new Restaurant[sortedS.length-x];
        for(int i=0;i<finalS.length;i++){
            finalS[i] = sortedS[i];
        }
        return finalS;

    }

    /** Sorts array of Restaurant objects in descending order of Rating. 
     * 
     * @param restaurants                   Array of Restaurants. 
     * 
     * Checks if array is empty first to save time. 
     * Then passed to merge sort if not.
     * Merge sort flag for Rating sorting.
     * 
     * @return Array sorted by Rating, Name then ID.
    */
    public Restaurant[] getRestaurantsByRating(Restaurant[] restaurants) {
        if(restaurants.length==0){
            return new Restaurant[0];
        }
        Restaurant[] sortedR = mergeSort(restaurants, restaurants.length, 5);
        return sortedR;
    }

    /** Splits up a RestaurantDistance array recursively to then be sorted and merged.
     * 
     * @param restaur                   Array of RestaurantDistances to be split up.
     * @param n                         Sie of array restaur.
     * 
     * Will split the array into two, then calling the same function recursively.
     * Until the resulting arrays are smaller than 2 elements.
     * Then calling the function which will sort the data.
     * 
     * @return Sorted RestaurantDistance array.
     *         Using the original array, both halves of the array and their end indexes. 
    */
    public static RestaurantDistance[] mergeSort(RestaurantDistance[] restaur, int n){

        if (n<2){
            return restaur;
        }

        int mid = n/2;
        RestaurantDistance[] l = new RestaurantDistance[mid];
        RestaurantDistance[] r = new RestaurantDistance[n-mid];

        for (int i=0; i<mid; i++){
            l[i]=restaur[i];
        }
        for (int i=mid; i<n; i++){
            r[i-mid]=restaur[i];
        }

        mergeSort(l, mid);
        mergeSort(r, n-mid);
        return(sort(restaur,l,r,mid,n-mid));

    }

    /** Sorts and merges the split data passed from mergeSort.
     * 
     * @param rest                  Array of RestaurantDistances to be sorted.
     * @param l                     Left half of RestaurantDistance to be merged.
     * @param r                     Right half of RestaurantDistance to be merged.
     * @param left                  Last index of left array.
     * @param right                 Last index of right array.
     * 
     * Compares the elements of each array and will put the element that needs to be before into the array.
     * Once the end of an array is reached, the remaining elements can be added into the rest array.
     * 
     * @return Array sorted by Distance then ID. 
    */
    public static RestaurantDistance[] sort(RestaurantDistance[] rest,RestaurantDistance[] l,RestaurantDistance[] r,int left, int right){
        int i=0;
        int j=0;
        int k=0;

            while(i<left&&j<right){
                if(l[i].getDistance()<r[j].getDistance()){
                    rest[k++]=l[i++];
                }else if(l[i].getDistance()>r[j].getDistance()){
                    rest[k++] = r[j++];
                }else if (l[i].getRestaurant().getID()<r[j].getRestaurant().getID()){
                    rest[k++]=l[i++]; 
                }else{
                    rest[k++]=r[j++];
                }
            }

        while(i<left){
                rest[k++]=l[i++];
            }
            while(j<right){
                rest[k++]=r[j++];
            }
            return rest;
    }  

    /** Gets an array of RestaurantDistances sorted by ascending order of distance using coordinates. 
     * 
     * @param latitude                      Latitude coordinate.
     * @param longitude                     Longitude coordinate.
     * 
     * Generates the distance between each restaurant in the array using the haversine distance calculator.
     * 
     * @return Array sorted by Distance, ID. 
    */
    public RestaurantDistance[] getRestaurantsByDistanceFrom(float latitude, float longitude) {
        RestaurantDistance[] unDistance = new RestaurantDistance[restaurantArray.size()];
        for(int i=0; i<restaurantArray.size();i++){
            unDistance[i] = new RestaurantDistance((restaurantArray.get(i)),distance.inKilometres(latitude,longitude,restaurantArray.get(i).getLatitude(),restaurantArray.get(i).getLongitude()));
        }
        unDistance = mergeSort(unDistance, unDistance.length);

        return unDistance;
    }

    /** Gets an array of RestaurantDistances sorted by ascending order compared to the inputted restaurant array.
     * 
     * @param restaurants                   Array of restaurant objects.
     * @param latitude                      Latitude coordinate.
     * @param longitude                     Longitude coordinate. 
     * 
     * Forms RestaurantDistance objects by taking the elements in the restaurant array. 
     * Then calculating the distance using the calculator. 
     * Then generating a new RestuarantDistance object with these two pieces of information, for all elements in the array.
     * 
     * @return Array sorted by Distance, ID. 
    */
    public RestaurantDistance[] getRestaurantsByDistanceFrom(Restaurant[] restaurants, float latitude, float longitude) {
        for (int i=0;i<restaurants.length;i++){
            if((dataChecker.isValid(restaurants[i]))==false){
                return new RestaurantDistance[0];
            }
            restaurants[i].setID(dataChecker.extractTrueID(restaurants[i].getRepeatedID()));
        }
        RestaurantDistance[] unDistanceA = new RestaurantDistance[restaurants.length];
        for(int i=0; i<restaurants.length;i++){
            unDistanceA[i] = new RestaurantDistance(restaurants[i],distance.inKilometres(latitude,longitude,restaurants[i].getLatitude(),restaurants[i].getLongitude()));
        }
        unDistanceA = mergeSort(unDistanceA, unDistanceA.length);

        return unDistanceA;
    }

    /** Gets all Restaurants that have a name, cuisine or place that contain a search query.
     * 
     * @param searchTerm                    Search query to find matching Restaurants for.
     * 
     * Uses stringFormatter to remove accents.
     * Converts Everything to uppercase when comparing. 
     * Searches for each possible match, using convertToPlace to generate the place name.
     * 
     * @return Sorted array of Restaurants that contain the search query within the name, cuisine or place. 
    */
    public Restaurant[] getRestaurantsContaining(String searchTerm) {
        if(searchTerm==""){
            return new Restaurant[0];
        }
        String search = stringFormatter.convertAccentsFaster(searchTerm);
        MyArrayList<Restaurant> sortList = new MyArrayList<>();
        for(int i=0;i<restaurantArray.size();i++){
            if((restaurantArray.get(i).getName().toUpperCase().contains(search.toUpperCase()))||(restaurantArray.get(i).getCuisine().toString().toUpperCase().contains(search.toUpperCase()))||((convertPlace.convert(restaurantArray.get(i).getLatitude(),restaurantArray.get(i).getLongitude())).getName().toUpperCase().contains(search.toUpperCase()))){
                sortList.add(restaurantArray.get(i));
            }
        }

        Restaurant[] fin = new Restaurant[sortList.size()];
        for(int i=0;i<sortList.size();i++){
            fin[i]=sortList.get(i);
        }
        fin = mergeSort(fin,fin.length,2);
        return fin;
    }
}
