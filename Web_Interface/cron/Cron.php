<?php

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Description of Cron
 *
 * @author avay
 */
class Cron {

    
    private function setSent( $db, $message_id, $rowId) {
        
        if( $message_id ) {
            $insertQuery = "INSERT INTO `server_response`(`message_id`) VALUES ('{$message_id}')";
            if( $db->queryInsertDb( $insertQuery ) ) {
                $queryUpdate = "UPDATE `message` SET `sent`='1' WHERE `message_id` = '{$rowId}'";
                $db->queryUpdateDb( $queryUpdate );
            }
        }
    }

    public function checkMessage( ) {
        
        require './database/Database.php';
        require './gcm/Gcm.php';
        
        
        $db = new Database();
        
        $dateTime = new DateTime( 'now', new DateTimeZone( 'Asia/Calcutta' ) );
        
        $queryTime = $dateTime->format( 'Y-m-d H:i:s' );
        
        $queryString = "SELECT * FROM `message` WHERE `sent` = 0 AND `datetime` <= '{$queryTime}' ";
                
        if( $query = $db->queryDb( $queryString ) ) {

            $gcm = new Gcm();
            $data = array();

            foreach ($query as $row) {

                switch ( $row['update_type'] ) {
                    case 0:
                        $data['type'] = 0;
                        $data['message'] = $row['description'];              
                        break;
                    case 1:
                        $data['type'] = 1;
                        $data['name'] = $row['name'];
                        $data['location'] = $row['location'];
                        $data['desc'] = $row['description'];
                        $data['long'] = $row['longitude'];
                        $data['lat'] = $row['latitude'];
                        break;
                    case 2:
                        $data['type'] = 2;
                        $data['url'] = 'http://10.42.0.1/gcm/images/'.$row['pick'];
                        $data['desc'] = $row['description'];
                        break;
                    default :
                        throw new Exception('Unsupported operation');
                }

                if ( $gcm_response = $gcm->sendNotifcation(json_encode($data) ) ) {
                    $jsonResponse = json_decode($gcm_response, TRUE);
                    $message_id = $jsonResponse['message_id'];
                    $this->setSent( $db, $message_id, $row['message_id']);
                }
                sleep(1);               
            } 

        }
        
    }
    
}
