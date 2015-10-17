<?php    

    session_start();
    
    if( !isset( $_SESSION['admin'] ) || !$_SESSION['admin'] ) {
        header('Location: index.php');
    }
        

    $css_links = '<link href="./css/bootstrap.min.css" rel="stylesheet">';
    $js_links = $js_links = '<script src="./js/jquery-1.10.2.min.js"></script>'
              . '<script src="./js/jquery.ui.widget.js"></script>'
              . '<script src="./js/jquery.iframe-transport.js"></script>'
              . '<script src="./js/jquery.fileupload.js"></script>'
              . '<script src="./js/bootstrap.min.js"></script>'
              . '<script src="./js/image-upload.js"></script>'
              . '<script>$(function(){ $("#navbar>ul>li:nth-child(3)").addClass("active"); })</script>';



    require './views/components/header.php';

    require_once './database/Database.php';
        
    $db = new Database();
    
    $server_response = '';
    
    if( isset( $_POST['submit'] ) ) {
        
        $event_name = htmlentities( $_POST['event-name'] );
        
        $event_location = htmlentities( $_POST['event-location'] );
        $postDateTime = $_POST['event-date'].' '.$_POST['event-time'];
        
        $datetime = DateTime::createFromFormat( 'Y-m-d H:i',  $postDateTime , new DateTimeZone( 'UTC' ) );
        $event_datetime = $datetime->format( 'Y-m-d H:i:s' );
        
        $event_description = htmlentities( $_POST['event-description'] ) ;
        $longitude = htmlentities( $_POST['location-longitude'] );
        $latitude = htmlentities( $_POST['location-latitude'] );
        
        $update_type = 1;
        
        $sqlQuery = "UPDATE `message` SET `datetime`='{$event_datetime}',`description`='{$event_description}',`name`='{$event_name}',`location`='{$event_location}',`longitude`='{$longitude}',`latitude`='{$latitude}' WHERE `message_id` = '{$_GET['id']}'";
        
        if( $db->queryUpdateDb( $sqlQuery ) ) {
            $server_response = '<div class="alert alert-success" role="alert">Event has been <strong>successfully</strong> updated</div>';
        } else {
            echo mysqli_error(Database::$conn);
            $server_response = '<div class="alert alert-danger" role="alert">Something went wrong!!! Thats all we know</div>';
        }
        
    }
    
    
    if( isset( $_GET['id'] ) ) {
        
        $sqlQuery = "SELECT `datetime`, `update_type`, `description`, `name`, `location`, `longitude`, `latitude` FROM `message` WHERE `message_id` = '{$_GET['id']}'";
        
        if( $query = $db->queryDb( $sqlQuery ) ) {
            
            $row = $query[0];
            
            if( $row['update_type'] == 1 ) {
                
                $datetime = DateTime::createFromFormat( 'Y-m-d H:i:s',  $row['datetime'] , new DateTimeZone( 'UTC' ) );
                
                $event_name = $row['name'];
                $event_location = $row['location'];
                $event_longitude = $row['longitude'];
                $event_latitude = $row['latitude'];
                $event_date = $datetime->format('Y-m-d');
                $event_time = $datetime->format('H:i');
                $event_description = $row['description'];
                
                
            } else {
                die('<div class="container text-center"><h1>Invalid alert id.</h1></div>');
            }
            
        }
        
        require './views/components/navigation.php';
        require './views/edit_event.php';
        
    } else {
        die('<div class="container text-center"><h1>Invalid alert id.</h1></div>');
    }
    
    require './views/components/footer.php';