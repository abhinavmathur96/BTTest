package com.majorproject.async_band;

import java.util.Map;
import java.util.HashMap;

/**
 * Object containing all data variables to be used by Neo Ring
 */

public class NeoDataPacket {
    private Integer max_leds = 12;
    //All values will be included in the final only if they are set to values
    //other than their defaults
    //How many vibration motors to use? 1 or 2
    private Integer vibrate = 0;
    //LED color. Choose from W,V,B,G,Y,O,R
    private String color_leds = "W";
    //Vibration duration (milliseconds)
    private Integer vibrate_duration = 50;
    //How many time to vibrate
    private Integer vibrate_repeat = 1;
    //Start index of LED choice. Can take values [0,12)
    private Integer led_start_ind = 0;
    //End index of LED choice. Can take values [0,12)
    //To choose only one LED eg. Choose 5th LED led_start_ind = 4 and led_end_ind = 4
    private Integer led_end_ind = 11;
    //How many times to rotate? -1 for perpetual rotation
    private Integer rotate = 0;

    public String parse() {
        String f_str = "";
        if (vibrate > 0) {
            f_str += "V"+vibrate.toString();
        }
        f_str += "C"+color_leds;
        if (vibrate_duration >= 0) {
            f_str += "D"+vibrate_duration.toString();
        }
        if (vibrate_repeat > 0) {
            f_str += "R"+vibrate_repeat.toString();
        }
        f_str += "S"+led_start_ind.toString()+
                 "E"+led_end_ind.toString()+
                 "O"+rotate.toString();
        return f_str;
    }

    public Integer getVibrate() {
        return vibrate;
    }

    public void setVibrate(Integer vibrate) {
        if (vibrate == 1 || vibrate == 2) {
            this.vibrate = vibrate;
        }
    }

    public String getColor_leds() {
        return color_leds;
    }

    public void setColor_leds(String color_leds) {
        if (
                color_leds.equals("W") ||
                color_leds.equals("V") ||
                color_leds.equals("B") ||
                color_leds.equals("G") ||
                color_leds.equals("Y") ||
                color_leds.equals("O") ||
                color_leds.equals("R")
           ) {
            this.color_leds = color_leds;
        }
    }

    public Map getLed_ind() {
        Map<Integer, Integer> res = new HashMap<>();
        res.put(led_start_ind, led_end_ind);
        return res;
    }

    public void setLed_ind(Integer led_start_ind, Integer led_end_ind) {
        if (
                led_start_ind >= 0 &&
                led_end_ind <= 11 &&
                led_start_ind <= led_end_ind
            ) {
            this.led_start_ind = led_start_ind;
            this.led_end_ind = led_end_ind;
        }
    }

    public Integer getRotate() {
        return rotate;
    }

    public void setRotate(Integer rotate) {
        if (rotate > -2) {
            this.rotate = rotate;
        }
    }
}
