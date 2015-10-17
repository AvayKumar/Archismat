<?php
    
    session_start();
    
    if( !isset( $_SESSION['admin'] ) || !$_SESSION['admin'] ) {
        header('Location: index.php');
    }
        

    $css_links = '<link href="./css/bootstrap.min.css" rel="stylesheet">';
    $js_links = '<script src="./js/jquery-1.10.2.min.js"></script>'
              . '<script src="./js/bootstrap.min.js"></script>'
              . '<script>$(function(){ $("#navbar>ul>li:nth-child(3)").addClass("active"); })</script>';



    require './views/components/header.php';

    require_once './database/Database.php';
        
    $db = new Database();
    
    $server_response = '';
    
    if( isset( $_POST['submit'] ) ) {
        
        $alert_body = addslashes( $_POST['alert-description'] ) ;
        
        $postDateTime = $_POST['alert-date'].' '.$_POST['alert-time'];
        
        $datetime = DateTime::createFromFormat( 'Y-m-d H:i',  $postDateTime , new DateTimeZone( 'UTC' ) );
        $alert_datetime = $datetime->format( 'Y-m-d H:i:s' );
        
        $updateQuery = "UPDATE `message` SET `datetime`= '{$alert_datetime}', `description`= '{$alert_body}'  WHERE `message_id` = '{$_GET['id']}'";
        
        if( $db->queryUpdateDb( $updateQuery ) ) {
            $server_response = '<div class="alert alert-success" role="alert">Alert has been <strong>successfully</strong> updated</div>';
        } else {
            $server_response = '<div class="alert alert-danger" role="alert">Something went wrong!!! Thats all we know</div>';
        }
        
    }
    
    
    if( isset( $_GET['id'] ) ) {
        
        $sqlQuery = "SELECT `datetime`, `update_type`, `description` FROM `message` WHERE `message_id` = '{$_GET['id']}'";
        
        if( $query = $db->queryDb( $sqlQuery ) ) {
            
            $row = $query[0];
            
            if( $row['update_type'] == 0 ) {
                
                $datetime = DateTime::createFromFormat( 'Y-m-d H:i:s',  $row['datetime'] , new DateTimeZone( 'UTC' ) );
                
                $alert_date = $datetime->format('Y-m-d');
                $alert_time = $datetime->format('H:i');
                $alert_description = $row['description'];
                
            } else {
                die('<div class="container text-center"><h1>Invalid alert id.</h1></div>');
            }
            
        }
        
        require './views/components/navigation.php';
        require './views/edit_alert.php';
        
    } else {
        die('<div class="container text-center"><h1>Invalid alert id.</h1></div>');
    }
    
    require './views/components/footer.php';
    


