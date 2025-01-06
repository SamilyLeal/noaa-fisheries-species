# NOAA Fisheries Species Web Scraper 🐙 🫧
This project is a web scraping application designed to gather information about various animal species from the NOAA Fisheries website. Built with Java 17, Spring Boot, and JSoup, it provides a REST API with endpoints for retrieving species data categorized by animal type.

### Tech Stack 
- **Java 17**
- **Spring Boot**
  - Web
  - Devtools 
- **Jsoup**
- **Maven**


### Setup 🐟
Prerequisites: Java 17 and Maven must be installed! 

1. Clone the repository:
   ```
   git clone https://github.com/SamilyLeal/noaa-fisheries-species/
   ```
3. Navigate to the project directory:
   ```
   cd noaa-fisheries-species
   ```
5. Build the application:
   ```
   mvn clean install
   ```
7. Run the application:
   ```
   mvn spring-boot:run
   ```

### Endpoints 🐳
The application exposes a REST API that accepts HTTP GET requests to retrieve species data.

#### Example Request

To retrieve data for a category, send a GET request with the category as a query parameter:
```
GET api/v1?category=fish-sharks
```

#### Response
The response includes a list of species with their details:
```
[
  {
    "name": "Acadian Redfish",
    "scientificName": "Sebastes fasciatus",
    "alsoKnownAs": "Redfish, Ocean perch, Labrador redfish",
    "imageUrl": "https://www.fisheries.noaa.gov//s3//styles/original/s3/2022-07/640x427-Acadian-Redfish-NOAAFisheries.png?itok=a-5et1W1",
    "additionalDetails": {
      "Length": "18 to 20 inches long",
      "Region": "New England/Mid-Atlantic",
      "Lifespan": "50 years or more"
    },
    "scientificClassification": {
      "Order": "Scorpaeniformes",
      "Kingdom": "Animalia",
      "Phylum": "Chordata",
      "Genus": "Sebastes",
      "Family": "Scorpaenidae",
      "Class": "Actinopterygii",
      "Species": "fasciatus"
    }
  }
]
```
<br>

| Endpoint Available |
| -- |
| api/v1?category=fish-sharks |
| api/v1?category=whales |
| api/v1?category=dolphins-porpoises |
| api/v1?category=invertebrates |
| api/v1?category=sea-turtles |
| api/v1?category=seals-sea-lions |

### License 
This project is licensed under the MIT License.
