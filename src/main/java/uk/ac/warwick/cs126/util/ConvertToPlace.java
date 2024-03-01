package uk.ac.warwick.cs126.util;

import uk.ac.warwick.cs126.interfaces.IConvertToPlace;
import uk.ac.warwick.cs126.models.Place;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

import org.apache.commons.io.IOUtils;

/** Class for implementing ConvertToPlace and operations related. */
public class ConvertToPlace implements IConvertToPlace {
    /**@param places                    Stores all places as result of getPlacesArray. */
    Place[] places;

    /** Sets array to result of getPlacesArray. */
    public ConvertToPlace() {
        places = getPlacesArray();
    }

    /** Searches through all places to find a match with given latitudes and logitudes. 
     * 
     * @param latitude                  Input latitiude for matching.
     * @param longitude                 Input longitude for matching. 
     * 
     * Loops through checking if each place has both a matching latitude and logitude.
     * @return Place object found from data, an empty one if not found.
    */
    public Place convert(float latitude, float longitude) {
        for(int i=0; i<places.length;i++){
            if(places[i].getLatitude()==latitude & places[i].getLongitude()==longitude){
                return places[i];
            }
        }
        return new Place("", "", 0.0f, 0.0f);
    }

    /**Gets all places you are able to search through from the data given. 
     * 
     * @return Place array of all places to search. 
    */
    public Place[] getPlacesArray() {
        Place[] placeArray = new Place[0];

        try {
            InputStream resource = ConvertToPlace.class.getResourceAsStream("/data/placeData.tsv");
            if (resource == null) {
                String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
                String resourcePath = Paths.get(currentPath, "data", "placeData.tsv").toString();
                File resourceFile = new File(resourcePath);
                resource = new FileInputStream(resourceFile);
            }

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

            Place[] loadedPlaces = new Place[lineCount - 1];

            BufferedReader tsvReader = new BufferedReader(new InputStreamReader(
                    new ByteArrayInputStream(inputStreamBytes), StandardCharsets.UTF_8));

            int placeCount = 0;
            String row;

            tsvReader.readLine();
            while ((row = tsvReader.readLine()) != null) {
                if (!("".equals(row))) {
                    String[] data = row.split("\t");
                    Place place = new Place(
                            data[0],
                            data[1],
                            Float.parseFloat(data[2]),
                            Float.parseFloat(data[3]));
                    loadedPlaces[placeCount++] = place;
                }
            }
            tsvReader.close();

            placeArray = loadedPlaces;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return placeArray;
    }
}

