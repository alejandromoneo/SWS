package com.safeline.restController;

import com.safeline.entity.Unit7wr;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * Created by alejandromoneomartinez on 17/7/17.
 */
@RestController
public class SourceController {
    /*
    @RequestMapping(path="sourcelist/7WR/Slist.json", method= RequestMethod.POST)
    public HttpEntity sourceListtxt(@RequestHeader HttpHeaders header, @RequestBody String unit7wr){

        ObjectMapper mapper = new ObjectMapper();

        try {
            Unit7wr convertedUnit= mapper.readValue(unit7wr, Unit7wr.class);
            System.out.println("Source recived 2");
            System.out.println(header);
            System.out.println(unit7wr);

            System.out.println(convertedUnit);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity(HttpStatus.OK);
    }*/

    @RequestMapping(path="sourcelist/7WR/Slist.json", method= RequestMethod.POST)
    public HttpEntity sourceListtxt(@RequestBody Unit7wr unit7wr){
        System.out.println("Source recived 2");
        System.out.println(unit7wr);

        return new ResponseEntity(HttpStatus.OK);
    }
}
