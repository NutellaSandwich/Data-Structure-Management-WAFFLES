# CS126 WAFFLES Coursework Report [2102807]

## CustomerStore
### Overview
* I have used an `ArrayList` structure for customer store as it has simple operations and I believed since many functions required the use of the whole store then arraylists are the most practical for this.
* I have use a  `HashMap`  of `Linked Lists` for the blacklist and ID storage as these are times where you know the one value you want to look up so you can find it instantly. 
* The blacklist consisted of Long, Long Hashmaps whereas for where you need to be able to get the object in methods such as getCustomer(id), I used a hashmap of Long, Customer.
* For the sorting I used mergeSort as the worst case time is still quite good and it wasn't too difficult to implement. 
### Space Complexity

Store         | Worst Case | Description
------------- | ---------- | -----------
CustomerStore | O(n)       | I have used a single `ArrayList` to store customers and 2 HashMaps for IDs and blacklisted IDs. <br>Where `n` is total customers added.


### Time Complexity


Method                           | Average Case     | Description
-------------------------------- | ---------------- | -----------
addCustomer(Customer c)          | O(1)             | Array add is constant time, having to remove due to same ID is no considered for the average case
addCustomer(Customer[] c)        | O(n)             | Doing add n times <br>`n` is the length of the array c
getCustomer(Long id)             | O(1)             | Hash Map search is instant
getCustomers()                   | O(n)             | MergeSort takes O(nlogn) but copying arrayList out to array takes O(n) <br>`n` is total customers in the store
getCustomers(Customer[] c)       | O(nlogn)         | MergeSort used <br>`n` is the length of the input array
getCustomersByName()             | O(n)             | MergeSort takes O(nlogn) but copying arrayList out to array takes O(n) <br>`n` is total customers in the store
getCustomersByName(Customer[] c) | O(nlogn)         | MergeSort used <br>`n` is the length of the input array
getCustomersContaining(String s) | O(n) + O(x)      | String Formatter uses a HashMap so O(1)*the number of characters to be converted <br>Looping through the arrayList takes O(n)<br>`n` is total customers in the store<br>`x` is the number of accented characters in the s


<div style="page-break-after: always;"></div>

## FavouriteStore
### Overview
* I have used an `ArrayList` structure for Favourite store as it has simple operations for it and I believed since many functions required the use of the whole store then arraylists are the most practical for this.
* I have use a  `HashMap`  of `Linked Lists` for the blacklist and ID storage as these are times where you know the one value you want to look up so you can find it instantly. 
* The blacklist consisted of Long, Long Hashmaps whereas for where you need to be able to get the object in methods such as getFavourite(id), I used a hashmap of Long, Favourite.
* I have used `Sets` in favourite store for the common, missing and not common functions as the operations in these are set operations so it made sense to do so.<br> In these functions i also used `HashMaps` in order to instantly find the correlating object to do with the IDs found as this is much quicker than looping through all the sets.
* I also used another `HashMap` as an extra storage for addFavourite to implement the edge cases so lookup for matching customer and restaurant ids in the favourite would be instant.
* For all the sorting I used mergeSort as the worst case time is still quite good and it wasn't too difficult to implement. 
* To get top Favourites, for both functions, I used a Set and a 2D object array<br>The set is to ensure that there is only one customer/Restaurant ID for the favourite counts<br>The object is so that different data types can be stored.  

### Space Complexity
Store          | Worst Case | Description
-------------- | ---------- | -----------
FavouriteStore | O(n)     | I have used `ArrayList` and 3 `HashMaps` which are all smaller than the ArrayList so do not affect the size<br>Where `n` is total favourites in the store

### Time Complexity
Method                                                          | Average Case     | Description
--------------------------------------------------------------- | ---------------- | -----------
addFavourite(Favourite f)                                       | O(n)             | All adding and finding is O(1) as the finding is through HashMaps, looping through takes O(n) to check for favourites with matching cust and rest IDs <br>`n` is the total favourites in the store
addFavourite(Favourite[] f)                                     | O(n)*O(x)        | Uses O(n) add function <br>`n` is the total favourites in the store<br>`x` is the length of f
getFavourite(Long id)                                           | O(1)             | HashMap used
getFavourites()                                                 | O(n)             | MergeSort is O(nlogn) but looping through arrayList is O(n) <br>`n` is the total favourites in the store
getFavourites(Favourite[] f)                                    | N/A              | N/A (not in guide or program)
getFavouritesByCustomerID(Long id)                              | O(n)             | Loops through array to find matches<br>Actual matches take O(1) using Hash Map <br>`n` is the total favourites in the store
getFavouritesByRestaurantID(Long id)                            | O(n)             | Loops through array to find matches<br>Actual matches take O(1) using Hash Map <br>`n` is the total favourites in the store
getCommonFavouriteRestaurants(<br>&emsp; Long id1, Long id2)    | O(n)             | Creating the set requires calling getFavouritesByCustomerID(Long id)<br>HashMaps are used to find the matching objects in the sets<br>`n` is the total favourites in the store
getMissingFavouriteRestaurants(<br>&emsp; Long id1, Long id2)   | O(n)             | Creating the set requires calling getFavouritesByCustomerID(Long id)<br>HashMaps are used to find the matching objects in the sets<br>`n` is the total favourites in the store
getNotCommonFavouriteRestaurants(<br>&emsp; Long id1, Long id2) | O(n)             | Creating the set requires calling getFavouritesByCustomerID(Long id)<br>HashMaps are used to find the matching objects in the sets<br>`n` is the total favourites in the store
getTopCustomersByFavouriteCount()                               | O(n)             | Need to first loop through favourite Array<br>`n` is the total favourites in the store
getTopRestaurantsByFavouriteCount()                             | O(n)             | Need to first loop through favourite Array<br>`n` is the total favourites in the store

<div style="page-break-after: always;"></div>

## RestaurantStore
### Overview
* I have used an `ArrayList` structure for restaurant store as it has simple operations for it and I believed since many functions required the use of the whole store then arraylists are the most practical for this.
* I have use a  `HashMap`  of `Linked Lists` for the blacklist and ID storage as these are times where you know the one value you want to look up so you can find it instantly. 
* The blacklist consisted of Long, Long Hashmaps whereas for where you need to be able to get the object in methods such as getRestaurants(id), I used a hashmap of Long, Restaurant.
* For the sorting I used mergeSort as the worst case time is still quite good and it wasn't too difficult to implement. 

### Space Complexity
Store           | Worst Case | Description
--------------- | ---------- | -----------
RestaurantStore | O(n)       | I have used a single `ArrayList` to store customers and 2 HashMaps for IDs and blacklisted IDs. <br>Where `n` is total customers added.

### Time Complexity
Method                                                                        | Average Case     | Description
----------------------------------------------------------------------------- | ---------------- | -----------
addRestaurant(Restaurant r)                                                   | O(1)             | Adding is constant time and removing isn't average
addRestaurant(Restaurant[] r)                                                 | O(n)             | Doing add n times <br>`n` is the length of the array r
getRestaurant(Long id)                                                        | O(1)             | HashMap used
getRestaurants()                                                              | O(n)             | Have to loop through restaurantArray to copy to array <br>`n` is size of the restaurantArray
getRestaurants(Restaurant[] r)                                                | O(nlogn)         | Merge Sort used <br>`n` is the length of the input array
getRestaurantsByName()                                                        | O(n)             | Loops through restaurantArray <br>`n` is the size of the restaurantArray
getRestaurantsByDateEstablished()                                             | O(n)             | Loops through restaurantArray <br>`n` is the size of the restaurantArray
getRestaurantsByDateEstablished(<br>&emsp; Restaurant[] r)                    | O(nlogn)         | Merge Sort used <br>`n` is the length of the input array
getRestaurantsByWarwickStars()                                                | O(n)             | Loops through restaurantArray <br>`n` is the size of the restaurantArray
getRestaurantsByRating(Restaurant[] r)                                        | O(nlogn)         | Merge Sort used <br>`n` is the length of the input array
getRestaurantsByDistanceFrom(<br>&emsp; float lat, float lon)                 | O(n)             | Loops through restaurantArray <br>`n` is the size of the restaurantArray
getRestaurantsByDistanceFrom(<br>&emsp; Restaurant[] r, float lat, float lon) | O(n)             | Loops through Array <br>`n` is the length of the input array
getRestaurantsContaining(String s)                                            | O(n) + O(x)      | String Formatter uses a HashMap so O(1)*the number of characters to be converted <br>Looping through the arrayList takes O(n)<br>`n` is total customers in the store<br>`x` is the number of accented characters in the s

<div style="page-break-after: always;"></div>

## ReviewStore
### Overview
-

### Space Complexity
Store           | Worst Case | Description
--------------- | ---------- | -----------
ReviewStore     | O(-)       | -

### Time Complexity
Method                                     | Average Case     | Description
------------------------------------------ | ---------------- | -----------
addReview(Review r)                        | O(-)             | -
addReview(Review[] r)                      | O(-)             | -
getReview(Long id)                         | O(-)             | -
getReviews()                               | O(-)             | -
getReviewsByDate()                         | O(-)             | -
getReviewsByRating()                       | O(-)             | -
getReviewsByCustomerID(Long id)            | O(-)             | -
getReviewsByRestaurantID(Long id)          | O(-)             | -
getAverageCustomerReviewRating(Long id)    | O(-)             | -
getAverageRestaurantReviewRating(Long id)  | O(-)             | -
getCustomerReviewHistogramCount(Long id)   | O(-)             | -
getRestaurantReviewHistogramCount(Long id) | O(-)             | -
getTopCustomersByReviewCount()             | O(-)             | -
getTopRestaurantsByReviewCount()           | O(-)             | -
getTopRatedRestaurants()                   | O(-)             | -
getTopKeywordsForRestaurant(Long id)       | O(-)             | -
getReviewsContaining(String s)             | O(-)             | -

<div style="page-break-after: always;"></div>

## Util
### Overview
* **ConvertToPlace** 
    *  I have used a regular `Array` structure as you can simply loop through the array and checks for both fields
    *  I would have used a `HashMap` however you cannot use a static block to initialise it only once so the load time is very high
* **DataChecker**
    * The only data storage used was an array for isValid(ID) so I could count the instances of digits by incremented their places in an array
    * I thought that using any other dataTypes would have been excessive as all operations could be done within the types they are in
* **HaversineDistanceCalculator (HaversineDC)**
    * I simply followed the formula for this, using Math functions where possible such as toRadians as this is faster than doing the conversion myself
    * The two functions are the same speed as the only idfference is dividing by one number which is a very small change
* **KeywordChecker**
    * -
* **StringFormatter**
    * I used a `HashMap` to store the accent and conversion information
    * This is done in a static block so it is only needed to be added once
    * This makes it much faster as to convert to an unnaccented character is instant
    * The convert function just checks each character in the input string and builds up the unaccented string

### Space Complexity
Util               | Worst Case | Description
-------------------| ---------- | -----------
ConvertToPlace     | O(n)       | Just stored in a regular array<br>`n` is the size of the data set
DataChecker        | O(1)       | Only array of fixed size, nothing that would change size is checked
HaversineDC        | O(1)       | Stores fixed values
KeywordChecker     | O(-)       | -
StringFormatter    | O(n)       | All accents stored in a hashmap<br>`n` is the size of the accents array

### Time Complexity
Util              | Method                                                                             | Average Case     | Description
----------------- | ---------------------------------------------------------------------------------- | ---------------- | -----------
ConvertToPlace    | convert(float lat, float lon)                                                      | O(n)             | Loops through Data set from getPlacesArray<br>`n` is the size of the array returned by getPlacesArray 
DataChecker       | extractTrueID(String[] repeatedID)                                                 | O(1)             | As the repeated IDs are validate to be of fixed size 16 before hand so O(16) = O(1)
DataChecker       | isValid(Long id)                                                                   | O(1)             | Strings are checked to be of length 16 so O(16) = O(1)
DataChecker       | isValid(Customer customer)                                                         | O(1)             | All methods simply return values or we know to take O(1)
DataChecker       | isValid(Favourite favourite)                                                       | O(1)             | All methods simply return values or we know to take O(1)
DataChecker       | isValid(Restaurant restaurant)                                                     | O(1)             | All methods simply return values or we know to take O(1)
DataChecker       | isValid(Review review)                                                             | O(-)             | -
HaversineDC       | inKilometres(<br>&emsp; float lat1, float lon1, <br>&emsp; float lat2, float lon2) | O(1)             | Simply calculations performed once with no loops
HaversineDC       | inMiles(<br>&emsp; float lat1, float lon1, <br>&emsp; float lat2, float lon2)      | O(1)           | Simply calculations performed once with no loops
KeywordChecker    | isAKeyword(String s)                                                               | O(-)             | -
StringFormatter   | convertAccentsFaster(String s)                                                     | O(n)             | Since hashmap characters only added once it is not considered<br>Only need to consider looping through string so<br> `n` is the length of the inputted string
