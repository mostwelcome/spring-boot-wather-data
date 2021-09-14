package com.psl.AssignmentISV.Controller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.psl.AssignmentISV.Exceptions.UserServiceException;
import com.psl.AssignmentISV.Request.Location;
import com.psl.AssignmentISV.Request.UserDetailsRequest;
import com.psl.AssignmentISV.Response.UserDetailsResponse;
import com.psl.AssignmentISV.Security.SecurityConstants;
import com.psl.AssignmentISV.Service.UserService;
import com.psl.AssignmentISV.Shared.dto.UserDTO;

@RestController
@RequestMapping(value="users", produces = MediaType.APPLICATION_JSON_VALUE)
public class WeatherController {

	@Autowired
	UserService userService;

	@Autowired
	RestTemplate restTemplate;

	@PostMapping("/registration")
	public UserDetailsResponse createUser(@RequestBody UserDetailsRequest userDetails) throws UserServiceException {

		UserDetailsResponse returnValue = new UserDetailsResponse();

		UserDTO userDto = new UserDTO();

		BeanUtils.copyProperties(userDetails, userDto);

		UserDTO createdUser = userService.createUser(userDto);

		BeanUtils.copyProperties(createdUser, returnValue);

		return returnValue;

	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	@GetMapping("/{cityName}")
	public @ResponseBody ResponseEntity<String> getWeather(@PathVariable String cityName)
			throws JsonMappingException, JsonProcessingException {
		//String api_key = "pZrqi9yXpiQVuG99rtCxHDEeJQfyVuzY";

		final String uri = "http://dataservice.accuweather.com/locations/v1/cities/search?apikey=" + SecurityConstants.getApiKey() + "&q="
				+ cityName + "&language=en-us";
		RestTemplate restTemplate = new RestTemplate();
		Location[] loc = restTemplate.getForObject(uri, Location[].class);

		try {
			final String uri1 = "http://dataservice.accuweather.com/forecasts/v1/daily/1day/" + loc[0].getKey()
					+ "?apikey=" + SecurityConstants.getApiKey() + "&language=en-us";
			RestTemplate restTemplate1 = new RestTemplate();

			String loc1 = restTemplate1.getForObject(uri1, String.class);

			JSONObject obj = new JSONObject(loc1);
		     JSONObject jsonObject = new JSONObject();

			JSONArray arr = obj.getJSONArray("DailyForecasts");
			String WeatheDetails = null;
			for (int i = 0; i < arr.length(); i++) {
				String date = arr.getJSONObject(i).getString("Date");
				String Temp_min_value = arr.getJSONObject(i).getJSONObject("Temperature").getJSONObject("Minimum")
						.getString("Value");
				String Temp_min_value_unit = arr.getJSONObject(i).getJSONObject("Temperature").getJSONObject("Minimum")
						.getString("Unit");

				String Temp_max_value = arr.getJSONObject(i).getJSONObject("Temperature").getJSONObject("Maximum")
						.getString("Value");
				String Temp_max_value_unit = arr.getJSONObject(i).getJSONObject("Temperature").getJSONObject("Maximum")
						.getString("Unit");

				String day = arr.getJSONObject(i).getJSONObject("Day").getString("IconPhrase");
				String night = arr.getJSONObject(i).getJSONObject("Night").getString("IconPhrase");

				  jsonObject.put("City", cityName);

				     jsonObject.put("date", date);
				     jsonObject.put("TemperatureMinimum", Temp_min_value);
				     jsonObject.put("TemperatureMaximum", Temp_max_value);
				     jsonObject.put("TemperatureUnit", Temp_max_value_unit);

				     jsonObject.put("DayWeather", day);
				     jsonObject.put("NightWeather", night);
			}
			return new ResponseEntity<String>(jsonObject.toString(), HttpStatus.OK);

		} catch (Exception ex) {
			System.out.println("Please enter valid city name");
			 JSONObject errorObject = new JSONObject();		
			 try {
				errorObject.put("error", "Please enter valid city name");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			 return new ResponseEntity<String>(errorObject.toString(), HttpStatus.BAD_REQUEST);
		}

	}

}
