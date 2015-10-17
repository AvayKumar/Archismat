<?php

class Register {
    
    public static function registerDevice( $token ) {
        
        require_once './database/Database.php';
        
        $db = new Database();
        $insertQuery = "INSERT INTO `registered_devices`(`token_id`) VALUES ('{$token}')";
        return $db->queryInsertDb($insertQuery);
    }
    
    public static function updateRegisteredToken( $token, $tokenId) {
        
        require_once './database/Database.php';
        
        $db = new Database();
        $updateQuery = "UPDATE `registered_devices` SET `token_id`='{$token}' WHERE `registration_id`='{$tokenId}'";
        return $db->queryUpdateDb( $updateQuery );
    }
    
}    

if( isset( $_GET['token'] ) && $_GET['token'] != '' ) {

    if( $_GET['refreshed'] == 'false' ) {
        
        if( Register::registerDevice( $_GET['token'] ) ) {
            //send notification to registered user
            
            require './gcm/Gcm.php';
            $gcm = new Gcm();
            
            $messageWellcome = '{"type": "0", "message":"Well come to Archismat. We are happy to see you."}';
            $gcm->sendNotifcation( $messageWellcome, $_GET['token']);   //Notification sent
            
            echo mysqli_insert_id( Database::$conn );
        } else {
            echo -1;
        }

    } else if( $_GET['refreshed'] == 'true' ) {
        
        if( Register::updateRegisteredToken( $_GET['token'], $_GET['update_id'] )  ) {
            echo 1;
        } else {
            echo -1;
        }
        
    }
        
}