package uk.ac.warwick.cs126.stores;

import uk.ac.warwick.cs126.interfaces.IFavouriteStore;
import uk.ac.warwick.cs126.models.Favourite;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.IOUtils;

import uk.ac.warwick.cs126.structures.MyArrayList;
import uk.ac.warwick.cs126.structures.MyHashMap;
import uk.ac.warwick.cs126.structures.MySet;



import uk.ac.warwick.cs126.util.DataChecker;

/** Class for implementing FavouriteStore and operations related. */
public class FavouriteStore implements IFavouriteStore {

    private MyArrayList<Favourite> favouriteArray;
    private DataChecker dataChecker;
    private MyHashMap<Long,Favourite> blacklist;
    private MyHashMap<String,Favourite> storage;
    private MyHashMap<Long,Favourite> ids;

    /** Initialises Variables. */
    public FavouriteStore() {
        favouriteArray = new MyArrayList<>();
        dataChecker = new DataChecker();
        blacklist = new MyHashMap<>();
        storage = new MyHashMap<>();
        ids = new MyHashMap<>();
    }

    /** Loads the Favourite data inputted into the favouriteArray. */
    public Favourite[] loadFavouriteDataToArray(InputStream resource) {
        Favourite[] favouriteArray = new Favourite[0];

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

            Favourite[] loadedFavourites = new Favourite[lineCount - 1];

            BufferedReader csvReader = new BufferedReader(new InputStreamReader(
                    new ByteArrayInputStream(inputStreamBytes), StandardCharsets.UTF_8));

            int favouriteCount = 0;
            String row;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

            csvReader.readLine();
            while ((row = csvReader.readLine()) != null) {
                if (!("".equals(row))) {
                    String[] data = row.split(",");
                    Favourite favourite = new Favourite(
                            Long.parseLong(data[0]),
                            Long.parseLong(data[1]),
                            Long.parseLong(data[2]),
                            formatter.parse(data[3]));
                    loadedFavourites[favouriteCount++] = favourite;
                }
            }
            csvReader.close();

            favouriteArray = loadedFavourites;

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return favouriteArray;
    }

    /** Attempts to add a favourite to the storage.
     * 
     * @param favourite                     The favourite object to be added. 
     * 
     * Concatenates CustomerIDs and RestaurantIDs together use as a key for a HashMap.
     * This is used for twist cases in addition of objects.
     * Validates the faovurite object using DataChecker.
     * Adds to a HashMap of ID's so they can be searched for quickly.
     * 
     * @return True or False for if the favourite is added or not.
    */
    public boolean addFavourite(Favourite favourite) {
        Long favID = favourite.getID();
        Long custID = favourite.getCustomerID();
        Long restID = favourite.getRestaurantID();
        if(dataChecker.isValid(favourite)==true){
            String conc = Long.toString(favourite.getCustomerID())+(Long.toString(favourite.getRestaurantID()));
            if(blacklist.find(favID)){
                return false;
            }
            if(getFavourite(favID)!=null){
                String newConc = Long.toString(getFavourite(favID).getCustomerID())+Long.toString(getFavourite(favID).getRestaurantID());
                favouriteArray.remove(getFavourite(favID));
                ids.remove(favID);
                if(storage.find(newConc)){
                    favouriteArray.add(storage.get(newConc));
                    storage.remove(newConc);
                    ids.add((storage.get(newConc).getID()),storage.get(newConc));
                }
                blacklist.add(favID,favourite);
                return false;
            }
            
            for(int i=0;i<favouriteArray.size();i++){
                if(favouriteArray.get(i).getCustomerID().equals(custID) & favouriteArray.get(i).getRestaurantID().equals(restID)){
                    if(favourite.getDateFavourited().compareTo(favouriteArray.get(i).getDateFavourited())<0){
                        storage.add(conc,favouriteArray.get(i));
                        if(ids.find(favouriteArray.get(i).getID())){
                            ids.remove(favouriteArray.get(i).getID());
                        }
                        favouriteArray.remove(favouriteArray.get(i));
                        favouriteArray.add(favourite);
                        ids.add(favourite.getID(),favourite);
                        return true;
                    }
                    else if (favourite.getDateFavourited().compareTo(favouriteArray.get(i).getDateFavourited())>0){
                        storage.add(conc,favourite);
                        return false;
                    }
                }
            }
            
            favouriteArray.add(favourite);
            ids.add(favourite.getID(),favourite);
            return true;
        }
        return false;
    }

    /** Attempts to add all favourite objects from input array.
     * 
     * @param favourites                    Array of favourite objects to add.
     * 
     * Checks if favourite element is null first to save time.
     * 
     * @return True or False for if all favourites are added or not. 
    */
    public boolean addFavourite(Favourite[] favourites) {
        boolean flag = true;
        for(int i=0;i<favourites.length;i++){
            if(favourites[i]==null){
                flag = false;
            }
            else if(addFavourite(favourites[i])==false){
                flag = false;
            }
        }
        return flag;
    }

    /** Attempts to find a Favourite with given ID in the store.
     * 
     * @param id                    ID to find relating Favourite object.
     * 
     * Searches using HashMap.
     * 
     * @return Favourite object found or null if none is found.
    */
    public Favourite getFavourite(Long id) {
        if(ids.find(id)){
            return(ids.get(id));
        }else{
            return null;
        }
    }

    /** Splits up a Favourite Array recursively to then be sorted and merged.
     * 
     * @param fav                       Array of Favourites to be split up.
     * @param n                         Size of array fav.
     * @param flag                      Indicates what the array needs to be sorted by when passed to sort.
     * 
     * Will split the array into two, then calling the same function recursively.
     * Until the resulting arrays are smaller than 2 elements.
     * Then calling the function which will sort the data.
     * 
     * @return Sorted Favourite array. 
     *         Using the original array, both halves of the array, their end indexes and what to sort by.
    */
    public static Favourite[] mergeSort(Favourite[] fav, int n, int flag){
        if (n<2){
            return fav;
        }
        int mid = n/2;
        Favourite[] l = new Favourite[mid];
        Favourite[] r = new Favourite[n-mid];

        for (int i=0; i<mid; i++){
            l[i]=fav[i];
        }
        for (int i=mid; i<n; i++){
            r[i-mid]=fav[i];
        }
        mergeSort(l, mid,flag);
        mergeSort(r, n-mid,flag);
        return(sort(fav,l,r,mid,n-mid,flag));

    }

    /** Sorts and merges the split data passed from mergeSort.
     * 
     * @param fav                   Array of Favourites to be sorted.
     * @param l                     Left half of Favourite array to be merged.
     * @param r                     Right half of Favourite array to be merged.
     * @param left                  Last Index of left array.
     * @param right                 Last index of right array.
     * @param flag                  Indicates what the array needs to be sorted by.
     * 
     * Compares the elements of each array and will put the element that needs to be before into the array.
     * Once the end of an array is reaches, the reamining elements can be added into the fav array.
     * 
     * When the flag is 1 it sorts by ID.
     * When it is 2 it sorts by Date Favourited then ID. 
    */
    public static Favourite[] sort(Favourite[] fav,Favourite[] l,Favourite[] r,int left, int right, int flag){
        int i=0;
        int j=0;
        int k=0;
        if(flag == 1){
            while(i<left&&j<right){
                if(l[i].getID()<=r[j].getID()){
                    fav[k++]=l[i++];
                }else{
                    fav[k++] = r[j++];
                }
            }
        }

        if(flag == 2){
            while(i<left&&j<right){
                if((l[i].getDateFavourited().compareTo(r[j].getDateFavourited()))>0){
                    fav[k++]=l[i++];  
                }else if ((l[i].getDateFavourited().compareTo(r[j].getDateFavourited()))<0){
                    fav[k++]=r[j++];
                }else if (l[i].getID()<=r[j].getID()){
                    fav[k++]=l[i++]; 
                }else{
                    fav[k++]=r[j++];
                }
            }
        }

        while(i<left){
                fav[k++]=l[i++];
            }
            while(j<right){
                fav[k++]=r[j++];
            }
            return fav;

    }

    /** Gets all favourites and sorts them by ID in ascending order.
     * 
     * Creates Array of all Favourites and passes this to merge sort.
     * 
     * @return Array of Favourites sorted by asscending order of ID or null if the favouriteStore is empty. 
    */
    public Favourite[] getFavourites() {

        Favourite[] unsortedFav = new Favourite[favouriteArray.size()];

        for(int i=0;i<favouriteArray.size();i++){
            unsortedFav[i] = favouriteArray.get(i);
        }

        if(favouriteArray.size()==0){
            return new Favourite[0];
        }else{
            Favourite[] sortedFav = mergeSort(unsortedFav, unsortedFav.length, 1);
            return sortedFav;
        }
    }

    /** Gets an array of Favourites by a certain customerID and sorts them by Date.
     * 
     * @param id                    customerID to find matching favourites for. 
     * 
     * Adds all favourites with matching customerIDs to an arrayList. 
     * ArrayList is converted to array and sorted. 
     * Sorted by Date Favourited from newest to oldest. 
     * If they are the same, ascending order of ID.
     * 
     * @return Array of Favourites with matching customerID. 
    */
    public Favourite[] getFavouritesByCustomerID(Long id) {
        MyArrayList <Favourite> favList = new MyArrayList<>();
        for (int i=0;i<favouriteArray.size();i++){
            if(favouriteArray.get(i).getCustomerID().compareTo(id)==0){
                favList.add(favouriteArray.get(i));
            }
        }
        if (favList.size()==0){
            return new Favourite[0];
        }
        Favourite[] favFin = new Favourite[favList.size()];
        for(int i=0;i<favList.size();i++){
            favFin[i] = favList.get(i);
        }
        favFin = mergeSort(favFin, favFin.length, 2);
        return favFin;
        
    }

    /** Gets an array of Favourites by a certain restaurantID and sorts them by Date.
     * 
     * @param id                    restaurantID to find matching favourites for. 
     * 
     * Adds all favourites with matching restaurantIDs to an arrayList.
     * ArrayList is converted to array and sorted. 
     * Sorted by Date Favourited from newest to oldest. 
     * If they are the same, ascending order of ID.
    */
    public Favourite[] getFavouritesByRestaurantID(Long id) {
        MyArrayList <Favourite> favList = new MyArrayList<>();
        for (int i=0;i<favouriteArray.size();i++){
            if(favouriteArray.get(i).getRestaurantID().compareTo(id)==0){
                favList.add(favouriteArray.get(i));
            }
        }
        if (favList.size()==0){
            return new Favourite[0];
        }
        Favourite[] favFin = new Favourite[favList.size()];
        for(int i=0;i<favList.size();i++){
            favFin[i] = favList.get(i);
        }
        favFin = mergeSort(favFin, favFin.length, 2);
        return favFin;
    }

    /** Gets array of Restaurant IDs from favourites in common between two customers.
     * 
     * @param customer1ID                   First customer whos favourites are being compared.
     * @param customer2ID                   Second customer whos favourites are being compared.
     * 
     * Set and hashmaps of favourites created.
     * Set used to find intersection between the two to find common favourites. 
     * Hash Map used to then add appropriate favourites from set to an array.
     * Array is then sorted by Date, ID.
     * 
     * @return Array of Restaurant IDs that are in both customer1 and customer2. 
    */
    public Long[] getCommonFavouriteRestaurants(Long customer1ID, Long customer2ID) {
        MySet<Long>set1 = new MySet<>();
        MySet<Long>set2 = new MySet<>();
        Favourite[] fav1 = getFavouritesByCustomerID(customer1ID);
        Favourite[] fav2 = getFavouritesByCustomerID(customer2ID);

        MyHashMap<Long,Favourite> ids = new MyHashMap<>();


        for(int i=0; i<fav1.length;i++){
            set1.add(fav1[i].getRestaurantID());
            ids.add(fav1[i].getRestaurantID(),fav1[i]);
        }
        for(int i=0; i<fav2.length;i++){
            set2.add(fav2[i].getRestaurantID());
            ids.add(fav2[i].getRestaurantID(),fav2[i]);
        }
        set1 = set1.intersection(set1,set2);

        Favourite[] fin = new Favourite[set1.size()];
        if(fin.length<=0){
            return new Long[0];
        }

        int x = 0;
        for(int i=0; i<set1.size(); i++){
            if(ids.find(set1.get(i))){
                fin[i] = ids.get(set1.get(i));
            }
        }

        fin = mergeSort(fin,fin.length,2);
 
        Long[] ret = new Long[fin.length];

        for(int q=0;q<fin.length;q++){
            ret[q] = fin[q].getRestaurantID();
        }

        return ret;
    }

    /** Gets array of Restaurant IDs from favourites than are in one customer but not the other.
     *
     * @param customer1ID                   Customer whos favourites are being compared. 
     * @param customer2ID                   Customer who is checked to not contain those in customer 1. 
     * 
     * Set and hashmaps of favourites created.
     * Set used to find difference between the two to find common favourites. 
     * Hash Map used to then add appropriate favourites from set to an array.
     * Array is then sorted by Date, ID.
     * 
     * @return Array oF Restaurant IDs that are favourited by customer1 but not customer2. 
    */
    public Long[] getMissingFavouriteRestaurants(Long customer1ID, Long customer2ID) {
        MySet<Long>set1 = new MySet<>();
        MySet<Long>set2 = new MySet<>();
        Favourite[]fav1 = getFavouritesByCustomerID(customer1ID);
        Favourite[]fav2 = getFavouritesByCustomerID(customer2ID);

        MyHashMap<Long,Favourite> ids = new MyHashMap<>();

        for(int i=0; i<fav1.length;i++){
            set1.add(fav1[i].getRestaurantID());
            ids.add(fav1[i].getRestaurantID(),fav1[i]);
        }
        for(int i=0; i<fav2.length;i++){
            set2.add(fav2[i].getRestaurantID());
        }

        set1 = set1.difference(set1,set2);

        Favourite[] fin = new Favourite[set1.size()];
        if(fin.length<=0){
            return new Long[0];
        }

        for(int i=0; i<set1.size(); i++){
            if(ids.find(set1.get(i))){
                fin[i] = ids.get(set1.get(i));
            }
        }

        fin = mergeSort(fin,fin.length,2);
        Long[] ret = new Long[fin.length];
        for(int q=0;q<fin.length;q++){
            ret[q] = fin[q].getRestaurantID();
        }
        return ret;
    }

    /** Gets array of Restaurant IDs from favourites that do not match between customers.
     * 
     * @param customer1ID                    First customer whos favourites are being compared.
     * @param customer2ID                    Second customer whos favourites are being compared.
     * 
     * Set and hashmaps of favourites created.
     * Set used to find symmetric difference between the two to find common favourites. 
     * Hash Map used to then add appropriate favourites from set to an array.
     * Array is then sorted by Date, ID.
     * 
     * @return Array oF Restaurant IDs that are not matching between cutomer 1 and 2. 
    */
    public Long[] getNotCommonFavouriteRestaurants(Long customer1ID, Long customer2ID) {
        MySet<Long>set1 = new MySet<>();
        MySet<Long>set2 = new MySet<>();
        Favourite[]fav1 = getFavouritesByCustomerID(customer1ID);
        Favourite[]fav2 = getFavouritesByCustomerID(customer2ID);

        MyHashMap<Long,Favourite> ids = new MyHashMap<>();

        for(int i=0; i<fav1.length;i++){
            set1.add(fav1[i].getRestaurantID());
            ids.add(fav1[i].getRestaurantID(),fav1[i]);            
        }
        for(int i=0; i<fav2.length;i++){
            set2.add(fav2[i].getRestaurantID());
            ids.add(fav2[i].getRestaurantID(),fav2[i]);

        }

        set1 = set1.symDifference(set1,set2);

        Favourite[] fin = new Favourite[set1.size()];
        if(fin.length<=0){
            return new Long[0];
        }

        for(int i=0; i<set1.size(); i++){
            if(ids.find(set1.get(i))){
                fin[i] = ids.get(set1.get(i));
            }
        }

        fin = mergeSort(fin,fin.length,2);
        Long[] ret = new Long[fin.length];
        for(int q=0;q<fin.length;q++){
            ret[q] = fin[q].getRestaurantID();
        }
        return ret;
    }

    /** Splits up 2D Object array recursively to then be sorted and merged.
     * 
     * @param fav                   2D Array of Objects containing num of favourites, the customer ID and Date. 
     * @param n                     Size of Array of Objects.
     * 
     * Will split the array into two, then calling the same function recursively.
     * Until the resulting split arrays are smaller than 2 elements.
     * Then calling the function which will sort the data.
     * 
     * @return Sorted Object array.
     *         Using the original array, both halves of the array and their end indexes.
    */
    public static Object[][] mergeSort(Object[][] fav, int n){
        if (n<2){
            return fav;
        }
        int mid = n/2;
        Object[][] l = new Object[mid][3];
        Object[][] r = new Object[n-mid][3];

        for (int i=0; i<mid; i++){
            l[i]=fav[i];
        }
        for (int i=mid; i<n; i++){
            r[i-mid]=fav[i];
        }
        mergeSort(l, mid);
        mergeSort(r, n-mid);
        return(sort(fav,l,r,mid,n-mid));

    }

    /** Sorts and merges the split data passed from mergeSort.
     * 
     * @param fav                   2D Array of Objects containing num of favourites, the customer ID and Date.
     * @param l                     Left half of Object array to be merged.
     * @param r                     Right half of Object array to be merged.
     * @param left                  Last index of left array.
     * @param right                 Last index or right array.
     * 
     * Compares the elements of each array and will put the element that needs to be before into the array.
     * Once the end of an array is reached, the remaining elements can be added into the fav array.
     * 
     * @return Array sorted by a customerIDs number or favourites, then latest Date favourited, and then the customerID. 
    */
    public static Object[][] sort(Object[][] fav,Object[][] l,Object[][] r,int left, int right){
        int i=0;
        int j=0;
        int k=0;

        while(i<left&&j<right){
            if((int)(l[i][0])>(int)(r[j][0])){
                fav[k++]=l[i++];  
            }else if ((int)(l[i][0])<(int)(r[j][0])){
                fav[k++]=r[j++];
            }else if(((Date)(l[i][2])).compareTo((Date)(r[j][2]))<0){
                fav[k++]=l[i++];  
            }else if (((Date)(l[i][2])).compareTo((Date)(r[j][2]))>0){
                fav[k++]=r[j++];
            }else if ((Long)(l[i][1])<(Long)(r[j][1])){
                fav[k++]=l[i++]; 
            }else{
                fav[k++]=r[j++];
            }
        }

        while(i<left){
                fav[k++]=l[i++];
            }
            while(j<right){
                fav[k++]=r[j++];
            }
            return fav;
    }

    /** Gets the top 20 CustomerIDs that have the most favourites.
     * 
     * Creates a Set of CustomerIDs so there are no duplicates.
     * Then adds Objects containing the customerID. 
     * The object contains the number of favourites of a customerID and the latest favourite. 
     * It gets these using getFavouritesByCustomerID and the result of the set.
     * 
     * The Object array is sorted using the 2D array mergeSort. 
     * 
     * @return Array of customerIDs obtained from the sortain 2D Object array.
    */
    public Long[] getTopCustomersByFavouriteCount() {
        MySet<Long>custIDs = new MySet<>();
        for (int i=0;i<favouriteArray.size();i++){
            custIDs.add(favouriteArray.get(i).getCustomerID());
        }

        Object[][] toSort = new Object[custIDs.size()][3];

        for (int i=0;i<custIDs.size();i++){
            Favourite[] favs = getFavouritesByCustomerID(custIDs.get(i));
            toSort[i][0] = favs.length;
            toSort[i][1] = custIDs.get(i);
            toSort[i][2] = favs[0].getDateFavourited();
        }

        toSort = mergeSort(toSort,toSort.length);

        Long[] ids = new Long[toSort.length];

        for(int i=0;i<20;i++){
            ids[i] = (Long)(toSort[i][1]);
        }

        return ids;
    }

    /** Gets the top 20 restaurantIDs that have the most favourites.
     * 
     * Creates a Set of favouriteIDs so there are no duplicates.
     * Then adds Objects containing the favaouriteID. 
     * The object contains the number of favourites of a restaurantID and the latest favourite. 
     * It gets these using getFavouritesByRestaurantID and the result of the set.
     * 
     * The Object array is sorted using the 2D array mergeSort. 
     * 
     * @return Array of restaurantIDs obtained from the sortain 2D Object array.
    */
    public Long[] getTopRestaurantsByFavouriteCount() {
        MySet<Long>restIDs = new MySet<>();
        for (int i=0;i<favouriteArray.size();i++){
            restIDs.add(favouriteArray.get(i).getRestaurantID());
        }

        Object[][] toSort = new Object[restIDs.size()][3];

        for (int i=0;i<restIDs.size();i++){
            Favourite[] favs = getFavouritesByRestaurantID(restIDs.get(i));
            toSort[i][0] = favs.length;
            toSort[i][1] = restIDs.get(i);
            toSort[i][2] = favs[0].getDateFavourited();
        }

        toSort = mergeSort(toSort,toSort.length);

        Long[] ids = new Long[toSort.length];

        for(int i=0;i<20;i++){
            ids[i] = (Long)(toSort[i][1]);
        }

        return ids;
    }
}
