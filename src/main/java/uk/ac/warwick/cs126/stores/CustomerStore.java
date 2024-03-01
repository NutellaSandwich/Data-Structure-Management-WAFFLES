package uk.ac.warwick.cs126.stores;

import uk.ac.warwick.cs126.interfaces.ICustomerStore;
import uk.ac.warwick.cs126.models.Customer;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.commons.io.IOUtils;

import uk.ac.warwick.cs126.structures.MyArrayList;
import uk.ac.warwick.cs126.structures.MyHashMap;

import uk.ac.warwick.cs126.util.DataChecker;
import uk.ac.warwick.cs126.util.StringFormatter;

/** Class for implementing CustomerStore and operations related. */
public class CustomerStore implements ICustomerStore {

    private MyArrayList<Customer> customerArray;
    private DataChecker dataChecker;
    private MyHashMap<Long,Long> blacklist;
    private StringFormatter stringFormatter;
    private MyHashMap<Long,Customer> ids;

    /** Initialises Variables. */
    public CustomerStore() {
        customerArray = new MyArrayList<>();
        dataChecker = new DataChecker();
        blacklist = new MyHashMap<>();
        stringFormatter = new StringFormatter();
        ids = new MyHashMap<>();
    }

    /** Loads the Customer data inputed into the customerArray. */
    public Customer[] loadCustomerDataToArray(InputStream resource) {
        Customer[] customerArray = new Customer[0];

        try {
            byte[] inputStreamBytes = IOUtils.toByteArray(resource);
            BufferedReader lineReader = new BufferedReader(new InputStreamReader(
                    new ByteArrayInputStream(inputStreamBytes), StandardCharsets.UTF_8));

            int lineCount = 0;
            String line;
            while ((line=lineReader.readLine()) != null) {
                if (!("".equals(line))) {
                    lineCount++;
                }
            }
            lineReader.close();

            Customer[] loadedCustomers = new Customer[lineCount - 1];

            BufferedReader csvReader = new BufferedReader(new InputStreamReader(
                    new ByteArrayInputStream(inputStreamBytes), StandardCharsets.UTF_8));

            int customerCount = 0;
            String row;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

            csvReader.readLine();
            while ((row = csvReader.readLine()) != null) {
                if (!("".equals(row))) {
                    String[] data = row.split(",");

                    Customer customer = (new Customer(
                            Long.parseLong(data[0]),
                            data[1],
                            data[2],
                            formatter.parse(data[3]),
                            Float.parseFloat(data[4]),
                            Float.parseFloat(data[5])));

                    loadedCustomers[customerCount++] = customer;
                }
            }
            csvReader.close();

            customerArray = loadedCustomers;

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return customerArray;
    }

    /** Attempts tp add a customer object to the storage.
     * 
     * @param customer                     The customer object to be added.
     * 
     * Only Adds if the customer is Valid, checked with DataChecker.
     * Adds customer both to store and Hash Map of ID's so it can be searched for quickly.
     * 
     * @return True or False for if customer is added or not.  
     */
    public boolean addCustomer(Customer customer) {

        Long custID = customer.getID();
        if(dataChecker.isValid(customer) == true){
            if (blacklist.find(custID)){
                return false;
            }
            else if (getCustomer(custID)!=null){
                customerArray.remove(getCustomer(custID));
                blacklist.add(custID,custID);
                return false;
            }else{
                customerArray.add(customer);
                ids.add(customer.getID(),customer);
                return true;
            }
        }else{
            return false;
        }
    }

    /** Attempts to add all customer objects from input array.
     * 
     * @param customers                    Array of customer objects to add.
     * 
     * Checks if customer element is null first to save time. 
     * 
     * @return True or False for if all customers are added or not.
     */
    public boolean addCustomer(Customer[] customers) {
        boolean flag = true;
        for(int i=0;i<customers.length;i++){
            if(customers[i]==null){
                flag = false;
            }
            else if(addCustomer(customers[i])==false){
                flag = false;
            }
        }
        return flag;
    }

    /** Attempts to find a Customer with given ID in the store.
     * 
     * @param id                    ID to find relating Customer object.
     * 
     * Searches using HashMap.
     * 
     * @return Customer object found or null if none is found.
    */
    public Customer getCustomer(Long id) {
        if(ids.find(id)){
            return(ids.get(id));
        }
        return null;
    }

    /** Splits up a Customer Array recursively to then be sorted and merged.
     * 
     * @param custom                    Array of Customers to be split up.
     * @param n                         Size of array custom.
     * @param flag                      Indicates what the array needs to be sorted by when passed to sort.
     * 
     * Will split the array into two, then calling the same function recursively.
     * Until the resulting arrays are smaller than 2 elements.
     * Then calling the function which will sort the data.
     * 
     * @return Sorted Customer array.
     *         Using the original array, both halves of the array, their end indexes and what to sort by.  
    */
    public static Customer[] mergeSort(Customer[] custom, int n, int flag){
        if (n<2){
            return custom;
        }
        int mid = n/2;
        Customer[] l = new Customer[mid];
        Customer[] r = new Customer[n-mid];

        for (int i=0; i<mid; i++){
            l[i]=custom[i];
        }
        for (int i=mid; i<n; i++){
            r[i-mid]=custom[i];
        }
        mergeSort(l, mid,flag);
        mergeSort(r, n-mid,flag);
        return(sort(custom,l,r,mid,n-mid,flag));

    }

    /** Sorts and merges the split data passed from mergeSort.
     * 
     * @param custom                    Array of Customers to be sorted.
     * @param l                         Left half of Customer array to be merged.
     * @param r                         Right half of Customer array to be merged.
     * @param left                      Last index of left array.
     * @param right                     Last index of right array.
     * @param flag                      Indicates what the array needs to be sorted by. 
     * 
     * Compares the elements of each array and will put the element that needs to be before into the array. 
     * Once the end of an array is reached, the remaining elements can be added into the cust array. 
     * 
     * When the flag is 1 it sorts by ID. 
     * When it is 2, it sorts by Last Name, First Name then ID.
     * 
     * @return Array sorted by whatever is needed.  
    */
    public static Customer[] sort(Customer[] cust,Customer[] l,Customer[] r,int left, int right, int flag){
        int i=0;
        int j=0;
        int k=0;
        if(flag == 1){
            while(i<left&&j<right){
                if(l[i].getID()<=r[j].getID()){
                    cust[k++]=l[i++];
                }else{
                    cust[k++] = r[j++];
                }
            }
        }

        if(flag==2){
            while(i<left&&j<right){
                if((l[i].getLastName().toUpperCase().compareTo(r[j].getLastName().toUpperCase()))<0){
                    cust[k++]=l[i++];  
                }else if ((l[i].getLastName().toUpperCase().compareTo(r[j].getLastName().toUpperCase()))>0){
                    cust[k++]=r[j++];
                }else if ((l[i].getFirstName().toUpperCase().compareTo(r[j].getFirstName().toUpperCase()))<0){
                    cust[k++]=l[i++]; 
                }else if ((l[i].getFirstName().toUpperCase().compareTo(r[j].getFirstName().toUpperCase()))>0){
                    cust[k++]=r[j++]; 
                }else if (l[i].getID()<=r[j].getID()){
                    cust[k++]=l[i++]; 
                }else{
                    cust[k++]=r[j++];
                }
            }
        }
        while(i<left){
            cust[k++]=l[i++];
            }
        while(j<right){
            cust[k++]=r[j++];
            }
        return cust;

    }

    /** Gets all customers and sorts them by ID in ascending order. 
     * 
     * Creates Array of all Customers and passes this to merge sort.
     * 
     * @return Array of customers sorted by ascending order of ID or null if the customer store is empty.
    */
    public Customer[] getCustomers() {

       Customer[] unsortedArr = new Customer[customerArray.size()];

       for (int i=0;i<customerArray.size();i++){
            unsortedArr[i] = customerArray.get(i);
       }

       if(customerArray.size()==0){
        return new Customer[0];
       }else{
            Customer[] sortedArr =  mergeSort(unsortedArr, unsortedArr.length, 1);
            return sortedArr;
       }
    }

    /** Sorts array of Customer objects in ascending order of ID.
     * 
     * @param customers                     Array of Customers.
     * 
     * Checks if array is empty first to save time. 
     * Then passed to merge sort if not.
     * Merge sort flag for ID sorting. 
     * 
     * @return Array sorted in ascending order of ID.
    */
    public Customer[] getCustomers(Customer[] customers) {
        if(customers.length==0){
            return new Customer[0];
        }
        Customer[] sorArr = mergeSort(customers, customers.length, 1);
        return sorArr;
    }

    /** Gets all customers and sorts them alphabetically by LastName. 
     * If they are the same, FirstName. 
     * If they are the same, ascending order of ID. 
     * 
     * Merge sort flag for Name sorting.
     * 
     * @return Array sorted alphabetically by LastName, FirstName then ascending by ID.
    */
    public Customer[] getCustomersByName() {
        
        Customer[] unsortedN = new Customer[customerArray.size()];

        for(int i=0;i<customerArray.size();i++){
            unsortedN[i] = customerArray.get(i);
        }
        Customer[] sortedN = mergeSort(unsortedN, unsortedN.length, 2);
        return sortedN;
    }

    /** Sorts array of Customer objects by LastName, FirstName, ID.
     * 
     * @param customers                     Array of Customers.
     * 
     * Checks if array is empty to save time.
     * Passed to Merge Sort for Name sorting.
     * 
     * @return Array sorted by LastName, FirstName, ID.
    */
    public Customer[] getCustomersByName(Customer[] customers) {
        if(customers.length==0){
            return new Customer[0];
        }
        Customer[] sorArr = mergeSort(customers, customers.length, 2);
        return sorArr;
    }

    /** Gets all customers that have a First and Last Name that contain a search query
     * 
     * @param searchTerm                        Search query to find matching customers for.
     * 
     * Uses stringFormatter to remove accents. 
     * Converts Everything to Uppercase when comparing.
     * Searches for Concatenated String or First and Last names with a space between. 
     * Sorted by calling previous getCustomersByName with array function.
     * 
     * @return Sorted arry of Customers who's First and Last names contain the searchTerm. 
    */
    public Customer[] getCustomersContaining(String searchTerm) {
        if(searchTerm==""){
            return new Customer[0];
        }
        String search = stringFormatter.convertAccentsFaster(searchTerm);
        MyArrayList<Customer> sortList = new MyArrayList<>();
        for(int i=0;i<customerArray.size();i++){
            if((customerArray.get(i).getFirstName().toUpperCase()+ " " +customerArray.get(i).getLastName()).toUpperCase().contains(search.toUpperCase())){
                sortList.add(customerArray.get(i));
            }
        }

        Customer[] fin = new Customer[sortList.size()];
        for (int i=0;i<sortList.size();i++){
            fin[i]=sortList.get(i);
        }
        fin = getCustomersByName(fin);
        return fin;
    }
}
