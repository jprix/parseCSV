package com.iterable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;




public class CSVReader {

        public static void main(String[] args) {

          String csvFile = "/Users/jay.parisi/Downloads/se_assignment_users.csv - tomato_users.csv.csv";
          // String csvFile = "/Users/jay.parisi/Downloads/se_assignment_users.one-user.csv";

            BufferedReader br = null;
            String line = "";
            String cvsSplitBy = ",";

            try {

                br = new BufferedReader(new FileReader(csvFile));
                //declare a list
                List<Map> userList = new LinkedList();


                int counter = 0;
                int lines = 0;

                while ((line = br.readLine()) != null) {
                    lines++;
                    // use comma as separator
                    String[] user = line.split(cvsSplitBy);
                    Map<String, Object> lineObject = new HashMap();
                    //extract needed fields

                    lineObject.put("email", user[2]);
                    lineObject.put("dataFields", new HashMap());
                    lineObject.put("userId", user[9]);
                    lineObject.put("mergedNestedFields", new HashMap());




                    userList.add(lineObject);
                    counter++;
                    //stop iterating once reach 50
                    if (counter == 50) {
                        break;
                    }


                }

                //Bulk Hashmap 2 json
                //create users root in json collection
                Map<String, Object> finalResult = new HashMap();

                if (counter > 2) {
                    finalResult.put("users", userList);
                }

                Gson gson = new GsonBuilder().create();
                String result = gson.toJson(finalResult); // set Bulk json body

                //System.out.println(result);
                String singleResult = gson.toJson(userList); //set single json body
               // System.out.println(singleResult);



                if (counter > 2) {
                    //fire bulk Request

                    HttpResponse response = Unirest.post("https://api.iterable.com/api/users/bulkUpdate").header("accept", "application/json").header("Content-Type", "application/json").queryString("api_key", "put API KEY HERE").body(result).asJson();
                    System.out.println("Status: This was a Bulk" + " " + response.getStatus());
                    System.out.println(result);

                } else {
                    //fire single request
                    HttpResponse response = Unirest.post("https://api.iterable.com/api/users/update").header("accept", "application/json").header("Content-Type", "application/json").queryString("api_key", "put API KEY HERE").body(singleResult).asJson();
                    //response.getStatus();
                    System.out.println("Status: This was a Single" + " " + response.getStatus() + " " + response.getHeaders());
                    System.out.println(singleResult);

                }





            } catch (Exception e) {
                e.printStackTrace();
            }
                finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }

