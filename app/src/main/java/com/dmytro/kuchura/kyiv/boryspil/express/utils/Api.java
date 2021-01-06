package com.dmytro.kuchura.kyiv.boryspil.express.utils;

public class Api {

    /** All API links. */
    public interface Url {
        String API_TRAINS_LIST = "http://138.197.186.137:8080/api/trains";
        String API_CURRENT_TRAINS_LIST = "http://138.197.186.137:8080/api/current";
        String API_TRAIN_INFO = "http://138.197.186.137:8080/api/train/";
    }
}
