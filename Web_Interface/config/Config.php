<?php

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Description of Config
 *
 * @author avay
 */
class Config {
    
    /**@var array config data*/
    private static $data = null;
    
    public static function getConfig( $key = NULL ) {
        if( $key == NULL ) {
            return self::getData();
        }
        $data = self::getData();
        
        if( !array_key_exists($key, $data) ) {
            throw new Exception('Invalid key');
        }
        return $data[$key];
    }
    
    private static function getData() {
        if( self::$data !== null ) {
            return self::$data;
        }
        self::$data = parse_ini_file('./config/config.ini');
        return self::$data;
    }
    
}
