package edu.cornell.tech.foundry.behavioralextensionscore;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by jameskizer on 2/2/17.
 */
public class BXHelpers {

    static Random rnd = new Random();

    static public <T> T coinFlip(T obj1, T obj2, double bias) {

        double realBias = Math.max( Math.min(bias, 1.0), 0.0 );
        double flip = rnd.nextDouble();
        if (flip < realBias) {
            return obj1;
        }
        else {
            return obj2;
        }

    }

}