package uk.ac.warwick.cs126.util;

import uk.ac.warwick.cs126.interfaces.IDataChecker;

import uk.ac.warwick.cs126.models.Customer;
import uk.ac.warwick.cs126.models.Restaurant;
import uk.ac.warwick.cs126.models.Favourite;
import uk.ac.warwick.cs126.models.Review;

import java.util.Date;

/** Class for implementing DataChecker and operations related. */
public class DataChecker implements IDataChecker {

    public DataChecker() {
    }

    /** Returns TrueID extracted from the repeated ID. 
     * 
     * @param repeatedID                    ID to have trueID extracted from.
     * 
     * @return True ID obtained from RepeatedID.
    */
    public Long extractTrueID(String[] repeatedID) {
        Long value;
        if (repeatedID.length == 3){
            if((repeatedID[0]).compareTo(repeatedID[1])==0){
                value = Long.parseLong(repeatedID[0]);
                return (value);
            }else if((repeatedID[1]).compareTo(repeatedID[2])==0){
                value = Long.parseLong(repeatedID[1]);
                return (value);
            }else if((repeatedID[0]).compareTo(repeatedID[2])==0){
                value = Long.parseLong(repeatedID[0]);
                return (value);
            }
        }
        return null;
    }

    /** Validates ID to required specification.
     * 
     * @param inputID                   ID to be validated.
     * 
     * Has a count for each digit in a 9 digit array to see if a digit appears more than 3 times.
     * 
     * @return True or False for if ID is valid or not.                    
    */
    public boolean isValid(Long inputID) {
        int[] num = {0,0,0,0,0,0,0,0,0};
        String str1 = Long.toString(inputID);
        if(str1.length()==16){
            for (int i=0;i<str1.length();i++){
                if (Character.getNumericValue(str1.charAt(i))==0){
                    return false;
                }
                num[Character.getNumericValue(str1.charAt(i))-1]++;
            }
            for(int x=0;x<9;x++){
                if (num[x]>3){
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /** Validates Customer object. 
     * 
     * @param customer                  Customer object to be validated. 
     * 
     * All variables that can be checked for null, are. 
     * The ID of the customer object is validated. 
     * 
     * @return True or False for if the customer object is valid. 
    */
    public boolean isValid(Customer customer) {
        if (customer == null) return false;
        if(customer.getID()==null) return false;
        if (isValid(customer.getID())==false) return false;
        if(customer.getStringID()== null) return false;
        if(customer.getFirstName()==null) return false;
        if(customer.getLastName()==null) return false;
        if(customer.getDateJoined()==null) return false;

        return true;
    }

    /** Validates Restaurant object.
     * 
     * @param restaurant                    Restaurant object to be validated.
     * 
     * All variables that can be checked for null, are.
     * ID and trueID are validated.
     * Variable checked to be withing specified range.
     * 
     * @return True or False for if restaurant object is valid. 
    */
    public boolean isValid(Restaurant restaurant) {
        if(restaurant==null) return false;
        if(isValid(restaurant.getID())==false) return false;
        if(restaurant.getStringID()==null) return false;
        if(restaurant.getRepeatedID()==null||restaurant.getName()==null||restaurant.getOwnerFirstName()==null||restaurant.getOwnerLastName()==null||restaurant.getCuisine()==null||restaurant.getEstablishmentType()==null||restaurant.getPriceRange()==null||restaurant.getDateEstablished()==null){
            return false;
        }
        if(isValid(extractTrueID(restaurant.getRepeatedID()))==false) return false;
        if(!((restaurant.getFoodInspectionRating()>=0&&(restaurant.getFoodInspectionRating()<=5)))) return false;
        if(!((restaurant.getWarwickStars()>=0&&(restaurant.getWarwickStars()<=3))))return false;
        if(restaurant.getLastInspectedDate().compareTo(restaurant.getDateEstablished())<0)return false;
        if(!((restaurant.getCustomerRating()==0||(restaurant.getCustomerRating()<=5&&restaurant.getCustomerRating()>=1))))return false;
        return true;
    }

    /** Validates Favourite object.
     * 
     * @param favourite                     Favourite object to be validated.
     * 
     * All variables that can be checked for null, are.
     * ID, CustomerID and RestaurantID are validated.
     * 
     * @return True or False for if favourite object is valid. 
    */
    public boolean isValid(Favourite favourite) {
        if(favourite!=null){
            if(isValid(favourite.getID())==true){
                if(favourite.getStringID()!=null){
                    if(isValid(favourite.getCustomerID())==true){
                        if(isValid(favourite.getRestaurantID())==true){
                            if(favourite.getDateFavourited()!=null){
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean isValid(Review review) {
        // TODO
        return false;
    }
}