<?php

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Description of Database
 *
 * @author avay
 */
class Database {
    
    public static $conn = null;
    
    public function __construct() {
        
        require_once './config/Config.php';
        
        $config = Config::getConfig();
        
        if( self::$conn == null ) {
            self::$conn = mysqli_connect($config['host'], $config['username'], $config['password'], $config['dbname']);
        }        
    }
    
    public function queryDb( $sqlQuery = NULL ) {
        
        if( $sqlQuery != NULL ) {
            
            if( $query = mysqli_query(self::$conn, $sqlQuery) ) {
                
                $count = mysqli_num_rows( $query );
                
                if( $count > 0 ) {
                    $response = array();
                    
                    while( $row = mysqli_fetch_assoc( $query ) ) {
                        $response[] = $row;
                    }
                    
                    return $response;
                } else {
                    return 0;
                }
                
            } else {
                return FALSE;
            }
            
        } else {
            throw new Exception('Invalid query string');
        }
        
    }
    
    public function queryInsertDb( $sqlQuery = NULL ) {
        
        if( $sqlQuery != NULL ) {
            
            return mysqli_query(self::$conn, $sqlQuery);
            
        } else {
            return FALSE;
        }
        
    }
    
    public function queryUpdateDb( $sqlQuery = NULL ) {
        
        if( $sqlQuery != NULL ) {
            
             return mysqli_query(self::$conn, $sqlQuery);
            
        } else {
            return FALSE;
        }
        
    }
    
    public function queryDeleteDb( $sqlQuery = NULL ) {
        
        if( $sqlQuery != NULL ) {
            
            return mysqli_query(self::$conn, $sqlQuery);
            
        } else {
            return FALSE;
        }
        
    }
   
}
