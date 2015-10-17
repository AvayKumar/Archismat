<?php

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Description of Gcm
 *
 * @author avay
 */
class Gcm {
    
    public function sendNotifcation( $message = null, $send_to = null) {
        
        include_once './config/Config.php';
        
        $url = 'https://android.googleapis.com/gcm/send';   // Send request to thsi url
        
        $fields = array();
        
        if( $send_to !== null && $message !== null ) {
                $fields['to'] = $send_to;
                $fields['data'] = array( 'message' => $message );
        } else if( $send_to == null && $message !== null ) {
            $fields['to'] = '/topics/archismat';
            $fields['data'] = array( 'message' => $message );
        } else {
            return FALSE;
        }
        
        //echo json_encode($fields);
        
        $google_api_key = Config::getConfig('google_api_key');
        
        $header = array(
            'Authorization: key='.$google_api_key,
            'Content-Type: application/json'
        );
        
        // Open connection
        $ch = curl_init();
        
        curl_setopt($ch, CURLOPT_URL, $url);
        
        curl_setopt($ch, CURLOPT_POST, TRUE);
        curl_setopt($ch, CURLOPT_HTTPHEADER, $header);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        
        // Disabling SSL Certificate support temporarly
        curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);

        curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));
        
        // Execute post
        $response = curl_exec($ch);
        
        if( $response === FALSE ) {
            return FALSE;
        }
        
        //close connection
        curl_close($ch);
        
        return $response;
    }
    
}
